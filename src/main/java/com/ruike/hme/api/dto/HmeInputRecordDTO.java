package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class HmeInputRecordDTO implements Serializable {
    private static final long serialVersionUID = 2946421035674553170L;

    @ApiModelProperty("工单")
    private List<String> workOrderNumIdList;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("工位ID集合")
    private List<String> workcellIdList;

    @ApiModelProperty("EO")
    private List<String> eoIdList;

    @ApiModelProperty("SN编码")
    private List<String> identificationList;

    @ApiModelProperty("投料编码")
    private String materialLotCode;

    @ApiModelProperty("投料物料")
    private String materialId;

    @ApiModelProperty("供应商批次")
    private List<String> lotList;

    @ApiModelProperty("工序")
    private String processId;

    @ApiModelProperty("投料时间开始")
    private String createDateFrom;

    @ApiModelProperty("投料时间截止")
    private String createDateTo;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("投料编码集合")
    private List<String> materialLotCodeList;

    @ApiModelProperty("投料物料集合")
    private List<String> materialIdList;

    @ApiModelProperty("导出任务")
    private HmeExportTaskVO exportTaskVO;

    public void initParam() {
        materialLotCodeList = StringUtils.isNotBlank(materialLotCode) ? Arrays.asList(StringUtils.split(materialLotCode, ",")) : null;
        materialIdList = StringUtils.isNotBlank(materialId) ? Arrays.asList(StringUtils.split(materialId, ",")) : null;
    }

}
