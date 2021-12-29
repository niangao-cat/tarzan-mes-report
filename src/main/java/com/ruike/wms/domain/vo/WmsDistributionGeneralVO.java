package com.ruike.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 配送综合查询报表
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 16:06
 */
@Data
@ExcelSheet(title = "工单配送综合查询报表")
public class WmsDistributionGeneralVO {
    @ApiModelProperty("序号")
    @ExcelProperty(value = "序号", index = 0)
    private Integer sequence;

    @ApiModelProperty("配送单ID")
    @ExcelIgnore
    private String instructionDocId;

    @ApiModelProperty("配送单号")
    @ExcelColumn(title = "配送单号")
    @ExcelProperty(value = "配送单", index = 1)
    @ColumnWidth(18)
    private String instructionDocNum;

    @ApiModelProperty("状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    @ExcelIgnore
    private String instructionDocStatus;

    @ApiModelProperty("状态含义")
    @ExcelColumn(title = "状态")
    @ExcelProperty(value = "配送单状态", index = 2)
    @ColumnWidth(13)
    private String instructionDocStatusMeaning;

    @ApiModelProperty("是否备齐")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "suiteFlagMeaning")
    @ExcelIgnore
    private String suiteFlag;

    @ApiModelProperty("是否备齐含义")
    @ExcelColumn(title = "是否备齐")
    @ExcelProperty(value = "是否备齐", index = 3)
    @ColumnWidth(13)
    private String suiteFlagMeaning;

    @ApiModelProperty("工厂")
    @ExcelColumn(title = "工厂")
    @ExcelProperty(value = "工厂", index = 4)
    @ColumnWidth(13)
    private String siteCode;

    @ApiModelProperty("产线")
    @ExcelColumn(title = "产线")
    @ExcelProperty(value = "产线", index = 5)
    @ColumnWidth(13)
    private String productionLineCode;

    @ApiModelProperty("工段")
    @ExcelColumn(title = "工段")
    @ExcelProperty(value = "工段", index = 6)
    @ColumnWidth(13)
    private String workcellCode;

    @ApiModelProperty(value = "目标仓库")
    @ExcelColumn(title = "目标仓库")
    @ExcelProperty(value = "目标仓库", index = 7)
    @ColumnWidth(13)
    private String toWarehouseCode;

    @ApiModelProperty("创建人")
    @ExcelProperty(value = "创建人", index = 8)
    @ColumnWidth(13)
    private String createdByName;

    @ApiModelProperty("创建时间")
    @ExcelColumn(title = "创建时间")
    @ExcelProperty(value = "创建时间", index = 9)
    @ColumnWidth(24)
    private String creationDate;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注")
    @ExcelProperty(value = "备注", index = 12)
    @ColumnWidth(18)
    private String remark;

    @ApiModelProperty("行号")
    @ExcelColumn(title = "行号")
    @ExcelProperty(value = "行号", index = 13)
    @ColumnWidth(18)
    private String instructionLineNum;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(title = "物料编码")
    @ExcelProperty(value = "物料", index = 14)
    @ColumnWidth(18)
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(title = "物料名称")
    @ExcelProperty(value = "物料描述", index = 15)
    @ColumnWidth(18)
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(title = "物料版本")
    @ExcelProperty(value = "物料版本", index = 16)
    @ColumnWidth(18)
    private String materialVersion;

    @ApiModelProperty("行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_LINE_STATUS", meaningField = "instructionStatusMeaning")
    @ExcelIgnore
    private String instructionStatus;

    @ApiModelProperty("行状态含义")
    @ExcelColumn(title = "行状态")
    @ExcelProperty(value = "行状态", index = 17)
    @ColumnWidth(18)
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "需求数量")
    @ExcelColumn(title = "需求数量")
    @ExcelProperty(value = "需求数量", index = 19)
    @ColumnWidth(18)
    private BigDecimal quantity;

    @ApiModelProperty(value = "备料数量")
    @ExcelColumn(title = "备料数量")
    @ExcelProperty(value = "备料数量", index = 20)
    @ColumnWidth(18)
    private BigDecimal actualQty;

    @ApiModelProperty(value = "签收数量")
    @ExcelColumn(title = "签收数量")
    @ExcelProperty(value = "签收数量", index = 21)
    @ColumnWidth(18)
    private BigDecimal signedQty;

    @ApiModelProperty("单位编码")
    @ExcelColumn(title = "单位")
    @ExcelProperty(value = "单位", index = 22)
    @ColumnWidth(18)
    private String uomCode;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单分配数量")
    private BigDecimal woDistQty;

    @ApiModelProperty("销售订单行-行号")
    @ExcelColumn(title = "销售订单行-行号")
    @ExcelProperty(value = "销售订单行-行号", index = 18)
    @ColumnWidth(18)
    private String soLine;

    @ApiModelProperty("更新人")
    @ExcelProperty(value = "更新人", index = 10)
    @ColumnWidth(18)
    private String lastUpdatedByName;

    @ApiModelProperty("更新时间")
    @ExcelProperty(value = "更新时间", index = 11)
    @ColumnWidth(18)
    private String lastUpdateDate;
}
