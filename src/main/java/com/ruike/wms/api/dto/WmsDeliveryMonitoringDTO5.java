package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringDTO5
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-14 15:55
 **/
@Data
public class WmsDeliveryMonitoringDTO5 implements Serializable {

    private static final long serialVersionUID = 8015804515312890678L;
    @ApiModelProperty(value = "月份")
    private String months;

    @ApiModelProperty(value = "配送单条数")
    private BigDecimal qty;
}
