package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 16:31
 */
@Data
public class HmeTagCheckVO3 implements Serializable {

    private static final long serialVersionUID = 480483368691928239L;

    @ApiModelProperty("物料组")
    private String itemGroupId;
    @ApiModelProperty("来源工序ID")
    private String sourceWorkcellId;
    @ApiModelProperty("数据项Id")
    private String tagId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty("工序编码")
    private String processCode;
    @ApiModelProperty("工序描述")
    private String processName;
}
