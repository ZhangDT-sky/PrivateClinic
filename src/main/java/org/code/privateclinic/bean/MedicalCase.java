package org.code.privateclinic.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalCase {
    private Long caseId;

    @NotNull(message = "患者ID不能为空")
    private Long patientId;

    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    private String symptom;

    private String diagnosis;

    @NotBlank(message = "病例状态不能为空")
    @Size(max = 20, message = "病例状态长度不能超过20个字符")
    @Pattern(regexp = "^(NEW|TREATING|PRESCRIBED|FINISHED)$", message = "病例状态必须是 NEW、TREATING、PRESCRIBED 或 FINISHED")
    private String caseStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime visitTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

