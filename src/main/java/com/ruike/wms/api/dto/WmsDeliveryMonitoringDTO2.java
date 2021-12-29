package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringDTO2
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-14 11:35
 **/
@Data
public class WmsDeliveryMonitoringDTO2 implements Serializable {

    private static final long serialVersionUID = -1394686858195350163L;
    @ApiModelProperty(value = "产线")
    private String proLineName;

    @ApiModelProperty(value = "新建条数")
    private BigDecimal newQty;

    @ApiModelProperty(value = "下达条数")
    private BigDecimal releasedQty;

    @ApiModelProperty(value = "备料中条数")
    private BigDecimal prepareExecuteQty;

    @ApiModelProperty(value = "备料完成条数")
    private BigDecimal prepareCompleteQty;

    @ApiModelProperty(value = "签收中条数")
    private BigDecimal signExecuteQty;

    @ApiModelProperty(value = "签收完成条数")
    private BigDecimal signCompleteQty;

    @ApiModelProperty(value = "关闭条数")
    private BigDecimal closeQty;

    @ApiModelProperty(value = "产线总条数")
    private BigDecimal proLineSum;
}
