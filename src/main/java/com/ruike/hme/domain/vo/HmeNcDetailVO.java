package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工序不良记录
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:22
 */
@Data
@ExcelSheet(title = "工序不良报表")
public class HmeNcDetailVO implements Serializable {
    private static final long serialVersionUID = 7383406711599029902L;

    @ApiModelProperty("不良id")
    private String ncRecordId;

    @ApiModelProperty("rootId")
    private String workcellId;

    @ApiModelProperty("工位Id")
    private String stationId;

    @ApiModelProperty("车间")
    @ExcelColumn(zh = "车间", order = 26)
    private String workShop;

    @ApiModelProperty("生产线")
    @ExcelColumn(zh = "生产线", order = 27)
    private String prodLineName;

    @ApiModelProperty("提交工段")
    @ExcelColumn(zh = "提交工段", order = 28)
    private String rootCauseLineName;

    private String lineWorkcellId;

    @ApiModelProperty("提交工序")
    @ExcelColumn(zh = "提交工序", order = 3)
    private String rootCauseProcessName;

    private String processId;

    @ApiModelProperty("责任工位")
    @ExcelColumn(zh = "责任工位", order = 4)
    private String station;

    @ApiModelProperty(value = "提交工位")
    @ExcelColumn(zh = "提交工位", order = 5)
    private String rootCauseWorkcellName;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码", order = 6)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述", order = 7)
    private String materialName;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号", order = 8)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本", order = 9)
    private String productionVersion;

    @ApiModelProperty("不良单号")
    @ExcelColumn(zh = "不良单号", order = 10)
    private String incidentNumber;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "HME.NC_INCIDENT_STATUS", meaningField = "ncIncidentStatusMeaning")
    private String ncIncidentStatus;

    @ApiModelProperty("单据状态含义")
    @ExcelColumn(zh = "单据状态", order = 11)
    private String ncIncidentStatusMeaning;

    @ApiModelProperty("序列号id")
    private String materialId;

    @ApiModelProperty("序列号")
    @ExcelColumn(zh = "序列号", order = 0)
    private String materialLotNum;

    @ApiModelProperty("实验代码")
    @ExcelColumn(zh = "实验代码", order = 1)
    private String labCode;

    @ApiModelProperty("质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty("质量状态含义")
    @ExcelColumn(zh = "质量状态", order = 2)
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    @ExcelColumn(zh = "是否冻结", order = 12)
    private String freezeFlagMeaning;

    @ApiModelProperty(value = "是否转型")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "transformFlagMeaning")
    private String transformFlag;

    @ApiModelProperty(value = "是否转型含义")
    @ExcelColumn(zh = "是否转型", order = 13)
    private String transformFlagMeaning;

    @ApiModelProperty("不良代码组")
    @ExcelColumn(zh = "不良代码组", order = 14)
    private String descriptionType;

    @ApiModelProperty("不良代码")
    @ExcelColumn(zh = "不良代码", order = 15)
    private String ncCode;

    @ApiModelProperty("不良代码描述")
    @ExcelColumn(zh = "不良代码描述", order = 16)
    private String ncDescription;

    @ApiModelProperty("处理方式编码")
    @LovValue(lovCode = "HME.NC_PROCESS_METHOD", meaningField = "processMethodName")
    private String processMethod;

    @ApiModelProperty("处理方式")
    @ExcelColumn(zh = "处理方式", order = 17)
    private String processMethodName;

    @ApiModelProperty(value = "转型物料Id")
    private String transMaterialId;

    @ApiModelProperty(value = "转型物料编码")
    @ExcelColumn(zh = "转型物料编码", order = 18)
    private String transMaterialCode;

    @ApiModelProperty(value = "转型物料描述")
    @ExcelColumn(zh = "转型物料描述", order = 19)
    private String transMaterialName;

    @ApiModelProperty("提交人")
    @ExcelColumn(zh = "提交人", order = 20)
    private String submitUserName;

    @ApiModelProperty("提交人备注")
    @ExcelColumn(zh = "提交人备注", order = 21)
    private String comments;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = BaseConstants.Pattern.DATETIME, timezone = "GMT+8")
    @ExcelColumn(zh = "提交时间", pattern = BaseConstants.Pattern.DATETIME, order = 22)
    private Date dateTime;

    @ApiModelProperty("处理人id")
    private String closedUserId;

    @ApiModelProperty("处理人")
    @ExcelColumn(zh = "处理人", order = 23)
    private String processUserName;

    @ApiModelProperty("处理人备注")
    @ExcelColumn(zh = "处理人备注", order = 24)
    private String subComments;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(pattern = BaseConstants.Pattern.DATETIME, timezone = "GMT+8")
    @ExcelColumn(zh = "处理时间", pattern = BaseConstants.Pattern.DATETIME, order = 25)
    private Date closedDateTime;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("产品id")
    private String materialLotId;

    @ApiModelProperty(value = "提交工位")
    private String rootCauseWorkcell;
}