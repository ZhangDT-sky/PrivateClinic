package org.code.privateclinic.service;

import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.mapper.PatientMapper;
import org.code.privateclinic.service.impl.PatientServiceImpl;
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
 * 患者服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setPatientId(1L);
        testPatient.setPatientName("张三");
        testPatient.setGender("男");
        testPatient.setAge(30);
        testPatient.setPhone("13800138000");
        testPatient.setDoctorId(1L);
    }

    /**
     * 单元测试1: 添加患者成功
     */
    @Test
    void testAddPatient_Success() {
        when(patientMapper.addPatient(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setPatientId(1L);
            return 1;
        });

        int result = patientService.addPatient(testPatient);

        assertEquals(1, result);
        assertNotNull(testPatient.getPatientId());
        verify(patientMapper, times(1)).addPatient(any(Patient.class));
    }

    /**
     * 单元测试2: 查询患者列表
     */
    @Test
    void testGetPatientList() {
        List<Patient> mockPatients = List.of(testPatient);
        when(patientMapper.getPatientList()).thenReturn(mockPatients);

        List<Patient> result = patientService.getPatientList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getPatientName());
        verify(patientMapper, times(1)).getPatientList();
    }
}

