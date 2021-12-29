package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:04
 */
@Data
@ExcelSheet(zh = "材料不良明细")
public class HmeMaterielBadDetailedVO implements Serializable {

    private static final long serialVersionUID = -4833756987158897138L;

    @ApiModelProperty("提交工段")
    @ExcelColumn(zh = "提交工段", order = 2)
    private String workcellCode;
    @ApiModelProperty("工段描述")
    @ExcelColumn(zh = "工段描述", order = 3)
    private String workcellName;
    @ApiModelProperty("提交工序")
    @ExcelColumn(zh = "提交工序", order = 4)
    private String procedureCode;
    @ApiModelProperty("工序描述")
    @ExcelColumn(zh = "工序描述", order = 5)
    private String procedureName;
    @ApiModelProperty("提交工位编码")
    @ExcelColumn(zh = "提交工位编码", order = 6)
    private String stationCode;
    @ApiModelProperty("提交工位描述")
    @ExcelColumn(zh = "提交工位描述", order = 7)
    private String stationName;
    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码", order = 8)
    private String materialCode;
    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述", order = 9)
    private String materialName;
    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号", order = 10)
    private String workOrderNum;
    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本", order = 11)
    private String productionVersion;
    @ApiModelProperty("不良单号")
    @ExcelColumn(zh = "不良单号", order = 12)
    private String incidentNumber;
    @ApiModelProperty("单据状态")
    @LovValue(value = "HME.NC_INCIDENT_STATUS", meaningField = "ncIncidentStatusMeaning")
    private String ncIncidentStatus;
    @ApiModelProperty("单据状态含义")
    @ExcelColumn(zh = "单据状态", order = 13)
    private String ncIncidentStatusMeaning;
    @ApiModelProperty("不良条码号")
    @ExcelColumn(zh = "不良条码号", order = 14)
    private String materialLotCode;
    @ApiModelProperty("组件编码")
    @ExcelColumn(zh = "组件编码", order = 15)
    private String assemblyCode;
    @ApiModelProperty("单位")
    @ExcelColumn(zh = "单位", order = 16)
    private String uomCode;
    @ApiModelProperty("组件物料描述")
    @ExcelColumn(zh = "组件物料描述", order = 17)
    private String assemblyName;
    @ApiModelProperty("该条码已投数量")
    @ExcelColumn(zh = "该条码已投数量", order = 18)
    private BigDecimal releaseQty;
    @ApiModelProperty("不良申请数量")
    @ExcelColumn(zh = "不良申请数量", order = 19)
    private String qty;
    @ApiModelProperty("组件条码批次")
    @ExcelColumn(zh = "组件条码批次", order = 20)
    private String lot;
    @ApiModelProperty("供应商批次")
    @ExcelColumn(zh = "供应商批次", order = 21)
    private String attrValue;
    @ApiModelProperty("是否冻结")
    @ExcelColumn(zh = "是否冻结", order = 22)
    private String freezeFlag;
    @ApiModelProperty("不良代码组编码")
    @ExcelColumn(zh = "不良代码组编码", order = 23)
    private String ncGroupCode;
    @ApiModelProperty("不良代码组描述")
    @ExcelColumn(zh = "不良代码组描述", order = 24)
    private String description;
    @ApiModelProperty("不良代码编码")
    @ExcelColumn(zh = "不良代码编码", order = 25)
    private String ncCode;
    @ApiModelProperty("不良代码描述")
    @ExcelColumn(zh = "不良代码描述", order = 26)
    private String ncDescription;
    @ApiModelProperty("处置方式")
    @LovValue(value = "HME.NC_PROCESS_METHOD", meaningField = "processMethodMeaning")
    private String processMethod;
    @ApiModelProperty("处理方式")
    @ExcelColumn(zh = "处理方式", order = 26)
    private String processMethodMeaning;
    @ApiModelProperty("责任工位编码")
    @ExcelColumn(zh = "责任工位编码", order = 27)
    private String dutyCode;
    @ApiModelProperty("责任工位描述")
    @ExcelColumn(zh = "责任工位描述", order = 28)
    private String dutyName;
    @ApiModelProperty("提交人")
    @ExcelColumn(zh = "提交人", order = 29)
    private String realName;
    @ApiModelProperty("提交时间")
    @ExcelColumn(zh = "提交时间", order = 30)
    private String dateTime;
    @ApiModelProperty("提交人备注")
    @ExcelColumn(zh = "提交人备注", order = 31)
    private String comments;
    @ApiModelProperty("处理人")
    @ExcelColumn(zh = "处理人", order = 32)
    private String closedName;
    @ApiModelProperty("处理时间")
    @ExcelColumn(zh = "处理时间", order = 33)
    private String closedDateTime;
    @ApiModelProperty("处理人备注")
    @ExcelColumn(zh = "处理人备注", order = 34)
    private String closedComments;
    @ApiModelProperty("车间")
    @ExcelColumn(zh = "车间", order = 35)
    private String workshopName;
    @ApiModelProperty("生产线")
    @ExcelColumn(zh = "生产线", order = 36)
    private String prodLineCode;
    @ApiModelProperty("产线描述")
    @ExcelColumn(zh = "产线描述", order = 37)
    private String prodLineName;
    @ApiModelProperty("条码Id")
    private String materialLotId;
    @ApiModelProperty("工单id")
    private String workOrderId;
}
