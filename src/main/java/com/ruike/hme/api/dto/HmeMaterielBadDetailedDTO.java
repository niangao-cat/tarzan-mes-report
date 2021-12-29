package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:06
 */
@Data
public class HmeMaterielBadDetailedDTO implements Serializable {

    private static final long serialVersionUID = 8357126788603584681L;

    @ApiModelProperty("提交时间开始")
    @NotNull
    private Date dateTimeFrom;
    @ApiModelProperty("提交时间结束")
    @NotNull
    private Date dateTimeTo;
    @ApiModelProperty("生产线Id")
    private String prodLineId;
    @ApiModelProperty("提交工位集合")
    private List<String> stationIdList;
    @ApiModelProperty("提交工位")
    private String stationId;
    @ApiModelProperty("责任工位集合")
    private List<String> dutyIdList;
    @ApiModelProperty("责任工位")
    private String dutyId;
    @ApiModelProperty("产品料号集合")
    private List<String> materialCodeList;
    @ApiModelProperty("产品料号")
    private String materialCode;
    @ApiModelProperty("组件料号集合")
    private List<String> assemblyCodeList;
    @ApiModelProperty("组件料号")
    private String assemblyCode;
    @ApiModelProperty("工单号集合")
    private List<String> workOrderNumList;
    @ApiModelProperty("工单号")
    private String workOrderNum;
    @ApiModelProperty("条码号集合")
    private List<String> materialLotCodeList;
    @ApiModelProperty("条码号")
    private String materialLotCode;
    @ApiModelProperty("不良代码Id集合")
    private List<String> ncCodeIdList;
    @ApiModelProperty("不良代码Id")
    private String ncCodeId;
    @ApiModelProperty("供应商批次集合")
    private List<String> attrValueList;
    @ApiModelProperty("供应商批次")
    private String attrValue;
    @ApiModelProperty("工序Id集合")
    private List<String> processIdList;
    @ApiModelProperty("工序Id")
    private String processId;
    @ApiModelProperty("工段Id集合")
    private List<String> lineWorkcellIdList;
    @ApiModelProperty("工段Id")
    private String lineWorkcellId;
    @ApiModelProperty("提交人")
    private Long realNameId;
    @ApiModelProperty("单据状态")
    private List<String> ncIncidentStatusList;
    @ApiModelProperty("单据状态")
    private String ncIncidentStatus;
    @ApiModelProperty("处理人")
    private Long closedNameId;
    @ApiModelProperty("处置方式")
    private String processMethod;
    @ApiModelProperty("是否冻结")
    private String freezeFlag;
    @ApiModelProperty("处理时间开始")
    private String closedDateTimeFrom;
    @ApiModelProperty("处理时间结束")
    private String closedDateTimeTo;
    @ApiModelProperty("默认站点")
    private String siteId;
    @ApiModelProperty("车间")
    private String workshopId;
    @ApiModelProperty("不良代码组")
    private String ncGroupId;
    @ApiModelProperty("处置方式")
    private List<String> processMethodList;
    @ApiModelProperty("不良单号")
    private String incidentNumber;

    public void initParam() {
        processMethodList = StringUtils.isNotBlank(processMethod) ? Arrays.asList(StringUtils.split(processMethod, ",")) : null;
        ncIncidentStatusList = StringUtils.isNotBlank(ncIncidentStatus) ? Arrays.asList(StringUtils.split(ncIncidentStatus, ",")) : null;
        lineWorkcellIdList = StringUtils.isNotBlank(lineWorkcellId) ? Arrays.asList(StringUtils.split(lineWorkcellId, ",")) : null;
        processIdList = StringUtils.isNotBlank(processId) ? Arrays.asList(StringUtils.split(processId, ",")) : null;
        attrValueList = StringUtils.isNotBlank(attrValue) ? Arrays.asList(StringUtils.split(attrValue, ",")) : null;
        ncCodeIdList = StringUtils.isNotBlank(ncCodeId) ? Arrays.asList(StringUtils.split(ncCodeId, ",")) : null;
        materialLotCodeList = StringUtils.isNotBlank(materialLotCode) ? Arrays.asList(StringUtils.split(materialLotCode, ",")) : null;
        workOrderNumList = StringUtils.isNotBlank(workOrderNum) ? Arrays.asList(StringUtils.split(workOrderNum, ",")) : null;
        assemblyCodeList = StringUtils.isNotBlank(assemblyCode) ? Arrays.asList(StringUtils.split(assemblyCode, ",")) : null;
        materialCodeList = StringUtils.isNotBlank(materialCode) ? Arrays.asList(StringUtils.split(materialCode, ",")) : null;
        dutyIdList = StringUtils.isNotBlank(dutyId) ? Arrays.asList(StringUtils.split(dutyId, ",")) : null;
        stationIdList = StringUtils.isNotBlank(stationId) ? Arrays.asList(StringUtils.split(stationId, ",")) : null;
    }
}
