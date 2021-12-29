package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * description  售后报价单
 *
 * @author wengang.qiang@hand-china 2021/10/14 20:24
 */
@Data
@ExcelSheet(zh = "售后报价单报表")
public class HmeAfterSalesQuotationVO implements Serializable {

    private static final long serialVersionUID = 6917684322322929297L;

    @ApiModelProperty(value = "创建日期")
    @ExcelColumn(zh = "创建日期", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号")
    private String model;

    @ApiModelProperty(value = "序列号")
    @ExcelColumn(zh = "序列号")
    private String serialNumber;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述")
    private String productDescription;

    @ApiModelProperty(value = "SN状态")
    @LovValue(lovCode = "HME.RECEIVE_STATUS", meaningField = "snStatusMeaning")
    private String snStatus;

    @ApiModelProperty(value = "SN状态描述")
    @ExcelColumn(zh = "SN状态")
    private String snStatusMeaning;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNo;

    @ApiModelProperty(value = "售达方")
    @ExcelColumn(zh = "售达方")
    private String sellerTo;

    @ApiModelProperty(value = "送达方")
    @ExcelColumn(zh = "送达方")
    private String servedBy;

    @ApiModelProperty(value = "电学器件已录入")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "electricNoFlagMeaning")
    private String electricNoFlag;

    @ApiModelProperty(value = "电学器件已录入")
    @ExcelColumn(zh = "电学器件已录入")
    private String electricNoFlagMeaning;

    @ApiModelProperty(value = "光学器件已录入")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "opticsNoFlagMeaning")
    private String opticsNoFlag;

    @ApiModelProperty(value = "光学器件已录入")
    @ExcelColumn(zh = "光学器件已录入")
    private String opticsNoFlagMeaning;

    @ApiModelProperty(value = "更换物料号")
    @ExcelColumn(zh = "更换物料号")
    private String replacementMaterialNo;

    @ApiModelProperty(value = "更换物料描述")
    @ExcelColumn(zh = "更换物料描述")
    private String replacementMaterialDescription;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量")
    private Long quantity;

    @ApiModelProperty(value = "拒绝原因")
    @LovValue(lovCode = "HME.QUOTATION_REJECTION_REASON", meaningField = "cancelReasonMeaning")
    private String cancelReason;

    @ApiModelProperty(value = "拒绝原因")
    @ExcelColumn(zh = "拒绝原因")
    private String cancelReasonMeaning;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注")
    private String remarks;

    @ApiModelProperty(value = "情况说明")
    @ExcelColumn(zh = "情况说明")
    private String situationDesc;

    @ApiModelProperty(value = "报价单状态")
    @LovValue(lovCode = "HME.QUOTATION_STATUS", meaningField = "quotationStatusMeaning")
    private String quotationStatus;

    @ExcelColumn(zh = "报价单状态")
    @ApiModelProperty(value = "报价单状态描述")
    private String quotationStatusMeaning;

    @ApiModelProperty(value = "SAP报价单号")
    @ExcelColumn(zh = "SAP报价单号")
    private String sapQuotationNo;

    @ApiModelProperty(value = "返回类型")
    @LovValue(lovCode = "HME.BACK_TYPE", meaningField = "returnTypeMeaning")
    private String returnType;

    @ApiModelProperty(value = "返回类型")
    @ExcelColumn(zh = "返回类型描述")
    private String returnTypeMeaning;

    @ApiModelProperty(value = "发货日期")
    @ExcelColumn(zh = "发货日期" , pattern = "yyyy-MM-dd HH:mm:ss")
    private Date theDateOfIssuance;

    @ApiModelProperty(value = "创建人id")
    private Long creator;

    @ApiModelProperty(value = "更新时间")
    @ExcelColumn(zh = "更新时间" , pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "更新人id")
    private Long updater;

    @ApiModelProperty(value = "提交时间")
    @ExcelColumn(zh = "提交时间" , pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submissionTime;

    @ApiModelProperty(value = "提交人id")
    private Long submitter;

    @ApiModelProperty(value = "创建人姓名")
    @ExcelColumn(zh = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "更新人姓名")
    @ExcelColumn(zh = "更新人姓名")
    private String updaterName;

    @ApiModelProperty(value = "提交人姓名")
    @ExcelColumn(zh = "提交人姓名")
    private String submitterName;

    @ApiModelProperty(value = "报价单头主键")
    private String quotationHeaderId;


}
