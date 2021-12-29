package com.ruike.hme.api.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * COS工位加工汇总查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:11
 */
@Data
public class HmeCosWorkcellSummaryQuery {
    @ApiModelProperty("工位ID")
    private String stationId;
    @ApiModelProperty("工序ID")
    private String processId;
    @ApiModelProperty("工段ID")
    private String lineId;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("WAFER")
    private String wafer;
    @ApiModelProperty("COS类型")
    private String cosType;
    @ApiModelProperty("产品编码")
    private String materialId;
    @ApiModelProperty("操作人")
    private String operatorId;
    @ApiModelProperty("时间从")
    private Date creationDateFrom;
    @ApiModelProperty("时间至")
    private Date creationDateTo;

    @ApiModelProperty(value = "工位列表", hidden = true)
    @JsonIgnore
    private Set<String> workcells;

    @ApiModelProperty(value = "作业类型列表", hidden = true)
    @JsonIgnore
    private List<String> jobTypeList;

    @ApiModelProperty(value = "工位ID列表", hidden = true)
    @JsonIgnore
    private List<String> stationIdList;

    @ApiModelProperty(value = "工序ID列表", hidden = true)
    @JsonIgnore
    private List<String> processIdList;

    @ApiModelProperty(value = "工段ID列表", hidden = true)
    @JsonIgnore
    private List<String> lineIdList;

    @ApiModelProperty(value = "产线ID列表", hidden = true)
    @JsonIgnore
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "产品ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;

    @ApiModelProperty(value = "工单列表", hidden = true)
    @JsonIgnore
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "WAFER列表", hidden = true)
    private List<String> waferList;

    public void initParam(Long tenantId, LovAdapter lovAdapter) {
        workcells = lovAdapter.queryLovValue("HME.COS_LL_WORKCELL", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        jobTypeList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        stationIdList = StringCommonUtils.splitIntoList(stationId);
        processIdList = StringCommonUtils.splitIntoList(processId);
        lineIdList = StringCommonUtils.splitIntoList(lineId);
        prodLineIdList = StringCommonUtils.splitIntoList(prodLineId);
        materialIdList = StringCommonUtils.splitIntoList(materialId);
        workOrderNumList = StringCommonUtils.splitIntoList(workOrderNum);
        waferList = StringCommonUtils.splitIntoList(wafer);
    }
}
