package org.code.privateclinic.controller;

import org.code.privateclinic.bean.Drug;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    private DrugService drugService;

    /**
     * 获取所有药品列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<Drug>> getDrugList(){
        return ResponseMessage.success(drugService.getDrugList());
    }

    /**
     * 根据ID获取药品信息
     */
    @GetMapping("/{drugId}")
    public ResponseMessage<Drug> getDrugById(@PathVariable Long drugId){
        Drug drug = drugService.getDrugById(drugId);
        if(drug == null){
            return ResponseMessage.failed("未查询到相关药品信息");
        }
        return ResponseMessage.success(drug);
    }

    /**
     * 根据药品名称获取药品信息
     */
    @GetMapping("/name/{drugName}")
    public ResponseMessage<Drug> getDrugByName(@PathVariable String drugName){
        Drug drug = drugService.getDrugByName(drugName);
        if(drug == null){
            return ResponseMessage.failed("未查询到相关药品信息");
        }
        return ResponseMessage.success(drug);
    }

    /**
     * 添加药品
     */
    @PostMapping
    public ResponseMessage<Drug> addDrug(@RequestBody Drug drug){
        try {
            int result = drugService.addDrug(drug);
            if(result > 0){
                return ResponseMessage.success(drug, "添加药品成功");
            } else {
                return ResponseMessage.failed("添加药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新药品信息
     */
    @PutMapping
    public ResponseMessage<String> updateDrug(@RequestBody Drug drug){
        try {
            int result = drugService.updateDrug(drug);
            if(result > 0){
                return ResponseMessage.success("更新药品信息成功");
            } else {
                return ResponseMessage.failed("更新药品信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 逻辑删除药品（更新状态为0）
     */
    @DeleteMapping("/{drugId}")
    public ResponseMessage<String> deleteDrug(@PathVariable Long drugId){
        try {
            int result = drugService.deleteDrug(drugId);
            if(result > 0){
                return ResponseMessage.success("删除药品成功");
            } else {
                return ResponseMessage.failed("删除药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 物理删除药品
     */
    @DeleteMapping("/physical/{drugId}")
    public ResponseMessage<String> deleteDrugPhysical(@PathVariable Long drugId){
        try {
            int result = drugService.deleteDrugPhysical(drugId);
            if(result > 0){
                return ResponseMessage.success("物理删除药品成功");
            } else {
                return ResponseMessage.failed("物理删除药品失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新药品库存
     */
    @PutMapping("/stock/{drugId}")
    public ResponseMessage<String> updateStock(@PathVariable Long drugId, @RequestParam Integer stock){
        try {
            int result = drugService.updateStock(drugId, stock);
            if(result > 0){
                return ResponseMessage.success("更新药品库存成功");
            } else {
                return ResponseMessage.failed("更新药品库存失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

