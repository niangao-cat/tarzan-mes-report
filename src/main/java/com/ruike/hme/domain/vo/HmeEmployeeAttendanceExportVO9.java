package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEmployeeAttendanceExportVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/07/19 10:26
 **/
@Data
public class HmeEmployeeAttendanceExportVO9 implements Serializable {
    private static final long serialVersionUID = 7253182134739980751L;

    @ApiModelProperty(value = "子组织ID")
    private String organizationId;

    @ApiModelProperty(value = "父组织ID")
    private String parentOrganizationId;

    @ApiModelProperty(value = "父组织编码")
    private String parentOrganizationCode;

    @ApiModelProperty(value = "父组织名称")
    private String parentOrganizationName;

    @ApiModelProperty(value = "数量")
    private Integer qtyInt;
}
