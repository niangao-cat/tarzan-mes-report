package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosFunctionVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/05/10 14:15:21
 **/
@Data
public class HmeCosFunctionVO3 implements Serializable {
    private static final long serialVersionUID = -2805554303488756582L;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

    @ApiModelProperty(value = "实验代码")
    private String labCode;
}
