package org.code.privateclinic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.bean.Patient;
import org.code.privateclinic.mapper.PatientMapper;
import org.code.privateclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public List<Patient> getPatientList() {
        log.info("开始查询患者列表");
        long startTime = System.currentTimeMillis();

        try {
            List<Patient> patientList = patientMapper.getPatientList();
            long endTime = System.currentTimeMillis();

            log.info("成功查询到患者列表，共 {} 条记录，耗时 {} ms",
                    patientList != null ? patientList.size() : 0, (endTime - startTime));

            return patientList;
        } catch (Exception e) {
            log.error("查询患者列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Patient getPatientById(Long patientId) {
        log.info("开始根据ID查询患者信息，患者ID: {}", patientId);
        long startTime = System.currentTimeMillis();

        try {
            Patient patient = patientMapper.getPatientById(patientId);
            long endTime = System.currentTimeMillis();

            if (patient == null) {
                log.warn("未找到ID为 {} 的患者信息，耗时 {} ms", patientId, (endTime - startTime));
            } else {
                log.info("成功查询到ID为 {} 的患者信息，耗时 {} ms", patientId, (endTime - startTime));
            }

            return patient;
        } catch (Exception e) {
            log.error("根据ID查询患者信息发生异常，患者ID: {}，错误信息: {}",
                    patientId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Patient> getPatientByDoctorId(Long doctorId) {
        log.info("开始根据医生ID查询患者列表，医生ID: {}", doctorId);
        long startTime = System.currentTimeMillis();

        try {
            List<Patient> patientList = patientMapper.getPatientByDoctorId(doctorId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到医生ID为 {} 的患者列表，共 {} 条记录，耗时 {} ms",
                    doctorId, patientList != null ? patientList.size() : 0, (endTime - startTime));

            return patientList;
        } catch (Exception e) {
            log.error("根据医生ID查询患者列表发生异常，医生ID: {}，错误信息: {}",
                    doctorId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addPatient(Patient patient) {
        log.info("开始添加患者，患者姓名: {}，负责医生ID: {}", patient.getPatientName(), patient.getDoctorId());
        long startTime = System.currentTimeMillis();

        try {
            if (patient.getDoctorId() == null) {
                log.error("添加患者失败，负责医生ID为空");
                throw new RuntimeException("负责医生ID不能为空");
            }

            int result = patientMapper.addPatient(patient);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加患者，患者ID: {}，患者姓名: {}，负责医生ID: {}，耗时 {} ms",
                        patient.getPatientId(), patient.getPatientName(), patient.getDoctorId(), (endTime - startTime));
            } else {
                log.warn("添加患者失败，患者姓名: {}，耗时 {} ms", patient.getPatientName(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加患者发生异常，患者姓名: {}，错误信息: {}",
                    patient.getPatientName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updatePatient(Patient patient) {
        log.info("开始更新患者信息，患者ID: {}", patient.getPatientId());
        long startTime = System.currentTimeMillis();

        try {
            if (patient.getPatientId() == null) {
                log.error("更新患者信息失败，患者ID为空");
                throw new RuntimeException("患者ID不能为空");
            }

            int result = patientMapper.updatePatient(patient);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新患者信息，患者ID: {}，耗时 {} ms",
                        patient.getPatientId(), (endTime - startTime));
            } else {
                log.warn("更新患者信息失败，患者ID: {}，耗时 {} ms",
                        patient.getPatientId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新患者信息发生异常，患者ID: {}，错误信息: {}",
                    patient.getPatientId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deletePatient(Long patientId) {
        log.info("开始删除患者，患者ID: {}", patientId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询患者，确定是否存在
            Patient patient = patientMapper.getPatientById(patientId);
            if (patient == null) {
                log.warn("删除患者失败，患者ID: {} 不存在", patientId);
                return 0;
            }

            int result = patientMapper.deletePatient(patientId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功删除患者，患者ID: {}，患者姓名: {}，耗时 {} ms",
                        patientId, patient.getPatientName(), (endTime - startTime));
            } else {
                log.warn("删除患者失败，患者ID: {}，耗时 {} ms", patientId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("删除患者发生异常，患者ID: {}，错误信息: {}",
                    patientId, e.getMessage(), e);
            throw e;
        }
    }
}

