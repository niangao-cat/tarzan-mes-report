package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/11 15:00
 */
@Data
public class HmeCosReturnVO2 implements Serializable {

    private static final long serialVersionUID = 3609908795608448338L;

    @ApiModelProperty("目标条码")
    private String targetMaterialLotId;

    @ApiModelProperty("数量")
    private BigDecimal returnQty;
}
