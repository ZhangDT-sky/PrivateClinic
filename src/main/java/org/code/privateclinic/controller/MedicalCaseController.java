package org.code.privateclinic.controller;

import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.MedicalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-case")
public class MedicalCaseController {

    @Autowired
    private MedicalCaseService medicalCaseService;

    /**
     * 获取所有病例列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseList(){
        return ResponseMessage.success(medicalCaseService.getMedicalCaseList());
    }

    /**
     * 根据ID获取病例信息
     */
    @GetMapping("/{caseId}")
    public ResponseMessage<MedicalCase> getMedicalCaseById(@PathVariable Long caseId){
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
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByPatientId(@PathVariable Long patientId){
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByPatientId(patientId));
    }

    /**
     * 根据医生ID获取病例列表
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByDoctorId(@PathVariable Long doctorId){
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByDoctorId(doctorId));
    }

    /**
     * 根据状态获取病例列表
     */
    @GetMapping("/status/{caseStatus}")
    public ResponseMessage<List<MedicalCase>> getMedicalCaseByStatus(@PathVariable String caseStatus){
        return ResponseMessage.success(medicalCaseService.getMedicalCaseByStatus(caseStatus));
    }

    /**
     * 添加病例
     */
    @PostMapping
    public ResponseMessage<MedicalCase> addMedicalCase(@RequestBody MedicalCase medicalCase){
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
    public ResponseMessage<String> updateMedicalCase(@RequestBody MedicalCase medicalCase){
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
    public ResponseMessage<String> deleteMedicalCase(@PathVariable Long caseId){
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

