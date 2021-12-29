package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * COS在制报表查询条件
 *
 * @author @author wenqiang.yin@hand-china.com 2021/01/27 11:26
 */
@Data
public class HmeCosInProductionDTO implements Serializable {

    private static final long serialVersionUID = -2576591625088391603L;

    @ApiModelProperty("工单")
    private List<String> workOrderNumList;

    @ApiModelProperty("工单ID")
    private List<String> workOrderIdList;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("COS类型")
    private List<String> cosTypeList;

    @ApiModelProperty("WAFER")
    private List<String> waferList;

    @ApiModelProperty("产品编码")
    private List<String> materialCodeList;

    @ApiModelProperty("操作人")
    private Long operatorId;

    @ApiModelProperty("条码ID")
    private List<String> materialLotIdList;

    @ApiModelProperty("条码")
    private List<String> materialLotCodeList;

    @ApiModelProperty("开始时间")
    private String creationDateFrom;

    @ApiModelProperty("结束时间")
    private String creationDateTo;

    @ApiModelProperty(value = "工位列表")
    @JsonIgnore
    private List<String> workcellIdList;

    @ApiModelProperty(value = "工位列表", hidden = true)
    @JsonIgnore
    private List<String> workcellList;

    @ApiModelProperty(value = "作业类型列表", hidden = true)
    @JsonIgnore
    private List<String> jobTypeList;

    @ApiModelProperty(value = "工序作业ID", hidden = true)
    @JsonIgnore
    private List<String> jobIdList;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "产线集合")
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "工序集合")
    private List<String> processIdList;

    @ApiModelProperty(value = "工序")
    private String processId;

    @ApiModelProperty(value = "工段集合")
    private List<String> lineWorkcellIdList;

    @ApiModelProperty(value = "工段集合")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;

    @ApiModelProperty(value = "实验代码")
    private List<String> labCodeList;

    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

    @ApiModelProperty(value = "不良标识")
    private String ncFlag;

    @ApiModelProperty(value = "工单版本")
    private String productionVersion;

    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

    @ApiModelProperty(value = "呆滞标识")
    private String sluggishFlag;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;

    public void initParam(Long tenantId, LovAdapter lovAdapter) {
        workcellList = lovAdapter.queryLovValue("HME.COS_LL_WORKCELL", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        jobTypeList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(prodLineId.split(",")) : null;
        workcellIdList = StringUtils.isNotBlank(workcellId) ? Arrays.asList(workcellId.split(",")) : null;
        processIdList = StringUtils.isNotBlank(processId) ? Arrays.asList(processId.split(",")) : null;
        lineWorkcellIdList = StringUtils.isNotBlank(lineWorkcellId) ? Arrays.asList(lineWorkcellId.split(",")) : null;
    }
}
