package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验员检验情况
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:44
 */
@Data
public class QmsProductQualityInspectionTypeVO {
    @ApiModelProperty(value = "检验型号")
    private List<String> typeNames;
    @ApiModelProperty(value = "检验数量")
    private List<Long> inspectionQuantitys;
    @ApiModelProperty(value = "不良数量")
    private List<Long> inspectionBadQuantitys;
    @ApiModelProperty(value = "合格数量")
    private List<Long> standardQuantitys;
}
