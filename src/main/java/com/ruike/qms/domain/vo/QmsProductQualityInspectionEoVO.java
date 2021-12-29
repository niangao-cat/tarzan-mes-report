package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 所有eo
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:34
 */
@Data
public class QmsProductQualityInspectionEoVO {
    @ApiModelProperty(value = "eoId")
    private String eoId;
    @ApiModelProperty(value = "出站人员Id")
    private Long siteOutBy;
}
