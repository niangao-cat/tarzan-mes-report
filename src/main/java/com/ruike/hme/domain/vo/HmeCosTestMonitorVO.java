package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:54
 */
@Data
@ExcelSheet(zh = "COS良率监控报表")
public class HmeCosTestMonitorVO implements Serializable {

    private static final long serialVersionUID = -279772787383054529L;

    @ApiModelProperty("截留单据号")
    @ExcelColumn(zh = "截留单据号", order = 1)
    private String monitorDocNum;
    @ApiModelProperty("COS类型")
    @ExcelColumn(zh = "COS类型", order = 2)
    private String cosType;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    @ExcelColumn(zh = "产线", order = 3)
    private String prodLineCode;
    @ApiModelProperty("WAFER")
    @ExcelColumn(zh = "Wafer", order = 4)
    private String wafer;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("工单编码")
    @ExcelColumn(zh = "工单", order = 5)
    private String workOrderNum;
    @ApiModelProperty("测试当前良率")
    @ExcelColumn(zh = "测试当前良率", order = 6)
    private String testPassRate;
    @ApiModelProperty("来料录入总数")
    @ExcelColumn(zh = "来料录入总数", order = 7)
    private BigDecimal incomeInputTotal;
    @ApiModelProperty("来料录入基准数")
    @ExcelColumn(zh = "来料录入基准数", order = 8)
    private BigDecimal incomeInputBaseNum;
    @ApiModelProperty("来料良率")
    private BigDecimal inputPassRate;
    @ApiModelProperty("取片出站总数")
    @ExcelColumn(zh = "取片出站总数", order = 9)
    private BigDecimal qpSiteOutTotal;
    @ApiModelProperty("贴片进站总数")
    @ExcelColumn(zh = "贴片进站总数", order = 10)
    private BigDecimal tpSiteInTotal;
    @ApiModelProperty("贴片出站总数")
    @ExcelColumn(zh = "贴片出站总数", order = 11)
    private BigDecimal tpSiteOutTotal;
    @ApiModelProperty("贴片滞留")
    @ExcelColumn(zh = "贴片滞留", order = 12)
    private BigDecimal tpStayNum;
    @ApiModelProperty("打线进站总数")
    @ExcelColumn(zh = "打线进站总数", order = 13)
    private BigDecimal dxSiteInTotal;
    @ApiModelProperty("打线出站总数")
    @ExcelColumn(zh = "打线出站总数", order = 14)
    private BigDecimal dxSiteOutTotal;
    @ApiModelProperty("打线滞留")
    @ExcelColumn(zh = "打线滞留", order = 15)
    private BigDecimal dxStayNum;
    @ApiModelProperty("测试进站总数")
    @ExcelColumn(zh = "测试进站总数", order = 16)
    private BigDecimal csSiteInTotal;
    @ApiModelProperty("测试出站总数")
    @ExcelColumn(zh = "测试出站总数", order = 17)
    private BigDecimal csSiteOutTotal;
    @ApiModelProperty("测试滞留")
    @ExcelColumn(zh = "测试滞留", order = 18)
    private BigDecimal csStayNum;
    @ApiModelProperty("目检进站总数")
    @ExcelColumn(zh = "目检进站总数", order = 19)
    private BigDecimal mjSiteInTotal;
    @ApiModelProperty("目检出站总数")
    @ExcelColumn(zh = "目检出站总数", order = 20)
    private BigDecimal mjSiteOutTotal;
    @ApiModelProperty("目检滞留")
    @ExcelColumn(zh = "目检滞留", order = 21)
    private BigDecimal mjStayNum;
    @ApiModelProperty("操作时间")
    @ExcelColumn(zh = "操作时间", order = 22)
    private String operationDate;
}
