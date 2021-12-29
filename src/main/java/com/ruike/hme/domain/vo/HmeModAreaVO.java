package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 10:51
 */
@Data
public class HmeModAreaVO implements Serializable {

    private static final long serialVersionUID = -1320140927915407600L;

    @ApiModelProperty("区域id")
    private String areaId;
    @ApiModelProperty(value = "区域编号")
    private String areaCode;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "区域描述")
    private String description;
}
