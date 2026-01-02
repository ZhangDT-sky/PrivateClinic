package org.code.privateclinic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.code.privateclinic.bean.Prescription;
import org.code.privateclinic.bean.User;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.PrescriptionService;
import org.code.privateclinic.service.UserService;
import org.code.privateclinic.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    /**
     * 获取所有处方列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Prescription>> getPrescriptionList(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看处方列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看处方列表");
        }
        return ResponseMessage.success(prescriptionService.getPrescriptionList());
    }

    /**
     * 根据ID获取处方信息
     */
    @GetMapping("/{prescriptionId}")
    public ResponseMessage<Prescription> getPrescriptionById(HttpServletRequest request, @PathVariable Long prescriptionId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看处方信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看处方信息");
        }
        Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
        if(prescription == null){
            return ResponseMessage.failed("未查询到相关处方信息");
        }
        return ResponseMessage.success(prescription);
    }

    /**
     * 根据病例ID获取处方列表
     */
    @GetMapping("/case/{caseId}")
    public ResponseMessage<List<Prescription>> getPrescriptionByCaseId(HttpServletRequest request, @PathVariable Long caseId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看处方列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看处方列表");
        }
        return ResponseMessage.success(prescriptionService.getPrescriptionByCaseId(caseId));
    }

    /**
     * 根据医生ID获取处方列表
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseMessage<List<Prescription>> getPrescriptionByDoctorId(HttpServletRequest request, @PathVariable Long doctorId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能查看处方列表
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权查看处方列表");
        }
        return ResponseMessage.success(prescriptionService.getPrescriptionByDoctorId(doctorId));
    }

    /**
     * 添加处方
     */
    @PostMapping
    public ResponseMessage<Prescription> addPrescription(HttpServletRequest request, @RequestBody Prescription prescription){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能添加处方
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权添加处方");
        }
        try {
            int result = prescriptionService.addPrescription(prescription);
            if(result > 0){
                return ResponseMessage.success(prescription, "添加处方成功");
            } else {
                return ResponseMessage.failed("添加处方失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新处方信息
     */
    @PutMapping
    public ResponseMessage<String> updatePrescription(HttpServletRequest request, @RequestBody Prescription prescription){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能更新处方信息
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权更新处方信息");
        }
        try {
            int result = prescriptionService.updatePrescription(prescription);
            if(result > 0){
                return ResponseMessage.success("更新处方信息成功");
            } else {
                return ResponseMessage.failed("更新处方信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 删除处方
     */
    @DeleteMapping("/{prescriptionId}")
    public ResponseMessage<String> deletePrescription(HttpServletRequest request, @PathVariable Long prescriptionId){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUserName(username);
        // 只有医生能删除处方
        if (user == null || (!"DOCTOR".equalsIgnoreCase(user.getRole()))) {
            return ResponseMessage.failed("只有医生有权删除处方");
        }
        try {
            int result = prescriptionService.deletePrescription(prescriptionId);
            if(result > 0){
                return ResponseMessage.success("删除处方成功");
            } else {
                return ResponseMessage.failed("删除处方失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

