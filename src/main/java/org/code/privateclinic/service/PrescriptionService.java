package org.code.privateclinic.service;

import org.code.privateclinic.bean.Prescription;

import java.util.List;

public interface PrescriptionService {
    /**
     * 获取所有处方列表
     */
    List<Prescription> getPrescriptionList();

    /**
     * 根据ID获取处方信息
     */
    Prescription getPrescriptionById(Long prescriptionId);

    /**
     * 根据病例ID获取处方列表
     */
    List<Prescription> getPrescriptionByCaseId(Long caseId);

    /**
     * 根据医生ID获取处方列表
     */
    List<Prescription> getPrescriptionByDoctorId(Long doctorId);

    /**
     * 添加处方
     */
    int addPrescription(Prescription prescription);

    /**
     * 更新处方信息
     */
    int updatePrescription(Prescription prescription);

    /**
     * 删除处方
     */
    int deletePrescription(Long prescriptionId);
}

