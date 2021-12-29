package com.ruike.hme.domain.valueobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:12
 */
@Data
public class NcRecordVO implements Serializable {
    private static final long serialVersionUID = 2934128775379654944L;

    @ApiModelProperty("唯一标识，表ID，主键，供其他表做外键")
    private String ncRecordId;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "EO")
    private String eoId;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "物料批")
    private String materialLotId;

    @ApiModelProperty(value = "工作单元")
    private String workcellId;

    @ApiModelProperty(value = "父不良记录")
    private String parentNcRecordId;

    @ApiModelProperty(value = "产生问题的源工作单元")
    private String rootCauseWorkcellId;

    @ApiModelProperty(value = "备注")
    private String comments;

}
