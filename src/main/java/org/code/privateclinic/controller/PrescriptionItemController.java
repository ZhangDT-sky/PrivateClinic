package org.code.privateclinic.controller;

import org.code.privateclinic.bean.PrescriptionItem;
import org.code.privateclinic.common.ResponseMessage;
import org.code.privateclinic.service.PrescriptionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription-item")
public class PrescriptionItemController {

    @Autowired
    private PrescriptionItemService prescriptionItemService;

    /**
     * 获取所有处方明细列表
     */
    @GetMapping("/list")
    public ResponseMessage<List<PrescriptionItem>> getPrescriptionItemList(){
        return ResponseMessage.success(prescriptionItemService.getPrescriptionItemList());
    }

    /**
     * 根据ID获取处方明细信息
     */
    @GetMapping("/{itemId}")
    public ResponseMessage<PrescriptionItem> getPrescriptionItemById(@PathVariable Long itemId){
        PrescriptionItem prescriptionItem = prescriptionItemService.getPrescriptionItemById(itemId);
        if(prescriptionItem == null){
            return ResponseMessage.failed("未查询到相关处方明细信息");
        }
        return ResponseMessage.success(prescriptionItem);
    }

    /**
     * 根据处方ID获取处方明细列表
     */
    @GetMapping("/prescription/{prescriptionId}")
    public ResponseMessage<List<PrescriptionItem>> getPrescriptionItemByPrescriptionId(@PathVariable Long prescriptionId){
        return ResponseMessage.success(prescriptionItemService.getPrescriptionItemByPrescriptionId(prescriptionId));
    }

    /**
     * 添加处方明细
     */
    @PostMapping
    public ResponseMessage<PrescriptionItem> addPrescriptionItem(@RequestBody PrescriptionItem prescriptionItem){
        try {
            int result = prescriptionItemService.addPrescriptionItem(prescriptionItem);
            if(result > 0){
                return ResponseMessage.success(prescriptionItem, "添加处方明细成功");
            } else {
                return ResponseMessage.failed("添加处方明细失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 更新处方明细信息
     */
    @PutMapping
    public ResponseMessage<String> updatePrescriptionItem(@RequestBody PrescriptionItem prescriptionItem){
        try {
            int result = prescriptionItemService.updatePrescriptionItem(prescriptionItem);
            if(result > 0){
                return ResponseMessage.success("更新处方明细信息成功");
            } else {
                return ResponseMessage.failed("更新处方明细信息失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 删除处方明细
     */
    @DeleteMapping("/{itemId}")
    public ResponseMessage<String> deletePrescriptionItem(@PathVariable Long itemId){
        try {
            int result = prescriptionItemService.deletePrescriptionItem(itemId);
            if(result > 0){
                return ResponseMessage.success("删除处方明细成功");
            } else {
                return ResponseMessage.failed("删除处方明细失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }

    /**
     * 根据处方ID删除所有处方明细
     */
    @DeleteMapping("/prescription/{prescriptionId}")
    public ResponseMessage<String> deletePrescriptionItemByPrescriptionId(@PathVariable Long prescriptionId){
        try {
            int result = prescriptionItemService.deletePrescriptionItemByPrescriptionId(prescriptionId);
            if(result > 0){
                return ResponseMessage.success("删除处方明细成功");
            } else {
                return ResponseMessage.failed("删除处方明细失败");
            }
        } catch (Exception e) {
            return ResponseMessage.failed(e.getMessage());
        }
    }
}

