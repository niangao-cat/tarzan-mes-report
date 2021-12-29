package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/28 10:32
 */
@Data
public class WmsDistributionGapDTO3 implements Serializable {

    private static final long serialVersionUID = 6036366004266790432L;

    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("产线集合")
    private List<String> prodLineIdList;

    public void initParam() {
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(StringUtils.split(prodLineId, ",")) : null;
    }
}
