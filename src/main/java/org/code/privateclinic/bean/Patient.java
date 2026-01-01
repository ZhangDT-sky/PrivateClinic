package org.code.privateclinic.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "姓名不能为空")
    private String patientName;

    @NotBlank(message = "性别不能为空")
    @Size(min = 1, max = 1)
    @Pattern(regexp = "^(男|女)$", message = "性别必须是男或女")
    private String gender;

    @NotBlank(message = "年龄不能为空")
    private Integer age;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    @Size(max = 200)
    private String address; //地址

    private String remark; //备注

    private Long doctor_id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
