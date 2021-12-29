package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateVO
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:38:45
 **/
@Data
public class HmeProdLinePassRateVO implements Serializable {
    private static final long serialVersionUID = 1014012147134104451L;

    @ApiModelProperty("时间段")
    private String dateSlot;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线名称")
    private String prodLineName;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("物料数据")
    private List<HmeProdLinePassRateVO2> materialData;
}
