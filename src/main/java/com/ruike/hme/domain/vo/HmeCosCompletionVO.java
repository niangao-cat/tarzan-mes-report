package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/6 17:18
 */
@Data
public class HmeCosCompletionVO implements Serializable {

    private static final long serialVersionUID = -7715761967714488586L;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

    @ApiModelProperty(value = "实验代码")
    private String labCode;
}
