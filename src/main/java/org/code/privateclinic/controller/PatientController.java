package org.code.privateclinic.controller;

import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * 获取所有患者列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Patient>> getPatientList(){
        return ResponseMessage.success(patientService.getPatientList());
    }

    /**
     * 根据ID获取患者信息
     */
    @GetMapping("/{patientId}")
    public ResponseMessage<Patient> getPatientById(@PathVariable Long patientId){
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
    public ResponseMessage<List<Patient>> getPatientByDoctorId(@PathVariable Long doctorId){
        return ResponseMessage.success(patientService.getPatientByDoctorId(doctorId));
    }

    /**
     * 添加患者
     */
    @PostMapping
    public ResponseMessage<Patient> addPatient(@RequestBody Patient patient){
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
    public ResponseMessage<String> updatePatient(@RequestBody Patient patient){
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
    public ResponseMessage<String> deletePatient(@PathVariable Long patientId){
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

