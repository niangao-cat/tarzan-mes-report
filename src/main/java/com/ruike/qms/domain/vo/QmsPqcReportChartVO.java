package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡检报表新增折线图 对应数据库字段
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/27 20:46
 */
@Data
public class QmsPqcReportChartVO implements Serializable {

    private static final long serialVersionUID = 6215878037720072832L;

    @ApiModelProperty(value = "车间ID")
    private String areaId;

    @ApiModelProperty(value = "车间名称")
    private String areaName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "时间类型")
    private String timeType;

    @ApiModelProperty(value = "数值")
    private Long value;

}
