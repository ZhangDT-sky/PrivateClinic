package org.code.privateclinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.PatientService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取所有患者列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Patient>> getPatientList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看患者列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看患者列表");
        }
        return ResponseMessage.success(patientService.getPatientList());
    }

    /**
     * 根据ID获取患者信息
     */
    @GetMapping("/{patientId}")
    public ResponseMessage<Patient> getPatientById(HttpServletRequest request, @PathVariable Long patientId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看患者信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看患者信息");
        }
        Patient patient = patientService.getPatientById(patientId);
        if(patient == null){
            return ResponseMessage.failed("未查询到相关患者信息");
        }
        return ResponseMessage.success(patient);
    }

    /**
     * 根据医生ID获取患者列表
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseMessage<List<Patient>> getPatientByDoctorId(HttpServletRequest request, @PathVariable Long doctorId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看患者列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看患者列表");
        }
        return ResponseMessage.success(patientService.getPatientByDoctorId(doctorId));
    }

    /**
     * 添加患者
     */
    @PostMapping
    public ResponseMessage<Patient> addPatient(HttpServletRequest request, @RequestBody Patient patient){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能添加患者
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权添加患者");
        }
        try {
            int result = patientService.addPatient(patient);
            if(result > 0){
                return ResponseMessage.success(patient, "添加患者成功");
            } else {
                return ResponseMessage.failed("添加患者失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新患者信息
     */
    @PutMapping
    public ResponseMessage<String> updatePatient(HttpServletRequest request, @RequestBody Patient patient){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能更新患者信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权更新患者信息");
        }
        try {
            int result = patientService.updatePatient(patient);
            if(result > 0){
                return ResponseMessage.success("更新患者信息成功");
            } else {
                return ResponseMessage.failed("更新患者信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 删除患者
     */
    @DeleteMapping("/{patientId}")
    public ResponseMessage<String> deletePatient(HttpServletRequest request, @PathVariable Long patientId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能删除患者
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权删除患者");
        }
        try {
            int result = patientService.deletePatient(patientId);
            if(result > 0){
                return ResponseMessage.success("删除患者成功");
            } else {
                return ResponseMessage.failed("删除患者失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

