package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/2 9:39
 */
@Data
public class HmeMakeCenterProduceBoardVO13 implements Serializable {

    private static final long serialVersionUID = 2261029033121609245L;

    @ApiModelProperty("标题")
    private String chartsTitle;
    @ApiModelProperty("不良总数")
    private Long mCount;
    @ApiModelProperty("标题")
    private List<HmeMakeCenterProduceBoardVO14> chartsValueList = new ArrayList<>();
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线名称")
    private String proLineName;
    @ApiModelProperty("产线排序")
    private Long prodLineOrder;

    @ApiModelProperty("阈值标识, Y-超出目标直通率 N-未超出")
    private String overFlag;
    @ApiModelProperty("目标直通率")
    private BigDecimal targetThroughRate;
    @ApiModelProperty("图表数据")
    private List<HmeMakeCenterProduceBoardVO12> chartValueList = new ArrayList<>();

}
