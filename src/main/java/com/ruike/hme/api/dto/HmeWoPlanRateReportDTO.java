package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmeWoPlanRateReportDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 14:24
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportDTO implements Serializable {
    private static final long serialVersionUID = -2035995778123658385L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("班次日期起")
    private String shiftDateFrom;

    @ApiModelProperty("班次日期至")
    private String shiftDateTo;

    @ApiModelProperty("工单下达时间起")
    private String publishDateFrom;

    @ApiModelProperty("工单下达时间至")
    private String publishDateTo;

    @ApiModelProperty("工单计划完成时间起")
    private String planEndTimeFrom;

    @ApiModelProperty("工单计划完成时间至")
    private String planEndTimeTo;

    @ApiModelProperty("工单完成时间起")
    private String woEndTimeFrom;

    @ApiModelProperty("工单完成时间至")
    private String woEndTimeTo;

    @ApiModelProperty("工段编码")
    private String workcellId;

    @ApiModelProperty("工单号")
    private List<String> workOrderNumList;
}
