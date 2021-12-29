package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 17:17
 */
@Data
@ExcelSheet(zh = "非标产品报表")
public class HmeNonStandardReportVO2 implements Serializable {

    private static final long serialVersionUID = -4438555298412095332L;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间", order = 0)
    private String workshopName;

    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线", order = 1)
    private String prodLineName;

    @ApiModelProperty(value = "工单状态")
    @LovValue(lovCode = "MT.WO_STATUS", meaningField = "woStatusMeaning")
    private String woStatus;

    @ApiModelProperty(value = "工单状态含义")
    @ExcelColumn(zh = "工单状态", order = 2)
    private String woStatusMeaning;

    @ApiModelProperty(value = "销售订单号")
    @ExcelColumn(zh = "销售订单号", order = 3)
    private String soNum;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号", order = 4)
    private String workOrderNum;

    @ApiModelProperty(value = "产品料号ID")
    private String materialId;

    @ApiModelProperty(value = "产品料号")
    @ExcelColumn(zh = "产品料号", order = 5)
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述", order = 6)
    private String materialName;

    @ApiModelProperty(value = "生产订单创建时间")
    @ExcelColumn(zh = "生产订单创建时间", order = 7)
    private String creationDate;

    @ApiModelProperty(value = "生产订单下达时间")
    @ExcelColumn(zh = "生产订单下达时间", order = 8)
    private String releaseDate;

    @ApiModelProperty(value = "客户编码")
    @ExcelColumn(zh = "客户编码", order = 9)
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    @ExcelColumn(zh = "客户名称", order = 10)
    private String customerName;

    @ApiModelProperty(value = "工单数量")
    @ExcelColumn(zh = "工单数量", order = 11)
    private BigDecimal woQty;

    @ApiModelProperty(value = "待上线数量")
    @ExcelColumn(zh = "待上线数量", order = 12)
    private BigDecimal waitQty;

    @ApiModelProperty(value = "在线数量")
    @ExcelColumn(zh = "在线数量", order = 13)
    private BigDecimal wipQty;

    @ApiModelProperty(value = "完成数量")
    @ExcelColumn(zh = "完成数量", order = 14)
    private BigDecimal completedQty;

    @ApiModelProperty(value = "待入库")
    @ExcelColumn(zh = "待入库", order = 15)
    private BigDecimal notStock;

    @ApiModelProperty(value = "已入库")
    @ExcelColumn(zh = "已入库", order = 16)
    private BigDecimal inStock;

    @ApiModelProperty(value = "工单备注")
    @ExcelColumn(zh = "工单备注", order = 17)
    private String remark;
}
