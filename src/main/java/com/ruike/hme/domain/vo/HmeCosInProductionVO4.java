package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/24 21:27
 */
@Data
public class HmeCosInProductionVO4 implements Serializable {

    private static final long serialVersionUID = 5045283814419154997L;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

    @ApiModelProperty(value = "EO")
    private String workcellId;

    @ApiModelProperty(value = "不良记录")
    private String cosNcRecordId;
}
