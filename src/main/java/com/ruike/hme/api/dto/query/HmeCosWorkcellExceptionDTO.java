package com.ruike.hme.api.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * HmeCosWorkcellExceptionDTO
 * COS工位加工异常汇总表输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 12:38
 */
@Data
public class HmeCosWorkcellExceptionDTO implements Serializable {

    private static final long serialVersionUID = 5095294321248495473L;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "WAFER")
    private String waferNum;
    @ApiModelProperty(value = "产品编码")
    private String materialId;
    @ApiModelProperty(value = "操作人")
    private String id;
    @ApiModelProperty(value = "不良代码")
    private String ncCodeId;
    @ApiModelProperty(value = "产线")
    private String prodLineId;
    @ApiModelProperty(value = "工段")
    private String workcellLineId;
    @ApiModelProperty(value = "工序")
    private String workcellProcessId;
    @ApiModelProperty(value = "工位")
    private String workcellStationId;
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "不良代码列表", hidden = true)
    @JsonIgnore
    private List<String> ncCodeIdList;
    @ApiModelProperty(value = "产线", hidden = true)
    @JsonIgnore
    private List<String> prodLineIdList;
    @ApiModelProperty(value = "工段", hidden = true)
    @JsonIgnore
    private List<String> workcellLineIdList;
    @ApiModelProperty(value = "工序", hidden = true)
    @JsonIgnore
    private List<String> workcellProcessIdList;
    @ApiModelProperty(value = "工位", hidden = true)
    @JsonIgnore
    private List<String> workcellStationIdList;

    public void initParam() {
        this.ncCodeIdList = StringUtils.isBlank(this.ncCodeId) ? null : Arrays.asList(StringUtils.split(this.ncCodeId, ","));
        this.prodLineIdList = StringUtils.isBlank(this.prodLineId) ? null : Arrays.asList(StringUtils.split(this.prodLineId, ","));
        this.workcellLineIdList = StringUtils.isBlank(this.workcellLineId) ? null : Arrays.asList(StringUtils.split(this.workcellLineId, ","));
        this.workcellProcessIdList = StringUtils.isBlank(this.workcellProcessId) ? null : Arrays.asList(StringUtils.split(this.workcellProcessId, ","));
        this.workcellStationIdList = StringUtils.isBlank(this.workcellStationId) ? null : Arrays.asList(StringUtils.split(this.workcellStationId, ","));
    }
}
