package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateVO6
 *
 * @author: chaonan.hu@hand-china.com 2021-03-02 10:07:34
 **/
@Data
public class HmeProdLinePassRateVO6 implements Serializable {
    private static final long serialVersionUID = 1016432147134104451L;

    @ApiModelProperty("时间")
    private String date;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线名称")
    private String prodLineName;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("班次数据")
    private List<HmeProdLinePassRateVO7> shiftData;
}
