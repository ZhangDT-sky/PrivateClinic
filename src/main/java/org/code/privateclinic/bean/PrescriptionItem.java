package org.code.privateclinic.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionItem {
    private Long itemId;

    @NotNull(message = "处方ID不能为空")
    private Long prescriptionId;

    @NotNull(message = "药品ID不能为空")
    private Long drugId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @Size(max = 200, message = "用法用量长度不能超过200个字符")
    private String usageMethod;

    private BigDecimal price;

    // 药品信息字段（查询时关联 drug 表获取）
    private String drugName;        // 药品名称
    private String specification;   // 药品规格
    private Integer drugStatus;     // 药品状态（用于判断是否已删除）
}

