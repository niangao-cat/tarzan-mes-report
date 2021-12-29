package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 巡检报表新增折线图 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/27 21:27
 */
@Data
public class QmsPqcReportChartVO2 implements Serializable {
    private static final long serialVersionUID = -298566811185339319L;
    @ApiModelProperty(value = "时间轴")
    private List<String> timeList;

    @ApiModelProperty(value = "不合格数")
    private List<QmsPqcReportChartVO3> chartList;
}
