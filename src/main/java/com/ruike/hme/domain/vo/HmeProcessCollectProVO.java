package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 工序采集项报表 明细展现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 09:30
 */
@Data
public class HmeProcessCollectProVO implements Serializable {
    private static final long serialVersionUID = 5584734338525044055L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "采集项名称")
    private String proName;

    @ApiModelProperty(value = "采集项编码")
    private String proCode;

    @ApiModelProperty(value = "采集项描述")
    private String proResult;

    @ApiModelProperty(value = "标签编码")
    private String tagCode;
}
