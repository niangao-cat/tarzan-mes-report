package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/20 11:02
 */
@Data
public class HmeCosInNcRecordVO implements Serializable {

    private static final long serialVersionUID = 2178453965686762781L;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeName;

    @ApiModelProperty(value = "位置")
    private String position;

    @ApiModelProperty(value = "行")
    @JsonIgnore
    private Long ncLoadRow;

    @ApiModelProperty(value = "列")
    @JsonIgnore
    private Long ncLoadColumn;
}
