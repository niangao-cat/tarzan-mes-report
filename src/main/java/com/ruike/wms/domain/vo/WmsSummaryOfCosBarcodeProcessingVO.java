package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Data
@ExcelSheet(zh = "COS条码加工汇总")
public class WmsSummaryOfCosBarcodeProcessingVO implements Serializable {

    private static final long serialVersionUID = -1658498761312564300L;

    @ApiModelProperty("工单主键")
    private String workOrderId;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本")
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelColumn(zh = "版本描述")
    private String productionVersionDesc;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty("工单芯片数")
    @ExcelColumn(zh = "工单芯片数")
    private String qty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(zh = "WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("COS类型描述")
    @ExcelColumn(zh = "COS类型")
    private String cosTypeMeaning;

    @ApiModelProperty("条码")
    @ExcelColumn(zh = "条码")
    private String materialLotCode;

    @ApiModelProperty("条码id")
    private String materialLotId;

    @ApiModelProperty("cos数量")
    @ExcelColumn(zh = "COS数量")
    private BigDecimal snQty;

    @ApiModelProperty("合格数量")
    @ExcelColumn(zh = "合格数量")
    private BigDecimal okQty;

    @ApiModelProperty("不良总数")
    @ExcelColumn(zh = "不良总数")
    private BigDecimal ngQty;

    @ApiModelProperty("jobId")
    private String jobId;

    @ApiModelProperty("热沉类型")
    @ExcelColumn(zh = "热沉类型")
    private String sinkType;

    @ApiModelProperty("热沉条码")
    @ExcelColumn(zh = "热沉条码")
    private String sinkCode;

    @ApiModelProperty("热沉物料")
    @ExcelColumn(zh = "热沉物料")
    private String sinkMaterialCode;

    @ApiModelProperty("热沉供应商批次")
    @ExcelColumn(zh = "热沉供应商批次")
    private String sinkSupplierLot;

    @ApiModelProperty("金锡比")
    @ExcelColumn(zh = "金锡比")
    private String ausnRatio;

    @ApiModelProperty("金线条码")
    @ExcelColumn(zh = "金线条码")
    private String goldCode;

    @ApiModelProperty("金线物料")
    @ExcelColumn(zh = "金线物料")
    private String goldMaterialCode;

    @ApiModelProperty("金线供应商批次")
    @ExcelColumn(zh = "金线供应商批次")
    private String goldSupplierLot;

    @ApiModelProperty("操作人")
    @ExcelColumn(zh = "操作人")
    private String realName;

    @ApiModelProperty("操作人ID")
    private String createdBy;

    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码")
    private String workcellCode;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位描述")
    @ExcelColumn(zh = "工位描述")
    private String workcellName;

    @ApiModelProperty("工序Id")
    private String processId;

    @ApiModelProperty("工序")
    @ExcelColumn(zh = "工序描述")
    private String processName;

    @ApiModelProperty("工段Id")
    private String lineWorkcellId;

    @ApiModelProperty("工段")
    @ExcelColumn(zh = "工段描述")
    private String lineWorkcellName;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(zh = "生产线描述")
    private String prodLineName;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date creationDate;

    @ApiModelProperty("设备编码")
    @ExcelColumn(zh = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码")
    private String labCode;

}
