package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeEmployeeAttendanceExportVO13 implements Serializable {

    private static final long serialVersionUID = 863272414598575401L;

    @ApiModelProperty("不良记录ID")
    private String parentNcRecordId;

    @ApiModelProperty("不良记录ID")
    private String ncCodeId;

    @ApiModelProperty("不良记录描述")
    private String ncCodeDescription;
}
