package org.code.privateclinic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.bean.MedicalCase;
import org.code.privateclinic.mapper.MedicalCaseMapper;
import org.code.privateclinic.service.MedicalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MedicalCaseServiceImpl implements MedicalCaseService {

    @Autowired
    private MedicalCaseMapper medicalCaseMapper;

    @Override
    public List<MedicalCase> getMedicalCaseList() {
        log.info("开始查询病例列表");
        long startTime = System.currentTimeMillis();

        try {
            List<MedicalCase> medicalCaseList = medicalCaseMapper.getMedicalCaseList();
            long endTime = System.currentTimeMillis();

            log.info("成功查询到病例列表，共 {} 条记录，耗时 {} ms",
                    medicalCaseList != null ? medicalCaseList.size() : 0, (endTime - startTime));

            return medicalCaseList;
        } catch (Exception e) {
            log.error("查询病例列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public MedicalCase getMedicalCaseById(Long caseId) {
        log.info("开始根据ID查询病例信息，病例ID: {}", caseId);
        long startTime = System.currentTimeMillis();

        try {
            MedicalCase medicalCase = medicalCaseMapper.getMedicalCaseById(caseId);
            long endTime = System.currentTimeMillis();

            if (medicalCase == null) {
                log.warn("未找到ID为 {} 的病例信息，耗时 {} ms", caseId, (endTime - startTime));
            } else {
                log.info("成功查询到ID为 {} 的病例信息，耗时 {} ms", caseId, (endTime - startTime));
            }

            return medicalCase;
        } catch (Exception e) {
            log.error("根据ID查询病例信息发生异常，病例ID: {}，错误信息: {}",
                    caseId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MedicalCase> getMedicalCaseByPatientId(Long patientId) {
        log.info("开始根据患者ID查询病例列表，患者ID: {}", patientId);
        long startTime = System.currentTimeMillis();

        try {
            List<MedicalCase> medicalCaseList = medicalCaseMapper.getMedicalCaseByPatientId(patientId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到患者ID为 {} 的病例列表，共 {} 条记录，耗时 {} ms",
                    patientId, medicalCaseList != null ? medicalCaseList.size() : 0, (endTime - startTime));

            return medicalCaseList;
        } catch (Exception e) {
            log.error("根据患者ID查询病例列表发生异常，患者ID: {}，错误信息: {}",
                    patientId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MedicalCase> getMedicalCaseByDoctorId(Long doctorId) {
        log.info("开始根据医生ID查询病例列表，医生ID: {}", doctorId);
        long startTime = System.currentTimeMillis();

        try {
            List<MedicalCase> medicalCaseList = medicalCaseMapper.getMedicalCaseByDoctorId(doctorId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到医生ID为 {} 的病例列表，共 {} 条记录，耗时 {} ms",
                    doctorId, medicalCaseList != null ? medicalCaseList.size() : 0, (endTime - startTime));

            return medicalCaseList;
        } catch (Exception e) {
            log.error("根据医生ID查询病例列表发生异常，医生ID: {}，错误信息: {}",
                    doctorId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MedicalCase> getMedicalCaseByStatus(String caseStatus) {
        log.info("开始根据状态查询病例列表，病例状态: {}", caseStatus);
        long startTime = System.currentTimeMillis();

        try {
            List<MedicalCase> medicalCaseList = medicalCaseMapper.getMedicalCaseByStatus(caseStatus);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到状态为 {} 的病例列表，共 {} 条记录，耗时 {} ms",
                    caseStatus, medicalCaseList != null ? medicalCaseList.size() : 0, (endTime - startTime));

            return medicalCaseList;
        } catch (Exception e) {
            log.error("根据状态查询病例列表发生异常，病例状态: {}，错误信息: {}",
                    caseStatus, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addMedicalCase(MedicalCase medicalCase) {
        log.info("开始添加病例，患者ID: {}，医生ID: {}，病例状态: {}",
                medicalCase.getPatientId(), medicalCase.getDoctorId(), medicalCase.getCaseStatus());
        long startTime = System.currentTimeMillis();

        try {
            if (medicalCase.getPatientId() == null) {
                log.error("添加病例失败，患者ID为空");
                throw new RuntimeException("患者ID不能为空");
            }
            if (medicalCase.getDoctorId() == null) {
                log.error("添加病例失败，医生ID为空");
                throw new RuntimeException("医生ID不能为空");
            }
            // 设置默认状态为NEW
            if (medicalCase.getCaseStatus() == null || medicalCase.getCaseStatus().isEmpty()) {
                medicalCase.setCaseStatus("NEW");
                log.debug("病例状态为空，设置默认状态为NEW");
            }

            int result = medicalCaseMapper.addMedicalCase(medicalCase);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加病例，病例ID: {}，患者ID: {}，医生ID: {}，耗时 {} ms",
                        medicalCase.getCaseId(), medicalCase.getPatientId(), medicalCase.getDoctorId(), (endTime - startTime));
            } else {
                log.warn("添加病例失败，患者ID: {}，耗时 {} ms", medicalCase.getPatientId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加病例发生异常，患者ID: {}，错误信息: {}",
                    medicalCase.getPatientId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updateMedicalCase(MedicalCase medicalCase) {
        log.info("开始更新病例信息，病例ID: {}", medicalCase.getCaseId());
        long startTime = System.currentTimeMillis();

        try {
            if (medicalCase.getCaseId() == null) {
                log.error("更新病例信息失败，病例ID为空");
                throw new RuntimeException("病例ID不能为空");
            }

            int result = medicalCaseMapper.updateMedicalCase(medicalCase);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新病例信息，病例ID: {}，耗时 {} ms",
                        medicalCase.getCaseId(), (endTime - startTime));
            } else {
                log.warn("更新病例信息失败，病例ID: {}，耗时 {} ms",
                        medicalCase.getCaseId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新病例信息发生异常，病例ID: {}，错误信息: {}",
                    medicalCase.getCaseId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteMedicalCase(Long caseId) {
        log.info("开始删除病例，病例ID: {}", caseId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询病例，确定是否存在
            MedicalCase medicalCase = medicalCaseMapper.getMedicalCaseById(caseId);
            if (medicalCase == null) {
                log.warn("删除病例失败，病例ID: {} 不存在", caseId);
                return 0;
            }

            int result = medicalCaseMapper.deleteMedicalCase(caseId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功删除病例，病例ID: {}，患者ID: {}，耗时 {} ms",
                        caseId, medicalCase.getPatientId(), (endTime - startTime));
            } else {
                log.warn("删除病例失败，病例ID: {}，耗时 {} ms", caseId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("删除病例发生异常，病例ID: {}，错误信息: {}",
                    caseId, e.getMessage(), e);
            throw e;
        }
    }
}

