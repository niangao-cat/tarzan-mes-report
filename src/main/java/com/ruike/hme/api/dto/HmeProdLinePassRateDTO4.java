package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProdLinePassRateDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021-04-16 09:24:37
 **/
@Data
public class HmeProdLinePassRateDTO4 implements Serializable {
    private static final long serialVersionUID = 1013421347176181951L;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("父不良记录ID")
    private String ncRecordId;

    @ApiModelProperty("处置方式")
    private String processMethod;

    @ApiModelProperty("不良类型标识")
    private String componentRequired;

}
