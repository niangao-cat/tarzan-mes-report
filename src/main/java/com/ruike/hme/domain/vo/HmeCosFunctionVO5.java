package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeCosFunctionVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/05/10 14:15:21
 **/
@Data
public class HmeCosFunctionVO5 implements Serializable {
    private static final long serialVersionUID = 8949614720309752442L;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
}
