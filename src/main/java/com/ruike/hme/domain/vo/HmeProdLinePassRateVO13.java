package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeProdLinePassRateVO12
 *
 * @author: chaonan.hu@hand-china.com 2021-06-25 16:03:53
 **/
@Data
public class HmeProdLinePassRateVO13 implements Serializable {
    private static final long serialVersionUID = 1384012117424263171L;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("JobID")
    private String jobId;

    @ApiModelProperty("EO标识")
    private String identification;

    @ApiModelProperty("出站时间")
    private Date siteOutDate;

}
