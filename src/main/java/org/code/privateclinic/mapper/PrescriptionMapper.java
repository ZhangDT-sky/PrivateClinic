package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.Prescription;

import java.util.List;

@Mapper
public interface PrescriptionMapper {

    List<Prescription> getPrescriptionList();

    Prescription getPrescriptionById(Long prescriptionId);

    List<Prescription> getPrescriptionByCaseId(Long caseId);

    List<Prescription> getPrescriptionByDoctorId(Long doctorId);

    @Options(useGeneratedKeys = true, keyProperty = "prescriptionId")
    int addPrescription(Prescription prescription);

    int updatePrescription(Prescription prescription);

    int deletePrescription(Long prescriptionId);
}

