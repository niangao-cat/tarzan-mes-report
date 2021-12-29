package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 不良情况
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:44
 */
@Data
public class QmsProductQualityInspectionNcRecordVO {
    @ApiModelProperty(value = "检验员")
    private String realName;
    @ApiModelProperty(value = "型号")
    private String itemGroupDescription;
    @ApiModelProperty(value = "SN编码")
    private String identification;
    @ApiModelProperty(value = "不良分类")
    private String description;
    @ApiModelProperty(value = "检验人员")
    private Long createdBy;
    @ApiModelProperty(value = "备注")
    private String comments;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @ApiModelProperty(value = "eo_id")
    private String eoId;
}
