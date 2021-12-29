package com.ruike.hme.domain.vo;

import io.choerodon.mybatis.annotation.Cid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/6 14:14
 */
@Data
public class HmeCosFunctionVO implements Serializable {

    private static final long serialVersionUID = -1891758845863687955L;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String cosFunctionId;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "组织id")
    private String siteId;
    @ApiModelProperty(value = "电流")
    private String current;
    @ApiModelProperty(value = "功率等级")
    private String a01;
    @ApiModelProperty(value = "功率/w")
    private BigDecimal a02;
    @ApiModelProperty(value = "波长等级")
    private String a03;
    @ApiModelProperty(value = "波长/nm")
    private BigDecimal a04;
    @ApiModelProperty(value = "波长差/nm")
    private BigDecimal a05;
    @ApiModelProperty(value = "电压/V")
    private BigDecimal a06;
    @ApiModelProperty(value = "光谱宽度(单点)")
    private BigDecimal a07;
    @ApiModelProperty(value = "")
    private String a08;
    @ApiModelProperty(value = "")
    private String a09;
    @ApiModelProperty(value = "")
    private BigDecimal a010;
    @ApiModelProperty(value = "")
    private BigDecimal a011;
    @ApiModelProperty(value = "")
    private BigDecimal a012;
    @ApiModelProperty(value = "")
    private BigDecimal a013;
    @ApiModelProperty(value = "")
    private BigDecimal a014;
    @ApiModelProperty(value = "")
    private BigDecimal a15;
    @ApiModelProperty(value = "")
    private BigDecimal a16;
    @ApiModelProperty(value = "")
    private BigDecimal a17;
    @ApiModelProperty(value = "")
    private BigDecimal a18;
    @ApiModelProperty(value = "")
    private BigDecimal a19;
    @ApiModelProperty(value = "")
    private BigDecimal a20;
    @ApiModelProperty(value = "")
    private BigDecimal a21;
    @ApiModelProperty(value = "")
    private BigDecimal a22;
    @ApiModelProperty(value = "")
    private BigDecimal a23;
    @ApiModelProperty(value = "")
    private String a24;
    @ApiModelProperty(value = "")
    private String a25;
    @ApiModelProperty(value = "")
    private String a26;
    @ApiModelProperty(value = "")
    private String a27;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
}
