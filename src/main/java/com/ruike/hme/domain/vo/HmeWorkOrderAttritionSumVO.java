package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/27 10:31
 */
@Data
public class HmeWorkOrderAttritionSumVO implements Serializable {

    private static final long serialVersionUID = -6289374876579526872L;

    @ApiModelProperty
    private String eoComponentActualId;
    @ApiModelProperty(value = "工单")
    private String workOrderId;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "组件")
    private String bomComponentId;
    @ApiModelProperty(value = "装配数量")
    private BigDecimal assembleQty;
}
