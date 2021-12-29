package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:49
 */
@Data
public class HmeTagCheckQueryVO implements Serializable {

    private static final long serialVersionUID = 4546225404006018526L;

    @ApiModelProperty("事业部")
    private String businessId;
    @ApiModelProperty("类型")
    private String ruleType;
    @ApiModelProperty("规则头ID")
    private String ruleHeaderId;
    @ApiModelProperty("规则头集合")
    private List<String> ruleHeaderIdList;
    @ApiModelProperty("序列号")
    private String materialLotCode;
    @ApiModelProperty("序列号集合")
    private List<String> materialLotCodeList;
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("工单集合")
    private List<String> workOrderNumList;
    @ApiModelProperty("物料")
    private String materialCode;
    @ApiModelProperty("物料集合")
    private List<String> materialCodeList;
    @ApiModelProperty("出站时间从")
    private String siteOutDateFrom;
    @ApiModelProperty("出站时间至")
    private String siteOutDateTo;
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;

    public void initParam() {
        materialLotCodeList = StringUtils.isNotBlank(materialLotCode) ? Arrays.asList(StringUtils.split(materialLotCode, ",")) : null;
        workOrderNumList = StringUtils.isNotBlank(workOrderNum) ? Arrays.asList(StringUtils.split(workOrderNum, ",")) : null;
        materialCodeList = StringUtils.isNotBlank(materialCode) ? Arrays.asList(StringUtils.split(materialCode, ",")) : null;
        ruleHeaderIdList = StringUtils.isNotBlank(ruleHeaderId) ? Arrays.asList(StringUtils.split(ruleHeaderId, ",")) : null;
    }
}
