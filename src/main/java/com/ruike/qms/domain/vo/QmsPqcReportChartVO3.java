package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 巡检报表新增折线图 返回子VO
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/28 14:29
 */
@Data
public class QmsPqcReportChartVO3 implements Serializable {

    private static final long serialVersionUID = -5621849662727931265L;


    @ApiModelProperty(value = "车间名称")
    private String areaName;


    @ApiModelProperty(value = "工序名称")
    private String processName;


    @ApiModelProperty(value = "数值")
    private List<Long> valueList;

}
