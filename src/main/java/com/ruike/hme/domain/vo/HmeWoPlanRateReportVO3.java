package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName HmeWoPlanRateReportVO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 17:09
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportVO3 implements Serializable {
    private static final long serialVersionUID = 7602471069820341752L;

    @ApiModelProperty(value = "工段Id")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;

    @ApiModelProperty(value = "工段名称")
    private String workcellName;

    @ApiModelProperty(value = "工段顺序")
    private String workcellSequence;

    @ApiModelProperty(value = "班次时间")
    private String shiftDate;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "工单Id")
    private String workOrderid;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "工单下达日期")
    private String attrValue;

    @ApiModelProperty(value = "工单计划完成日期")
    private String planEndTime;

    @ApiModelProperty(value = "工单完成日期")
    private String actualEndDate;

    @ApiModelProperty(value = "班次顺序")
    private String calendarId;

    @ApiModelProperty(value = "班次顺序")
    private String sequence;

}
