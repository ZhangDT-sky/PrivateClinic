package org.code.privateclinic.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
    public List<Drug> getDrugList() {
        log.info("开始查询药品列表");
        long start = System.currentTimeMillis();
        try{
            if (redisTemplate.hasKey(CACHE_KEY_LIST)) {
                try{
                    String cache = redisTemplate.opsForValue().get(CACHE_KEY_LIST);
                    if(cache!=null && !cache.isEmpty()){
                        List<Drug> drugList = objectMapper.readValue(
                                cache, new TypeReference<List<Drug>>() {}
                        );
                        long end = System.currentTimeMillis();
                        log.info("从缓存获取药品列表成功，共 {} 条记录，耗时 {} ms",
                                drugList.size(), (end-start));
                        return drugList;
                    }
                }catch (Exception e){
                    log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}",
                            CACHE_KEY_LIST, e.getMessage(), e);
                    redisTemplate.delete(CACHE_KEY_LIST);
                }
            }else{
                log.debug("缓存键 {} 不存在，将从数据库查询", CACHE_KEY_LIST);
            }

            log.info("开始从数据库查询药品列表");
            List<Drug> drugList = drugMapper.getDrugList();

            if(drugList == null || drugList.isEmpty()){
                log.warn("数据库查询结果为空，返回空列表");
                return drugList;
            }
            log.info("数据库查询成功，共查询到 {} 条药品记录", drugList.size());

            try {
                String jsonValue = objectMapper.writeValueAsString(drugList);
                redisTemplate.opsForValue().set(
                        CACHE_KEY_LIST,
                        jsonValue,
                        CACHE_EXPIRE_HOURS,
                        TimeUnit.HOURS
                );
                log.info("药品列表数据已存入缓存，缓存键: {}，过期时间: {} 小时",
                        CACHE_KEY_LIST, CACHE_EXPIRE_HOURS);
            } catch (Exception e) {
                log.error("药品列表数据序列化失败，无法存入缓存，错误信息: {}",
                        e.getMessage(), e);
            }
            long end = System.currentTimeMillis();
            log.info("查询药品列表完成，共 {} 条记录，总耗时 {} ms",
                    drugList.size(), (end-start));

            return drugList;
        }catch (Exception e){
            log.error("查询药品列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Drug getDrugById(Long drugId) {
        log.info("开始根据ID查询药品信息，药品ID: {}", drugId);
        long startTime = System.currentTimeMillis();
        String cacheKey = CACHE_KEY_PREFIX + drugId;

        try {
            if (redisTemplate.hasKey(cacheKey)) {
                try {
                    String cache = redisTemplate.opsForValue().get(cacheKey);
                    if (cache != null && !cache.isEmpty()) {
                        Drug drug = objectMapper.readValue(cache, Drug.class);
                        long endTime = System.currentTimeMillis();
                        log.info("从缓存获取药品信息成功，药品ID: {}，耗时 {} ms", drugId, (endTime - startTime));
                        return drug;
                    }
                } catch (Exception e) {
                    log.warn("缓存数据反序列化失败，删除缓存键 {}，错误信息: {}",
                            cacheKey, e.getMessage(), e);
                    redisTemplate.delete(cacheKey);
                }
            }

            Drug drug = drugMapper.getDrugById(drugId);
            long endTime = System.currentTimeMillis();

            if (drug != null) {
                try {
                    String jsonValue = objectMapper.writeValueAsString(drug);
                    redisTemplate.opsForValue().set(
                            cacheKey,
                            jsonValue,
                            CACHE_EXPIRE_HOURS,
                            TimeUnit.HOURS
                    );
                    log.info("药品信息已存入缓存，药品ID: {}", drugId);
                } catch (Exception e) {
                    log.error("药品信息序列化失败，无法存入缓存，药品ID: {}，错误信息: {}",
                            drugId, e.getMessage(), e);
                }
                log.info("成功查询到ID为 {} 的药品信息，耗时 {} ms", drugId, (endTime - startTime));
            } else {
                log.warn("未找到ID为 {} 的药品信息，耗时 {} ms", drugId, (endTime - startTime));
            }

            return drug;
        } catch (Exception e) {
            log.error("根据ID查询药品信息发生异常，药品ID: {}，错误信息: {}",
                    drugId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Drug getDrugByName(String drugName) {
        log.info("开始根据药品名称查询药品信息，药品名称: {}", drugName);
        long startTime = System.currentTimeMillis();

        try {
            Drug drug = drugMapper.getDrugByName(drugName);
            long endTime = System.currentTimeMillis();

            if (drug == null) {
                log.warn("未找到名称为 {} 的药品信息，耗时 {} ms", drugName, (endTime - startTime));
            } else {
                log.info("成功查询到名称为 {} 的药品信息，耗时 {} ms", drugName, (endTime - startTime));
            }

            return drug;
        } catch (Exception e) {
            log.error("根据药品名称查询药品信息发生异常，药品名称: {}，错误信息: {}",
                    drugName, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addDrug(Drug drug) {
        log.info("开始添加药品，药品名称: {}", drug.getDrugName());
        long startTime = System.currentTimeMillis();

        try {
            // 检查药品名称是否已存在
            Drug existingDrug = drugMapper.getDrugByName(drug.getDrugName());
            if (existingDrug != null) {
                log.warn("药品名称 {} 已存在，无法添加", drug.getDrugName());
                throw new RuntimeException("药品名称已存在");
            }
            // 设置默认状态为启用
            if (drug.getStatus() == null) {
                drug.setStatus(1);
            }

            int result = drugMapper.addDrug(drug);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加药品，药品ID: {}，药品名称: {}，耗时 {} ms",
                        drug.getDrugId(), drug.getDrugName(), (endTime - startTime));
                // 清除缓存
                clearDrugCache();
            } else {
                log.warn("添加药品失败，药品名称: {}，耗时 {} ms", drug.getDrugName(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加药品发生异常，药品名称: {}，错误信息: {}",
                    drug.getDrugName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updateDrug(Drug drug) {
        log.info("开始更新药品信息，药品ID: {}", drug.getDrugId());
        long startTime = System.currentTimeMillis();

        try {
            if (drug.getDrugId() == null) {
                log.error("更新药品信息失败，药品ID为空");
                throw new RuntimeException("药品ID不能为空");
            }

            // 如果更新了药品名称，检查新药品名称是否已存在
            if (drug.getDrugName() != null && !drug.getDrugName().isEmpty()) {
                Drug existingDrug = drugMapper.getDrugByName(drug.getDrugName());
                if (existingDrug != null && !existingDrug.getDrugId().equals(drug.getDrugId())) {
                    log.warn("药品名称 {} 已被其他药品使用，无法更新", drug.getDrugName());
                    throw new RuntimeException("药品名称已被使用");
                }
            }

            int result = drugMapper.updateDrug(drug);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新药品信息，药品ID: {}，耗时 {} ms",
                        drug.getDrugId(), (endTime - startTime));
                // 清除缓存
                clearDrugCache();
            } else {
                log.warn("更新药品信息失败，药品ID: {}，耗时 {} ms",
                        drug.getDrugId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新药品信息发生异常，药品ID: {}，错误信息: {}",
                    drug.getDrugId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteDrug(Long drugId) {
        log.info("开始逻辑删除药品，药品ID: {}", drugId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询药品，确定是否存在
            Drug drug = drugMapper.getDrugById(drugId);
            if (drug == null) {
                log.warn("逻辑删除药品失败，药品ID: {} 不存在", drugId);
                return 0;
            }

            int result = drugMapper.deleteDrug(drugId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功逻辑删除药品，药品ID: {}，药品名称: {}，耗时 {} ms",
                        drugId, drug.getDrugName(), (endTime - startTime));
                // 清除缓存
                clearDrugCache();
            } else {
                log.warn("逻辑删除药品失败，药品ID: {}，耗时 {} ms", drugId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("逻辑删除药品发生异常，药品ID: {}，错误信息: {}",
                    drugId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteDrugPhysical(Long drugId) {
        log.info("开始物理删除药品，药品ID: {}", drugId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询药品，确定是否存在
            Drug drug = drugMapper.getDrugById(drugId);
            if (drug == null) {
                log.warn("物理删除药品失败，药品ID: {} 不存在", drugId);
                return 0;
            }

            int result = drugMapper.deleteDrugPhysical(drugId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功物理删除药品，药品ID: {}，药品名称: {}，耗时 {} ms",
                        drugId, drug.getDrugName(), (endTime - startTime));
                // 清除缓存
                clearDrugCache();
            } else {
                log.warn("物理删除药品失败，药品ID: {}，耗时 {} ms", drugId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("物理删除药品发生异常，药品ID: {}，错误信息: {}",
                    drugId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updateStock(Long drugId, Integer stock) {
        log.info("开始更新药品库存，药品ID: {}，库存: {}", drugId, stock);
        long startTime = System.currentTimeMillis();

        try {
            int result = drugMapper.updateStock(drugId, stock);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新药品库存，药品ID: {}，库存: {}，耗时 {} ms",
                        drugId, stock, (endTime - startTime));
                // 清除缓存
                clearDrugCache();
            } else {
                log.warn("更新药品库存失败，药品ID: {}，耗时 {} ms", drugId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新药品库存发生异常，药品ID: {}，错误信息: {}",
                    drugId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 清除药品列表缓存
     */
    private void clearDrugCache() {
        try {
            redisTemplate.delete(CACHE_KEY_LIST);
            log.debug("已清除药品列表缓存，缓存键: {}", CACHE_KEY_LIST);
        } catch (Exception e) {
            log.warn("清除药品列表缓存失败，错误信息: {}", e.getMessage());
        }
    }
}