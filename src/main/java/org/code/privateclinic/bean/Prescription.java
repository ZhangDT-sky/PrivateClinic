package org.code.privateclinic.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private Long prescriptionId;

    @NotNull(message = "病例ID不能为空")
    private Long caseId;

    // 患者姓名（用于显示）
    private String patientName;

    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    // 医生姓名（用于显示）
    private String doctorName;

    //金额
    private BigDecimal totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

