package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocInfoVO
 *
 * @author: chaonan.hu@hand-china.com 2021/3/10 09:19:23
 **/
@Data
public class HmeWipStocktakeDocInfoVO implements Serializable {
    private static final long serialVersionUID = 6012614423323146092L;

    @ApiModelProperty("盘点单ID")
    private String stocktakeId;

    @ApiModelProperty("盘点单号")
    private String stocktakeNum;

    @ApiModelProperty("扩展字段1")
    private String attribute1;
}
