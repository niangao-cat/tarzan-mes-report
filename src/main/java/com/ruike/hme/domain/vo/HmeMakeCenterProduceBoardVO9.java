package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/1 16:17
 */
@Data
public class HmeMakeCenterProduceBoardVO9 implements Serializable {
    private static final long serialVersionUID = 1786758101310903657L;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("柱状图名字")
    private String prodLineName;
    @ApiModelProperty("标题")
    private String chartTitle;
    @ApiModelProperty("阈值标识, Y-超出目标直通率 N-未超出")
    private String overFlag;
    @ApiModelProperty("目标直通率")
    private BigDecimal targetThroughRate;
    @ApiModelProperty("产线排序")
    private Long prodLineOrder;
    @ApiModelProperty("图表数据")
    private List<HmeMakeCenterProduceBoardVO12> chartValueList;



}
