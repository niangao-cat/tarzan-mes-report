package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 物料批 实验代码
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 10:23
 */
@Data
public class MaterialLotLabCodeVO {
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "序列号")
    private String loadSequence;
    @ApiModelProperty(value = "实验代码")
    private String labCode;
}
