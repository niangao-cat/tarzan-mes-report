package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/6 18:57
 */
@Data
public class HmeCosCompletionVO2 implements Serializable {

    private static final long serialVersionUID = 4780194318507449852L;

    @ApiModelProperty(value = "筛选规则")
    private String cosRuleCode;

    @ApiModelProperty(value = "采集项")
    private String collectionItem;

    @ApiModelProperty(value = "电流")
    private String current;

}
