package com.ruike.qms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;

/**
 * <p>
 * IQC检验看板
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:26
 */
@Data
@ExcelSheet(zh = "IQC日常工作计划报表")
public class QmsIqcInspectionKanbanVO {
    @ApiModelProperty(value = "检验员名称")
    @ExcelColumn(zh = "检验员", order = 0)
    @ExcelIgnore
    private String inspectorName;
    @ApiModelProperty(value = "供应商")
    @ExcelColumn(zh = "供应商", order = 1)
    @ExcelProperty(value = "供应商", index = 0)
    @ColumnWidth(18)
    private String supplierName;
    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(zh = "物料", order = 2)
    @ExcelProperty(value = "物料", index = 1)
    @ColumnWidth(18)
    private String materialName;
    @ApiModelProperty(value = "检验批次")
    @ExcelColumn(zh = "检验批次", order = 3)
    @ExcelProperty(value = "检验批次", index = 2)
    @ColumnWidth(18)
    private BigDecimal totalNum;
    @ApiModelProperty(value = "合格批次")
    @ExcelColumn(zh = "合格批次", order = 4)
    @ExcelProperty(value = "合格批次", index = 3)
    @ColumnWidth(18)
    private BigDecimal okNum;
    @ApiModelProperty(value = "不合格批次")
    @ExcelColumn(zh = "不合格批次", order = 5)
    @ExcelProperty(value = "不合格批次", index = 4)
    @ColumnWidth(18)
    private BigDecimal ngNum;
    @ApiModelProperty(value = "合格率")
    @ExcelColumn(zh = "合格率", order = 6)
    @ExcelProperty(value = "合格率", index = 5)
    @ColumnWidth(18)
    private BigDecimal okRate;
    @ApiModelProperty(value = "不良率")
    @ExcelColumn(zh = "不良率", order = 7)
    @ExcelProperty(value = "不良率", index = 6)
    @ColumnWidth(18)
    private BigDecimal ngRate;
}
