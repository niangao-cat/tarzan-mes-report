package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/7 16:29
 */
@Data
public class HmeMakeCenterProduceBoardVO17 implements Serializable {

    private static final long serialVersionUID = -8077591197704163123L;

    @ApiModelProperty("产品组编码")
    private String productionGroupCode;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("产线-产品组编码")
    private String yAxis;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料类型")
    private String materialType;
}
