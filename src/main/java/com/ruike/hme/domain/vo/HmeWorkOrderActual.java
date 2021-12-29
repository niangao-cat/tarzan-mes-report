package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/23 14:17
 */
@Data
public class HmeWorkOrderActual implements Serializable {

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "完成数")
    private Double completedQty;
}
