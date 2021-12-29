package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGap
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-19 10:56
 **/
@Data
public class WmsDistributionGapDTO implements Serializable {

    private static final long serialVersionUID = -3561772546439213179L;

    @ApiModelProperty(value = "产线")
    private String prodLineName;

    @ApiModelProperty(value = "工段")
    private String workcellCode;

    @ApiModelProperty(value = "工段描述")
    private String workcellName;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "当日生产需求")
    private BigDecimal demandQty1;

    @ApiModelProperty(value = "当日生产已配送")
    private BigDecimal deliveryQty1;

    @ApiModelProperty(value = "当日生产缺口")
    private BigDecimal diffQty1;

    @ApiModelProperty(value = "次日生产需求")
    private BigDecimal demandQty2;

    @ApiModelProperty(value = "次日生产已配送")
    private BigDecimal deliveryQty2;

    @ApiModelProperty(value = "次日生产缺口")
    private BigDecimal diffQty2;

    @ApiModelProperty(value = "线边非限制库存")
    private BigDecimal lineQty;

    @ApiModelProperty(value = "仓库非限制库存")
    private BigDecimal stockQty;

    @ApiModelProperty(value = "质检库存（待收区库存）")
    private BigDecimal receiveQty;

    @ApiModelProperty(value = "配送时长")
    private String deliveryHours;

    @ApiModelProperty(value = "工段")
    private String workcellId;

    @ApiModelProperty(value = "物料")
    private String materialId;

}
