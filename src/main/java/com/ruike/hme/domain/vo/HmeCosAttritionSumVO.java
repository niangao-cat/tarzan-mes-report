package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/30 14:18
 */
@Data
public class HmeCosAttritionSumVO implements Serializable {

    private static final long serialVersionUID = 5252250624193650465L;

    @ApiModelProperty(value = "工单")
    private String workOrderId;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;
    @ApiModelProperty(value = "不良数")
    private BigDecimal defectCount;
    @ApiModelProperty(value = "序列号")
    private String loadSequence;
    @ApiModelProperty(value = "组件行")
    private String bomComponentId;
}
