package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/31 13:53
 */
@Data
public class HmeMakeCenterProduceBoardVO6 implements Serializable {

    private static final long serialVersionUID = -8071094424559477434L;

    @ApiModelProperty("横轴名称")
    private String xAxisName;
    @ApiModelProperty("横轴类型 0-计划完成 1-已完成")
    private String xAxisType;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("数值")
    private List<BigDecimal> valueList;
    @ApiModelProperty("占比率")
    private List<BigDecimal>  proportionList;
    @ApiModelProperty("预警颜色")
    private String noticeColor;
    @ApiModelProperty("预警数据下标")
    private List<Integer> noticeIndexList;
}
