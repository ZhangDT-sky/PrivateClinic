package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.MedicalCase;

import java.util.List;

@Mapper
public interface MedicalCaseMapper {

    List<MedicalCase> getMedicalCaseList();

    MedicalCase getMedicalCaseById(Long caseId);

    List<MedicalCase> getMedicalCaseByPatientId(Long patientId);

    List<MedicalCase> getMedicalCaseByDoctorId(Long doctorId);

    List<MedicalCase> getMedicalCaseByStatus(String caseStatus);

    @Options(useGeneratedKeys = true, keyProperty = "caseId")
    int addMedicalCase(MedicalCase medicalCase);

    int updateMedicalCase(MedicalCase medicalCase);

    int deleteMedicalCase(Long caseId);
}

