package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmeWoPlanRateReportVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 15:15
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportVO implements Serializable {
    private static final long serialVersionUID = -1717744590880784015L;

    @ApiModelProperty(value = "日期")
    private String dataTime;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "工单数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "工单下达日期")
    private String publishDate;

    @ApiModelProperty(value = "工单计划完成日期")
    private String planEndTime;

    @ApiModelProperty(value = "工单完成日期")
    private String woEndTime;

    @ApiModelProperty(value = "工段数据")
    private List<HmeWoPlanRateReportVO2> resultList;

    @ApiModelProperty(value = "原始数据")
    private List<HmeWoPlanRateReportVO3> sourceList;

}
