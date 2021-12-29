package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateVO9
 *
 * @author: chaonan.hu@hand-china.com 2021-06-25 11:23:51
 **/
@Data
public class HmeProdLinePassRateVO9 implements Serializable {
    private static final long serialVersionUID = 1014012117424104451L;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;
}
