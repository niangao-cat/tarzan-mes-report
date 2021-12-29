package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmeWoPlanRateReportVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 15:15
 * @Version 1.0
 **/
@Data
public class HmeWoPlanRateReportVO4 implements Serializable {

    private static final long serialVersionUID = 9135463087693627113L;
    @ApiModelProperty(value = "工段Id")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;

    @ApiModelProperty(value = "工段名称")
    private String workcellName;

    @ApiModelProperty(value = "工段顺序")
    private String workcellSequence;

    public HmeWoPlanRateReportVO4(String workcellId, String workcellCode, String workcellName, String workcellSequence) {
        this.workcellCode = workcellCode;
        this.workcellId = workcellId;
        this.workcellName = workcellName;
        this.workcellSequence = workcellSequence;
    }
}
