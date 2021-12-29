package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/1 18:23
 */
@Data
@ExcelSheet(title = "售后在制品盘点成品报表")
public class HmeInventoryEndProductVO3 implements Serializable {

    private static final long serialVersionUID = 3106201549052961970L;

    @ApiModelProperty(value = "主键")
    private String splitRecordId;

    @ApiModelProperty(value = "顶层主键")
    private String topSplitRecordId;

    @ApiModelProperty(value = "父层主键")
    private String parentSplitRecordId;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "接收SN")
    @ExcelColumn(zh = "接收SN", order = 0)
    private String snNum;

    @ApiModelProperty(value = "当前SN")
    @ExcelColumn(zh = "当前SN", order = 1)
    private String materialLotCode;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号", order = 5)
    private String materialModel;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号", order = 6)
    private String workOrderNum;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码", order = 9)
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述", order = 10)
    private String materialName;

    @ApiModelProperty(value = "来源SN")
    @ExcelColumn(zh = "来源SN", order = 11)
    private String sourceSnNum;

    @ApiModelProperty(value = "半成品数量")
    @ExcelColumn(zh = "半成品数量", order = 12)
    private BigDecimal qty;

    @ApiModelProperty(value = "拆机时间")
    @ExcelColumn(zh = "拆机时间", order = 13)
    private String splitTime;

    @ApiModelProperty(value = "当前状态")
    @LovValue(lovCode = "HME.SPLIT_STATUS", meaningField = "splitStatusMeaning")
    private String splitStatus;

    @ApiModelProperty(value = "当前状态含义")
    @ExcelColumn(zh = "当前状态", order = 15)
    private String splitStatusMeaning;

    @ApiModelProperty(value = "当前工位编码")
    @ExcelColumn(zh = "当前工位编码", order = 16)
    private String workcellCode;

    @ApiModelProperty(value = "当前工位描述")
    @ExcelColumn(zh = "当前工位描述", order = 17)
    private String workcellName;

    @ApiModelProperty(value = "所在仓库编码")
    @ExcelColumn(zh = "所在仓库编码", order = 18)
    private String warehouseCode;

    @ApiModelProperty(value = "所在仓库名称")
    @ExcelColumn(zh = "所在仓库名称", order = 19)
    private String warehouseName;

    @ApiModelProperty(value = "所在货位编码")
    @ExcelColumn(zh = "所在货位编码", order = 21)
    private String locatorCode;

    @ApiModelProperty(value = "所在货位名称")
    @ExcelColumn(zh = "所在货位名称", order = 22)
    private String locatorName;

    @ApiModelProperty(value = "物料组")
    @ExcelColumn(zh = "物料组", order = 23)
    private String itemGroupCode;

    @ApiModelProperty(value = "物料组描述")
    @ExcelColumn(zh = "物料组描述", order = 24)
    private String itemGroupDescription;

    @ApiModelProperty(value = "返回类型")
    private String backType;

    @ApiModelProperty(value = "所在仓库类型")
    private String warehouseType;

    @ApiModelProperty(value = "所在仓库类型含义")
    @ExcelColumn(zh = "所在仓库类型", order = 20)
    private String warehouseTypeMeaning;

    @ApiModelProperty(value = "工单状态含义")
    @ExcelColumn(zh = "工单状态", order = 7)
    private String wordOrderStatusMeaning;

    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    @ExcelColumn(zh = "条码有效性", order = 2)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "在制标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "mfFlagMeaning")
    private String mfFlag;

    @ApiModelProperty(value = "在制标识含义")
    @ExcelColumn(zh = "在制标识", order = 3)
    private String mfFlagMeaning;

    @ApiModelProperty(value = "条码状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "materialLotCodeStatusMeaning")
    private String materialLotCodeStatus;

    @ApiModelProperty(value = "条码状态含义")
    @ExcelColumn(zh = "条码状态", order = 4)
    private String materialLotCodeStatusMeaning;

    @ApiModelProperty(value = "工单结束时间")
    @ExcelColumn(zh = "工单结束时间", order = 14)
    private String actualEndDate;

    @ApiModelProperty(value = "是否创建SN")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "createSnFlagMeaning")
    private String createSnFlag;

    @ApiModelProperty(value = "是否创建SN含义")
    @ExcelColumn(zh = "是否创建SN", order = 8)
    private String createSnFlagMeaning;

    @ApiModelProperty(value = "入库单号")
    @ExcelColumn(zh = "入库单号", order = 25)
    private String instructionDocNum;

    @ApiModelProperty(value = "入库单是否为空")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "docNumFlagMeaning")
    private String docNumFlag;

    @ApiModelProperty(value = "入库单是否为空含义")
    @ExcelColumn(zh = "入库单是否为空", order = 26)
    private String docNumFlagMeaning;
}
