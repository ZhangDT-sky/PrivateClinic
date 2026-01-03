package org.code.privateclinic.service.impl;

import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.mapper.PatientMapper;
import org.code.privateclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    @Loggable("查询患者列表")
    public List<Patient> getPatientList() {
        return patientMapper.getPatientList();
    }

    @Override
    @Loggable("根据ID查询患者信息")
    public Patient getPatientById(Long patientId) {
        return patientMapper.getPatientById(patientId);
    }

    @Override
    @Loggable("根据医生ID查询患者列表")
    public List<Patient> getPatientByDoctorId(Long doctorId) {
        return patientMapper.getPatientByDoctorId(doctorId);
    }

    @Override
    @Loggable("添加患者")
    public int addPatient(Patient patient) {
        if (patient.getDoctorId() == null) {
            throw new RuntimeException("负责医生ID不能为空");
        }
        return patientMapper.addPatient(patient);
    }

    @Override
    @Loggable("更新患者信息")
    public int updatePatient(Patient patient) {
        if (patient.getPatientId() == null) {
            throw new RuntimeException("患者ID不能为空");
        }
        return patientMapper.updatePatient(patient);
    }

    @Override
    @Loggable("删除患者")
    public int deletePatient(Long patientId) {
        Patient patient = patientMapper.getPatientById(patientId);
        if (patient == null) {
            return 0;
        }
        // 数据库级联删除：删除患者时，会自动删除相关的病历、处方和处方项
        return patientMapper.deletePatient(patientId);
    }
}
