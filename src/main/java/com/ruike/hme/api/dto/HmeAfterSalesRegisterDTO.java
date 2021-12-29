package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeAfterSalesRegisterDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/17 11:01
 * @Version 1.0
 **/
@Data
public class HmeAfterSalesRegisterDTO implements Serializable {

    private static final long serialVersionUID = 444162826960647905L;

    @ApiModelProperty("SN")
    private List<String> snList;

    @ApiModelProperty("产品编码")
    private List<String> materialCodeList;

    @ApiModelProperty("当前SN")
    private List<String> currentSnList;

    @ApiModelProperty("当前工位编码")
    private String currentWorkcellCode;

    @ApiModelProperty("物流公司")
    private String logisticsCompany;

    @ApiModelProperty("物流单号")
    private String logisticsNumber;

    @ApiModelProperty("批次号")
    private String batchNumber;

    @ApiModelProperty("当前状态")
    private String receiveStatus;

    @ApiModelProperty("签收人")
    private String createdBy;

    @ApiModelProperty("拆箱人")
    private String receiveBy;

    @ApiModelProperty("返回时间起")
    private String creationDateFrom;

    @ApiModelProperty("返回时间至")
    private String creationDateTo;

    @ApiModelProperty("拆箱时间起")
    private String receiveDateFrom;

    @ApiModelProperty("拆箱时间至")
    private String receiveDateTo;

    @ApiModelProperty("生产完工时间起")
    private String actualEndTimeFrom;

    @ApiModelProperty("生产完工时间至")
    private String actualEndTimeTo;

}
