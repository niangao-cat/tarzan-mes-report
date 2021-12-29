package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 11:39
 */
@Data
@ExcelSheet(title = "COS目检条码表")
public class HmeCosCheckBarcodesVO {

    private String jobId;

    private String loadSequence;

    @ApiModelProperty("行")
    private Long loadRow;

    @ApiModelProperty("列")
    private Long loadColumn;

    @ApiModelProperty("工单号")
    @ExcelColumn(title = "工单号", order = 1)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(title = "工单版本", order = 2)
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelColumn(title = "版本描述", order = 3)
    private String productionVersionDesc;

    @ApiModelProperty("产品编码")
    @ExcelColumn(title = "产品编码", order = 4)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(title = "产品描述", order = 5)
    private String materialName;

    @ApiModelProperty("工单芯片数")
    @ExcelColumn(title = "工单芯片数", order = 6)
    private String qty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(title = "WAFER", order = 7)
    private String wafer;

    @ApiModelProperty("COS类型")
    @ExcelColumn(title = "COS类型", order = 8)
    private String cosType;

    @ApiModelProperty("条码")
    @ExcelColumn(title = "条码", order = 9)
    private String materialLotCode;

    @ApiModelProperty("COS数量")
    @ExcelColumn(title = "COS数量", order = 10)
    private BigDecimal snQty;

    @ApiModelProperty("合格数量")
    @ExcelColumn(title = "合格数量", order = 11)
    private String passQty;

    @ApiModelProperty("位置")
    @ExcelColumn(title = "位置", order = 12)
    private String rowCloumn;

    @ApiModelProperty("热沉编号")
    @ExcelColumn(title = "热沉编号", order = 13)
    private String hotSinkCode;

    @ApiModelProperty("不良编码")
    @ExcelColumn(title = "不良编码", order = 14)
    private String ncCode;

    @ApiModelProperty("不良描述")
    @ExcelColumn(title = "不良描述", order = 15)
    private String description;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注", order = 16)
    private String note;

    @ApiModelProperty("工序状态")
    @ExcelColumn(title = "工序状态", order = 17)
    private String orderType;

    @ApiModelProperty("操作者")
    @ExcelColumn(title = "操作者", order = 18)
    private String operatorName;

    @ApiModelProperty("进站时间")
    @ExcelColumn(title = "进站时间", pattern = BaseConstants.Pattern.DATETIME, order = 19)
    private Date siteInDate;

    @ApiModelProperty("出站时间")
    @ExcelColumn(title = "出站时间", pattern = BaseConstants.Pattern.DATETIME, order = 20)
    private Date siteOutDate;

    @ApiModelProperty(value = "加工时长/m")
    @ExcelColumn(title = "加工时长/m", order = 21)
    private BigDecimal processTime;

    @ApiModelProperty("工位编码")
    @ExcelColumn(title = "工位编码", order = 22)
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(title = "工位描述", order = 23)
    private String workcellName;

    @ApiModelProperty("设备编码")
    @ExcelColumn(title = "设备编码", order = 24)
    private String equipment;

    @ApiModelProperty("测试机台")
    @ExcelColumn(title = "测试机台", order = 25)
    private String bench;

    @ApiModelProperty("贴片设备")
    @ExcelColumn(title = "贴片设备", order = 26)
    private String patch;

    @ApiModelProperty("热沉类型")
    @ExcelColumn(title = "热沉类型", order = 27)
    private String hotType;

    @ApiModelProperty("热沉投料条码")
    @ExcelColumn(title = "热沉投料条码", order = 28)
    private String barcode;

    @ApiModelProperty("热沉供应商批次")
    @ExcelColumn(title = "热沉供应商批次", order = 29)
    private String hotSinkSupplierLot;

    @ApiModelProperty("金锡比")
    @ExcelColumn(title = "金锡比", order = 30)
    private String ausnRatio;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(title = "实验代码", order = 31)
    private String experimentCode;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序描述")
    @ExcelColumn(title = "工序描述", order = 32)
    private String processName;

    @ApiModelProperty(value = "工段编码")
    private String lineWorkcellCode;

    @ApiModelProperty(value = "工段描述")
    @ExcelColumn(title = "工段描述", order = 33)
    private String lineWorkcellName;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线描述")
    @ExcelColumn(title = "产线描述", order = 34)
    private String prodLineName;

}
