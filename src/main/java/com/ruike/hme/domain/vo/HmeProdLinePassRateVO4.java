package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeProdLinePassRateVO4
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 17:37:13
 **/
@Data
public class HmeProdLinePassRateVO4 implements Serializable {
    private static final long serialVersionUID = 1013412162936504451L;

    @ApiModelProperty(value = "父不良记录Id")
    private String ncRecordId;

    @ApiModelProperty(value = "identification")
    private String identification;

    @ApiModelProperty(value = "发现工位Id")
    private String foundWorkcellId;

    @ApiModelProperty(value = "发现工位")
    private String foundWorkcellName;

    @ApiModelProperty(value = "责任工位Id")
    private String responsedWorkcellId;

    @ApiModelProperty(value = "责任工位")
    private String responsedWorkcellName;

    @ApiModelProperty(value = "不良分类")
    private String ncType;

    @ApiModelProperty(value = "不良分类含义")
    private String ncTypeMeaning;

    @ApiModelProperty(value = "不良类型")
    private String description;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良状态")
    private String ncStatus;

    @ApiModelProperty(value = "不良状态含义")
    private String ncStatusMeaning;

    @ApiModelProperty(value = "不良物料条码")
    private String materialLotCode;

    @ApiModelProperty(value = "不良物料编码Id")
    private String materialId;

    @ApiModelProperty(value = "不良物料编码")
    private String materialName;

    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "处理意见")
    private String disposeOpinion;

    @ApiModelProperty(value = "记录人Id")
    private String userId;

    @ApiModelProperty(value = "记录人")
    private String userName;

    @ApiModelProperty(value = "记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    @ApiModelProperty(value = "处理人Id")
    private String closedUserId;

    @ApiModelProperty(value = "处理人")
    private String closedUserName;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closedDateTime;

}
