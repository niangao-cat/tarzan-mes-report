package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:34
 */
@Data
public class QmsProductQualityInspectionVO {
    @ApiModelProperty(value = "检验员检验情况")
    private QmsProductQualityInspectionUserVO productQualityInspectionUserVO;
    @ApiModelProperty(value = "各型号检验情况")
    private QmsProductQualityInspectionTypeVO qmsProductQualityInspectionTypeVO;
}
