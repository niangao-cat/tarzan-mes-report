package com.ruike.hme.domain.valueobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:15
 */
@Data
public class HmeCosOperationRecord {
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String operationRecordId;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工单id")
    private String workOrderId;
    @ApiModelProperty(value = "容器类型id")
    private String containerTypeId;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "平均波长 Avg λ（nm）")
    private BigDecimal averageWavelength;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "LOTNO")
    private String lotNo;
    @ApiModelProperty(value = "wafer")
    private String wafer;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "作业批次")
    private String jobBatch;
    @ApiModelProperty(value = "BAR条数")
    private Long barNum;
    @ApiModelProperty(value = "芯片数")
    private Long cosNum;
    @ApiModelProperty(value = "工艺Id")
    private String operationId;
    @ApiModelProperty(value = "wkcId")
    private String workcellId;
    @ApiModelProperty(value = "设备id")
    private String equipmentId;
    @ApiModelProperty(value = "剩余芯片数")
    private Long surplusCosNum;
    @ApiModelProperty(value = "芯片物料id")
    private String materialId;
}
