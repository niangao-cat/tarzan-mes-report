package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/3 16:48
 */
@Data
public class HmeCosReturnDTO implements Serializable {

    private static final long serialVersionUID = 4858399153569578044L;

    @ApiModelProperty("操作时间从")
    private String operateDateFrom;
    @ApiModelProperty("操作时间从")
    private String operateDateTo;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("工单")
    private String workOrderNums;
    @ApiModelProperty("工单列表")
    @JsonIgnore
    private List<String> workOrderNumList;
    @ApiModelProperty("产品")
    private String materialId;
    @ApiModelProperty("处理方式")
    private String returnType;
    @ApiModelProperty("WAFER")
    private String waferNums;
    @ApiModelProperty("WAFER列表")
    @JsonIgnore
    private List<String> waferNumList;
    @ApiModelProperty("COS类型")
    private String cosType;
    @ApiModelProperty("退料条码")
    private String returnMaterialLotCodes;
    @ApiModelProperty("退料条码")
    @JsonIgnore
    private List<String> returnMaterialLotCodeList;
    @ApiModelProperty("退料物料")
    private String returnMaterialId;
    @ApiModelProperty("目标条码")
    private String targetMaterialLotCodes;
    @ApiModelProperty("目标条码")
    @JsonIgnore
    private List<String> targetMaterialLotCodeList;
    @ApiModelProperty("供应商")
    private String supplierId;
    @ApiModelProperty("供应商批次")
    private String supplierLots;
    @ApiModelProperty("供应商批次")
    @JsonIgnore
    private List<String> supplierLotList;
    @ApiModelProperty("库存批次")
    private String lot;
    @ApiModelProperty("不良代码")
    private String ncCodeId;
    @ApiModelProperty("操作人")
    private Long createdById;
    @ApiModelProperty("退料工位")
    private String workcellId;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;

    public void initParam() {
        workOrderNumList = StringUtils.isNotBlank(workOrderNums) ? Arrays.asList(StringUtils.split(workOrderNums, ",")) : null;
        waferNumList = StringUtils.isNotBlank(waferNums) ? Arrays.asList(StringUtils.split(waferNums, ",")) : null;
        returnMaterialLotCodeList = StringUtils.isNotBlank(returnMaterialLotCodes) ? Arrays.asList(StringUtils.split(returnMaterialLotCodes, ",")) : null;
        targetMaterialLotCodeList = StringUtils.isNotBlank(targetMaterialLotCodes) ? Arrays.asList(StringUtils.split(targetMaterialLotCodes, ",")) : null;
        supplierLotList = StringUtils.isNotBlank(supplierLots) ? Arrays.asList(StringUtils.split(supplierLots, ",")) : null;
    }
}
