package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库明细查询报表 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 10:55
 */
@Data
@ExcelSheet(title = "入库明细")
public class HmeStockInDetailsVO implements Serializable {

    private static final long serialVersionUID = 2322193349254542146L;

    @ApiModelProperty("站点")
    @ExcelColumn(title = "站点", order = 0)
    private String siteCode;

    @ApiModelProperty("制造部")
    @ExcelColumn(title = "制造部", order = 1)
    private String attrValue;

    @ApiModelProperty("工单编号")
    @ExcelColumn(title = "工单编号", order = 2)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(title = "工单版本", order = 3)
    private String productionVersion;

    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码", order = 4)
    private String materialCode;

    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述", order = 5)
    private String materialName;

    @ApiModelProperty("工单类型")
    @ExcelColumn(title = "工单类型", order = 6)
    private String workOrderType;

    @ApiModelProperty("工单状态")
    @ExcelColumn(title = "工单状态", order = 7)
    private String workOrderStatus;

    @ApiModelProperty("生产线")
    @ExcelColumn(title = "生产线", order = 8)
    private String prodLineName;

    @ApiModelProperty("工单数量")
    @ExcelColumn(title = "工单数量", order = 9)
    private BigDecimal qty;

    @ApiModelProperty("已入库数量")
    private BigDecimal actualQty;

    @ApiModelProperty("已入库数量总数")
    @ExcelColumn(title = "已入库数量", order = 10)
    private BigDecimal actualQtySum;

    @ApiModelProperty("入库率")
    @ExcelColumn(title = "入库率", order = 11)
    private String rate;

    @ApiModelProperty("序列号")
    @ExcelColumn(title = "序列号", order = 12)
    private String materialLotCode;

    @ApiModelProperty("入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ExcelColumn(title = "入库时间", order = 13)
    private String creationDateStr;

    @ApiModelProperty("入库容器")
    @ExcelColumn(title = "入库容器", order = 14)
    private String containerCode;

    @ApiModelProperty("库存地点")
    @ExcelColumn(title = "库存地点", order = 15)
    private String locatorCode;

    @ApiModelProperty("作业人")
    @ExcelColumn(title = "作业人", order = 16)
    private String realName;

    @ApiModelProperty("单据号")
    @ExcelColumn(title = "单据号", order = 17)
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态含义")
    @ExcelColumn(title = "单据状态", order = 18)
    private String instructionDocStatusMeaning;
}
