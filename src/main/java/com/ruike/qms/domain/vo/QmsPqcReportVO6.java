package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * QmsPqcReportVO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/11 15:40:11
 **/
@Data
@ExcelSheet(zh = "巡检报表")
public class QmsPqcReportVO6 implements Serializable {
    private static final long serialVersionUID = -6314273636947700326L;

    @ApiModelProperty(value = "车间ID")
    private String areaId;

    @ApiModelProperty(value = "车间名称")
    @ExcelColumn(zh = "车间",order = 1)
    private String areaName;

    @ApiModelProperty(value = "车间不合格数")
    @ExcelColumn(zh = "车间不合格数",order = 2)
    private long areaNcNum;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工序",order = 3)
    private String processName;

    @ApiModelProperty(value = "工序不合格数")
    @ExcelColumn(zh = "工序不合格数",order = 4)
    private long processNcNum;

    @ApiModelProperty(value = "检验员")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "检验员")
    @ExcelColumn(zh = "检验员",order = 5)
    private String lastUpdatedByName;

    @ApiModelProperty(value = "问题点")
    @ExcelColumn(zh = "问题点",order = 6)
    private String attribute1;

    @ApiModelProperty(value = "检验时间")
    @ExcelColumn(zh = "检验时间",order = 7)
    private String inspectionFinishDateStr;
}
