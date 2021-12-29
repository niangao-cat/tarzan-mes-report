package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 芯片装载作业 设备
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 10:21
 */
@Data
public class LoadJobEquipmentVO {
    @ApiModelProperty(value = "装载作业ID")
    private String loadJobId;
    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备描述")
    private String assetName;
}
