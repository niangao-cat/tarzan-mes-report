package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/3 9:58
 */
@Data
public class HmeMakeCenterProduceBoardVO15 implements Serializable {

    private static final long serialVersionUID = 698980829704161631L;

    @ApiModelProperty("巡检不良率")
    private String inspectionNcRate;
    @ApiModelProperty("巡检不良列表")
    private List<HmeMakeCenterProduceBoardVO16> inspectionNcList;
}
