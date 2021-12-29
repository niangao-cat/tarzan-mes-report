package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/1 19:42
 */
@Data
public class HmeCalendarShiftVO implements Serializable {

    private static final long serialVersionUID = 2208823063921854548L;

    @ApiModelProperty("作为班次日历分配唯一标识")
    private String calendarShiftId;
    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty("班次所在日期")
    private Date shiftDate;
    @ApiModelProperty("班次代码")
    private String shiftCode;
    @ApiModelProperty("班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty("班次结束时间")
    private Date shiftEndTime;
}
