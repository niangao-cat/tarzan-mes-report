package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:27
 */
@Data
public class WmsFinishWarehouseVO implements Serializable {

    private static final long serialVersionUID = -6395433910051562320L;

    @ApiModelProperty(value = "站点")
    private String siteCode;
    @ApiModelProperty(value = "制造部")
    private String areaCode;
    @ApiModelProperty(value = "制造部名称")
    private String areaName;
    @ApiModelProperty(value = "生产线")
    private String prodLineName;
    @ApiModelProperty(value = "产品编码")
    private String materialCode;
    @ApiModelProperty(value = "产品描述")
    private String materialName;
    @ApiModelProperty(value = "版本")
    private String productionVersion;
    @ApiModelProperty(value = "完工数量")
    private BigDecimal finishQty;
    @ApiModelProperty(value = "入库数量")
    private BigDecimal warehousingQty;
    @ApiModelProperty(value = "物料组")
    private String itemGroupCode;
    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;
    @ApiModelProperty("物料分类")
    private String materialCategory;
    @ApiModelProperty("物料分类")
    private String materialCategoryMeaning;
    @ApiModelProperty("汇总类型 F-完工 W-入库")
    @JsonIgnore
    private String summaryType;
    @ApiModelProperty("库存地点")
    private String locatorCode;
}
