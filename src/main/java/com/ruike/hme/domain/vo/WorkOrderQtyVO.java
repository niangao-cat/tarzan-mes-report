package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 09:45
 */
@Data
public class WorkOrderQtyVO {
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
}
