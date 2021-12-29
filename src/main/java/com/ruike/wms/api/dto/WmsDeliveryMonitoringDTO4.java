package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringDTO4
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-14 15:16
 **/
@Data
public class WmsDeliveryMonitoringDTO4 implements Serializable {

    private static final long serialVersionUID = 2541012392662262719L;
    @ApiModelProperty(value = "1月配送单条数")
    private BigDecimal qty1;
    @ApiModelProperty(value = "2月配送单条数")
    private BigDecimal qty2;
    @ApiModelProperty(value = "3月配送单条数")
    private BigDecimal qty3;
    @ApiModelProperty(value = "4月配送单条数")
    private BigDecimal qty4;
    @ApiModelProperty(value = "5月配送单条数")
    private BigDecimal qty5;
    @ApiModelProperty(value = "6月配送单条数")
    private BigDecimal qty6;
    @ApiModelProperty(value = "7月配送单条数")
    private BigDecimal qty7;
    @ApiModelProperty(value = "8月配送单条数")
    private BigDecimal qty8;
    @ApiModelProperty(value = "9月配送单条数")
    private BigDecimal qty9;
    @ApiModelProperty(value = "10月配送单条数")
    private BigDecimal qty10;
    @ApiModelProperty(value = "11月配送单条数")
    private BigDecimal qty11;
    @ApiModelProperty(value = "12月配送单条数")
    private BigDecimal qty12;
}
