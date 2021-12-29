package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/25 1:36
 */
@Data
public class HmeMakeCenterProduceBoardVO22 implements Serializable {

    private static final long serialVersionUID = 5867518583466958185L;

    @ApiModelProperty("图表标题")
    private String chartTitle;
    @ApiModelProperty("阈值")
    private String overFlag;
    @ApiModelProperty("目标头直通率")
    private BigDecimal targetHeaderThroughRate;
    @ApiModelProperty("工序")
    private String processName;
    @ApiModelProperty("工序直通率")
    private BigDecimal processThroughRate;
    @ApiModelProperty("目标直通率")
    private BigDecimal targetLineThroughRate;
    @ApiModelProperty("直通率")
    private BigDecimal throughRate;
    @ApiModelProperty("attribute1-产线")
    private String attribute1;
    @ApiModelProperty("attribute2-产品组")
    private String attribute2;
}
