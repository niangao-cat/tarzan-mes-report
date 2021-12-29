package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGapDTO2
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-24 16:29
 **/
@Data
public class WmsDistributionGapDTO2 implements Serializable {

    private static final long serialVersionUID = -8352313845060569446L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "冻结标志")
    private String freezeFlag;

    @ApiModelProperty(value = "货位类型")
    private String locatorType;

    @ApiModelProperty(value = "库存")
    private BigDecimal qty;

}
