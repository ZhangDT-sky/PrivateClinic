package org.code.privateclinic.service.impl;

import org.code.privateclinic.annotation.Loggable;
import org.code.privateclinic.bean.PrescriptionItem;
import org.code.privateclinic.mapper.PrescriptionItemMapper;
import org.code.privateclinic.service.PrescriptionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionItemServiceImpl implements PrescriptionItemService {

    @Autowired
    private PrescriptionItemMapper prescriptionItemMapper;

    @Override
    @Loggable("查询处方明细列表")
    public List<PrescriptionItem> getPrescriptionItemList() {
        return prescriptionItemMapper.getPrescriptionItemList();
    }

    @Override
    @Loggable("根据ID查询处方明细信息")
    public PrescriptionItem getPrescriptionItemById(Long itemId) {
        return prescriptionItemMapper.getPrescriptionItemById(itemId);
    }

    @Override
    @Loggable("根据处方ID查询处方明细列表")
    public List<PrescriptionItem> getPrescriptionItemByPrescriptionId(Long prescriptionId) {
        return prescriptionItemMapper.getPrescriptionItemByPrescriptionId(prescriptionId);
    }

    @Override
    @Loggable("添加处方明细")
    public int addPrescriptionItem(PrescriptionItem prescriptionItem) {
        if (prescriptionItem.getPrescriptionId() == null) {
            throw new RuntimeException("处方ID不能为空");
        }
        if (prescriptionItem.getDrugId() == null) {
            throw new RuntimeException("药品ID不能为空");
        }
        if (prescriptionItem.getQuantity() == null || prescriptionItem.getQuantity() <= 0) {
            throw new RuntimeException("数量必须大于0");
        }
        return prescriptionItemMapper.addPrescriptionItem(prescriptionItem);
    }

    @Override
    @Loggable("更新处方明细信息")
    public int updatePrescriptionItem(PrescriptionItem prescriptionItem) {
        if (prescriptionItem.getItemId() == null) {
            throw new RuntimeException("明细ID不能为空");
        }
        return prescriptionItemMapper.updatePrescriptionItem(prescriptionItem);
    }

    @Override
    @Loggable("删除处方明细")
    public int deletePrescriptionItem(Long itemId) {
        PrescriptionItem prescriptionItem = prescriptionItemMapper.getPrescriptionItemById(itemId);
        if (prescriptionItem == null) {
            return 0;
        }
        return prescriptionItemMapper.deletePrescriptionItem(itemId);
    }

    @Override
    @Loggable("根据处方ID删除所有处方明细")
    public int deletePrescriptionItemByPrescriptionId(Long prescriptionId) {
        return prescriptionItemMapper.deletePrescriptionItemByPrescriptionId(prescriptionId);
    }
}
