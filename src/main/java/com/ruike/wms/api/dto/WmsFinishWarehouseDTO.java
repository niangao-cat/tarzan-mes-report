package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:27
 */
@Data
public class WmsFinishWarehouseDTO implements Serializable {

    private static final long serialVersionUID = 6272765655724264083L;

    @ApiModelProperty("站点")
    private String siteId;
    @ApiModelProperty("制造部")
    private String areaId;
    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("产品编码")
    private String materialCode;
    @ApiModelProperty("版本")
    private String productionVersion;
    @ApiModelProperty("库存地点")
    private String warehouseId;
    @ApiModelProperty("查询时间起")
    private String queryDateFrom;
    @ApiModelProperty("查询时间至")
    private String queryDateTo;
    @ApiModelProperty("物料组")
    private String itemGroup;
    @ApiModelProperty("物料分类")
    private String materialCategory;

    @ApiModelProperty("制造部集合")
    private List<String> areaIdList;
    @ApiModelProperty("产线集合")
    private List<String> prodLineIdList;
    @ApiModelProperty("产品编码集合")
    private List<String> materialCodeList;
    @ApiModelProperty("库存地点集合")
    private List<String> warehouseIdList;
    @ApiModelProperty("物料组集合")
    private List<String> itemGroupList;

    public void initParam() {
        areaIdList = StringUtils.isNotBlank(areaId) ? Arrays.asList(StringUtils.split(areaId, ",")) : null;
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(StringUtils.split(prodLineId, ",")) : null;
        materialCodeList = StringUtils.isNotBlank(materialCode) ? Arrays.asList(StringUtils.split(materialCode, ",")) : null;
        warehouseIdList = StringUtils.isNotBlank(warehouseId) ? Arrays.asList(StringUtils.split(warehouseId, ",")) : null;
        itemGroupList = StringUtils.isNotBlank(itemGroup) ? Arrays.asList(StringUtils.split(itemGroup, ",")) : null;
    }
}
