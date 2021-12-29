package com.ruike.hme.api.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * COS完工芯片明细报表 查询对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 13:54
 */
@Data
public class CosCompletionDetailQuery implements Serializable {
    private static final long serialVersionUID = -7188480864838097090L;

    @ApiModelProperty(value = "预筛选时间从")
    private String preSelectionDateFrom;

    @ApiModelProperty(value = "预筛选时间至")
    private String preSelectionDateTo;

    @ApiModelProperty(value = "仓库")
    private String warehouseId;

    @ApiModelProperty(value = "货位")
    private String locatorId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "实验编码")
    private String labCode;

    @ApiModelProperty(value = "挑选来源条码")
    private String selectionSourceMaterialLotCode;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品编码")
    private String materialId;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "筛选状态")
    private String selectionStatus;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "器件序列号")
    private String identification;

    @ApiModelProperty(value = "器件物料")
    private String deviceMaterialId;

    @ApiModelProperty(value = "筛选批次")
    private String preSelectionLot;

    @ApiModelProperty(value = "筛选规则编码")
    private String selectionRuleCode;

    @ApiModelProperty(value = "是否绑定工单号")
    private String bindFlag;

    @ApiModelProperty(value = "投料工单")
    private String releaseWorkOrderNum;

    @ApiModelProperty(value = "热沉条码")
    private String hotSinkMaterialLotCode;

    @ApiModelProperty(value = "热沉供应商批次号")
    private String hotSinkSupplierLot;

    @ApiModelProperty(value = "金线条码")
    private String goldMaterialLotCode;

    @ApiModelProperty(value = "金线供应商批次")
    private String goldSupplierLot;

    @ApiModelProperty(value = "预筛选操作人ID")
    private String preSelectionOperatorId;

    @ApiModelProperty(value = "装箱操作人ID")
    private String loadOperatorId;

    @ApiModelProperty(value = "装箱时间从")
    private String loadDateFrom;

    @ApiModelProperty(value = "装箱时间至")
    private String loadDateTo;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "仓库列表", hidden = true)
    @JsonIgnore
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "货位列表", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    @ApiModelProperty(value = "货位列表(包含仓库下货位)", hidden = true)
    @JsonIgnore
    private List<String> allLocatorIdList;

    @ApiModelProperty(value = "产品编码列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;

    @ApiModelProperty(value = "EOID列表", hidden = true)
    @JsonIgnore
    private List<String> eoIdList;

    @ApiModelProperty(value = "条码ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialLotIdList;

    @ApiModelProperty(value = "条码列表", hidden = true)
    @JsonIgnore
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "热沉编码列表", hidden = true)
    @JsonIgnore
    private List<String> hotSinkCodeList;

    @ApiModelProperty(value = "WAFER列表", hidden = true)
    @JsonIgnore
    private List<String> waferList;

    @ApiModelProperty(value = "实验编码列表", hidden = true)
    @JsonIgnore
    private List<String> labCodeList;

    @ApiModelProperty(value = "挑选来源条码列表", hidden = true)
    @JsonIgnore
    private List<String> selectionSourceMaterialLotCodeList;

    @ApiModelProperty(value = "虚拟号列表", hidden = true)
    @JsonIgnore
    private List<String> virtualNumList;

    @ApiModelProperty(value = "器件序列号列表", hidden = true)
    @JsonIgnore
    private List<String> identificationList;

    @ApiModelProperty(value = "器件物料列表", hidden = true)
    @JsonIgnore
    private List<String> deviceMaterialIdList;

    @ApiModelProperty(value = "COS类型列表", hidden = true)
    @JsonIgnore
    private List<String> cosTypeList;

    @ApiModelProperty(value = "筛选批次列表", hidden = true)
    @JsonIgnore
    private List<String> preSelectionLotList;

    @ApiModelProperty(value = "筛选规则编码列表", hidden = true)
    @JsonIgnore
    private List<String> selectionRuleCodeList;

    @ApiModelProperty(value = "热沉条码列表", hidden = true)
    @JsonIgnore
    private List<String> hotSinkMaterialLotCodeList;

    @ApiModelProperty(value = "热沉供应商批次号列表", hidden = true)
    @JsonIgnore
    private List<String> hotSinkSupplierLotList;

    @ApiModelProperty(value = "金线条码列表", hidden = true)
    @JsonIgnore
    private List<String> goldMaterialLotCodeList;

    @ApiModelProperty(value = "金线供应商批次列表", hidden = true)
    @JsonIgnore
    private List<String> goldSupplierLotList;

    @ApiModelProperty(value = "物料组列表列表")
    @JsonIgnore
    private List<String> itemGroupList;

    @ApiModelProperty(value = "工单号列表")
    @JsonIgnore
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "租户", hidden = true)
    @JsonIgnore
    private Long tenantId;

    public void initParam(Long tenantId, LovAdapter lovAdapter) {
        warehouseIdList = StringCommonUtils.splitIntoList(warehouseId);
        locatorIdList = StringCommonUtils.splitIntoList(locatorId);
        materialIdList = StringCommonUtils.splitIntoList(materialId);
        materialLotCodeList = StringCommonUtils.splitIntoList(materialLotCode);
        hotSinkCodeList = StringCommonUtils.splitIntoList(hotSinkCode);
        waferList = StringCommonUtils.splitIntoList(wafer);
        labCodeList = StringCommonUtils.splitIntoList(labCode);
        selectionSourceMaterialLotCodeList = StringCommonUtils.splitIntoList(selectionSourceMaterialLotCode);
        virtualNumList = StringCommonUtils.splitIntoList(virtualNum);
        identificationList = StringCommonUtils.splitIntoList(identification);
        deviceMaterialIdList = StringCommonUtils.splitIntoList(deviceMaterialId);
        cosTypeList = StringCommonUtils.splitIntoList(cosType);
        preSelectionLotList = StringCommonUtils.splitIntoList(preSelectionLot);
        selectionRuleCodeList = StringCommonUtils.splitIntoList(selectionRuleCode);
        hotSinkMaterialLotCodeList = StringCommonUtils.splitIntoList(hotSinkMaterialLotCode);
        hotSinkSupplierLotList = StringCommonUtils.splitIntoList(hotSinkSupplierLot);
        goldMaterialLotCodeList = StringCommonUtils.splitIntoList(goldMaterialLotCode);
        goldSupplierLotList = StringCommonUtils.splitIntoList(goldSupplierLot);
        this.tenantId = tenantId;
//        itemGroupList = lovAdapter.queryLovValue("", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        itemGroupList = new ArrayList<>();
        workOrderNumList = StringCommonUtils.splitIntoList(workOrderNum);
    }
}
