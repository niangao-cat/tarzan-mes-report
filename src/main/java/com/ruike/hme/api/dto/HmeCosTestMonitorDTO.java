package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:43
 */
@Data
public class HmeCosTestMonitorDTO implements Serializable {

    private static final long serialVersionUID = -3766542104700883644L;

    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("截留单据号")
    private String monitorDocNums;
    @ApiModelProperty("截留单据号列表")
    private List<String> monitorDocNumList;
    @ApiModelProperty("wafer")
    private String waferNums;
    @ApiModelProperty("wafer列表")
    private List<String> waferNumList;
    @ApiModelProperty("COS类型")
    private String cosTypes;
    @ApiModelProperty("COS类型列表")
    private List<String> cosTypeList;
    @ApiModelProperty("导出任务")
    private HmeExportTaskVO exportTaskVO;

    public void initParam() {
        monitorDocNumList = StringUtils.isNotBlank(monitorDocNums) ? Arrays.asList(StringUtils.split(monitorDocNums, ",")) : null;
        waferNumList = StringUtils.isNotBlank(waferNums) ? Arrays.asList(StringUtils.split(waferNums, ",")) : null;
        cosTypeList = StringUtils.isNotBlank(cosTypes) ? Arrays.asList(StringUtils.split(cosTypes, ",")) : null;
    }
}
