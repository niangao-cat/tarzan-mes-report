package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 工段
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 20:41
 */
@Data
public class WorkcellVO {
    @ApiModelProperty("工段ID")
    private String workcellId;

    @ApiModelProperty("工段编码")
    private String workcellCode;

    @ApiModelProperty("工段名称")
    private String workcellName;

    @ApiModelProperty("父级工段ID")
    private String parentWorkcellId;

    @ApiModelProperty("父级工段编码")
    private String parentWorkcellCode;

    @ApiModelProperty("父级工段名称")
    private String parentWorkcellName;
}
