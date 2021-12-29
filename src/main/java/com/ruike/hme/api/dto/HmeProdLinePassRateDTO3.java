package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021-04-16 09:15:21
 **/
@Data
public class HmeProdLinePassRateDTO3 implements Serializable {
    private static final long serialVersionUID = 1013421347176156451L;

    @ApiModelProperty("jobId")
    private String jobId;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("EO标识")
    private String identification;

}
