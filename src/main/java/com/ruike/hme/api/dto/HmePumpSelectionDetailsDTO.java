package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmePumpSelectionDetailsDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/11/05 11:01
 **/
@Data
public class HmePumpSelectionDetailsDTO implements Serializable {
    private static final long serialVersionUID = -7826169796416794544L;

    @ApiModelProperty(value = "预筛选时间从")
    private Date creationDateFrom;

    @ApiModelProperty(value = "预筛选时间至")
    private Date creationDateTo;

    @ApiModelProperty(value = "泵浦源SN,以,分隔的字符串")
    private String materialLotCode;

    @ApiModelProperty(value = "筛选状态")
    private String status;

    @ApiModelProperty(value = "仓库ID,以,分隔的字符串")
    private String warehouseId;

    @ApiModelProperty(value = "货位ID,以,分隔的字符串")
    private String locatorId;

    @ApiModelProperty(value = "工单号,以,分隔的字符串")
    private String workOrderNum;

    @ApiModelProperty(value = "物料ID,以,分隔的字符串")
    private String materialId;

    @ApiModelProperty(value = "原容器号,以,分隔的字符串")
    private String oldContainerCode;

    @ApiModelProperty(value = "目标容器,以,分隔的字符串")
    private String newContainerCode;

    @ApiModelProperty(value = "预筛选操作人ID")
    private Long createdBy;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "装箱时间从")
    private Date packedDateFrom;

    @ApiModelProperty(value = "装箱时间至")
    private Date packedDateTo;

    @ApiModelProperty(value = "装箱操作人ID")
    private Long packedBy;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "筛选产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "筛选工位ID")
    private String workcellId;

    @ApiModelProperty(value = "筛选规则编码,以,分隔的字符串")
    private String ruleCode;

    @ApiModelProperty(value = "筛选批次,以,分隔的字符串")
    private String selectionLot;

    @ApiModelProperty(value = "组合物料ID,以,分隔的字符串")
    private String combMaterialId;

    @ApiModelProperty(value = "BOM版本号,以,分隔的字符串")
    private String revision;

    @ApiModelProperty(value = "组合件SN,以,分隔的字符串")
    private String combMaterialLotCode;

    @ApiModelProperty(value = "投料工单,以,分隔的字符串")
    private String releaseWorkOrderNum;


    @ApiModelProperty(value = "泵浦源SN集合,后端自用")
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "仓库ID集合,后端自用")
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "货位ID集合,后端自用")
    private List<String> locatorIdList;

    @ApiModelProperty(value = "工单号集合，后端自用")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "物料ID集合，后端自用")
    private List<String> materialIdList;

    @ApiModelProperty(value = "原容器号集合，后端自用")
    private List<String> oldContainerCodeList;

    @ApiModelProperty(value = "目标容器集合，后端自用")
    private List<String> newContainerCodeList;

    @ApiModelProperty(value = "筛选规则编码集合，后端自用")
    private List<String> ruleCodeList;

    @ApiModelProperty(value = "筛选批次集合，后端自用")
    private List<String> selectionLotList;

    @ApiModelProperty(value = "组合物料集合，后端自用")
    private List<String> combMaterialIdList;

    @ApiModelProperty(value = "BOM版本号集合，后端自用")
    private List<String> revisionList;

    @ApiModelProperty(value = "组合件SN集合，后端自用")
    private List<String> combMaterialLotCodeList;

    @ApiModelProperty(value = "投料工单集合")
    private List<String> releaseWorkOrderNumList;

    @ApiModelProperty(value = "工位集合")
    private List<String> workcellIdList;
}
