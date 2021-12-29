package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:30
 */
@Data
public class HmeInventoryEndProductVO implements Serializable {

    private static final long serialVersionUID = -4368090198634940735L;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "接收SN")
    private List<String> receiveSnNumList;

    @ApiModelProperty(value = "当前SN")
    private List<String> currentSnNumList;

    @ApiModelProperty(value = "产品编码")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "当前状态")
    private List<String> splitStatusList;

    @ApiModelProperty(value = "所在仓库编码")
    private List<String> warehouseCodeList;

    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty(value = "当前工位编码")
    private List<String> workcellCodeList;

    @ApiModelProperty(value = "工单状态")
    private String  wordOrderStatus;

    @ApiModelProperty(value = "条码状态")
    private String materialLotCodeStatus;

    @ApiModelProperty(value = "所在仓库类型")
    private String warehouseType;

    @ApiModelProperty(value = "是否创建SN")
    private String createSnFlag;

    @ApiModelProperty(value = "在制标识")
    private String mfFlag;

    @ApiModelProperty(value = "有效性")
    private String enabledFlag;

    @ApiModelProperty(value = "入库单是否为空")
    private String docNumFlag;

    @ApiModelProperty(value = "拆机时间从")
    private String splitTimeFrom;

    @ApiModelProperty(value = "拆机时间至")
    private String splitTimeTo;

    @ApiModelProperty(value = "工单结束时间从")
    private String actualEndDateFrom;

    @ApiModelProperty(value = "工单结束时间至")
    private String actualEndDateTo;

    @ApiModelProperty(value = "工单状态")
    private List<String>  wordOrderStatusList;

    @ApiModelProperty(value = "条码状态")
    private List<String> materialLotCodeStatusList;

    public void initParam() {
        wordOrderStatusList = StringUtils.isNotBlank(wordOrderStatus) ? Arrays.asList(StringUtils.split(wordOrderStatus, ",")) : null;
        materialLotCodeStatusList = StringUtils.isNotBlank(materialLotCodeStatus) ? Arrays.asList(StringUtils.split(materialLotCodeStatus, ",")) : null;

    }
}
