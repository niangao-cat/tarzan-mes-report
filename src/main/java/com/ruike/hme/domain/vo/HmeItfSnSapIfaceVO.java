package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description itf_sn_sap_iface 接口表
 *
 * @author wengang.qiang@hand-china.com 2021/10/15 11:18
 */
@Data
public class HmeItfSnSapIfaceVO implements Serializable {

    private static final long serialVersionUID = -4636442094934036765L;

    @ApiModelProperty(value = "序列号")
    private String sernr;

    @ApiModelProperty(value = "序列号状态")
    private String sttxt;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;


}
