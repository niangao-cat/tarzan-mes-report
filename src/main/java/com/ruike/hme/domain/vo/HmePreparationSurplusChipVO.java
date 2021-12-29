package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmePreparationSurplusChipVO
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:58:32
 **/
@Data
@ExcelSheet(zh = "COS筛选剩余芯片统计报表")
public class HmePreparationSurplusChipVO implements Serializable {
    private static final long serialVersionUID = -5165958913207955710L;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库",order = 1)
    private String warehouseCode;

    @ApiModelProperty(value = "库位ID")
    private String locatorId;

    @ApiModelProperty(value = "库位")
    @ExcelColumn(zh = "库位",order = 2)
    private String locatorCode;

    @ApiModelProperty(value = "预挑选基础表ID")
    private String preSelectionId;

    @ApiModelProperty(value = "旧盒号ID")
    private String oldMaterialLotId;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "筛选批次")
    @ExcelColumn(zh = "筛选批次",order = 3)
    private String preSelectionLot;

    @ApiModelProperty(value = "器件物料ID")
    private String deviceMaterialId;

    @ApiModelProperty(value = "器件物料编码")
    @ExcelColumn(zh = "器件物料编码",order = 4)
    private String deviceMaterialCode;

    @ApiModelProperty(value = "器件物料描述")
    @ExcelColumn(zh = "器件物料描述",order = 5)
    private String deviceMaterialName;

    @ApiModelProperty(value = "筛选规则编码")
    @ExcelColumn(zh = "筛选规则编码",order = 6)
    private String preRuleCode;

    @ApiModelProperty(value = "条码")
    @ExcelColumn(zh = "条码",order = 7)
    private String materialLotCode;

    @ApiModelProperty(value = "cos类型")
    @ExcelColumn(zh = "cos类型",order = 8)
    private String cosType;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 9)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 10)
    private String materialName;

    @ApiModelProperty(value = "筛选批次总数")
    @ExcelColumn(zh = "筛选批次总数",order = 11)
    private int preSelectionCount;

    @ApiModelProperty(value = "该盒COS总数")
    @ExcelColumn(zh = "该盒COS总数",order = 12)
    private int cosCount;

    @ApiModelProperty(value = "该盒未挑选数")
    @ExcelColumn(zh = "该盒未挑选数",order = 13)
    private int noPreCount;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "条码数量",order = 14)
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "操作人")
    @ExcelColumn(zh = "操作人",order = 15)
    private String userName;

    @ApiModelProperty(value = "操作人Id")
    private Long createdBy;

    @ApiModelProperty(value = "筛选时间")
    @ExcelColumn(zh = "筛选时间",order = 16)
    private String preparationDate;
}
