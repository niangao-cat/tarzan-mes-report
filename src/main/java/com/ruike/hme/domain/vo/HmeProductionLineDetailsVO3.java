package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes->HmeProductionLineDetailsVO3
 * @description:
 * @author: chaonan.hu@hand-china.com 2021/04/25 14:35:12
 **/
@Data
public class HmeProductionLineDetailsVO3 implements Serializable {
    private static final long serialVersionUID = 7104670661419773654L;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "在制合计")
    private BigDecimal totalQty;
}
