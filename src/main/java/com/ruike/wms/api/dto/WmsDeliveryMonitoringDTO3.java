package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringDTO3
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-14 11:42
 **/
@Data
public class WmsDeliveryMonitoringDTO3 implements Serializable {

    private static final long serialVersionUID = -7774116841539583735L;
    @ApiModelProperty(value = "产线")
    private String prodLineName;

    @ApiModelProperty(value = "配送单状态")
    private String instructionDocStatus;

    @ApiModelProperty(value = "配送单条数")
    private BigDecimal quantity;
}
