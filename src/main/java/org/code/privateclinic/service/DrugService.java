package org.code.privateclinic.service;

import org.code.privateclinic.bean.Drug;

import java.util.List;

public interface DrugService {
    /**
     * 获取所有药品列表
     */
    List<Drug> getDrugList();

    /**
     * 根据ID获取药品信息
     */
    Drug getDrugById(Long drugId);

    /**
     * 根据药品名称获取药品信息
     */
    Drug getDrugByName(String drugName);

    /**
     * 添加药品
     */
    int addDrug(Drug drug);

    /**
     * 更新药品信息
     */
    int updateDrug(Drug drug);

    /**
     * 删除药品（逻辑删除）
     */
    int deleteDrug(Long drugId);

    /**
     * 物理删除药品
     */
    int deleteDrugPhysical(Long drugId);

    /**
     * 更新药品库存
     */
    int updateStock(Long drugId, Integer stock);
}