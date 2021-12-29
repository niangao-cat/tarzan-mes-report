package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/28 0:41
 */
@Data
public class HmeInventoryEndProductVO5 implements Serializable {

    private static final long serialVersionUID = -2419504176578728245L;

    @ApiModelProperty(value = "单据id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据单据")
    private String instructionDocNum;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

}
