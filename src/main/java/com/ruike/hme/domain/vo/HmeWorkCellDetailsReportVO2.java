package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeWorkCellDetailsReportVO - 工位产量明细报表返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 15:16
 */
@Data
@ExcelSheet(zh = "工位产量明细查询报表")
public class HmeWorkCellDetailsReportVO2 implements Serializable {

    private static final long serialVersionUID = -4640821094339438627L;

    @ApiModelProperty(value = "SN作业ID")
    private String jobId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "生产线")
    @ExcelColumn(zh = "生产线")
    private String productionLineName;

    private String productionLineId;

    @ApiModelProperty(value = "工段")
    @ExcelColumn(zh = "工段")
    private String lineWorkcellName;

    private String lineWorkcellId;

    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序")
    private String processWorkcellName;

    private String processId;

    @ApiModelProperty(value = "工位")
    @ExcelColumn(zh = "工位")
    private String stationWorkcellName;

    @ApiModelProperty(value = "产品料号")
    @ExcelColumn(zh = "产品料号")
    private String snMaterialName;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialDesc;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品序列号")
    @ExcelColumn(zh = "产品序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "作业类型")
    @ExcelColumn(zh = "作业类型")
    private String jobTypeName;

    @ApiModelProperty(value = "作业编码")
    @JsonIgnore
    private String jobPlatformCode;

    @ApiModelProperty(value = "进出站标识")
    @JsonIgnore
    private String flag;

    @ApiModelProperty(value = "作业平台")
    @ExcelColumn(zh = "作业平台")
    private String jobPlatform;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "返修标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "isRework")
    private String reworkFlag;

    @ApiModelProperty(value = "是否返修含义")
    @ExcelColumn(zh = "返修标识")
    private String isRework;

    @ApiModelProperty(value = "进站创建人")
    @JsonIgnore
    private String siteInBy;

    @ApiModelProperty(value = "出站创建人")
    @JsonIgnore
    private String siteOutBy;

    @ApiModelProperty(value = "作业人")
    @ExcelColumn(zh = "作业人")
    private String workerName;

    @ApiModelProperty(value = "作业时间")
    @ExcelColumn(zh = "作业时间",pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date workerTime;


}
