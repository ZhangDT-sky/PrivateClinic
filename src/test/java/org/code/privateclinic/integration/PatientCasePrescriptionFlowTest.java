package org.code.privateclinic.integration;

import org.code.privateclinic.bean.*;
import org.code.privateclinic.service.*;
import org.code.privateclinic.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 整体测试: 查询患者的所有病例和处方
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

