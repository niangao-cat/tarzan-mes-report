package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeEmployeeAssignDTO implements Serializable {
    private static final long serialVersionUID = 3843736273796769631L;
    @ApiModelProperty("员工编码")
    private String employeeNum;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("资质Id")
    private String qualityId;

    @ApiModelProperty("资质类型")
    private String qualityType;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("资质熟练度")
    private String proficiency;

    @ApiModelProperty("有效时间起")
    private String dateFrom;

    @ApiModelProperty("有效时间止")
    private String dateTo;

}
