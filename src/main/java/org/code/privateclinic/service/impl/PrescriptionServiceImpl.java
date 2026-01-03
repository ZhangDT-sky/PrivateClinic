package org.code.privateclinic.service.impl;

import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.Prescription;
import org.code.privateclinic.mapper.PrescriptionMapper;
import org.code.privateclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Override
    @Loggable("查询处方列表")
    public List<Prescription> getPrescriptionList() {
        return prescriptionMapper.getPrescriptionList();
    }

    @Override
    @Loggable("根据ID查询处方信息")
    public Prescription getPrescriptionById(Long prescriptionId) {
        return prescriptionMapper.getPrescriptionById(prescriptionId);
    }

    @Override
    @Loggable("根据病例ID查询处方列表")
    public List<Prescription> getPrescriptionByCaseId(Long caseId) {
        return prescriptionMapper.getPrescriptionByCaseId(caseId);
    }

    @Override
    @Loggable("根据医生ID查询处方列表")
    public List<Prescription> getPrescriptionByDoctorId(Long doctorId) {
        return prescriptionMapper.getPrescriptionByDoctorId(doctorId);
    }

    @Override
    @Loggable("添加处方")
    public int addPrescription(Prescription prescription) {
        if (prescription.getCaseId() == null) {
            throw new RuntimeException("病例ID不能为空");
        }
        if (prescription.getDoctorId() == null) {
            throw new RuntimeException("医生ID不能为空");
        }
        return prescriptionMapper.addPrescription(prescription);
    }

    @Override
    @Loggable("更新处方信息")
    public int updatePrescription(Prescription prescription) {
        if (prescription.getPrescriptionId() == null) {
            throw new RuntimeException("处方ID不能为空");
        }
        return prescriptionMapper.updatePrescription(prescription);
    }

    @Override
    @Loggable("删除处方")
    public int deletePrescription(Long prescriptionId) {
        Prescription prescription = prescriptionMapper.getPrescriptionById(prescriptionId);
        if (prescription == null) {
            return 0;
        }
        // 数据库级联删除：删除处方时，会自动删除相关的处方项
        return prescriptionMapper.deletePrescription(prescriptionId);
    }
}
