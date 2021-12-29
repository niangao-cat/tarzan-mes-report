package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/28 19:30
 */
@Data
public class WmsDistributionGapDTO4 implements Serializable {

    private static final long serialVersionUID = 6504029058839849026L;

    @ApiModelProperty("班次")
    private String shiftCode;
    @ApiModelProperty("工位")
    private String workcellId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("数量")
    private BigDecimal qty;
}
