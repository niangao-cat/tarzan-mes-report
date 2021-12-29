package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeProcessCollectVO3 implements Serializable {
    private static final long serialVersionUID = 6658723347007984433L;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "表名")
    private String tableName;
}
