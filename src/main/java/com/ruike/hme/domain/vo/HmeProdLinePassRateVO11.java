package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProdLinePassRateVO11
 *
 * @author: chaonan.hu@hand-china.com 2021-06-25 15:30:14
 **/
@Data
public class HmeProdLinePassRateVO11 implements Serializable {
    private static final long serialVersionUID = 1384012117424102171L;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("EO标识")
    private String identification;

    @ApiModelProperty("jobId,返修产品直通率时需要")
    private String jobId;
}
