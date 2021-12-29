package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * COS条码加工异常汇总返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:00
 */
@Data
@ExcelSheet(zh = "COS条码异常汇总")
public class HmeCosBarCodeExceptionVO implements Serializable {

    private static final long serialVersionUID = 4563749491472143022L;

    @ApiModelProperty("工单")
    @ExcelColumn(zh = "工单",order = 0)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本",order = 1)
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelColumn(zh = "版本描述",order = 2)
    private String productionVersionDesc;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码",order = 3)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述",order = 4)
    private String materialName;

    @ApiModelProperty("工单芯片数")
    @ExcelColumn(zh = "工单芯片数",order = 5)
    private BigDecimal qty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(zh = "WAFER",order = 6)
    private String waferNum;

    @ApiModelProperty("COS类型")
    @ExcelColumn(zh = "COS类型",order = 7)
    private String cosType;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码",order = 8)
    private String labCode;

    @ApiModelProperty("条码ID")
    private String materialLotId;

    @ApiModelProperty("条码")
    @ExcelColumn(zh = "条码",order = 9)
    private String materialLotCode;

    @ApiModelProperty("cos数量")
    @ExcelColumn(zh = "COS数量",order = 10)
    private BigDecimal snQty;

    @ApiModelProperty("不良总数")
    @ExcelColumn(zh = "不良总数",order = 11)
    private BigDecimal defectCountSum;

    @ApiModelProperty("位置")
    @ExcelColumn(zh = "位置",order = 12)
    private String location;

    @ApiModelProperty("热沉编号")
    @ExcelColumn(zh = "热沉编号",order = 13)
    private String hotSinkCode;

    @ApiModelProperty("不良代码")
    @ExcelColumn(zh = "不良代码",order = 14)
    private String description;

    @ApiModelProperty("热沉类型")
    @ExcelColumn(zh = "热沉类型",order = 15)
    private String heatSinkType;

    @ApiModelProperty("热沉条码")
    @ExcelColumn(zh = "热沉条码",order = 16)
    private String heatSinkMaterialLot;

    @ApiModelProperty("热沉物料ID")
    private String heatSinkMaterialId;

    @ApiModelProperty("热沉物料编码")
    @ExcelColumn(zh = "热沉物料编码",order = 17)
    private String heatSinkMaterialCode;

    @ApiModelProperty("热沉供应商批次")
    @ExcelColumn(zh = "热沉供应商批次",order = 18)
    private String heatSinkSupplierLot;

    @ApiModelProperty("焊料金锡比")
    @ExcelColumn(zh = "焊料金锡比",order = 19)
    private String solderAusnRatio;

    @ApiModelProperty("金线条码")
    @ExcelColumn(zh = "金线条码",order = 20)
    private String goldWireMaterialLot;

    @ApiModelProperty("金线物料ID")
    private String goldWireMaterialId;

    @ApiModelProperty("金线物料编码")
    @ExcelColumn(zh = "金线物料编码",order = 21)
    private String goldWireMaterialCode;

    @ApiModelProperty("金线供应商批次")
    @ExcelColumn(zh = "金线供应商批次",order = 22)
    private String goldWireSupplierLot;

    @ApiModelProperty("操作时间")
    @ExcelColumn(zh = "操作时间",order = 23)
    private String creationDate;

    @ApiModelProperty("操作者")
    @ExcelColumn(zh = "操作者",order = 24)
    private String realName;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码",order = 25)
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(zh = "工位描述",order = 26)
    private String workcellName;

    @ApiModelProperty("工序描述")
    @ExcelColumn(zh = "工序描述",order = 27)
    private String processName;

    @ApiModelProperty("工段描述")
    @ExcelColumn(zh = "工段描述",order = 28)
    private String lineWorkcellName;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(zh = "生产线描述",order = 29)
    private String prodLineName;

    @ApiModelProperty("设备编码")
    @ExcelColumn(zh = "设备编码",order = 30)
    private String assetEncoding;

    @ApiModelProperty("工序作业ID")
    private String jobId;

    @ApiModelProperty("芯片序列号")
    private String loadSequence;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;};
        if (o == null || getClass() != o.getClass()) {return false;};
        HmeCosBarCodeExceptionVO a = (HmeCosBarCodeExceptionVO) o;
        return Objects.equals(workOrderNum, a.workOrderNum) &&
                Objects.equals(qty, a.qty) &&
                Objects.equals(productionVersion, a.productionVersion) &&
                Objects.equals(productionVersionDesc, a.productionVersionDesc) &&
                Objects.equals(materialLotId, a.materialLotId) &&
                Objects.equals(materialLotCode, a.materialLotCode) &&
                Objects.equals(waferNum, a.waferNum) &&
                Objects.equals(cosType, a.cosType) &&
                Objects.equals(snQty, a.snQty) &&
                Objects.equals(heatSinkMaterialLot, a.heatSinkMaterialLot) &&
                Objects.equals(goldWireMaterialLot, a.goldWireMaterialLot) &&
                Objects.equals(hotSinkCode, a.hotSinkCode) &&
                Objects.equals(heatSinkMaterialId, a.heatSinkMaterialId) &&
                Objects.equals(heatSinkMaterialCode, a.heatSinkMaterialCode) &&
                Objects.equals(heatSinkSupplierLot, a.heatSinkSupplierLot) &&
                Objects.equals(solderAusnRatio, a.solderAusnRatio) &&
                Objects.equals(goldWireMaterialId, a.goldWireMaterialId) &&
                Objects.equals(goldWireMaterialCode, a.goldWireMaterialCode) &&
                Objects.equals(goldWireSupplierLot, a.goldWireSupplierLot) &&
                Objects.equals(realName, a.realName) &&
                Objects.equals(workcellCode, a.workcellCode) &&
                Objects.equals(workcellName, a.workcellName) &&
                Objects.equals(materialCode, a.materialCode) &&
                Objects.equals(materialName, a.materialName) &&
                Objects.equals(location, a.location) &&
                Objects.equals(jobId, a.jobId) &&
                Objects.equals(loadSequence, a.loadSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrderNum,qty,productionVersion,productionVersionDesc,materialLotCode,materialLotId,snQty,waferNum,cosType,
                heatSinkMaterialLot,goldWireMaterialLot,hotSinkCode,heatSinkMaterialId,heatSinkMaterialCode,heatSinkSupplierLot,
                solderAusnRatio,goldWireMaterialId,goldWireMaterialCode,goldWireSupplierLot,realName,workcellCode,workcellName,
                materialCode,materialName,location,jobId,loadSequence);
    }
}