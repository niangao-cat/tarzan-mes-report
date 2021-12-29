package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * @ClassName HmeAfterSalesRegisterVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/17 11:28
 * @Version 1.0
 **/
@Data
@ExcelSheet(zh = "售后登记报表")
public class HmeAfterSalesRegisterVO implements Serializable {
    private static final long serialVersionUID = 3454824775792832616L;

    @ApiModelProperty("serviceReceiveId")
    private String serviceReceiveId;

    @ApiModelProperty("接收SN")
    @ExcelColumn(zh = "接收SN")
    private String snNum;

    @ApiModelProperty("当前SN")
    @ExcelColumn(zh = "当前SN")
    private String currentSnNum;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty("物流公司")
    @LovValue(lovCode = "HME.LOGISTICS", meaningField = "logisticsCompanyMeaning")
    private String logisticsCompany;

    @ApiModelProperty("物流公司描述")
    @ExcelColumn(zh = "物流公司")
    private String logisticsCompanyMeaning;

    @ApiModelProperty("物流单号")
    @ExcelColumn(zh = "物流单号")
    private String logisticsNumber;

    @ApiModelProperty("批次号")
    @ExcelColumn(zh = "批次号")
    private String batchNumber;

    @ApiModelProperty("当前状态")
    @LovValue(lovCode = "HME.RECEIVE_STATUS", meaningField = "receiveStatusMeaning")
    private String receiveStatus;

    @ApiModelProperty("当前状态含义")
    @ExcelColumn(zh = "当前状态")
    private String receiveStatusMeaning;

    @ApiModelProperty("返回时间")
    @ExcelColumn(zh = "返回时间")
    private String creationDate;

    @ApiModelProperty("返回年月")
    @ExcelColumn(zh = "返回年月")
    private String creationYearMonth;

    @ApiModelProperty("签收人")
    private String createdBy;

    @ApiModelProperty("签收人姓名")
    @ExcelColumn(zh = "签收人")
    private String signedBy;

    @ApiModelProperty("拆箱时间")
    @ExcelColumn(zh = "拆箱时间")
    private String receiveDate;

    @ApiModelProperty("拆箱人")
    private String receiveBy;

    @ApiModelProperty("拆箱人姓名")
    @ExcelColumn(zh = "拆箱人")
    private String unpackBy;

    @ApiModelProperty("型号")
    @ExcelColumn(zh = "型号")
    private String model;

    @ApiModelProperty("客户名称")
    @ExcelColumn(zh = "客户名称")
    private String customerName;

    @ApiModelProperty("返回类型")
    @LovValue(lovCode = "HME.BACK_TYPE", meaningField = "backTypeMeaning")
    private String backType;

    @ApiModelProperty("返回类型含义")
    @ExcelColumn(zh = "返回类型")
    private String backTypeMeaning;

    @ApiModelProperty("当前工位编码")
    @ExcelColumn(zh = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty("当前工位名称")
    @ExcelColumn(zh = "当前工位名称")
    private String workcellName;

    @ApiModelProperty("入库时间")
    @ExcelColumn(zh = "入库时间")
    private String receiptDate;

    @ApiModelProperty("入库年月")
    @ExcelColumn(zh = "入库年月")
    private String receiptYearMonth;

    @ApiModelProperty("内部订单号")
    @ExcelColumn(zh = "内部订单号")
    private String internalOrderNum;

    @ApiModelProperty("备注")
    @ExcelColumn(zh = "备注")
    private String remark;

    @ApiModelProperty("生产完工时间")
    @ExcelColumn(zh = "生产完工时间")
    private String actualEndTime;

    @ApiModelProperty("有效时长（天）")
    @ExcelColumn(zh = "有效时长（天）")
    private String actualData;
}
