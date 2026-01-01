package org.code.privateclinic.controller;

import org.code.privateclinic.bean.Prescription;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * 获取所有处方列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Prescription>> getPrescriptionList(){
        return ResponseMessage.success(prescriptionService.getPrescriptionList());
    }

    /**
     * 根据ID获取处方信息
     */
    @GetMapping("/{prescriptionId}")
    public ResponseMessage<Prescription> getPrescriptionById(@PathVariable Long prescriptionId){
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
    public ResponseMessage<List<Prescription>> getPrescriptionByCaseId(@PathVariable Long caseId){
        return ResponseMessage.success(prescriptionService.getPrescriptionByCaseId(caseId));
    }

    /**
     * 根据医生ID获取处方列表
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseMessage<List<Prescription>> getPrescriptionByDoctorId(@PathVariable Long doctorId){
        return ResponseMessage.success(prescriptionService.getPrescriptionByDoctorId(doctorId));
    }

    /**
     * 添加处方
     */
    @PostMapping
    public ResponseMessage<Prescription> addPrescription(@RequestBody Prescription prescription){
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
    public ResponseMessage<String> updatePrescription(@RequestBody Prescription prescription){
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
    public ResponseMessage<String> deletePrescription(@PathVariable Long prescriptionId){
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

