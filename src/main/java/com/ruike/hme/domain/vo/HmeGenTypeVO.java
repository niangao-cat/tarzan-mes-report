package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 11:25
 */
@Data
public class HmeGenTypeVO implements Serializable {

    private static final long serialVersionUID = -2704446689435849338L;

    @ApiModelProperty(value = "类型id")
    private String genTypeId;
    @ApiModelProperty(value = "服务包")
    private String module;
    @ApiModelProperty(value = "类型组编码")
    private String typeGroup;
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    @ApiModelProperty(value = "备注")
    private String description;
}
