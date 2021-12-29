package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 设备故障监控
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 10:04
 */
@Data
public class HmeEquipmentFaultMonitorDTO implements Serializable {

    private static final long serialVersionUID = -8281426873410310439L;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "设备ID列表")
    @JsonIgnore
    private List<String> equipmentIdList;

    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;

    @ApiModelProperty(value = "使用部门")
    private String areaId;

    @ApiModelProperty(value = "存放地方")
    private String location;

    @ApiModelProperty(value = "异常描述")
    private String exceptionId;

    @ApiModelProperty(value = "异常发生时间从")
    private String creationStartTime;

    @ApiModelProperty(value = "异常发生时间至")
    private String creationEndTime;

    @ApiModelProperty(value = "异常关闭时间从")
    private String closeStartTime;

    @ApiModelProperty(value = "异常关闭时间至")
    private String closeEndTime;

    @ApiModelProperty(value = "设备异常状态")
    private String equipmentExceptionStatus;

    public void initParam() {
        this.equipmentIdList = StringUtils.isBlank(equipmentId) ? null : Arrays.asList(StringUtils.split(equipmentId, ","));
    }
}
