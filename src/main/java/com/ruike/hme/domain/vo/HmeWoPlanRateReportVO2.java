package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @ClassName HmeWoPlanRateReportVO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 15:25
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportVO2 implements Serializable {
    private static final long serialVersionUID = -4455933190073695346L;

    @ApiModelProperty(value = "工段名称")
    private String workCellId;

    @ApiModelProperty(value = "工段名称")
    private String workCellCode;

    @ApiModelProperty(value = "工段名称")
    private String workCellName;

    @ApiModelProperty(value = "工段顺序")
    private String workcellSequence;

    @ApiModelProperty(value = "计划投产")
    private BigDecimal plannedProduction;

    @ApiModelProperty(value = "实际投产")
    private BigDecimal actualProduction;

    @ApiModelProperty(value = "实际投产比例")
    private String actualProductionRatio;

    @ApiModelProperty(value = "计划交付")
    private BigDecimal plannedAelivery;

    @ApiModelProperty(value = "实际交付")
    private BigDecimal actualAelivery;

    @ApiModelProperty(value = "实际交付比例")
    private String actualAeliveryRatio;

    @ApiModelProperty(value = "在制数量")
    private BigDecimal quantityUnderProduction;

    @ApiModelProperty(value = "在制标准")
    private BigDecimal inProcessStandards;

    @ApiModelProperty(value = "在制百分比")
    private String percentageInProduction;

}
