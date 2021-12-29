package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 16:07
 */
@Data
public class WmsFinishWarehouseVO2 implements Serializable {

    private static final long serialVersionUID = 2491011322246434408L;

    @ApiModelProperty(value = "总页数")
    private int totalPages;
    @ApiModelProperty(value = "总大小")
    private long totalElements;
    @ApiModelProperty(value = "当前页数量")
    private int numberOfElements;
    @ApiModelProperty(value = "大小")
    private int size;
    @ApiModelProperty(value = "页码")
    private int number;
    @ApiModelProperty(value = "内容")
    private List<WmsFinishWarehouseVO> content;
    @ApiModelProperty(value = "完工汇总")
    private BigDecimal finishSumQty;
    @ApiModelProperty(value = "入库汇总")
    private BigDecimal warehousingSumQty;
}
