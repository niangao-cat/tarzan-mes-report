package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsSummaryOfCosBarcodeProcessingVO2 implements Serializable {

    private static final long serialVersionUID = -5034901913840600298L;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("工序Id")
    private String processId;

    @ApiModelProperty("工序")
    private String processName;

    @ApiModelProperty("工段Id")
    private String lineWorkcellId;

    @ApiModelProperty("工段")
    private String lineWorkcellName;

    @ApiModelProperty("产线Id")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线名称")
    private String prodLineName;
}
