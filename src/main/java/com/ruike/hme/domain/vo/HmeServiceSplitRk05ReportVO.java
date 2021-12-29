package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * 售后在制品盘点-半成品报表
 *
 * @author penglin.sui@hand-china.com 2021-03-31 16:40:00
 */

@Data
@ExcelSheet(zh = "售后在制品盘点-半成品")
public class HmeServiceSplitRk05ReportVO implements Serializable {
    private static final long serialVersionUID = -8696385536176429420L;

    @ApiModelProperty("主键ID")
    private String splitRecordId;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号",order = 1)
    private String workOrderNum;

    @ApiModelProperty("接收SN")
    @ExcelColumn(zh = "接收SN",order = 2)
    private String snNum;

    @ApiModelProperty("当前SN")
    @ExcelColumn(zh = "当前SN",order = 3)
    private String materialLotCode;

    @ApiModelProperty("有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty("有效性含义")
    @ExcelColumn(zh = "条码有效性",order = 4)
    private String enableFlagMeaning;

    @ApiModelProperty("在制标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "mfFlagMeaning")
    private String mfFlag;

    @ApiModelProperty("在制标识含义")
    @ExcelColumn(zh = "在制标识",order = 5)
    private String mfFlagMeaning;

    @ApiModelProperty("条码状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;

    @ApiModelProperty("条码状态含义")
    @ExcelColumn(zh = "条码状态",order = 6)
    private String materialLotStatusMeaning;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码",order = 7)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述",order = 8)
    private String materialName;

    @ApiModelProperty("工单状态")
    @LovValue(lovCode = "MT.WO_STATUS", meaningField = "workOrderStatusMeaning")
    private String workOrderStatus;

    @ApiModelProperty("工单状态含义")
    @ExcelColumn(zh = "工单状态",order = 9)
    private String workOrderStatusMeaning;

    @ApiModelProperty("是否创建SN")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "isCreateSnMeaning")
    private String isCreateSn;

    @ApiModelProperty("是否创建SN含义")
    @ExcelColumn(zh = "是否创建SN",order = 10)
    private String isCreateSnMeaning;

    @ApiModelProperty("工单结束时间")
    @ExcelColumn(zh = "工单结束时间",order = 11)
    private String actualEndDate;

    @ApiModelProperty("拆机时间")
    @ExcelColumn(zh = "拆机时间",order = 12)
    private String splitTime;

    @ApiModelProperty("当前状态")
    @LovValue(lovCode = "HME.SPLIT_STATUS", meaningField = "splitStatusMeaning")
    private String splitStatus;

    @ApiModelProperty("当前状态含义")
    @ExcelColumn(zh = "当前状态",order = 13)
    private String splitStatusMeaning;

    @ApiModelProperty("当前工位编码")
    @ExcelColumn(zh = "当前工位编码",order = 14)
    private String workcellCode;

    @ApiModelProperty("当前工位描述")
    @ExcelColumn(zh = "当前工位描述",order = 15)
    private String workcellName;

    @ApiModelProperty("所在货位编码")
    @ExcelColumn(zh = "所在货位编码",order = 16)
    private String locatorCode;

    @ApiModelProperty("所在货位名称")
    @ExcelColumn(zh = "所在货位名称",order = 17)
    private String locatorName;

    @ApiModelProperty("所在仓库编码")
    @ExcelColumn(zh = "所在仓库编码",order = 18)
    private String warehouseCode;

    @ApiModelProperty("所在仓库名称")
    @ExcelColumn(zh = "所在仓库名称",order = 19)
    private String warehouseName;

    @ApiModelProperty("物料组")
    @ExcelColumn(zh = "物料组",order = 20)
    private String itemGroupCode;

    @ApiModelProperty("物料组描述")
    @ExcelColumn(zh = "物料组描述",order = 21)
    private String itemGroupDescription;
}
