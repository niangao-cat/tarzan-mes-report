package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 11:32
 */
@Data
public class HmeCosCheckBarcodesDTO {

    @ApiModelProperty("工单")
    private List<String> workOrderNumList;

    @ApiModelProperty("产品编码,以逗号分隔的ID字符串")
    private String materialId;

    @ApiModelProperty("WAFER")
    private List<String> waferList;

    @ApiModelProperty("COS类型,以逗号分隔的字符串")
    private String cosType;

    @ApiModelProperty("条码")
    private List<String> materialLotCodeList;

    @ApiModelProperty("热沉编号,以逗号分隔的字符串")
    private String hotSinkCode;

    @ApiModelProperty("不良代码,以逗号分隔的ID字符串")
    private String ncCodeId;

    @ApiModelProperty("工序状态")
    private String orderType;

    @ApiModelProperty("操作人")
    private String operatorId;

    @ApiModelProperty("进站时间从")
    private Date siteInDateFrom;

    @ApiModelProperty("进站时间至")
    private Date siteInDateTo;

    @ApiModelProperty("出站时间从")
    private Date siteOutDateFrom;

    @ApiModelProperty("出站时间至")
    private Date siteOutDateTo;

    @ApiModelProperty("测试机台")
    private List<String> benchList;

    @ApiModelProperty("贴片设备")
    private List<String> patchList;

    @ApiModelProperty("热沉投料条码")
    private List<String> barcodeList;

    @ApiModelProperty("热沉供应商批次,以逗号分隔的字符串")
    private String hotSinkSupplierLot;

    @ApiModelProperty(value = "实验代码")
    private List<String> experimentCodeList;

    @ApiModelProperty("产线,以逗号分隔的ID字符串")
    private String prodLineId;

    @ApiModelProperty("工段,以逗号分隔的ID字符串")
    private String lineWorkcellId;

    @ApiModelProperty("工序,以逗号分隔的ID字符串")
    private String processId;

    @ApiModelProperty("工位,以逗号分隔的ID字符串")
    private String workcellId;
}
