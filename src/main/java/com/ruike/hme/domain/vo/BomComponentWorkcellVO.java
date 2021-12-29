package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * bom行工位信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 20:19
 */
@Data
public class BomComponentWorkcellVO implements Serializable {
    private static final long serialVersionUID = -2656553145813601985L;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("组件行Id")
    private String bomComponentId;

    @ApiModelProperty("产线id")
    private String prodLineId;

    @ApiModelProperty("工段ID")
    private String workcellId;

    @ApiModelProperty("工段编码")
    private String workcellCode;

    @ApiModelProperty("工段名称")
    private String workcellName;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("优先级")
    private String priority;
}
