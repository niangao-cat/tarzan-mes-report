package com.ruike.hme.domain.valueobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 芯片不良记录
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:17
 */
@Data
public class HmeCosNcRecord {
    @ApiModelProperty("表ID，主键")
    private String cosNcRecordId;
    @ApiModelProperty(value = "站点id")
    private String siteId;
    @ApiModelProperty(value = "操作人")
    private Long userId;
    @ApiModelProperty(value = "EO_JOB_SN表主键")
    private String jobId;
    @ApiModelProperty(value = "物料批")
    private String materialLotId;
    @ApiModelProperty(value = "缺陷数量")
    private BigDecimal defectCount;
    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;
    @ApiModelProperty(value = "不良代码分类，缺陷/瑕疵/修复")
    private String ncType;
    @ApiModelProperty(value = "NC记录的组件")
    private String componentMaterialId;
    @ApiModelProperty(value = "工艺")
    private String operationId;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkCode;
    @ApiModelProperty(value = "工单号")
    private String workOrderId;
    @ApiModelProperty(value = "wafer")
    private String waferNum;
    @ApiModelProperty(value = "芯片类型")
    private String cosType;
    @ApiModelProperty(value = "备注")
    private String comments;
    @ApiModelProperty(value = "来源行")
    private Long ncLoadRow;
    @ApiModelProperty(value = "来源列")
    private Long ncLoadColumn;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "芯片位置")
    private String loadNum;
    private Date creationDate;
    private Long createdBy;
}
