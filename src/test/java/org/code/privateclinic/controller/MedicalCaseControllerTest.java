package org.code.privateclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.service.MedicalCaseService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicalCase testMedicalCase;

    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_USERNAME = "doctor1";

    @BeforeEach
    void setUp() {
        testMedicalCase = new MedicalCase();
        testMedicalCase.setCaseId(1L);
        testMedicalCase.setPatientId(1L);
        testMedicalCase.setDoctorId(1L);
        testMedicalCase.setSymptom("头痛、发热");
        testMedicalCase.setDiagnosis("感冒");
        testMedicalCase.setCaseStatus("NEW");

        // 模拟认证：token -> 用户名 -> 医生用户
        when(jwtTokenUtil.getUsernameFromToken(TEST_TOKEN)).thenReturn(TEST_USERNAME);
        User doctor = new User();
        doctor.setUserName(TEST_USERNAME);
        doctor.setRole("DOCTOR");
        when(userService.getUserByUserName(TEST_USERNAME)).thenReturn(doctor);
    }

    /**
     * 功能测试: 根据患者ID查询病例列表接口
     */
    @Test
    void testGetMedicalCaseByPatientId() throws Exception {
        List<MedicalCase> cases = List.of(testMedicalCase);
        when(medicalCaseService.getMedicalCaseByPatientId(1L)).thenReturn(cases);


        mockMvc.perform(get("/medical-case/patient/1")
                        .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].patientId").value(1));
    }
}

