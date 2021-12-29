package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/23 14:23
 */
@Data
public class HmeCosOperationRecordVO implements Serializable {

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "工单")
    private String workOrderId;

    @ApiModelProperty(value = "芯片数")
    private Long cosNum;
}
