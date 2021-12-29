package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/31 13:51
 */
@Data
public class HmeMakeCenterProduceBoardVO5 implements Serializable {

    private static final long serialVersionUID = -6658605186117609570L;

    @ApiModelProperty("纵轴")
    private List<String> yAxisList;
    @ApiModelProperty("横轴")
    private List<HmeMakeCenterProduceBoardVO6> xAxisList;
    @ApiModelProperty("当月达成率")
    private BigDecimal  monthRate;


}
