package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * <p>
 * 工单损耗汇总报表 展示数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 09:41
 */
@Data
@ExcelSheet(zh = "工单损耗汇总")
public class WorkOrderAttritionSumRepresentationDTO implements Serializable, Comparable<WorkOrderAttritionSumRepresentationDTO> {
    private static final long serialVersionUID = 2379260601735277851L;

    @ApiModelProperty("序号")
    @ExcelColumn(zh = "序号")
    private Integer sequenceNum;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty("工单状态")
    @LovValue(lovCode = "MT.WO_STATUS", meaningField = "workOrderStatusMeaning")
    private String workOrderStatus;

    @ApiModelProperty("工单状态含义")
    @ExcelColumn(zh = "工单状态")
    private String workOrderStatusMeaning;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本")
    private String productionVersion;

    @ApiModelProperty("工单版本描述")
    @ExcelColumn(zh = "版本描述")
    private String productionVersionDescription;

    @ApiModelProperty("工单类型")
    @LovValue(lovCode = "MT.WO_TYPE", meaningField = "workOrderTypeMeaning")
    private String workOrderType;

    @ApiModelProperty("工单类型含义")
    @ExcelColumn(zh = "工单类型")
    private String workOrderTypeMeaning;

    @ApiModelProperty("产品ID")
    private String assemblyMaterialId;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码")
    private String assemblyMaterialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述")
    private String assemblyMaterialName;

    @ApiModelProperty("工单数量")
    @ExcelColumn(zh = "工单数量")
    private BigDecimal woQuantity;

    @ApiModelProperty("完成数量")
    @ExcelColumn(zh = "完成数量")
    private BigDecimal completedQuantity;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线名称")
    @ExcelColumn(zh = "产线")
    private String prodLineName;

    @ApiModelProperty(value = "工序ID", hidden = true)
    private String processId;

    @ApiModelProperty("工段ID")
    private String workcellId;

    @ApiModelProperty("工段编码")
    private String workcellCode;

    @ApiModelProperty("工段名称")
    @ExcelColumn(zh = "工段")
    private String workcellName;

    @ApiModelProperty("组件行Id")
    private String bomComponentId;

    @ApiModelProperty(value = "来源组件行Id", hidden = true)
    private String sourceBomComponentId;

    @ApiModelProperty(value = "来源类型", hidden = true)
    private String sourceType;

    @ApiModelProperty("组件行号")
    @ExcelColumn(zh = "组件行")
    private Integer bomLineNumber;

    @ApiModelProperty("组件物料ID")
    private String componentMaterialId;

    @ApiModelProperty("组件物料编码")
    @ExcelColumn(zh = "物料编码")
    private String componentMaterialCode;

    @ApiModelProperty("组件物料描述")
    @ExcelColumn(zh = "物料描述")
    private String componentMaterialName;

    @ApiModelProperty("BOM替代组")
    @ExcelColumn(zh = "BOM替代组")
    private String bomSubstituteGroup;

    @ApiModelProperty("全局替代组")
    @ExcelColumn(zh = "全局替代组")
    private String globalSubstituteGroup;

    @ApiModelProperty("单位用量")
    @ExcelColumn(zh = "单位用量")
    private BigDecimal usageQty;

    @ApiModelProperty("单位Id")
    private String uomId;

    @ApiModelProperty("单位名称")
    @ExcelColumn(zh = "单位")
    private String uomName;

    @ApiModelProperty("需求数量")
    @ExcelColumn(zh = "需求数量")
    private BigDecimal demandQuantity;

    @ApiModelProperty("损耗率")
    @ExcelColumn(zh = "损耗率(%)")
    private BigDecimal attritionChance;

    @ApiModelProperty("损耗上限")
    @ExcelColumn(zh = "损耗上限")
    private BigDecimal attritionLimit;

    @ApiModelProperty("装配数量")
    @ExcelColumn(zh = "装配数量")
    private BigDecimal assemblyQuantity;

    @ApiModelProperty("材料不良报废")
    @ExcelColumn(zh = "材料不良报废")
    private BigDecimal materialNcScrapQty;

    @ApiModelProperty("工序不良报废")
    @ExcelColumn(zh = "工序不良报废")
    private BigDecimal processNcScrapQty;

    @ApiModelProperty("计划内报损")
    @ExcelColumn(zh = "计划内报损")
    private BigDecimal plannedScrappedQuantity;

    @ApiModelProperty("计划外报损")
    @ExcelColumn(zh = "计划外报损")
    private BigDecimal unplannedScrappedQuantity;

    @ApiModelProperty("实物报损数量")
    @ExcelColumn(zh = "实物报损数量")
    private BigDecimal scrappedQuantity;

    @ApiModelProperty("联产品损失")
    @ExcelColumn(zh = "联产品损失")
    private BigDecimal coproductScrappedQuantity;

    @ApiModelProperty("计划内报损合计")
    @ExcelColumn(zh = "计划内报损合计")
    private BigDecimal plannedScrappedSumQuantity;

    @ApiModelProperty("计划外报损合计")
    @ExcelColumn(zh = "计划外报损合计")
    private BigDecimal unplannedScrappedSumQuantity;

    @ApiModelProperty("报损合计")
    @ExcelColumn(zh = "报损合计")
    private BigDecimal scrappedSumQuantity;

    @ApiModelProperty("实际损耗率")
    @ExcelColumn(zh = "实物损耗率(%)")
    private BigDecimal materialAttritionChance;

    @ApiModelProperty("实际损耗率")
    @ExcelColumn(zh = "实际损耗率(%)")
    private BigDecimal actualAttritionChance;

    @ApiModelProperty("损耗率差异")
    @ExcelColumn(zh = "损耗率差异(%)")
    private BigDecimal attritionChanceDifference;

    @ApiModelProperty("是否超工单报损")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "attritionOverFlagMeaning")
    private String attritionOverFlag;

    @ApiModelProperty("是否超工单报损含义")
    @ExcelColumn(zh = "是否超工单报损")
    private String attritionOverFlagMeaning;

    @ApiModelProperty("不良待审核数量")
    @ExcelColumn(zh = "不良待审核数量")
    private BigDecimal pendingNcQuantity;

    @Override
    public int compareTo(WorkOrderAttritionSumRepresentationDTO o) {
        if (StringUtils.equals(this.getWorkOrderId(), o.getWorkOrderId())) {
            return Optional.ofNullable(this.getBomLineNumber()).orElse(Integer.MAX_VALUE).compareTo(Optional.ofNullable(o.getBomLineNumber()).orElse(Integer.MAX_VALUE));
        } else {
            return this.getWorkOrderId().compareTo(o.getWorkOrderId());
        }
    }
}
