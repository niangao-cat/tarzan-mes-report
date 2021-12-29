package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProdLinePassRateVO10
 *
 * @author: chaonan.hu@hand-china.com 2021-06-25 11:23:51
 **/
@Data
public class HmeProdLinePassRateVO10 implements Serializable {
    private static final long serialVersionUID = 1014012117424102171L;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    private String prodLineName;
}
