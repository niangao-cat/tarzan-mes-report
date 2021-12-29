package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/24 20:25
 */
@Data
public class HmeCosInProductionVO2 implements Serializable {

    private static final long serialVersionUID = -7642703296668470357L;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("工序Id")
    private String processId;

    @ApiModelProperty("工序")
    private String processName;

    @ApiModelProperty("工段Id")
    private String lineWorkcellId;

    @ApiModelProperty("工段")
    private String lineWorkcellName;
}
