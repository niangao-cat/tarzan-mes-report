package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 配送需求数量计算
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 15:22
 */
@Data
public class HmeDistributionDemandQtyVO {
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("站点")
    private String siteId;
    @ApiModelProperty("工段")
    private String workcellId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;

    public HmeDistributionDemandQtyVO() {
    }

    public HmeDistributionDemandQtyVO(String materialId, String materialVersion, String siteId) {
        this.materialId = materialId;
        this.materialVersion = materialVersion;
        this.siteId = siteId;
    }

    public HmeDistributionDemandQtyVO(String materialId, String materialVersion, String siteId, String workcellId) {
        this.materialId = materialId;
        this.materialVersion = materialVersion;
        this.siteId = siteId;
        this.workcellId = workcellId;
    }
}
