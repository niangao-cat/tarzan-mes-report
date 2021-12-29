package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/17 14:46
 */
@Data
public class HmeCosStaffProductVO implements Serializable {

    private static final long serialVersionUID = 4137807891903095543L;

    @ApiModelProperty(value = "创建时间从")
    private String createDateFrom;
    @ApiModelProperty(value = "创建时间至")
    private String createDateTo;
    @ApiModelProperty(value = "产线")
    private String prodLineId;
    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "员工")
    private String userId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "物料编码")
    @JsonIgnore
    private List<String> materialCodeList;
    @ApiModelProperty(value = "物料版本")
    @JsonIgnore
    private List<String> materialVersionList;
    @ApiModelProperty(value = "COS类型")
    @JsonIgnore
    private List<String> cosTypeList;
    @ApiModelProperty(value = "工位")
    @JsonIgnore
    private List<String> workcellIdList;
    @ApiModelProperty(value = "产线")
    @JsonIgnore
    private List<String> prodLineIdList;
    @ApiModelProperty(value = "工段")
    @JsonIgnore
    private List<String> lineWorkcellIdList;
    @ApiModelProperty(value = "工序")
    @JsonIgnore
    private List<String> processIdList;
    @ApiModelProperty(value = "员工")
    @JsonIgnore
    private List<String> userIdList;


    public void initParam () {
        materialCodeList = StringUtils.isNotBlank(materialCode) ? Arrays.asList(StringUtils.split(materialCode, ",")) : null;
        materialVersionList = StringUtils.isNotBlank(materialVersion) ? Arrays.asList(StringUtils.split(materialVersion, ",")) : null;
        cosTypeList = StringUtils.isNotBlank(cosType) ? Arrays.asList(StringUtils.split(cosType, ",")) : null;
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(StringUtils.split(prodLineId, ",")) : null;
        lineWorkcellIdList = StringUtils.isNotBlank(lineWorkcellId) ? Arrays.asList(StringUtils.split(lineWorkcellId, ",")) : null;
        processIdList = StringUtils.isNotBlank(processId) ? Arrays.asList(StringUtils.split(processId, ",")) : null;
        userIdList = StringUtils.isNotBlank(userId) ? Arrays.asList(StringUtils.split(userId, ",")) : null;
    }
}
