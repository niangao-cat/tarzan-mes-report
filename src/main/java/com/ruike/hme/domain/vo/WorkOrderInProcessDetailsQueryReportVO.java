package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 工单在制明细查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Data
@ExcelSheet(zh = "工单在制明细查询报表")
public class WorkOrderInProcessDetailsQueryReportVO implements Serializable {

    private static final long serialVersionUID = 5117709893672116182L;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号" ,order = 0)
    private String workOrderNum;

    @ApiModelProperty("工单物料编码")
    @ExcelColumn(zh = "工单物料编码", order = 1)
    private String materialCode;

    @ApiModelProperty("工单物料名称")
    @ExcelColumn(zh = "工单物料编码", order = 2)
    private String materialName;

    @ApiModelProperty("生产版本")
    @ExcelColumn(zh = "生产版本", order = 4)
    private String productionVersion;

    @ApiModelProperty("产品组")
    @ExcelColumn(zh = "产品组", order = 5)
    private String itemGroupCode;

    @ApiModelProperty("产品组描述")
    @ExcelColumn(zh = "产品组描述", order = 6)
    private String itemGroupDescription;

    @ApiModelProperty("工单类型")
    @ExcelColumn(zh = "工单类型", order = 7)
    private String typedesc;

    @ApiModelProperty("生产线编码")
    @ExcelColumn(zh = "生产线编码", order = 8)
    private String prodLineCode;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(zh = "生产线描述", order = 9)
    private String prodlinedesc;

    @ApiModelProperty("工单状态")
    @ExcelColumn(zh = "工单状态", order = 10)
    private String statusdesc;

    @ApiModelProperty("工单数量")
    @ExcelColumn(zh = "工单数量", order = 11)
    private Long qty;

    @ApiModelProperty("下达数量")
    @ExcelColumn(zh = "下达数量", order = 12)
    private Long releasedQty;

    @ApiModelProperty("完工数量")
    @ExcelColumn(zh = "完工数量", order = 13)
    private Long completedQty;

    @ApiModelProperty("EO编码")
    @ExcelColumn(zh = "EO编码", order = 14)
    private String eoNum;

    @ApiModelProperty("SN编码")
    @ExcelColumn(zh = "SN编码", order = 15)
    private String identification;

    @ApiModelProperty(value = "返修SN")
    @ExcelColumn(zh = "返修SN", order = 16)
    private String reworkMaterialLotCode;

    @ApiModelProperty("实验代码")
    @ExcelColumn(zh = "实验代码", order = 17)
    private String labCode;

    @ApiModelProperty("SN物料编码")
    @ExcelColumn(zh = "SN物料编码", order = 18)
    private String snMaterialCode;

    @ApiModelProperty("SN物料描述")
    @ExcelColumn(zh = "SN物料描述", order = 19)
    private String snMaterialName;

    @ApiModelProperty("序号")
    @ExcelColumn(zh = "序号", order = 20)
    private String stepName;

    @ApiModelProperty("工艺步骤")
    @ExcelColumn(zh = "工艺步骤", order = 21)
    private String rsdesc;

    @ApiModelProperty("当前工序编码")
    @ExcelColumn(zh = "当前工序编码", order = 22)
    private String processCode;

    @ApiModelProperty("当前工序描述")
    @ExcelColumn(zh = "当前工序描述", order = 23)
    private String processName;

    @ApiModelProperty("当前工位编码")
    @ExcelColumn(zh = "当前工位编码", order = 24)
    private String workcellCode;

    @ApiModelProperty("当前工位描述")
    @ExcelColumn(zh = "当前工位描述", order = 25)
    private String mwdesc;

    @ApiModelProperty("加工开始时间")
    private Date workingDate;

    @ApiModelProperty("加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 26)
    private String workingDateStr;

    @ApiModelProperty("加工结束时间")
    private Date completedDate;

    @ApiModelProperty("加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 27)
    private String completedDateStr;

    @ApiModelProperty("加工时长（分）")
    @ExcelColumn(zh = "加工时长（分）", order = 28)
    private BigDecimal timeDifference;

    @ApiModelProperty("呆滞时间")
    @ExcelColumn(zh = "呆滞时间", order = 29)
    private String timeDifferenceStr;

    @ApiModelProperty("呆滞标准")
    @ExcelColumn(zh = "呆滞标准", order = 30)
    private String timeStardand;

    @ApiModelProperty("呆滞标记")
    @ExcelColumn(zh = "呆滞标记", order = 31)
    private String timeFlag;

    @ApiModelProperty("加工人员")
    @ExcelColumn(zh = "加工人员", order = 32)
    private String realName;

    @ApiModelProperty("加工人id")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "产品状态")
    @LovValue(lovCode = "HME.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "产品状态含义")
    @ExcelColumn(zh = "产品状态", order = 33)
    private String qualityStatusMeaning;

    @ApiModelProperty("是否不良")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "reworkStepFlagMeaning")
    private String reworkStepFlag;

    @ApiModelProperty("是否不良")
    @ExcelColumn(zh = "是否不良", order = 34)
    private String reworkStepFlagMeaning;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    @ExcelColumn(zh = "是否冻结", order = 35)
    private String freezeFlagMeaning;

    @ApiModelProperty(value = "是否转型")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "transformFlagMeaning")
    private String transformFlag;

    @ApiModelProperty(value = "是否转型含义")
    @ExcelColumn(zh = "是否转型", order = 36)
    private String transformFlagMeaning;

    @ApiModelProperty(value = "是否拆机")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "afFlagMeaning")
    private String afFlag;

    @ApiModelProperty(value = "是否拆机含义")
    @ExcelColumn(zh = "是否拆机", order = 37)
    private String afFlagMeaning;

    @ApiModelProperty(value = "最新不良代码项")
    @ExcelColumn(zh = "最新不良代码项", order = 38)
    private String latestNcTag;


}
