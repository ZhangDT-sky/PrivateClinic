package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.PrescriptionItem;

import java.util.List;

@Mapper
public interface PrescriptionItemMapper {

    List<PrescriptionItem> getPrescriptionItemList();

    PrescriptionItem getPrescriptionItemById(Long itemId);

    List<PrescriptionItem> getPrescriptionItemByPrescriptionId(Long prescriptionId);

    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int addPrescriptionItem(PrescriptionItem prescriptionItem);

    int updatePrescriptionItem(PrescriptionItem prescriptionItem);

    int deletePrescriptionItem(Long itemId);

    int deletePrescriptionItemByPrescriptionId(Long prescriptionId);
}

