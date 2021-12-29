package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 不良eo
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:34
 */
@Data
public class QmsProductQualityInspectionNcEoVO {
    @ApiModelProperty(value = "物料批")
    private String materialLotId;
    @ApiModelProperty(value = "发生不良的装配件")
    private String materialId;
    @ApiModelProperty(value = "eo_id")
    private String eoId;
    @ApiModelProperty(value = "NC状态，打开/关闭/取消")
    private String ncStatus;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creationDate;
    @ApiModelProperty(value = "备注")
    private String commnets;
    @ApiModelProperty(value = "型号")
    private String itemGroupDescription;
    @ApiModelProperty(value = "型号编码")
    private String itemGroupCode;
}
