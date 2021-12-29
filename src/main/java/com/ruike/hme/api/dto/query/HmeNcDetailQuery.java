package com.ruike.hme.api.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工序不良记录 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:11
 */
@Data
public class HmeNcDetailQuery {
    @ApiModelProperty(value = "不良发起时间起")
    private Date beginTime;

    @ApiModelProperty(value = "不良发起时间至")
    private Date endTime;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "车间")
    private String workshopId;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "责任工位")
    private String stationId;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "不良单号")
    private String incidentNum;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty("处理方式")
    private String processMethod;

    @ApiModelProperty("不良代码组Id")
    private String ncGroupId;

    @ApiModelProperty("不良代码")
    private String ncCodeId;

    @ApiModelProperty("产品序列号")
    private String sn;

    @ApiModelProperty("提交工位")
    private String workcellId;

    @ApiModelProperty(value = "提交工序")
    private String processId;

    @ApiModelProperty(value = "提交工段")
    private String lineId;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "是否转型")
    private String transformFlag;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "提交人ID")
    private Long submitUserId;

    @ApiModelProperty(value = "处理人ID")
    private Long processUserId;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "责任工位列表", hidden = true)
    @JsonIgnore
    private List<String> stationIdList;

    @ApiModelProperty(value = "产品编码列表", hidden = true)
    @JsonIgnore
    private List<String> materialCodeList;

    @ApiModelProperty(value = "工单号列表", hidden = true)
    @JsonIgnore
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "不良代码列表", hidden = true)
    @JsonIgnore
    private List<String> ncCodeIdList;

    @ApiModelProperty(value = "序列号列表", hidden = true)
    @JsonIgnore
    private List<String> snList;

    @ApiModelProperty(value = "提交工位列表", hidden = true)
    @JsonIgnore
    private List<String> workcellIdList;

    @ApiModelProperty(value = "工序Id列表", hidden = true)
    @JsonIgnore
    private List<String> processIdList;

    @ApiModelProperty(value = "工段id列表", hidden = true)
    @JsonIgnore
    private List<String> lineIdList;

    @ApiModelProperty(value = "单据状态列表", hidden = true)
    @JsonIgnore
    private List<String> docStatusList;

    @ApiModelProperty("处理方式列表")
    @JsonIgnore
    private List<String> processMethodList;

    @ApiModelProperty(value = "不良处理时间起")
    private String ncHandleDateFrom;

    @ApiModelProperty(value = "不良处理时间至")
    private String ncHandleDateTo;

    public void validParam() {
        // 工单列表
        this.setWorkOrderNumList(StringUtils.isNotBlank(this.getWorkOrderNum()) ? Arrays.asList(this.getWorkOrderNum().split(",")) : null);
        // 物料编码列表
        this.setMaterialCodeList(StringUtils.isNotBlank(this.getMaterialCode()) ? Arrays.asList(this.getMaterialCode().split(",")) : null);
        // sn列表
        this.setSnList(StringUtils.isNotBlank(this.getSn()) ? Arrays.asList(this.getSn().split(",")) : null);
        // 责任工位列表
        this.setStationIdList(StringUtils.isNotBlank(this.getStationId()) ? Arrays.asList(this.getStationId().split(",")) : null);
        // 提交工位列表
        this.setWorkcellIdList(StringUtils.isNotBlank(this.getWorkcellId()) ? Arrays.asList(this.getWorkcellId().split(",")) : null);
        // 不良代码列表
        this.setNcCodeIdList(StringUtils.isNotBlank(this.getNcCodeId()) ? Arrays.asList(this.getNcCodeId().split(",")) : null);
        // 工序列表
        this.setProcessIdList(StringUtils.isNotBlank(this.getProcessId()) ? Arrays.asList(this.getProcessId().split(",")) : null);
        // 工段列表
        this.setLineIdList(StringUtils.isNotBlank(this.getLineId()) ? Arrays.asList(this.getLineId().split(",")) : null);
        // 处理方式列表
        this.setProcessMethodList(StringUtils.isNotBlank(this.getProcessMethod()) ? Arrays.asList(this.getProcessMethod().split(",")) : null);
        // 单据状态列表
        this.setDocStatusList(StringUtils.isNotBlank(this.getDocStatus()) ? Arrays.asList(this.getDocStatus().split(",")) : null);
    }

}