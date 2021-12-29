package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosFunctionVO4
 *
 * @author: chaonan.hu@hand-china.com 2021/05/10 14:15:21
 **/
@Data
public class HmeCosFunctionVO4 implements Serializable {
    private static final long serialVersionUID = -6112823926193777917L;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位描述")
    private String workcellName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序描述")
    private String processName;

    @ApiModelProperty(value = "工段ID")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工段编码")
    private String lineWorkcellCode;

    @ApiModelProperty(value = "工段描述")
    private String lineWorkcellName;
}
