package com.ruike.hme.api.dto;

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
 * 售后退库检测报表 展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 10:40
 */
@Data
@ExcelSheet(zh = "售后退库检测报表")
public class ServiceReturnCheckRepresentationDTO implements Serializable {
    private static final long serialVersionUID = 3137842945325692146L;

    @ApiModelProperty("售后接收ID")
    private String serviceReceiveId;

    @ApiModelProperty("SN")
    @ExcelColumn(zh = "SN")
    private String snNum;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    @ExcelColumn(zh = "物料名称")
    private String materialName;

    @ApiModelProperty("机型")
    @ExcelColumn(zh = "机型")
    private String machineType;

    @ApiModelProperty("收货时间")
    @ExcelColumn(zh = "收货时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date receiptDate;

    @ApiModelProperty("拆箱时间")
    @ExcelColumn(zh = "拆箱时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date splitDate;

    @ApiModelProperty("物流公司，对应值集【HME.LOGISTICS】")
    @LovValue(lovCode = "HME.LOGISTICS", meaningField = "logisticsMeaning")
    private String logistics;

    @ApiModelProperty("物流公司含义")
    @ExcelColumn(zh = "物流公司")
    private String logisticsMeaning;

    @ApiModelProperty(value = "物流单号")
    @ExcelColumn(zh = "物流单号")
    private String logisticsNumber;

    @ApiModelProperty(value = "批次号")
    @ExcelColumn(zh = "批次号")
    private String batchNumber;

    @ApiModelProperty("当前状态，对应值集【HME.RECEIVE_STATUS】")
    @LovValue(lovCode = "HME.RECEIVE_STATUS", meaningField = "receiveStatusMeaning")
    private String receiveStatus;

    @ApiModelProperty("当前状态含义")
    @ExcelColumn(zh = "当前状态")
    private String receiveStatusMeaning;

    @ApiModelProperty("校验项目ID")
    private String tagGroupId;

    @ApiModelProperty("校验项目编码")
    @ExcelColumn(zh = "校验项目编码")
    private String tagGroupCode;

    @ApiModelProperty("检验项目描述")
    @ExcelColumn(zh = "检验项目描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "检验结果")
    @ExcelColumn(zh = "检验结果")
    private String checkResult;

    @ApiModelProperty("备注")
    @ExcelColumn(zh = "备注")
    private String remark;

    @ApiModelProperty("附件UUID")
    private String attachmentUuid;

    @ApiModelProperty("最后更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty("最后更新人名称")
    @ExcelColumn(zh = "最后更新人")
    private String lastUpdatedByName;

    @ApiModelProperty("最后更新时间")
    @ExcelColumn(zh = "最后更新时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date lastUpdateDate;

    @ApiModelProperty("工位Id")
    private String stationId;

    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码")
    private String stationCode;

    @ApiModelProperty("工位名称")
    @ExcelColumn(zh = "工位名称")
    private String stationName;
}
