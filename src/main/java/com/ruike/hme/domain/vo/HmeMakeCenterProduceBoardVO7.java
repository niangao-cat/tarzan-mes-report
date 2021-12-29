package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/31 15:54
 */
@Data
public class HmeMakeCenterProduceBoardVO7 implements Serializable {

    private static final long serialVersionUID = -323830007832711323L;

    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料类型")
    private String materialType;
    @ApiModelProperty("产品描述")
    private String materialName;
    @ApiModelProperty("产品-产品描述")
    private String yAxis;
    @ApiModelProperty("月度计划ID")
    private String monthlyPlanId;
}
