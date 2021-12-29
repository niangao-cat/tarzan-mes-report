package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * SN工位
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 15:29
 */
@Data
public class SnWorkcellVO {
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批")
    private String materialLotCode;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工位描述")
    private String workcellName;
}
