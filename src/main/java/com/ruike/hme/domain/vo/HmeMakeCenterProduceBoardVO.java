package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:21
 */
@Data
public class HmeMakeCenterProduceBoardVO implements Serializable {

    private static final long serialVersionUID = 3066885556727595470L;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("产线")
    private String prodLineId;

    @ApiModelProperty("产线集合")
    private List<String> prodLineIdList;

    @ApiModelProperty("制造部")
    private String areaId;

    @ApiModelProperty("制造部编码")
    private String areaCode;

    public void initParam() {
        prodLineIdList = StringUtils.isNotBlank(prodLineId) ? Arrays.asList(StringUtils.split(prodLineId, ",")) : null;
    }
}
