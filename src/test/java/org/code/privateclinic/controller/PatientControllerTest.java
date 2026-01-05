package org.code.privateclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.service.PatientService;
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

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserService userService;

    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_USERNAME = "doctor1";

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setPatientId(1L);
        testPatient.setPatientName("张三");
        testPatient.setGender("男");
        testPatient.setAge(30);
        testPatient.setPhone("13800138000");
        testPatient.setDoctorId(1L);

        // 模拟认证：token -> 用户名 -> 医生用户
        when(jwtTokenUtil.getUsernameFromToken(TEST_TOKEN)).thenReturn(TEST_USERNAME);
        User doctor = new User();
        doctor.setUserName(TEST_USERNAME);
        doctor.setRole("DOCTOR");
        when(userService.getUserByUserName(TEST_USERNAME)).thenReturn(doctor);
    }


    /**
     * 功能测试: 获取患者列表接口
     */
    @Test
    void testGetPatientList() throws Exception {
        List<Patient> patients = List.of(testPatient);
        when(patientService.getPatientList()).thenReturn(patients);

        mockMvc.perform(get("/patient/list")
                        .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].patientName").value("张三"));
    }
}

