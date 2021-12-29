package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;


/**
 * <p>
 * 调拨汇总报表
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:30
 */
@Data
@ExcelSheet(title = "调拨汇总报表")
public class WmsTransferSummaryVO {
    @ApiModelProperty("序号")
    private Integer sequence;

    @ApiModelProperty("站点编码")
    @ExcelColumn(title = "工厂", order = 2)
    private String siteCode;

    @ApiModelProperty("调拨单号")
    @ExcelColumn(title = "调拨单", order = 0)
    private String instructionDocNum;

    @ApiModelProperty("状态")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("状态含义")
    @ExcelColumn(title = "调拨单状态", order = 1)
    private String instructionDocStatusMeaning;

    @ApiModelProperty("类型")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("类型含义")
    @ExcelColumn(title = "调拨单类型", order = 3)
    private String instructionDocTypeMeaning;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注", order = 6)
    private String remark;

    @ApiModelProperty("制单人")
    @ExcelColumn(title = "制单人", order = 4)
    private String createdByName;

    @ApiModelProperty("制单时间")
    @ExcelColumn(title = "制单时间", order = 5)
    private String creationDate;

    @ApiModelProperty("行号")
    @ExcelColumn(title = "行号", order = 7)
    private String instructionLineNum;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(title = "物料", order = 8)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(title = "物料描述", order = 10)
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(title = "物料版本", order = 11)
    private String materialVersion;

    @ApiModelProperty("行状态")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC_LINE.STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态含义")
    @ExcelColumn(title = "行状态", order = 15)
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "制单数量")
    @ExcelColumn(title = "制单数量", order = 12)
    private BigDecimal quantity;

    @ApiModelProperty(value = "已签收数量")
    @ExcelColumn(title = "已签收数量", order = 14)
    private BigDecimal actualQuantity;

    @ApiModelProperty(value = "来源仓库")
    @ExcelColumn(title = "来源仓库", order = 16)
    private String fromWarehouseCode;

    @ApiModelProperty(value = "来源货位")
    @ExcelColumn(title = "来源货位", order = 17)
    private String fromLocatorCode;

    @ApiModelProperty(value = "目标仓库")
    @ExcelColumn(title = "目标仓库", order = 18)
    private String toWarehouseCode;

    @ApiModelProperty(value = "目标货位")
    @ExcelColumn(title = "目标货位", order = 19)
    private String toLocatorCode;

    @ApiModelProperty("超发设置")
    @LovValue(lovCode = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty("超发设置含义")
    @ExcelColumn(title = "超发设置", order = 22)
    private String excessSettingMeaning;

    @ApiModelProperty("超发值")
    @ExcelColumn(title = "超发值", order = 23)
    private String excessValue;

    @ApiModelProperty("待调拨数量")
    @ExcelColumn(zh = "待调拨数量", order = 13)
    private BigDecimal waitAllocationQty;

    @ApiModelProperty("单位")
    @ExcelColumn(zh = "单位", order = 9)
    private String uomCode;

    @ApiModelProperty("最新执行人")
    @ExcelColumn(zh = "最新执行人", order = 20)
    private String executorUser;

    @ApiModelProperty("最新执行时间")
    @ExcelColumn(zh = "最新执行时间", order = 21)
    private String executorDate;
}
