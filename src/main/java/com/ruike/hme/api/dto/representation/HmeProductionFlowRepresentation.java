package com.ruike.hme.api.dto.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 生产流转查询报表 展现对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 15:50
 */
@Data
@ExcelSheet(zh = "生产流转查询报表")
public class HmeProductionFlowRepresentation implements Serializable {
    private static final long serialVersionUID = -953523236367176589L;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号")
    private Long lineNum;
    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线")
    private String prodLineCode;
    @ApiModelProperty(value = "工段")
    @ExcelColumn(zh = "工段")
    private String workcellLineName;
    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序")
    private String workcellProcessName;
    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "工单版本")
    @ExcelColumn(zh = "工单版本")
    private String productionVersion;
    @ApiModelProperty(value = "工单状态编码")
    @LovValue(lovCode = "MT.WO_STATUS",meaningField = "workOrderStatusMeaning")
    private String workOrderStatus;
    @ApiModelProperty(value = "工单状态含义")
    @ExcelColumn(zh = "工单状态")
    private String workOrderStatusMeaning;
    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "SN")
    @ExcelColumn(zh = "SN")
    private String materialLotCode;
    @ApiModelProperty(value = "返修SN")
    @ExcelColumn(zh = "返修SN")
    private String reworkMaterialLot;
    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;
    @ApiModelProperty(value = "步骤编码")
    private String stepName;
    @ApiModelProperty(value = "步骤名称")
    @ExcelColumn(zh = "工艺步骤")
    private String stepDescription;
    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码")
    private String labCode;
    @ApiModelProperty(value = "作业平台类型")
    @LovValue(value = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    private String jobType;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型")
    private String jobTypeMeaning;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位编码")
    @ExcelColumn(zh = "工位")
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "班次日期")
    @ExcelColumn(zh = "班次日期")
    private String shiftDate;
    @ApiModelProperty(value = "班次")
    @ExcelColumn(zh = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "进站人员")
    @ExcelColumn(zh = "进站人员")
    private String createUserName;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "出站人员")
    @ExcelColumn(zh = "出站人员")
    private String operatorUserName;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)")
    private BigDecimal processTime;
    @ApiModelProperty(value = "不良信息标识")
    @ExcelColumn(zh = "不良")
    private String ncInfoFlagMeaning;
    @ApiModelProperty(value = "是否返修")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "isRework")
    private String isReworkFlag;
    @ApiModelProperty(value = "是否返修含义")
    @ExcelColumn(zh = "是否返修")
    private String isRework;
    @ApiModelProperty(value = "设备编码")
    @ExcelColumn(zh = "设备编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备名称")
    @ExcelColumn(zh = "设备名称")
    private String assetName;

    @ApiModelProperty(value = "JOB ID")
    private String jobId;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "进站人ID")
    private Long createdBy;
    @ApiModelProperty(value = "出站人ID")
    private Long operatorId;

    @ApiModelProperty(value = "不良信息标识")
    private Boolean ncInfoFlag;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注")
    private String remark;
}
