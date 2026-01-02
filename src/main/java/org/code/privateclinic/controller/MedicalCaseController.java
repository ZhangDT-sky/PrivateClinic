package org.code.privateclinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.MedicalCaseService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-case")
public class MedicalCaseController {

    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取所有病例列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看病例列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看病例列表");
        }
        return ResponseMessage.success(medicalCaseService.getMedicalCaseList());
    }

    /**
     * 根据ID获取病例信息
     */
    @GetMapping("/{caseId}")
    public ResponseMessage<MedicalCase> getMedicalCaseById(HttpServletRequest request, @PathVariable Long caseId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看病例信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看病例信息");
        }
        MedicalCase medicalCase = medicalCaseService.getMedicalCaseById(caseId);
        if(medicalCase == null){
            return ResponseMessage.failed("未查询到相关病例信息");
        }
        return ResponseMessage.success(medicalCase);
    }

    /**
     * 根据患者ID获取病例列表
     */
    @GetMapping("/patient/{patientId}")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByPatientId(HttpServletRequest request, @PathVariable Long patientId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看病例列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看病例列表");
        }
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByPatientId(patientId));
    }

    /**
     * 根据医生ID获取病例列表
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByDoctorId(HttpServletRequest request, @PathVariable Long doctorId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看病例列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看病例列表");
        }
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByDoctorId(doctorId));
    }

    /**
     * 根据状态获取病例列表
     */
    @GetMapping("/status/{caseStatus}")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByStatus(HttpServletRequest request, @PathVariable String caseStatus){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看病例列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看病例列表");
        }
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByStatus(caseStatus));
    }

    /**
     * 添加病例
     */
    @PostMapping
    public ResponseMessage<MedicalCase> addMedicalCase(HttpServletRequest request, @RequestBody MedicalCase medicalCase){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能添加病例
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权添加病例");
        }
        try {
            int result = medicalCaseService.addMedicalCase(medicalCase);
            if(result > 0){
                return ResponseMessage.success(medicalCase, "添加病例成功");
            } else {
                return ResponseMessage.failed("添加病例失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新病例信息
     */
    @PutMapping
    public ResponseMessage<String> updateMedicalCase(HttpServletRequest request, @RequestBody MedicalCase medicalCase){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能更新病例信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权更新病例信息");
        }
        try {
            int result = medicalCaseService.updateMedicalCase(medicalCase);
            if(result > 0){
                return ResponseMessage.success("更新病例信息成功");
            } else {
                return ResponseMessage.failed("更新病例信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 删除病例
     */
    @DeleteMapping("/{caseId}")
    public ResponseMessage<String> deleteMedicalCase(HttpServletRequest request, @PathVariable Long caseId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能删除病例
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权删除病例");
        }
        try {
            int result = medicalCaseService.deleteMedicalCase(caseId);
            if(result > 0){
                return ResponseMessage.success("删除病例成功");
            } else {
                return ResponseMessage.failed("删除病例失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

