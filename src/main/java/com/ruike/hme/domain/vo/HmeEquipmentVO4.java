package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 10:50
 */
@Data
public class HmeEquipmentVO4 implements Serializable {
    private static final long serialVersionUID = -4736512636207818495L;
    @ApiModelProperty(value = "工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "设备名称")
    private String assetName;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
}
