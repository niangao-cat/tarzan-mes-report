package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description  售后报价单 查询条件
 *
 * @author wengang.qiang@hand-china 2021/10/14 21:01
 */
@Data
public class HmeAfterSalesQuotationDTO implements Serializable {

    private static final long serialVersionUID = 7674407108130144139L;

    @ApiModelProperty(value = "序列号")
    private List<String> serialNumberList;

    @ApiModelProperty(value = "序列号,以逗号分隔的字符串")
    private String serialNumber;

    @ApiModelProperty(value = "产品编码")
    private List<String> productCodeList;

    @ApiModelProperty(value = "产品编码,以逗号分隔的字符串")
    private String productCode;

    @ApiModelProperty(value = "SN状态")
    private String snStatus;

    @ApiModelProperty(value = "报价单状态")
    private String quotationStatus;

    @ApiModelProperty(value = "送达方")
    private String servedBy;

    @ApiModelProperty(value = "SAP报价单号")
    private List<String> sapQuotationNoList;

    @ApiModelProperty(value = "SAP报价单号,以逗号分隔的字符串")
    private String sapQuotationNo;

    @ApiModelProperty(value = "售达方")
    private String sellerTo;

    @ApiModelProperty(value = "创建人id")
    private Long creator;

    @ApiModelProperty(value = "更新人id")
    private Long updater;

    @ApiModelProperty(value = "提交人id")
    private Long submitter;

    @ApiModelProperty(value = "返回类型")
    private String returnType;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "更新时间从")
    private String updateTimeFrom;

    @ApiModelProperty(value = "更新时间至")
    private String updateTimeTo;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;

    @ApiModelProperty(value = "提交时间从")
    private String submissionTimeFrom;

    @ApiModelProperty(value = "提交时间至")
    private String submissionTimeTo;

    @ApiModelProperty(value = "拒绝原因")
    private String cancelReason;

    public void initParamSerialNumber() {
        this.serialNumberList = StringUtils.isNotBlank(this.serialNumber) ? Arrays.asList(StringUtils.split(this.serialNumber, ",")) : new ArrayList<>();
    }

    public void initParamProductCode() {
        this.productCodeList = StringUtils.isNotBlank(this.productCode) ? Arrays.asList(StringUtils.split(this.productCode, ",")) : new ArrayList<>();
    }

    public void initParamSAPQuotationNo() {
        this.sapQuotationNoList = StringUtils.isNotBlank(this.sapQuotationNo) ? Arrays.asList(StringUtils.split(this.sapQuotationNo, ",")) : new ArrayList<>();
    }

}
