package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 19:51
 */
@Data
@ExcelSheet(zh = "产量日明细")
public class HmeProductionLineDetailsDTO implements Serializable {

    @ApiModelProperty(value = "产线编号")
    private String productionLineId;
    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线", order = 4)
    private String production;
    @ApiModelProperty(value = "车间编码")
    private String workshopId;
    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间", order = 3)
    private String workshopName;
    @ApiModelProperty(value = "工段Id")
    private String lineWorkcellId;
    @ApiModelProperty(value = "工段")
    @ExcelColumn(zh = "工段", order = 5)
    private String lineWorkcellName;
    @ApiModelProperty(value = "班次工段Id")
    private String shiftWorkcellId;
    @ApiModelProperty(value = "班次id")
    private String shiftId;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "日期")
    @ExcelColumn(zh = "日期", order = 0)
    private String shiftDate;
    @ApiModelProperty(value = "开班时间")
    private Date shiftStartTime;
    @ApiModelProperty(value = "开班时间")
    @ExcelColumn(zh = "开班时间", order = 1)
    private String shiftStartTimeStr;
    @ApiModelProperty(value = "结班时间")
    private Date shiftEndTime;
    @ApiModelProperty(value = "结班时间")
    @ExcelColumn(zh = "结班时间", order = 2)
    private String shiftEndTimeStr;
    @ApiModelProperty(value = "生产指令Id")
    private String workOrderId;
    @ApiModelProperty(value = "不良数")
    private BigDecimal ncNumber;
    @ApiModelProperty(value = "计划开始时间")
    private String createData;
    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码", order = 6)
    private String productionNum;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述", order = 7)
    private String productionDes;
    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号", order = 8)
    private String workOrderNum;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "投产（首道）")
    @ExcelColumn(zh = "投产（首道）", order = 9)
    private Integer putData;
    @ApiModelProperty(value = "完工（末道）")
    @ExcelColumn(zh = "完工（末道）", order = 10)
    private Integer finishedData;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection1;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection1Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection2;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection2Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection3;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection3Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection4;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection4Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection5;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection5Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection6;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection6Repair;
    @ApiModelProperty(value = "首道工序")
    private String firstProcessId;
    @ApiModelProperty(value = "末道工序")
    private String endProcessId;
}
