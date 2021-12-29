package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeWoPlanRateReportVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 15:15
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportVO5 implements Serializable {

    private static final long serialVersionUID = -4808405585021233768L;
    @ApiModelProperty(value = "工段Id")
    private String workcellId;

    @ApiModelProperty(value = "日期")
    private String shiftDate;

    @ApiModelProperty(value = "班组")
    private String shiftCode;

    @ApiModelProperty(value = "工单")
    private String workOrderId;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

}
