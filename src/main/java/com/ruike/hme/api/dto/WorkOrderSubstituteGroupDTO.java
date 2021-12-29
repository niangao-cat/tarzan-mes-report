package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询物料替代组
 *
 * @return java.util.List<com.ruike.hme.api.dto.WorkOrderSubstituteGroupDTO>
 * @author xin.t@raycuslaser.com 2021/9/18
 */
@Data
public class WorkOrderSubstituteGroupDTO implements Serializable {
    private static final long serialVersionUID = -1566617299999958383L;

    @ApiModelProperty("物料号")
    private String materialCode;

    @ApiModelProperty("主物料号")
    private String mainMaterialCode;

    @ApiModelProperty("替代组")
    private String SubstituteGroup;
}
