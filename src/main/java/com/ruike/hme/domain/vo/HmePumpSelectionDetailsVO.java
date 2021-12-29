package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePumpSelectionDetailsVO
 *
 * @author: chaonan.hu@hand-china.com 2021/11/05 10:39
 **/
@Data
@ExcelSheet(zh = "泵浦源预筛选报表")
public class HmePumpSelectionDetailsVO implements Serializable {
    private static final long serialVersionUID = -7826169701819174544L;

    @ApiModelProperty(value = "头ID")
    private String pumpPreSelectionId;

    @ApiModelProperty(value = "行ID")
    private String pumpSelectionDetailsId;

    @ApiModelProperty(value = "组合物料ID")
    private String combMaterialId;

    @ApiModelProperty(value = "组合物料编码")
    @ExcelColumn(zh = "组合物料编码")
    private String combMaterialCode;

    @ApiModelProperty(value = "组合物料描述")
    @ExcelColumn(zh = "组合物料描述")
    private String combMaterialName;

    @ApiModelProperty(value = "BOMID")
    private String bomId;

    @ApiModelProperty(value = "BOM版本号")
    @ExcelColumn(zh = "BOM版本号")
    private String revision;

    @ApiModelProperty(value = "筛选批次")
    @ExcelColumn(zh = "筛选批次")
    private String selectionLot;

    @ApiModelProperty(value = "筛选规则ID")
    private String ruleHeadId;

    @ApiModelProperty(value = "筛选规则编码")
    @ExcelColumn(zh = "筛选规则编码")
    private String ruleCode;

    @ApiModelProperty(value = "总套数")
    private Long setsNum;

    @ApiModelProperty(value = "挑选顺序")
    private Long selectionOrder;

    @ApiModelProperty(value = "套数")
    @ExcelColumn(zh = "套数")
    private String setsNumber;

    @ApiModelProperty(value = "原容器ID")
    private String oldContainerId;

    @ApiModelProperty(value = "原容器号")
    @ExcelColumn(zh = "原容器号")
    private String oldContainerCode;

    @ApiModelProperty(value = "目标容器ID")
    private String newContainerId;

    @ApiModelProperty(value = "目标容器")
    @ExcelColumn(zh = "目标容器")
    private String newContainerCode;

    @ApiModelProperty(value = "泵浦源SN ID")
    private String materialLotId;

    @ApiModelProperty(value = "泵浦源SN")
    @ExcelColumn(zh = "泵浦源SN")
    private String materialLotCode;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "筛选状态")
    @LovValue(lovCode = "HME.PUMP_SELECT_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "筛选状态含义")
    @ExcelColumn(zh = "筛选状态")
    private String statusMeaning;

    @ApiModelProperty(value = "投料工单ID")
    private String releaseWorkOrderId;

    @ApiModelProperty(value = "投料工单")
    @ExcelColumn(zh = "投料工单")
    private String releaseWorkOrderNum;

    @ApiModelProperty(value = "组合件SN ID")
    private String combMaterialLotId;

    @ApiModelProperty(value = "组合件SN")
    @ExcelColumn(zh = "组合件SN")
    private String combMaterialLotCode;

    @ApiModelProperty(value = "预筛选操作人ID")
    private Long createdBy;

    @ApiModelProperty(value = "预筛选操作人")
    @ExcelColumn(zh = "预筛选操作人")
    private String createdByName;

    @ApiModelProperty(value = "预筛选时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "预筛选时间")
    private Date creationDate;

    @ApiModelProperty(value = "筛选工位ID")
    private String workcellId;

    @ApiModelProperty(value = "筛选工位编码")
    @ExcelColumn(zh = "筛选工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "筛选工位描述")
    @ExcelColumn(zh = "筛选工位描述")
    private String workcellName;

    @ApiModelProperty(value = "筛选产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "筛选产线编码")
    @ExcelColumn(zh = "筛选产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "筛选产线描述")
    @ExcelColumn(zh = "筛选产线描述")
    private String prodLineName;

    @ApiModelProperty(value = "装箱操作人ID")
    private Long packedBy;

    @ApiModelProperty(value = "装箱操作人")
    @ExcelColumn(zh = "装箱操作人")
    private String packedByName;

    @ApiModelProperty(value = "装箱时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "装箱时间")
    private Date packedDate;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    @ExcelColumn(zh = "是否冻结")
    private String freezeFlagMeaning;
}
