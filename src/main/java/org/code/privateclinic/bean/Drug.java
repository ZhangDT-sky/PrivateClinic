package org.code.privateclinic.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drug {
    private Long drugId;
    @NotBlank(message = "药品名称不能为空")
    @Size(max = 50, message = "药品名称长度不能超过50个字符")
    private String drugName;

    @NotBlank(message = "说明书不能为空")
    @Size(max = 100, message = "说明书长度不能超过100个字符")
    private String specification;

    private double price;

    private Integer stock;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
