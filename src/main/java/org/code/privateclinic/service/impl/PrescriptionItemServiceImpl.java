package org.code.privateclinic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.code.privateclinic.bean.PrescriptionItem;
import org.code.privateclinic.mapper.PrescriptionItemMapper;
import org.code.privateclinic.service.PrescriptionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PrescriptionItemServiceImpl implements PrescriptionItemService {

    @Autowired
    private PrescriptionItemMapper prescriptionItemMapper;

    @Override
    public List<PrescriptionItem> getPrescriptionItemList() {
        log.info("开始查询处方明细列表");
        long startTime = System.currentTimeMillis();

        try {
            List<PrescriptionItem> prescriptionItemList = prescriptionItemMapper.getPrescriptionItemList();
            long endTime = System.currentTimeMillis();

            log.info("成功查询到处方明细列表，共 {} 条记录，耗时 {} ms",
                    prescriptionItemList != null ? prescriptionItemList.size() : 0, (endTime - startTime));

            return prescriptionItemList;
        } catch (Exception e) {
            log.error("查询处方明细列表发生异常，错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PrescriptionItem getPrescriptionItemById(Long itemId) {
        log.info("开始根据ID查询处方明细信息，明细ID: {}", itemId);
        long startTime = System.currentTimeMillis();

        try {
            PrescriptionItem prescriptionItem = prescriptionItemMapper.getPrescriptionItemById(itemId);
            long endTime = System.currentTimeMillis();

            if (prescriptionItem == null) {
                log.warn("未找到ID为 {} 的处方明细信息，耗时 {} ms", itemId, (endTime - startTime));
            } else {
                log.info("成功查询到ID为 {} 的处方明细信息，耗时 {} ms", itemId, (endTime - startTime));
            }

            return prescriptionItem;
        } catch (Exception e) {
            log.error("根据ID查询处方明细信息发生异常，明细ID: {}，错误信息: {}",
                    itemId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<PrescriptionItem> getPrescriptionItemByPrescriptionId(Long prescriptionId) {
        log.info("开始根据处方ID查询处方明细列表，处方ID: {}", prescriptionId);
        long startTime = System.currentTimeMillis();

        try {
            List<PrescriptionItem> prescriptionItemList = prescriptionItemMapper.getPrescriptionItemByPrescriptionId(prescriptionId);
            long endTime = System.currentTimeMillis();

            log.info("成功查询到处方ID为 {} 的处方明细列表，共 {} 条记录，耗时 {} ms",
                    prescriptionId, prescriptionItemList != null ? prescriptionItemList.size() : 0, (endTime - startTime));

            return prescriptionItemList;
        } catch (Exception e) {
            log.error("根据处方ID查询处方明细列表发生异常，处方ID: {}，错误信息: {}",
                    prescriptionId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int addPrescriptionItem(PrescriptionItem prescriptionItem) {
        log.info("开始添加处方明细，处方ID: {}，药品ID: {}，数量: {}",
                prescriptionItem.getPrescriptionId(), prescriptionItem.getDrugId(), prescriptionItem.getQuantity());
        long startTime = System.currentTimeMillis();

        try {
            if (prescriptionItem.getPrescriptionId() == null) {
                log.error("添加处方明细失败，处方ID为空");
                throw new RuntimeException("处方ID不能为空");
            }
            if (prescriptionItem.getDrugId() == null) {
                log.error("添加处方明细失败，药品ID为空");
                throw new RuntimeException("药品ID不能为空");
            }
            if (prescriptionItem.getQuantity() == null || prescriptionItem.getQuantity() <= 0) {
                log.error("添加处方明细失败，数量无效");
                throw new RuntimeException("数量必须大于0");
            }

            int result = prescriptionItemMapper.addPrescriptionItem(prescriptionItem);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功添加处方明细，明细ID: {}，处方ID: {}，药品ID: {}，耗时 {} ms",
                        prescriptionItem.getItemId(), prescriptionItem.getPrescriptionId(), prescriptionItem.getDrugId(), (endTime - startTime));
            } else {
                log.warn("添加处方明细失败，处方ID: {}，耗时 {} ms", prescriptionItem.getPrescriptionId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("添加处方明细发生异常，处方ID: {}，错误信息: {}",
                    prescriptionItem.getPrescriptionId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int updatePrescriptionItem(PrescriptionItem prescriptionItem) {
        log.info("开始更新处方明细信息，明细ID: {}", prescriptionItem.getItemId());
        long startTime = System.currentTimeMillis();

        try {
            if (prescriptionItem.getItemId() == null) {
                log.error("更新处方明细信息失败，明细ID为空");
                throw new RuntimeException("明细ID不能为空");
            }

            int result = prescriptionItemMapper.updatePrescriptionItem(prescriptionItem);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功更新处方明细信息，明细ID: {}，耗时 {} ms",
                        prescriptionItem.getItemId(), (endTime - startTime));
            } else {
                log.warn("更新处方明细信息失败，明细ID: {}，耗时 {} ms",
                        prescriptionItem.getItemId(), (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("更新处方明细信息发生异常，明细ID: {}，错误信息: {}",
                    prescriptionItem.getItemId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deletePrescriptionItem(Long itemId) {
        log.info("开始删除处方明细，明细ID: {}", itemId);
        long startTime = System.currentTimeMillis();

        try {
            // 先查询处方明细，确定是否存在
            PrescriptionItem prescriptionItem = prescriptionItemMapper.getPrescriptionItemById(itemId);
            if (prescriptionItem == null) {
                log.warn("删除处方明细失败，明细ID: {} 不存在", itemId);
                return 0;
            }

            int result = prescriptionItemMapper.deletePrescriptionItem(itemId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功删除处方明细，明细ID: {}，处方ID: {}，耗时 {} ms",
                        itemId, prescriptionItem.getPrescriptionId(), (endTime - startTime));
            } else {
                log.warn("删除处方明细失败，明细ID: {}，耗时 {} ms", itemId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("删除处方明细发生异常，明细ID: {}，错误信息: {}",
                    itemId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deletePrescriptionItemByPrescriptionId(Long prescriptionId) {
        log.info("开始根据处方ID删除所有处方明细，处方ID: {}", prescriptionId);
        long startTime = System.currentTimeMillis();

        try {
            int result = prescriptionItemMapper.deletePrescriptionItemByPrescriptionId(prescriptionId);
            long endTime = System.currentTimeMillis();

            if (result > 0) {
                log.info("成功删除处方ID为 {} 的所有处方明细，共 {} 条，耗时 {} ms",
                        prescriptionId, result, (endTime - startTime));
            } else {
                log.warn("删除处方明细失败，处方ID: {}，耗时 {} ms", prescriptionId, (endTime - startTime));
            }

            return result;
        } catch (Exception e) {
            log.error("根据处方ID删除处方明细发生异常，处方ID: {}，错误信息: {}",
                    prescriptionId, e.getMessage(), e);
            throw e;
        }
    }
}

