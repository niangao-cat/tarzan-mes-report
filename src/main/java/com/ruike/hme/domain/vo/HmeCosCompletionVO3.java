package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author penglin.sui@hand-china.com 2021/7/14
 */
@Data
public class HmeCosCompletionVO3 implements Serializable {
    private static final long serialVersionUID = -8679170211735538300L;

    @ApiModelProperty(value = "EOID")
    private String eoId;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;
}
