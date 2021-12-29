package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HmeCosInNcRecordVO2 implements Serializable {
    private static final long serialVersionUID = -2516699549197180687L;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "不良次数")
    private BigDecimal ngCount;
}
