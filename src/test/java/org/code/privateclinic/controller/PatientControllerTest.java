package org.code.privateclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 患者控制器功能测试
 */
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

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
     * 功能测试1: 添加患者接口
     */
    @Test
    void testAddPatient() throws Exception {
        when(patientService.addPatient(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setPatientId(1L);
            return 1;
        });

        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.patientName").value("张三"));
    }

    /**
     * 功能测试2: 获取患者列表接口
     */
    @Test
    void testGetPatientList() throws Exception {
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientService.getPatientList()).thenReturn(patients);

        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].patientName").value("张三"));
    }
}

