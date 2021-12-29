package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/6 9:42
 */
@Data
public class HmeModSiteVO implements Serializable {

    private static final long serialVersionUID = 928517037074806285L;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("站点")
    private String siteId;
}
