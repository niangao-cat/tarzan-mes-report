package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/3 16:46
 */
@Data
@ExcelSheet(zh = "COS退料报表")
public class HmeCosReturnVO implements Serializable {

    private static final long serialVersionUID = -2946930958162062740L;

    @ApiModelProperty("退料产线")
    @ExcelColumn(zh = "退料产线", order = 1)
    private String prodLineCode;
    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号", order = 2)
    private String workOrderNum;
    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码", order = 3)
    private String materialCode;
    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述", order = 4)
    private String materialName;
    @ApiModelProperty("WAFER")
    @ExcelColumn(zh = "WAFER", order = 5)
    private String waferNum;
    @ApiModelProperty("COS类型")
    @ExcelColumn(zh = "COS类型", order = 6)
    private String cosType;
    @ApiModelProperty("退料条码")
    @ExcelColumn(zh = "退料条码", order = 7)
    private String returnMaterialLotCode;
    @ApiModelProperty("条码数量")
    @ExcelColumn(zh = "条码数量", order = 8)
    private BigDecimal primaryUomQty;
    @ApiModelProperty("组件物料编码")
    @ExcelColumn(zh = "组件物料编码", order = 9)
    private String componentMaterialCode;
    @ApiModelProperty("组件物料描述")
    @ExcelColumn(zh = "组件物料描述", order = 10)
    private String componentMaterialName;
    @ApiModelProperty("处理方式")
    @LovValue(lovCode = "HME.COS_RETURN_TYPE", meaningField = "returnTypeMeaning")
    private String returnType;
    @ApiModelProperty("处理方式")
    @ExcelColumn(zh = "处理方式", order = 11)
    private String returnTypeMeaning;
    @ApiModelProperty("单位")
    @ExcelColumn(zh = "单位", order = 12)
    private String uomCode;
    @ApiModelProperty("单位用量")
    @ExcelColumn(zh = "单位用量", order = 13)
    private BigDecimal usageQty;
    @ApiModelProperty("目标条码")
    private String targetMaterialLotId;
    @ApiModelProperty("目标条码")
    @ExcelColumn(zh = "目标条码", order = 14)
    private String targetMaterialLotCode;
    @ApiModelProperty("数量")
    @ExcelColumn(zh = "数量", order = 15)
    private BigDecimal returnQty;
    @ApiModelProperty("供应商")
    @ExcelColumn(zh = "供应商", order = 16)
    private String supplierName;
    @ApiModelProperty("供应商批次")
    @ExcelColumn(zh = "供应商批次", order = 17)
    private String supplierLot;
    @ApiModelProperty("库存批次")
    @ExcelColumn(zh = "库存批次", order = 18)
    private String lot;
    @ApiModelProperty("不良代码描述")
    @ExcelColumn(zh = "不良代码描述", order = 19)
    private String ncDescription;
    @ApiModelProperty("操作人")
    @ExcelColumn(zh = "操作人", order = 20)
    private String realName;
    @ApiModelProperty("操作时间")
    @ExcelColumn(zh = "操作时间", order = 21)
    private String creationDate;
    @ApiModelProperty("操作工位编码")
    @ExcelColumn(zh = "操作工位编码", order = 22)
    private String workcellCode;
    @ApiModelProperty("操作工位描述")
    @ExcelColumn(zh = "操作工位描述", order = 23)
    private String workcellName;
}
