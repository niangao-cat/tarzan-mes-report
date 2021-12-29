package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/24 21:09
 */
@Data
public class HmeCosInProductionVO3 implements Serializable {

    private static final long serialVersionUID = -1331606307200391321L;

    @ApiModelProperty (value = "实验代码")
    private String labCode;

    @ApiModelProperty (value = "条码")
    private String materialLotId;
}
