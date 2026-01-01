package org.code.privateclinic.service;

import org.code.privateclinic.bean.PrescriptionItem;

import java.util.List;

public interface PrescriptionItemService {
    /**
     * 获取所有处方明细列表
     */
    List<PrescriptionItem> getPrescriptionItemList();

    /**
     * 根据ID获取处方明细信息
     */
    PrescriptionItem getPrescriptionItemById(Long itemId);

    /**
     * 根据处方ID获取处方明细列表
     */
    List<PrescriptionItem> getPrescriptionItemByPrescriptionId(Long prescriptionId);

    /**
     * 添加处方明细
     */
    int addPrescriptionItem(PrescriptionItem prescriptionItem);

    /**
     * 更新处方明细信息
     */
    int updatePrescriptionItem(PrescriptionItem prescriptionItem);

    /**
     * 删除处方明细
     */
    int deletePrescriptionItem(Long itemId);

    /**
     * 根据处方ID删除所有处方明细
     */
    int deletePrescriptionItemByPrescriptionId(Long prescriptionId);
}

