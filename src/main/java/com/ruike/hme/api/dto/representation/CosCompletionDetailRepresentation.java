package com.ruike.hme.api.dto.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * COS完工芯片明细报表 展示对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 13:49
 */
@Data
@ExcelSheet(zh = "COS完工芯片明细报表")
public class CosCompletionDetailRepresentation implements Serializable {
    private static final long serialVersionUID = -7132259403711534867L;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库")
    private String parentLocatorCode;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "COS类型")
    @ExcelColumn(zh = "COS类型")
    private String cosType;

    @JsonIgnore
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    @ExcelColumn(zh = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "位置行")
    @JsonIgnore
    private Long loadRow;

    @ApiModelProperty(value = "位置列")
    @JsonIgnore
    private Long loadColumn;

    @ApiModelProperty(value = "位置")
    @ExcelColumn(zh = "位置")
    private String position;

    @ApiModelProperty(value = "WAFER")
    @ExcelColumn(zh = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "筛选状态")
    @LovValue(value = "HME.SELECT_STATUS", meaningField = "selectionStatusMeaning")
    private String selectionStatus;

    @ApiModelProperty(value = "筛选状态含义")
    @ExcelColumn(zh = "筛选状态")
    private String selectionStatusMeaning;

    @ApiModelProperty(value = "热沉编码")
    @ExcelColumn(zh = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "挑选来源条码")
    @ExcelColumn(zh = "挑选来源条码")
    private String selectedMaterialLotCode;

    @ApiModelProperty(value = "旧盒子位置")
    private String oldLoad;

    @ApiModelProperty(value = "挑选来源位置")
    @ExcelColumn(zh = "挑选来源位置")
    private String selectedPosition;

    @ApiModelProperty(value = "虚拟号")
    @ExcelColumn(zh = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "路数")
    @ExcelColumn(zh = "路数")
    private String attribute2;

    @ApiModelProperty(value = "器件序列号")
    @ExcelColumn(zh = "器件序列号")
    private String identification;

    @ApiModelProperty(value = "器件物料编码")
    @ExcelColumn(zh = "器件物料编码")
    private String deviceMaterialCode;

    @ApiModelProperty(value = "器件物料描述")
    @ExcelColumn(zh = "器件物料描述")
    private String deviceMaterialName;

    @ApiModelProperty(value = "筛选批次")
    @ExcelColumn(zh = "筛选批次")
    private String preSelectionLot;

    @ApiModelProperty(value = "筛选规则编码")
    @ExcelColumn(zh = "筛选规则编码")
    private String selectionRuleCode;

    @ApiModelProperty(value = "是否绑定工单号")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "bindFlagMeaning")
    private String bindFlag;

    @ApiModelProperty(value = "是否绑定工单号含义")
    @ExcelColumn(zh = "是否绑定工单号")
    private String bindFlagMeaning;

    @ApiModelProperty(value = "投料工单")
    @ExcelColumn(zh = "投料工单")
    private String releaseWorkOrderNum;

    @ApiModelProperty(value = "5A波长")
    @ExcelColumn(zh = "5A波长")
    private BigDecimal a04;

    @ApiModelProperty(value = "平均波长")
    @ExcelColumn(zh = "平均波长")
    private BigDecimal a04Avg;

    @ApiModelProperty(value = "功率")
    @ExcelColumn(zh = "功率")
    private BigDecimal power;

    @ApiModelProperty(value = "功率和")
    @ExcelColumn(zh = "功率和")
    private BigDecimal powerSum;

    @ApiModelProperty(value = "电压")
    @ExcelColumn(zh = "电压")
    private BigDecimal voltage;

    @ApiModelProperty(value = "电压和")
    @ExcelColumn(zh = "电压和")
    private BigDecimal voltageSum;

    @ApiModelProperty(value = "热沉条码")
    @ExcelColumn(zh = "热沉条码")
    private String hotSinkMaterialLotCode;

    @ApiModelProperty(value = "热沉供应商批次号")
    @ExcelColumn(zh = "热沉供应商批次号")
    private String hotSinkSupplierLot;

    @ApiModelProperty(value = "热沉焊料金锡比")
    @ExcelColumn(zh = "热沉焊料金锡比")
    private String hotSinkAuSnRate;

    @ApiModelProperty(value = "金线条码")
    @ExcelColumn(zh = "金线条码")
    private String goldMaterialLotCode;

    @ApiModelProperty(value = "金线供应商批次")
    @ExcelColumn(zh = "金线供应商批次")
    private String goldSupplierLot;

    @ApiModelProperty(value = "预筛选操作人ID", hidden = true)
    @JsonIgnore
    private Long preSelectionOperatorId;

    @ApiModelProperty(value = "预筛选操作人")
    @ExcelColumn(zh = "预筛选操作人")
    private String preSelectionOperatorName;

    @ApiModelProperty(value = "预筛选时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "预筛选时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date preSelectionDate;

    @ApiModelProperty(value = "筛选设备编码")
    @ExcelColumn(zh = "筛选设备编码")
    private String preSelectionAssetEncoding;

    @ApiModelProperty(value = "装箱操作人ID", hidden = true)
    @JsonIgnore
    private Long loadOperatorId;

    @ApiModelProperty(value = "装箱操作人")
    @ExcelColumn(zh = "装箱操作人")
    private String loadOperatorName;

    @ApiModelProperty(value = "装箱时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "装箱时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date loadDate;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    @ExcelColumn(zh = "是否冻结")
    private String freezeFlagMeaning;
}