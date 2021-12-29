package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/24 15:59
 */
@Data
public class HmeMakeCenterProduceBoardVO21 implements Serializable {

    private static final long serialVersionUID = 6143423969051996084L;

    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("COS产线标识")
    private String cosFlag;
    @ApiModelProperty("产线排序")
    private Long prodLineOrder;
}
