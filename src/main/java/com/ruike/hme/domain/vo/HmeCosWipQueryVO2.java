package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosWipQueryVO2 implements Serializable {

    private static final long serialVersionUID = 950683451252898947L;

    @ApiModelProperty(value = "条码Id")
    private String materialLotId;
}
