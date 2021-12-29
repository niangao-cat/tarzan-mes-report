package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeMonthlyPlanVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 12:01
 * @Version 1.0
 **/
@Data
@ExcelSheet(zh = "月度计划报表")
public class HmeMonthlyPlanVO implements Serializable {
    private static final long serialVersionUID = -471758174506242572L;

    @ApiModelProperty("产线编码")
    @ExcelColumn(zh = "产线编码", order = 2)
    private String prodLineCode;

    @ApiModelProperty("产线描述")
    @ExcelColumn(zh = "产线描述", order = 3)
    private String prodLineName;

    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码", order = 4)
    private String materialCode;

    @ApiModelProperty("物料名称")
    @ExcelColumn(zh = "物料名称", order = 5)
    private String materialName;

    @ApiModelProperty("计划数量")
    @ExcelColumn(zh = "计划数量", order = 6)
    private BigDecimal planQty;

    @ApiModelProperty("完工数量")
    @ExcelColumn(zh = "完工数量", order = 7)
    private BigDecimal qty;

    @ApiModelProperty("入库数量")
    @ExcelColumn(zh = "入库数量", order = 8)
    private BigDecimal actualQty;

    @ApiModelProperty("达成率")
    @ExcelColumn(zh = "达成率", order = 9)
    private String planReachRate;

    @ApiModelProperty("制造部")
    @ExcelColumn(zh = "部门", order = 1)
    private String areaName;

    @ApiModelProperty("制造部ID")
    @JsonIgnore
    private String areaId;

    @ApiModelProperty("物料ID")
    @JsonIgnore
    private String materialId;
}
