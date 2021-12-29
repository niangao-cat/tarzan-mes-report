package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 售后在制品盘点-半成品报表参数
 *
 * @author penglin.sui@hand-china.com 2021-03-31 16:42:00
 */

@Data
public class HmeServiceSplitRk05ReportDTO implements Serializable {
    private static final long serialVersionUID = -5296375222256341575L;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("接收SN")
    private String snNum;

    @ApiModelProperty("接收SN列表")
    private List<String> snNumList;

    @ApiModelProperty("当前SN")
    private String materialLotCode;

    @ApiModelProperty("当前SN列表")
    private List<String> materialLotCodeList;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品编码列表")
    private List<String> materialCodeList;

    @ApiModelProperty("当前状态")
    private String splitStatus;

    @ApiModelProperty("当前状态列表")
    private List<String> splitStatusList;

    @ApiModelProperty("所在仓库编码")
    private String warehouseCode;

    @ApiModelProperty("所在仓库编码列表")
    private List<String> warehouseCodeList;

    @ApiModelProperty("物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty("当前工位编码")
    private String workcellCode;

    @ApiModelProperty("当前工位编码列表")
    private List<String> workcellCodeList;

    @ApiModelProperty("工单状态")
    private String workOrderStatus;

    @ApiModelProperty("工单状态")
    private List<String> workOrderStatusList;

    @ApiModelProperty("条码状态")
    private String materialtLotStatus;

    @ApiModelProperty("条码状态")
    private List<String> materialtLotStatusList;

    @ApiModelProperty("是否创建SN")
    private String isCreateSn;

    @ApiModelProperty("条码有效性")
    private String materialLotEnableFlag;

    @ApiModelProperty("在制标识")
    private String mfFlag;

    @ApiModelProperty("拆机时间起")
    private String splitDateFrom;

    @ApiModelProperty("拆机时间至")
    private String splitDateTo;

    @ApiModelProperty("工单结束时间起")
    private String workOrderDateFrom;

    @ApiModelProperty("工单结束时间至")
    private String workOrderDateTo;
}
