package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/2 16:51
 */
@Data
public class HmeMakeCenterProduceBoardVO14 implements Serializable {

    private static final long serialVersionUID = 2400613945443228284L;
    @ApiModelProperty("工序id")
    private String processId;
    @ApiModelProperty("不良代码")
    private String ncCodeId;
    @ApiModelProperty("不良代码描述")
    private String description;
    @ApiModelProperty("不良代码数")
    private Integer ncCount;
    @ApiModelProperty("物料类型")
    private String materialType;
    @ApiModelProperty("产线")
    private String prodLineName;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("不良率")
    private BigDecimal defectiveRate;
}
