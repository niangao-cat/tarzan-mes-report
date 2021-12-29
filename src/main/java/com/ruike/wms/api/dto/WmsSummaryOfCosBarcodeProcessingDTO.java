package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ywj
 * @version 0.0.1
 * @description COS条码加工汇总表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
@Data
public class WmsSummaryOfCosBarcodeProcessingDTO implements Serializable {

    private static final long serialVersionUID = 7459001122289550840L;

    @ApiModelProperty(value = "工单,以逗号分隔的工单号字符串")
    private String workOrderNum;

    @ApiModelProperty(value = "COS类型,以逗号分隔的值集的值字符串")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String waferNum;

    @ApiModelProperty(value = "WAFER集合",hidden = true)
    private List<String> waferNumList;

    @ApiModelProperty(value = "产品编码ID")
    private String snMaterialId;

    @ApiModelProperty(value = "操作人ID")
    private String createdBy;

    @ApiModelProperty(value = "条码,以逗号分隔条码编码字符串")
    private String materialLotCode;

    @ApiModelProperty(value = "热沉条码")
    private String sinkCode;

    @ApiModelProperty(value = "金线条码")
    private String goldCode;

    @ApiModelProperty(value = "开始时间")
    private Date creationDateStart;

    @ApiModelProperty(value = "结束时间")
    private Date creationDateEnd;

    @ApiModelProperty(value = "作业类型")
    private List<String> jobTypes;

    @ApiModelProperty(value = "热沉物料ID,以逗号分隔的ID集合")
    private String sinkMaterialCodeId;

    @ApiModelProperty("金线物料ID,以逗号分隔的ID集合")
    private String goldMaterialId;

    @ApiModelProperty("热沉供应商批次")
    private String sinkSupplierLot;

    @ApiModelProperty("金线供应商批次")
    private String goldSupplierLot;

    @ApiModelProperty(value = "实验代码,以逗号分隔的代码字符串")
    private String labCode;

    @ApiModelProperty(value = "生产线ID,以逗号分隔的ID集合")
    private String prodLineId;

    @ApiModelProperty(value = "工段ID,以逗号分隔的ID集合")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工序ID,以逗号分隔的ID集合")
    private String processId;

    @ApiModelProperty(value = "工位ID,以逗号分隔的ID集合")
    private String workcellId;

    @ApiModelProperty(value = "工位ID集合,后端自用",hidden = true)
    private List<String> workcellIdList;

    @ApiModelProperty(value = "工单集合，后端自用",hidden = true)
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "COS类型集合，后端自用",hidden = true)
    private List<String> cosTypeList;

    @ApiModelProperty(value = "条码集合，后端自用",hidden = true)
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "热沉物料ID集合,后端自用",hidden = true)
    private List<String> sinkMaterialCodeIdList;

    @ApiModelProperty(value = "金线物料ID集合,后端自用",hidden = true)
    private List<String> goldMaterialIdList;

    @ApiModelProperty(value = "实验代码集合,后端自用",hidden = true)
    private List<String> labCodeList;

    @ApiModelProperty(value = "生产线ID集合,后端自用",hidden = true)
    private List<String> prodLineIdList;

    public void initParam(Long tenantId, LovAdapter lovAdapter) {
        // 查询直接数据
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);
        this.setJobTypes(lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList()));
        waferNumList = StringUtils.isBlank(waferNum) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(waferNum,",")));
        workOrderNumList = StringUtils.isNotBlank(workOrderNum) ? Arrays.asList(workOrderNum.split(",")) : null;
        cosTypeList = StringUtils.isNotBlank(cosType) ? Arrays.asList(cosType.split(",")) : null;
        materialLotCodeList = StringUtils.isNotBlank(materialLotCode) ? Arrays.asList(materialLotCode.split(",")) : null;
        sinkMaterialCodeIdList = StringUtils.isNotBlank(sinkMaterialCodeId) ? Arrays.asList(sinkMaterialCodeId.split(",")) : null;
        goldMaterialIdList = StringUtils.isNotBlank(goldMaterialId) ? Arrays.asList(goldMaterialId.split(",")) : null;
        labCodeList = StringUtils.isNotBlank(labCode) ? Arrays.asList(labCode.split(",")) : null;
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(prodLineId.split(",")) : null;
    }

}
