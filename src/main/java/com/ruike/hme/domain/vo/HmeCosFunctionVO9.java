package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeCosFunctionVO9
 * @Description TODO
 * @Author penglin.sui@hand-china.com
 * @Date 2021/07/26 19:34
 * @Version 1.0
 **/
@Data
public class HmeCosFunctionVO9 implements Serializable {
    private static final long serialVersionUID = -1670743332055184243L;

    @ApiModelProperty(value = "测试设备")
    private String testEquipment;

    @ApiModelProperty(value = "测试模式")
    private String a09;

    @ApiModelProperty(value = "阈值电流")
    private BigDecimal a010;

    @ApiModelProperty(value = "阈值电压")
    private BigDecimal a011;

    @ApiModelProperty(value = "测试电流")
    private String current;

    @ApiModelProperty(value = "电压")
    private BigDecimal a06;

    @ApiModelProperty(value = "功率")
    private BigDecimal a02;

    @ApiModelProperty(value = "中心波长")
    private BigDecimal a04;

    @ApiModelProperty(value = "SE")
    private BigDecimal a012;

    @ApiModelProperty(value = "线宽")
    private BigDecimal a013;

    @ApiModelProperty(value = "WPE")
    private BigDecimal a014;

    @ApiModelProperty(value = "波长差")
    private BigDecimal a05;

    @ApiModelProperty(value = "透镜功率")
    private BigDecimal a22;

    @ApiModelProperty(value = "PBS功率")
    private BigDecimal a23;

    @ApiModelProperty(value = "偏振度数")
    private BigDecimal a15;

    @ApiModelProperty(value = "X半宽高")
    private BigDecimal a16;

    @ApiModelProperty(value = "X86能量宽度")
    private BigDecimal a18;

    @ApiModelProperty(value = "Y86能量宽度")
    private BigDecimal a19;

    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal a17;

    @ApiModelProperty(value = "X95能量宽度")
    private BigDecimal a20;

    @ApiModelProperty(value = "Y95能量宽度")
    private BigDecimal a21;

    @ApiModelProperty(value = "功率等级")
    private String a01;

    @ApiModelProperty(value = "波长等级")
    private String a03;

    @ApiModelProperty(value = "电压等级")
    private String a27;

    @ApiModelProperty(value = "不良描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String a26;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "是否不良")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "ncFlagMeaning")
    private String ncFlag;

    @ApiModelProperty(value = "是否不良含义")
    private String ncFlagMeaning;

    @ApiModelProperty(value = "测试操作人ID")
    private String userId;

    @ApiModelProperty(value = "测试操作人")
    private String realName;

    @ApiModelProperty(value = "测试时间")
    private String creationDate;

    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
}
