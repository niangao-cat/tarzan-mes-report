package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 11:09
 */
@Data
public class HmeModProductionLineVO implements Serializable {

    private static final long serialVersionUID = 496195159376970941L;

    @ApiModelProperty(value = "产线id")
    private String prodLineId;
    @ApiModelProperty(value = "生产线编号")
    private String prodLineCode;
    @ApiModelProperty(value = "生产线名称")
    private String prodLineName;
    @ApiModelProperty(value = "生产线描述")
    private String description;
    @ApiModelProperty(value = "生产线类型，区分生产线类型为自有、外协或是采购")
    private String prodLineType;

}
