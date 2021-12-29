package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/17 18:03
 */
@Data
public class HmeCosStaffProductVO3 implements Serializable {

    private static final long serialVersionUID = 5993452711533668400L;

    @ApiModelProperty(value = "工序id")
    private String processId;
    @ApiModelProperty(value = "工序")
    private String processName;
    @ApiModelProperty(value = "工段id")
    private String lineWorkcellId;
    @ApiModelProperty(value = "工段")
    private String lineWorkcellName;
    @ApiModelProperty(value = "产线id")
    private String prodLineId;
    @ApiModelProperty(value = "产线")
    private String prodLineName;
}
