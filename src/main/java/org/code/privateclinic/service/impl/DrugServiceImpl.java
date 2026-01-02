package org.code.privateclinic.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.Drug;
import org.code.privateclinic.mapper.DrugMapper;
import org.code.privateclinic.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DrugServiceImpl implements DrugService {

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_KEY_PREFIX = "drug:";
    private static final String CACHE_KEY_LIST = "drugList";
    private static final long CACHE_EXPIRE_HOURS = 24;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Loggable("查询药品列表")
    public List<Drug> getDrugList() {
        if (redisTemplate.hasKey(CACHE_KEY_LIST)) {
            try {
                String cache = redisTemplate.opsForValue().get(CACHE_KEY_LIST);
                if (cache != null && !cache.isEmpty()) {
                    return objectMapper.readValue(cache, new TypeReference<List<Drug>>() {});
                }
            } catch (Exception e) {
                log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}", CACHE_KEY_LIST, e.getMessage());
                redisTemplate.delete(CACHE_KEY_LIST);
            }
        }

        List<Drug> drugList = drugMapper.getDrugList();
        if (drugList != null && !drugList.isEmpty()) {
            try {
                String jsonValue = objectMapper.writeValueAsString(drugList);
                redisTemplate.opsForValue().set(CACHE_KEY_LIST, jsonValue, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("药品列表数据序列化失败，无法存入缓存，错误信息: {}", e.getMessage());
            }
        }
        return drugList;
    }

    @Override
    @Loggable("根据ID查询药品信息")
    public Drug getDrugById(Long drugId) {
        String cacheKey = CACHE_KEY_PREFIX + drugId;
        if (redisTemplate.hasKey(cacheKey)) {
            try {
                String cache = redisTemplate.opsForValue().get(cacheKey);
                if (cache != null && !cache.isEmpty()) {
                    return objectMapper.readValue(cache, Drug.class);
                }
            } catch (Exception e) {
                log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}", cacheKey, e.getMessage());
                redisTemplate.delete(cacheKey);
            }
        }

        Drug drug = drugMapper.getDrugById(drugId);
        if (drug != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(drug);
                redisTemplate.opsForValue().set(cacheKey, jsonValue, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("药品信息序列化失败，无法存入缓存，药品ID: {}，错误信息: {}", drugId, e.getMessage());
            }
        }
        return drug;
    }

    @Override
    @Loggable("根据药品名称查询药品信息")
    public Drug getDrugByName(String drugName) {
        return drugMapper.getDrugByName(drugName);
    }

    @Override
    @Loggable("添加药品")
    public int addDrug(Drug drug) {
        Drug existingDrug = drugMapper.getDrugByName(drug.getDrugName());
        if (existingDrug != null) {
            throw new RuntimeException("药品名称已存在");
        }
        if (drug.getStatus() == null) {
            drug.setStatus(1);
        }

        int result = drugMapper.addDrug(drug);
        if (result > 0) {
            clearDrugCache();
        }
        return result;
    }

    @Override
    @Loggable("更新药品信息")
    public int updateDrug(Drug drug) {
        if (drug.getDrugId() == null) {
            throw new RuntimeException("药品ID不能为空");
        }
        if (drug.getDrugName() != null && !drug.getDrugName().isEmpty()) {
            Drug existingDrug = drugMapper.getDrugByName(drug.getDrugName());
            if (existingDrug != null && !existingDrug.getDrugId().equals(drug.getDrugId())) {
                throw new RuntimeException("药品名称已被使用");
            }
        }

        int result = drugMapper.updateDrug(drug);
        if (result > 0) {
            clearDrugCache();
        }
        return result;
    }

    @Override
    @Loggable("逻辑删除药品")
    public int deleteDrug(Long drugId) {
        Drug drug = drugMapper.getDrugById(drugId);
        if (drug == null) {
            return 0;
        }
        int result = drugMapper.deleteDrug(drugId);
        if (result > 0) {
            clearDrugCache();
        }
        return result;
    }

    @Override
    @Loggable("物理删除药品")
    public int deleteDrugPhysical(Long drugId) {
        Drug drug = drugMapper.getDrugById(drugId);
        if (drug == null) {
            return 0;
        }
        int result = drugMapper.deleteDrugPhysical(drugId);
        if (result > 0) {
            clearDrugCache();
        }
        return result;
    }

    @Override
    @Loggable("更新药品库存")
    public int updateStock(Long drugId, Integer stock) {
        int result = drugMapper.updateStock(drugId, stock);
        if (result > 0) {
            clearDrugCache();
        }
        return result;
    }

    private void clearDrugCache() {
        try {
            redisTemplate.delete(CACHE_KEY_LIST);
        } catch (Exception e) {
            log.warn("清除药品列表缓存失败，错误信息: {}", e.getMessage());
        }
    }
}
