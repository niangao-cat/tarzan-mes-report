package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 物料批实验代码
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 17:15
 */
@Data
public class SnStepLabCodeVO {
    @ApiModelProperty("物料批")
    private String materialLotId;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("实验代码")
    private String labCode;

    public SnStepLabCodeVO() {
    }

    public SnStepLabCodeVO(String materialLotId, String routerStepId) {
        this.materialLotId = materialLotId;
        this.routerStepId = routerStepId;
    }
}
