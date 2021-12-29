package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosBarCodeExceptionVO2 implements Serializable {
    private static final long serialVersionUID = 8994563057156174305L;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("工段ID")
    private String lineId;

    @ApiModelProperty("工段名称")
    private String lineName;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线名称")
    private String prodLineName;

    @ApiModelProperty("工位ID")
    private String workcellId;
}
