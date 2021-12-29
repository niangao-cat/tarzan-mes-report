package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 11:30
 */
@Data
public class HmeHzeroPlatformUnitDTO implements Serializable {

    private static final long serialVersionUID = 6107676378195748265L;

    @ApiModelProperty(value = "组织Id")
    private String unitId;

    @ApiModelProperty(value = "组织编码")
    private String unitCode;

    @ApiModelProperty(value = "组织名称")
    private String unitName;

    @ApiModelProperty(value = "组织类型编码")
    private String unitTypeCode;
}
