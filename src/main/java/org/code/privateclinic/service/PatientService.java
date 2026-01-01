package org.code.privateclinic.service;

import org.code.privateclinic.bean.Patient;

import java.util.List;

public interface PatientService {
    /**
     * 获取所有患者列表
     */
    List<Patient> getPatientList();

    /**
     * 根据ID获取患者信息
     */
    Patient getPatientById(Long patientId);

    /**
     * 根据医生ID获取患者列表
     */
    List<Patient> getPatientByDoctorId(Long doctorId);

    /**
     * 添加患者
     */
    int addPatient(Patient patient);

    /**
     * 更新患者信息
     */
    int updatePatient(Patient patient);

    /**
     * 删除患者
     */
    int deletePatient(Long patientId);
}

