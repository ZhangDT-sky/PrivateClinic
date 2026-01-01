package org.code.privateclinic.integration;

import org.code.privateclinic.bean.*;
import org.code.privateclinic.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 整体测试：患者-病例-处方完整流程测试
 */
@SpringBootTest
@Transactional
@Rollback
class PatientCasePrescriptionFlowTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionItemService prescriptionItemService;
    /**
     * 整体测试1: 完整业务流程 - 添加患者 -> 创建病例 -> 开处方 -> 添加处方明细
     */
    @Test
    void testAll(){
        //1. 添加患者
        Patient patient = new Patient();
        patient.setPatientName("李四");
        patient.setGender("女");
        patient.setAge(25);
        patient.setPhone("13900139000");
        patient.setDoctorId(1L);

        int patientResult = patientService.addPatient(patient);
        assertEquals(1, patientResult);
        assertNotNull(patient.getPatientId());

        Long patientId = patient.getPatientId();

        //2. 创建病例
        MedicalCase medicalCase = new MedicalCase();
        medicalCase.setPatientId(patientId);
        medicalCase.setDoctorId(1L);
        medicalCase.setSymptom("咳嗽、流鼻涕");
        medicalCase.setDiagnosis("上呼吸道感染");
        medicalCase.setCaseStatus("NEW");

        int caseResult = medicalCaseService.addMedicalCase(medicalCase);
        assertEquals(1, caseResult);
        assertNotNull(medicalCase.getCaseId());
        Long caseId = medicalCase.getCaseId();

        //3. 创建处方
        Prescription prescription = new Prescription();
        prescription.setCaseId(caseId);
        prescription.setDoctorId(1L);
        prescription.setTotalAmount(new BigDecimal("150.00"));

        int prescriptionResult = prescriptionService.addPrescription(prescription);
        assertEquals(1, prescriptionResult);
        assertNotNull(prescription.getPrescriptionId());
        Long prescriptionId = prescription.getPrescriptionId();

        //4. 添加处方明细
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionId(prescriptionId);
        item.setDrugId(1L);
        item.setQuantity(2);
        item.setUsageMethod("一日三次，每次一片");
        item.setPrice(new BigDecimal("75.00"));

        int itemResult = prescriptionItemService.addPrescriptionItem(item);
        assertEquals(1, itemResult);
        assertNotNull(item.getItemId());

        // 验证：查询处方明细
        List<PrescriptionItem> items = prescriptionItemService.getPrescriptionItemByPrescriptionId(prescriptionId);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
    }

    /**
     * 整体测试2: 查询患者的所有病例和处方
     */
    @Test
    void testQuery(){
        Long patientId = 1L;

        //查询该患者的所有病例
        List<MedicalCase> cases = medicalCaseService.getMedicalCaseByPatientId(patientId);

        assertNotNull(cases);
        // 如果有病例，验证每个病例都可以查询到对应的处方
        if(!cases.isEmpty()){
            MedicalCase medicalCase = cases.get(0);
            List<Prescription> prescriptions = prescriptionService.getPrescriptionByCaseId(medicalCase.getCaseId());
            assertNotNull(prescriptions);
        }
    }
}

