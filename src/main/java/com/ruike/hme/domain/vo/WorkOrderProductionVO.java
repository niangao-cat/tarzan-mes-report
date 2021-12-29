package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 工单产品
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 11:34
 */
@Data
public class WorkOrderProductionVO {
    @ApiModelProperty(value = "工单Id")
    private String workOrderId;
    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;
    @ApiModelProperty(value = "产品物料ID")
    private String materialId;
    @ApiModelProperty(value = "产品物料编码")
    private String materialCode;
    @ApiModelProperty(value = "产品物料名称")
    private String materialName;
    @ApiModelProperty(value = "产品物料版本")
    private String materialVersion;
}
