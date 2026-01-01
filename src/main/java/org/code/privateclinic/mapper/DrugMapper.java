package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.Drug;

import java.util.List;

@Mapper
public interface DrugMapper {

    List<Drug> getDrugList();

    Drug getDrugById(Long drugId);

    Drug getDrugByName(String drugName);

    @Options(useGeneratedKeys = true, keyProperty = "drugId")
    int addDrug(Drug drug);

    int updateDrug(Drug drug);

    int deleteDrug(Long drugId);

    int deleteDrugPhysical(Long drugId);

    int updateStock(Long drugId, Integer stock);
}
