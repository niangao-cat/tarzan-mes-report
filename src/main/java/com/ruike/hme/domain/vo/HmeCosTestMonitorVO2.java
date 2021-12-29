package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 19:53
 */
@Data
public class HmeCosTestMonitorVO2 implements Serializable {

    private static final long serialVersionUID = 8450623801826347590L;

    @ApiModelProperty("物料ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("WAFER")
    private String waferNum;
    @ApiModelProperty("工单")
    private String workOrderId;
    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("出站数量")
    private BigDecimal siteOutQty;
    @ApiModelProperty("作业类型")
    private String jobType;
    @ApiModelProperty("出站时间")
    private Date siteOutDate;
    @ApiModelProperty("进站时间")
    private Date siteInDate;
}
