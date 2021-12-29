package com.ruike.hme.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * COS在制报表
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 11:11
 */
@Data
@ExcelSheet(title = "cos在制报表")
public class HmeCosInProductionVO implements Serializable {

    private static final long serialVersionUID = 3096427287631700534L;

    @ApiModelProperty("作业id")
    @ExcelIgnore
    private String jobId;

    @ApiModelProperty("工单Id")
    @ExcelIgnore
    private String workOrderId;

    @ApiModelProperty("工单")
    @ExcelColumn(title = "工单")
    @ExcelProperty(value = "工单", index = 0)
    @ColumnWidth(18)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelProperty(value = "工单版本", index = 1)
    @ColumnWidth(20)
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelProperty(value = "版本描述", index = 2)
    @ColumnWidth(20)
    private String productionVersionDesc;

    @ApiModelProperty("产品编码")
    @ExcelColumn(title = "产品编码")
    @ExcelProperty(value = "产品编码", index = 3)
    @ColumnWidth(20)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(title = "产品描述")
    @ExcelProperty(value = "产品描述", index = 4)
    @ColumnWidth(20)
    private String materialName;

    @ApiModelProperty("产品组")
    @ExcelProperty(value = "产品组", index = 5)
    @ColumnWidth(20)
    private String productGroup;

    @ApiModelProperty("产品组描述")
    @ExcelProperty(value = "产品组描述", index = 6)
    @ColumnWidth(20)
    private String productGroupMeaning;

    @ApiModelProperty("工单类型")
    @LovValue(value = "MT.WO_TYPE", meaningField = "workOrderTypeMeaning")
    @ExcelIgnore
    private String workOrderType;

    @ApiModelProperty("工单类型")
    @ExcelColumn(title = "工单类型")
    @ExcelProperty(value = "工单类型", index = 7)
    @ColumnWidth(16)
    private String workOrderTypeMeaning;

    @ApiModelProperty("工单状态")
    @LovValue(value = "MT.WO_STATUS", meaningField = "statusMeaning")
    @ExcelIgnore
    private String status;

    @ApiModelProperty("工单状态")
    @ExcelColumn(title = "工单状态")
    @ExcelProperty(value = "工单状态", index = 8)
    @ColumnWidth(16)
    private String statusMeaning;

    @ApiModelProperty("工单数量")
    @ExcelColumn(title = "工单数量")
    @ExcelProperty(value = "工单数量", index = 9)
    @ColumnWidth(16)
    private String workOrderQty;

    @ApiModelProperty("工单投料数量（来料）")
    @ExcelColumn(title = "工单投料数量（来料）")
    @ExcelProperty(value = "工单投料数量（来料）", index = 10)
    @ColumnWidth(16)
    private Long cosNum;

    @ApiModelProperty("完工数量")
    @ExcelColumn(title = "完工数量")
    @ExcelProperty(value = "完工数量", index = 11)
    @ColumnWidth(16)
    private BigDecimal completedQty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(title = "WAFER")
    @ExcelProperty(value = "WAFER", index = 12)
    @ColumnWidth(16)
    private String wafer;

    @ApiModelProperty("COS类型")
    @ExcelColumn(title = "COS类型")
    @ExcelProperty(value = "COS类型", index = 13)
    @ColumnWidth(16)
    private String cosType;

    @ApiModelProperty("条码ID")
    @ExcelIgnore
    private String materialLotId;

    @ApiModelProperty("条码")
    @ExcelColumn(title = "条码")
    @ExcelProperty(value = "条码", index = 14)
    @ColumnWidth(20)
    private String materialLotCode;

    @ApiModelProperty("数量")
    @ExcelColumn(title = "数量")
    @ExcelProperty(value = "数量", index = 15)
    @ColumnWidth(16)
    private String qty;

    @ApiModelProperty("实验代码")
    @ExcelProperty(value = "实验代码", index = 16)
    @ColumnWidth(24)
    private String labCode;


    @ApiModelProperty("工位编码")
    @ExcelColumn(title = "工位编码")
    @ExcelProperty(value = "工位编码", index = 17)
    @ColumnWidth(16)
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(title = "工位描述")
    @ExcelProperty(value = "工位描述", index = 18)
    @ColumnWidth(16)
    private String workcellName;

    @ApiModelProperty("工序描述")
    @ExcelColumn(title = "工序描述")
    @ExcelProperty(value = "工序描述", index = 19)
    @ColumnWidth(16)
    private String processName;

    @ApiModelProperty("工段描述")
    @ExcelColumn(title = "工段描述")
    @ExcelProperty(value = "工段描述", index = 20)
    @ColumnWidth(16)
    private String lineWorkcellName;

    @ApiModelProperty("生产线编码")
    @ExcelIgnore
    private String prodLineCode;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(title = "生产线描述")
    @ExcelProperty(value = "生产线描述", index = 21)
    @ColumnWidth(16)
    private String prodLineName;

    @ApiModelProperty("加工人员")
    @ExcelIgnore
    private Long createdBy;

    @ApiModelProperty("加工人员")
    @ExcelColumn(title = "加工人员")
    @ExcelProperty(value = "加工人员", index = 22)
    @ColumnWidth(16)
    private String createdByName;

    @ApiModelProperty("加工开始时间")
    @ExcelColumn(title = "加工开始时间")
    @ExcelProperty(value = "加工开始时间", index = 23)
    @ColumnWidth(24)
    private String siteInDate;

    @ApiModelProperty("加工结束时间")
    @ExcelColumn(title = "加工结束时间")
    @ExcelProperty(value = "加工结束时间", index = 24)
    @ColumnWidth(24)
    private String siteOutDate;

    @ApiModelProperty("当前工序")
    @ExcelProperty(value = "当前工序", index = 25)
    @ColumnWidth(24)
    private String currentProcessName;

    @ApiModelProperty("呆滞时间")
    @ExcelProperty(value = "呆滞时间", index = 26)
    @ColumnWidth(24)
    private String sluggishTime;

    @ApiModelProperty("呆滞标准")
    @ExcelProperty(value = "呆滞标准", index = 27)
    @ColumnWidth(13)
    private String sluggishStandard;

    @ApiModelProperty("呆滞标记")
    @ExcelIgnore
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "sluggishFlagMeaning")
    private String sluggishFlag;

    @ApiModelProperty("呆滞标记")
    @ExcelProperty(value = "呆滞标记", index = 28)
    @ColumnWidth(13)
    private String sluggishFlagMeaning;

    @ApiModelProperty("是否冻结")
    @ExcelIgnore
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty("是否冻结")
    @ExcelProperty(value = "是否冻结", index = 29)
    @ColumnWidth(13)
    private String freezeFlagMeaning;

    @ApiModelProperty("是否不良")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "ncFlagMeaning")
    @ExcelIgnore
    private String ncFlag;

    @ApiModelProperty("是否不良")
    @ExcelProperty(value = "是否不良", index = 30)
    @ColumnWidth(13)
    private String ncFlagMeaning;

    @ApiModelProperty("工位")
    @ExcelIgnore
    private String workcellId;

    @ApiModelProperty("EOId")
    @ExcelIgnore
    private String eoId;
}
