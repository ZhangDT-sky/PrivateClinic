package org.code.privateclinic.service.impl;

import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.bean.Prescription;
import org.code.privateclinic.mapper.MedicalCaseMapper;
import org.code.privateclinic.service.MedicalCaseService;
import org.code.privateclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicalCaseServiceImpl implements MedicalCaseService {

    @Autowired
    private MedicalCaseMapper medicalCaseMapper;

    @Autowired
    private PrescriptionService prescriptionService;

    @Override
    @Loggable("查询病例列表")
    public List<MedicalCase> getMedicalCaseList() {
        return medicalCaseMapper.getMedicalCaseList();
    }

    @Override
    @Loggable("根据ID查询病例信息")
    public MedicalCase getMedicalCaseById(Long caseId) {
        return medicalCaseMapper.getMedicalCaseById(caseId);
    }

    @Override
    @Loggable("根据患者ID查询病例列表")
    public List<MedicalCase> getMedicalCaseByPatientId(Long patientId) {
        return medicalCaseMapper.getMedicalCaseByPatientId(patientId);
    }

    @Override
    @Loggable("根据医生ID查询病例列表")
    public List<MedicalCase> getMedicalCaseByDoctorId(Long doctorId) {
        return medicalCaseMapper.getMedicalCaseByDoctorId(doctorId);
    }

    @Override
    @Loggable("根据状态查询病例列表")
    public List<MedicalCase> getMedicalCaseByStatus(String caseStatus) {
        return medicalCaseMapper.getMedicalCaseByStatus(caseStatus);
    }

    @Override
    @Loggable("添加病例")
    public int addMedicalCase(MedicalCase medicalCase) {
        if (medicalCase.getPatientId() == null) {
            throw new RuntimeException("患者ID不能为空");
        }
        if (medicalCase.getDoctorId() == null) {
            throw new RuntimeException("医生ID不能为空");
        }
        if (medicalCase.getCaseStatus() == null || medicalCase.getCaseStatus().isEmpty()) {
            medicalCase.setCaseStatus("NEW");
        }
        return medicalCaseMapper.addMedicalCase(medicalCase);
    }

    @Override
    @Loggable("更新病例信息")
    public int updateMedicalCase(MedicalCase medicalCase) {
        if (medicalCase.getCaseId() == null) {
            throw new RuntimeException("病例ID不能为空");
        }
        return medicalCaseMapper.updateMedicalCase(medicalCase);
    }

    @Override
    @Loggable("删除病例")
    @Transactional(rollbackFor = Exception.class)
    public int deleteMedicalCase(Long caseId) {
        MedicalCase medicalCase = medicalCaseMapper.getMedicalCaseById(caseId);
        if (medicalCase == null) {
            return 0;
        }
        // 先删除该病例下的所有处方（deletePrescription方法内部会先删除处方项）
        List<Prescription> prescriptions = prescriptionService.getPrescriptionByCaseId(caseId);
        for (Prescription prescription : prescriptions) {
            prescriptionService.deletePrescription(prescription.getPrescriptionId());
        }
        // 再删除病例（父记录）
        return medicalCaseMapper.deleteMedicalCase(caseId);
    }
}
