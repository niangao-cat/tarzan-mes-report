package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 工序作业与设备
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 17:15
 */
@Data
public class JobEquipmentVO {
    @ApiModelProperty(value = "作业ID")
    private String jobId;
    @ApiModelProperty(value = "来源作业ID")
    private String sourceJobId;
    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备名称")
    private String assetName;
}
