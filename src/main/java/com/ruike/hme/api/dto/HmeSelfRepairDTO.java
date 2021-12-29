package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 自制件返修统计报表
 *
 * @author xin.t@raycuslaser 2021/7/5 17:17
 */
@Data
public class HmeSelfRepairDTO implements Serializable {

    private static final long serialVersionUID = -7826169701816794544L;

    @ApiModelProperty(value = "事业部ID")
    private String areaId;

    @ApiModelProperty(value = "返修SN")
    private String repairSnNum;

    @ApiModelProperty(value = "返修SN列表",hidden = true)
    private List<String> repairSnNumList;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品编码列表",hidden = true)
    private List<String> materialCodeList;

    @ApiModelProperty(value = "申请人工号")
    private String createdId;

    @ApiModelProperty(value = "申请人ID")
    private String createdBy;

    @ApiModelProperty(value = "条码有效性")
    private String enabledFlag;

    @ApiModelProperty(value = "在制标识")
    private String mfFlag;

    @ApiModelProperty(value = "条码状态")
    private String materialLotCodeStatus;

    @ApiModelProperty(value = "条码状态列表",hidden = true)
    private List<String> materialLotCodeStatusList;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单状态")
    private String  workOrderStatus;

    @ApiModelProperty(value = "工单状态列表",hidden = true)
    private List<String>  workOrderStatusList;

    @ApiModelProperty(value = "是否创建SN")
    private String createSnFlag;

    @ApiModelProperty(value = "工单创建时间从")
    private String actualStartDateFrom;

    @ApiModelProperty(value = "工单创建时间至")
    private String actualStartDateTo;

    @ApiModelProperty(value = "工单结束时间从")
    private String actualEndDateFrom;

    @ApiModelProperty(value = "工单结束时间至")
    private String actualEndDateTo;

    @ApiModelProperty(value = "所在仓库编码")
    private String warehouseCode;

    public void init(){
        repairSnNumList = StringUtils.isNotBlank(repairSnNum) ? Arrays.asList(StringUtils.split(repairSnNum,",")) : null;
        materialCodeList = StringUtils.isNotBlank(materialCode) ? Arrays.asList(StringUtils.split(materialCode,",")) : null;
        materialLotCodeStatusList = StringUtils.isNotBlank(materialLotCodeStatus) ? Arrays.asList(StringUtils.split(materialLotCodeStatus,",")) : null;
        workOrderStatusList = StringUtils.isNotBlank(workOrderStatus) ? Arrays.asList(StringUtils.split(workOrderStatus,",")) : null;

    }
}
