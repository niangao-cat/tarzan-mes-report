package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 17:11
 */
@Data
public class WmsFinishWarehouseVO3 implements Serializable {

    private static final long serialVersionUID = 7899200427396245882L;

    @ApiModelProperty("条码")
    private String materialLotId;
    @ApiModelProperty("实绩数量")
    private BigDecimal actualQty;
}
