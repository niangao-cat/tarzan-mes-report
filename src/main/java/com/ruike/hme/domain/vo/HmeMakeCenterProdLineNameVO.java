package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author yantianhai
 * @Description TODO
 * @Date 2021/11/2 9:49
 */
public class HmeMakeCenterProdLineNameVO {

    private String prodLineId;
    private String prodLineName;
    private String productionGroupCode;
    private String productionGroupId;
    @ApiModelProperty(value = "产线顺序")
    private Long prodLineOrder;
    @ApiModelProperty(value = "产品组描述")
    private String productionGroupDesc;

    public String getProductionGroupId() {
        return productionGroupId;
    }

    public void setProductionGroupId(String productionGroupId) {
        this.productionGroupId = productionGroupId;
    }

    public String getProductionGroupCode() {
        return productionGroupCode;
    }

    public void setProductionGroupCode(String productionGroupCode) {
        this.productionGroupCode = productionGroupCode;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public Long getProdLineOrder() {
        return prodLineOrder;
    }

    public void setProdLineOrder(Long prodLineOrder) {
        this.prodLineOrder = prodLineOrder;
    }

    public String getProductionGroupDesc() {
        return productionGroupDesc;
    }

    public void setProductionGroupDesc(String productionGroupDesc) {
        this.productionGroupDesc = productionGroupDesc;
    }
}
