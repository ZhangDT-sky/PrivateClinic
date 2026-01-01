package org.code.privateclinic.service;

import org.code.privateclinic.bean.MedicalCase;

import java.util.List;

public interface MedicalCaseService {
    /**
     * 获取所有病例列表
     */
    List<MedicalCase> getMedicalCaseList();

    /**
     * 根据ID获取病例信息
     */
    MedicalCase getMedicalCaseById(Long caseId);

    /**
     * 根据患者ID获取病例列表
     */
    List<MedicalCase> getMedicalCaseByPatientId(Long patientId);

    /**
     * 根据医生ID获取病例列表
     */
    List<MedicalCase> getMedicalCaseByDoctorId(Long doctorId);

    /**
     * 根据状态获取病例列表
     */
    List<MedicalCase> getMedicalCaseByStatus(String caseStatus);

    /**
     * 添加病例
     */
    int addMedicalCase(MedicalCase medicalCase);

    /**
     * 更新病例信息
     */
    int updateMedicalCase(MedicalCase medicalCase);

    /**
     * 删除病例
     */
    int deleteMedicalCase(Long caseId);
}

