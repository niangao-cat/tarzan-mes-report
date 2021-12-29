package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeRouterStepVO implements Serializable {

    private static final long serialVersionUID = -2010934798423631809L;

    @ApiModelProperty(value = "工艺步骤")
    private String routerStepId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "作业Id")
    private String jobId;

    @ApiModelProperty(value = "工单")
    private String workOrderId;

    @ApiModelProperty(value = "条码")
    private String materialLotId;
}
