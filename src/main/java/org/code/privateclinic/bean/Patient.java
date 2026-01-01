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
public class Patient {
    private Long patientId;

    @NotBlank(message = "患者姓名不能为空")
    @Size(max = 50, message = "患者姓名长度不能超过50个字符")
    private String patientName;

    @Size(max = 10, message = "性别长度不能超过10个字符")
    @Pattern(regexp = "^(男|女)$", message = "性别必须是 男 或 女")
    private String gender;

    private Integer age;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    @NotNull(message = "负责医生ID不能为空")
    private Long doctorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
