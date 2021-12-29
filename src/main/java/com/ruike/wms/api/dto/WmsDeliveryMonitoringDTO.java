package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringDTO
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-13 16:56
 **/
@Data
public class WmsDeliveryMonitoringDTO implements Serializable {

    private static final long serialVersionUID = -1275753016040834237L;

    @ApiModelProperty(value = "配送单状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "insDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty(value = "配送单条数")
    private BigDecimal quantity;

    @ApiModelProperty(value = "比例")
    private BigDecimal proportion;

    @ApiModelProperty(value = "配送单状态含义")
    private String insDocStatusMeaning;

    @ApiModelProperty(value = "颜色")
    private String color;

}
