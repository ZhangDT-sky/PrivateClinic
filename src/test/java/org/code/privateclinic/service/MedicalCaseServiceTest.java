package org.code.privateclinic.service;

import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.mapper.MedicalCaseMapper;
import org.code.privateclinic.service.impl.MedicalCaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 病例服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class MedicalCaseServiceTest {

    @Mock
    private MedicalCaseMapper medicalCaseMapper;

    @InjectMocks
    private MedicalCaseServiceImpl medicalCaseService;

    private MedicalCase testMedicalCase;

    @BeforeEach
    void setUp() {
        testMedicalCase = new MedicalCase();
        testMedicalCase.setCaseId(1L);
        testMedicalCase.setPatientId(1L);
        testMedicalCase.setDoctorId(1L);
        testMedicalCase.setSymptom("头痛、发热");
        testMedicalCase.setDiagnosis("感冒");
        testMedicalCase.setCaseStatus("NEW");
    }
    /**
     * 单元测试: 根据患者ID查询病例列表
     */
    @Test
    void testGetMedicalCaseByPatientId() {
        List<MedicalCase> mockCases = List.of(testMedicalCase);
        when(medicalCaseMapper.getMedicalCaseByPatientId(1L)).thenReturn(mockCases);

        List<MedicalCase> result = medicalCaseService.getMedicalCaseByPatientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("NEW", result.get(0).getCaseStatus());
        verify(medicalCaseMapper, times(1)).getMedicalCaseByPatientId(1L);
    }
}

