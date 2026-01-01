package org.code.privateclinic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.bean.Prescription;
import org.code.privateclinic.mapper.PrescriptionMapper;
import org.code.privateclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Override
    public List<Prescription> getPrescriptionList() {
        log.info("开始查询处方列表");
        long startTime = System.currentTimeMillis();

        try {
            List<Prescription> prescriptionList = prescriptionMapper.getPrescriptionList();
            long endTime = System.currentTimeMillis();

            log.info("成功查询到处方列表，共 {} 条记录，耗时 {} ms",
                    prescriptionList != null ? prescriptionList.size() : 0, (endTime - startTime));

            return prescriptionList;
        } catch (Exception e) {
            log.error("查询处方列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Prescription getPrescriptionById(Long prescriptionId) {
        log.info("开始根据ID查询处方信息，处方ID: {}", prescriptionId);
        long startTime = System.currentTimeMillis();

        try {
            Prescription prescription = prescriptionMapper.getPrescriptionById(prescriptionId);
            long endTime = System.currentTimeMillis();

            if (prescription == null) {
                log.warn("未找到ID为 {} 的处方信息，耗时 {} ms", prescriptionId, (endTime - startTime));
            } else {
                log.info("成功查询到ID为 {} 的处方信息，耗时 {} ms", prescriptionId, (endTime - startTime));
            }

            return prescription;
        } catch (Exception e) {
            log.error("根据ID查询处方信息发生异常，处方ID: {}，错误信息: {}",
                    prescriptionId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Prescription> getPrescriptionByCaseId(Long caseId) {
        log.info("开始根据病例ID查询处方列表，病例ID: {}", caseId);
        long startTime = System.currentTimeMillis();

        try {
            List<Prescription> prescriptionList = prescriptionMapper.getPrescriptionByCaseId(caseId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到病例ID为 {} 的处方列表，共 {} 条记录，耗时 {} ms",
                    caseId, prescriptionList != null ? prescriptionList.size() : 0, (endTime - startTime));

            return prescriptionList;
        } catch (Exception e) {
            log.error("根据病例ID查询处方列表发生异常，病例ID: {}，错误信息: {}",
                    caseId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Prescription> getPrescriptionByDoctorId(Long doctorId) {
        log.info("开始根据医生ID查询处方列表，医生ID: {}", doctorId);
        long startTime = System.currentTimeMillis();

        try {
            List<Prescription> prescriptionList = prescriptionMapper.getPrescriptionByDoctorId(doctorId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到医生ID为 {} 的处方列表，共 {} 条记录，耗时 {} ms",
                    doctorId, prescriptionList != null ? prescriptionList.size() : 0, (endTime - startTime));

            return prescriptionList;
        } catch (Exception e) {
            log.error("根据医生ID查询处方列表发生异常，医生ID: {}，错误信息: {}",
                    doctorId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addPrescription(Prescription prescription) {
        log.info("开始添加处方，病例ID: {}，医生ID: {}",
                prescription.getCaseId(), prescription.getDoctorId());
        long startTime = System.currentTimeMillis();

        try {
            if (prescription.getCaseId() == null) {
                log.error("添加处方失败，病例ID为空");
                throw new RuntimeException("病例ID不能为空");
            }
            if (prescription.getDoctorId() == null) {
                log.error("添加处方失败，医生ID为空");
                throw new RuntimeException("医生ID不能为空");
            }

            int result = prescriptionMapper.addPrescription(prescription);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加处方，处方ID: {}，病例ID: {}，医生ID: {}，耗时 {} ms",
                        prescription.getPrescriptionId(), prescription.getCaseId(), prescription.getDoctorId(), (endTime - startTime));
            } else {
                log.warn("添加处方失败，病例ID: {}，耗时 {} ms", prescription.getCaseId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加处方发生异常，病例ID: {}，错误信息: {}",
                    prescription.getCaseId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updatePrescription(Prescription prescription) {
        log.info("开始更新处方信息，处方ID: {}", prescription.getPrescriptionId());
        long startTime = System.currentTimeMillis();

        try {
            if (prescription.getPrescriptionId() == null) {
                log.error("更新处方信息失败，处方ID为空");
                throw new RuntimeException("处方ID不能为空");
            }

            int result = prescriptionMapper.updatePrescription(prescription);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新处方信息，处方ID: {}，耗时 {} ms",
                        prescription.getPrescriptionId(), (endTime - startTime));
            } else {
                log.warn("更新处方信息失败，处方ID: {}，耗时 {} ms",
                        prescription.getPrescriptionId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新处方信息发生异常，处方ID: {}，错误信息: {}",
                    prescription.getPrescriptionId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deletePrescription(Long prescriptionId) {
        log.info("开始删除处方，处方ID: {}", prescriptionId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询处方，确定是否存在
            Prescription prescription = prescriptionMapper.getPrescriptionById(prescriptionId);
            if (prescription == null) {
                log.warn("删除处方失败，处方ID: {} 不存在", prescriptionId);
                return 0;
            }

            int result = prescriptionMapper.deletePrescription(prescriptionId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功删除处方，处方ID: {}，病例ID: {}，耗时 {} ms",
                        prescriptionId, prescription.getCaseId(), (endTime - startTime));
            } else {
                log.warn("删除处方失败，处方ID: {}，耗时 {} ms", prescriptionId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("删除处方发生异常，处方ID: {}，错误信息: {}",
                    prescriptionId, e.getMessage(), e);
            throw e;
        }
    }
}

