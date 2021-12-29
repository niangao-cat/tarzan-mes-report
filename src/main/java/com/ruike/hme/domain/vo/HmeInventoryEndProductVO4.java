package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/2 1:02
 */
@Data
public class HmeInventoryEndProductVO4 implements Serializable {

    private static final long serialVersionUID = -3308740903522670392L;

    @ApiModelProperty(value = "主键")
    private String splitRecordId;

    @ApiModelProperty(value = "半成品数量")
    private BigDecimal subQty;
}
