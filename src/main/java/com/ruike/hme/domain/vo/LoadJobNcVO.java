package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 芯片装载作业 不良代码
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 10:18
 */
@Data
public class LoadJobNcVO {
    @ApiModelProperty(value = "装载作业ID")
    private String loadJobId;
    @ApiModelProperty(value = "不良代码")
    private String ncCode;
    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeDescription;
}
