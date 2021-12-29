package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/9 19:43
 */
@Data
public class HmeAfterSalesQuotationVO2 implements Serializable {

    private static final long serialVersionUID = 8124111320180402895L;

    @ApiModelProperty("头ID")
    private String quotationHeaderId;
    @ApiModelProperty("需求类型")
    private String demandType;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("'数量'")
    private String requsetQty;
    @ApiModelProperty("'备注'")
    private String remark;
}
