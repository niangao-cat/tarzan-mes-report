package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 21:21
 */
@Data
public class PendingNcQueryVO {
    @ApiModelProperty("工序ID")
    private String processId;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
}
