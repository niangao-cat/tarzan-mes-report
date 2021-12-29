package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/31 17:10
 */
@Data
public class HmeMakeCenterProduceBoardVO8 implements Serializable {

    private static final long serialVersionUID = 2105740391588254672L;

    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("计划完成数")
    private BigDecimal planFinishQty;
    @ApiModelProperty("已完成数")
    private BigDecimal finishQty;
}
