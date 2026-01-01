package org.code.privateclinic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.code.privateclinic.bean.Patient;

import java.util.List;

@Mapper
public interface PatientMapper {

    List<Patient> getPatientList();

    Patient getPatientById(Long patientId);

    List<Patient> getPatientByDoctorId(Long doctorId);

    @Options(useGeneratedKeys = true, keyProperty = "patientId")
    int addPatient(Patient patient);

    int updatePatient(Patient patient);

    int deletePatient(Long patientId);
}

