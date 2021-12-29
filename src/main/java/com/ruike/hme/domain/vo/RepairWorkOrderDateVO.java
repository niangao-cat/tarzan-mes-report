package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 维修订单时间
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:40
 */
@Data
public class RepairWorkOrderDateVO {
    @ApiModelProperty(value = "接收SN")
    private String snNum;

    @ApiModelProperty(value = "维修订单号")
    private String repairWorkOrderNum;

    @ApiModelProperty(value = "维修订单类型")
    private String internalOrderType;

    @ApiModelProperty(value = "维修订单类型含义")
    private String internalOrderTypeMeaning;

    @ApiModelProperty(value = "SAP创建时间")
    private Date sapCreationDate;

    @ApiModelProperty(value = "MES接收时间")
    private Date mesReceiveDate;
}
