package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeEmployeeAttendanceDTO17 implements Serializable {
    private static final long serialVersionUID = -2318635752069551458L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("版本")
    private String materialVersion;

    @ApiModelProperty("工序ID")
    private String processId;

    @ApiModelProperty("开始时间")
    private String dateFrom;

    @ApiModelProperty("结束时间")
    private String dateTo;

    @ApiModelProperty("类型")
    private String type;
}
