package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HmeExportTaskVO implements Serializable {
    @ApiModelProperty(value = "导出任务ID")
    private String taskId;

    @ApiModelProperty(value = "导出任务")
    private String taskCode;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "服务名")
    private String serviceName;

    @ApiModelProperty(value = "实例地址")
    private String hostName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "任务异常信息")
    private String errorInfo;

    @ApiModelProperty(value = "任务状态")
    private String state;

    @ApiModelProperty(value = "任务结束时间")
    private Date endDateTime;

    @ApiModelProperty(value = "")
    private String loginName;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新人")
    private Long lastUpdatedBy;
}
