package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:32
 */
@Data
public class HmeInventoryEndProductVO2 implements Serializable {

    private static final long serialVersionUID = -6807687496203159224L;


    @ApiModelProperty(value = "主键")
    private String splitRecordId;

    @ApiModelProperty(value = "顶层主键")
    private String topSplitRecordId;

    @ApiModelProperty(value = "父层主键")
    private String parentSplitRecordId;

    @ApiModelProperty(value = "接收SN")
    private String snNum;

    @ApiModelProperty(value = "当前SN")
    private String materialLotCode;

    @ApiModelProperty(value = "当前SN条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "型号")
    private String materialModel;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "来源SN")
    private String sourceSnNum;

    @ApiModelProperty(value = "半成品数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "拆机时间")
    private String splitTime;

    @ApiModelProperty(value = "当前状态")
    @LovValue(lovCode = "HME.SPLIT_STATUS", meaningField = "splitStatusMeaning")
    private String splitStatus;

    @ApiModelProperty(value = "当前状态含义")
    private String splitStatusMeaning;

    @ApiModelProperty(value = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "当前工位描述")
    private String workcellName;

    @ApiModelProperty(value = "所在仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "所在仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "所在货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "所在货位名称")
    private String locatorName;

    @ApiModelProperty(value = "物料组")
    private String itemGroupCode;

    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty(value = "展开标识 false-不 true-展开")
    private boolean unfoldFlag;

    @ApiModelProperty(value = "子级记录")
    private List<HmeInventoryEndProductVO2> childList;

    @ApiModelProperty(value = "返回类型")
    private String backType;

    @ApiModelProperty(value = "所在仓库类型")
    private String warehouseType;

    @ApiModelProperty(value = "所在仓库类型含义")
    private String warehouseTypeMeaning;

    @ApiModelProperty(value = "工单状态含义")
    private String wordOrderStatusMeaning;

    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "在制标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "mfFlagMeaning")
    private String mfFlag;

    @ApiModelProperty(value = "在制标识含义")
    private String mfFlagMeaning;

    @ApiModelProperty(value = "条码状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "materialLotCodeStatusMeaning")
    private String materialLotCodeStatus;

    @ApiModelProperty(value = "条码状态含义")
    private String materialLotCodeStatusMeaning;

    @ApiModelProperty(value = "工单结束时间")
    private String actualEndDate;

    @ApiModelProperty(value = "是否创建SN")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "createSnFlagMeaning")
    private String createSnFlag;

    @ApiModelProperty(value = "是否创建SN含义")
    private String createSnFlagMeaning;

    @ApiModelProperty(value = "入库单号")
    private String instructionDocNum;

    @ApiModelProperty(value = "入库单是否为空")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "docNumFlagMeaning")
    private String docNumFlag;

    @ApiModelProperty(value = "入库单是否为空含义")
    private String docNumFlagMeaning;
}
