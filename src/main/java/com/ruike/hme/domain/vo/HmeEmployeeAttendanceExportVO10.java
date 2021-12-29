package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeEmployeeAttendanceExportVO10 implements Serializable {

    private static final long serialVersionUID = -4694098172818279950L;

    @ApiModelProperty(value = "执行作业ID")
    private String eoId;

    @ApiModelProperty(value = "用户ID")
    private String siteInBy;

    @ApiModelProperty(value = "物料ID")
    private String snMaterialId;

    @ApiModelProperty(value = "物料版本")
    private String productionVersion;

    @ApiModelProperty(value = "工序ID")
    private String parentOrganizationId;
}
