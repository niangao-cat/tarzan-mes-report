package com.ruike.hme.api.dto.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 芯片装载作业 展现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 18:40
 */
@Data
@ExcelSheet(zh = "COS芯片作业记录")
public class HmeLoadJobRept implements Serializable {
    private static final long serialVersionUID = -2312042200184461230L;

    @ApiModelProperty(value = "主键ID")
    private String loadJobId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编码")
    @ExcelColumn(zh = "工单")
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本")
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelColumn(zh = "版本描述")
    private String productionVersionDescription;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "作业类型")
    @LovValue(lovCode = "HME_LOAD_JOB_TYPE", meaningField = "loadJobTypeMeaning")
    private String loadJobType;

    @ApiModelProperty(value = "作业类型含义")
    @ExcelColumn(zh = "作业类型")
    private String loadJobTypeMeaning;

    @ApiModelProperty(value = "wafer")
    @ExcelColumn(zh = "wafer")
    private String waferNum;

    @ApiModelProperty(value = "COS类型")
    @ExcelColumn(zh = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    @ExcelColumn(zh = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "位置行")
    private Integer loadRow;

    @ApiModelProperty(value = "位置列")
    private Integer loadColumn;

    @ApiModelProperty(value = "位置")
    @ExcelColumn(zh = "位置")
    private String position;

    @ApiModelProperty(value = "来源条码ID")
    private String sourceMaterialLotId;

    @ApiModelProperty(value = "来源条码编码")
    @ExcelColumn(zh = "来源条码")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "来源位置行")
    private Integer sourceLoadRow;

    @ApiModelProperty(value = "来源位置列")
    private Integer sourceLoadColumn;

    @ApiModelProperty(value = "来源位置")
    @ExcelColumn(zh = "来源位置")
    private String sourcePosition;

    @ApiModelProperty(value = "热沉编码")
    @ExcelColumn(zh = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "工艺描述")
    @ExcelColumn(zh = "工艺描述")
    private String operationDescription;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    @ExcelColumn(zh = "工位")
    private String workcellStationCode;

    @ApiModelProperty(value = "工位描述")
    @ExcelColumn(zh = "工位描述")
    private String workcellStationName;

    @ApiModelProperty(value = "工序描述")
    @ExcelColumn(zh = "工序描述")
    private String workcellProcessName;

    @ApiModelProperty(value = "工段描述")
    @ExcelColumn(zh = "工段描述")
    private String workcellLineName;

    @ApiModelProperty(value = "生产线描述")
    @ExcelColumn(zh = "生产线描述")
    private String prodLineName;

    @ApiModelProperty(value = "投料物料ID")
    private String bomMaterialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码")
    private String bomMaterialCode;

    @ApiModelProperty(value = "投料条码ID")
    private String bomMaterialLotId;

    @ApiModelProperty(value = "投料条码")
    @ExcelColumn(zh = "投料条码")
    private String bomMaterialLotCode;

    @ApiModelProperty(value = "供应商批次")
    @ExcelColumn(zh = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "供应商编码")
    @ExcelColumn(zh = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "不良代码")
    @ExcelColumn(zh = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码描述")
    @ExcelColumn(zh = "不良代码描述")
    private String ncCodeDescription;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME_LOAD_JOB_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "状态")
    private String statusMeaning;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注")
    private String remark;

    @ApiModelProperty(value = "设备")
    @ExcelColumn(zh = "设备")
    private String equipment;

    @ApiModelProperty(value = "金锡比")
    @ExcelColumn(zh = "金锡比")
    private String auSnRate;

    @ApiModelProperty(value = "贴片机台编码")
    @ExcelColumn(zh = "贴片机台编码")
    private String machineCode;

    @ApiModelProperty(value = "投料物料条码供应商ID")
    private String bomMaterialLotSupplier;

    @ApiModelProperty(value = "创建人")
    @ExcelColumn(zh = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelColumn(zh = "创建时间")
    private String creationDate;

    @ApiModelProperty(value = "更新人")
    @ExcelColumn(zh = "更新人")
    private String lastUpdateByName;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelColumn(zh = "更新时间")
    private String lastUpdateDate;

    @ApiModelProperty(value = "装载行序列号")
    @ExcelColumn(zh = "装载行序列号")
    private String loadSequence;
}
