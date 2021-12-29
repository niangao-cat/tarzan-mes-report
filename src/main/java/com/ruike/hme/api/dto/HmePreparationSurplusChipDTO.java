package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePreparationSurplusChipDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:58:32
 **/
@Data
public class HmePreparationSurplusChipDTO implements Serializable {
    private static final long serialVersionUID = -155783741716790449L;

    @ApiModelProperty(value = "筛选时间从")
    private Date preparationDateFrom;

    @ApiModelProperty(value = "筛选时间至")
    private Date preparationDateTo;

    @ApiModelProperty(value = "仓库,以逗号分隔的ID字符串")
    private String warehouseId;

    @ApiModelProperty(value = "货位,以逗号分隔的ID字符串")
    private String locatorId;

    @ApiModelProperty(value = "器件物料")
    private String deviceMaterialId;

    @ApiModelProperty(value = "条码,以逗号分隔的字符串")
    private String materialLotCode;

    @ApiModelProperty(value = "COS类型,以逗号分隔的字符串")
    private String cosType;

    @ApiModelProperty(value = "物料,以逗号分隔的ID字符串")
    private String materialId;

    @ApiModelProperty(value = "操作人,以逗号分隔的ID字符串")
    private String userId;

    @ApiModelProperty(value = "筛选批次,以逗号分隔的字符串")
    private String preSelectionLot;

    @ApiModelProperty(value = "筛选规则编码")
    private String preRuleCode;
}
