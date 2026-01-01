package org.code.privateclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.service.MedicalCaseService;
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
 * 病例控制器功能测试
 */
@WebMvcTest(MedicalCaseController.class)
class MedicalCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalCaseService medicalCaseService;

    @Autowired
    private ObjectMapper objectMapper;

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
     * 功能测试 1: 添加病例接口
     */
    @Test
    void testAddMedicalCase() throws Exception {
        when(medicalCaseService.addMedicalCase(any(MedicalCase.class))).thenAnswer(invocation -> {
            MedicalCase medicalCase = invocation.getArgument(0);
            medicalCase.setCaseId(1L);
            return 1;
        });

        mockMvc.perform(post("/medical-case")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMedicalCase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.caseStatus").value("NEW"));
    }

    /**
     * 功能测试 2: 根据患者ID查询病例列表接口
     */
    @Test
    void testGetMedicalCaseByPatientId() throws Exception {
        List<MedicalCase> cases = Arrays.asList(testMedicalCase);
        when(medicalCaseService.getMedicalCaseByPatientId(1L)).thenReturn(cases);


        mockMvc.perform(get("/medical-case/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].patientId").value(1));
    }
}

