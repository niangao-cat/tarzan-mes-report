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
public class HmeProdLinePassRateVO12 implements Serializable {
    private static final long serialVersionUID = 1384012117424263171L;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("JobID")
    private String jobId;

    @ApiModelProperty("父不良记录ID")
    private String ncRecordId;

    @ApiModelProperty("处置方式")
    private String processMethod;

    @ApiModelProperty("不良类型标识")
    private String componentRequired;

    @ApiModelProperty("不良创建时间")
    private Date creationDate;

}
