package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/8 11:34
 */
@Data
public class HmeMakeCenterProduceBoardVO18 implements Serializable {

    private static final long serialVersionUID = 5759851166455872423L;

    @ApiModelProperty("部门id")
    private String areaId;
    @ApiModelProperty("部门编码")
    private String areaCode;
    @ApiModelProperty("部门名称")
    private String areaName;
}
