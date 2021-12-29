package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备故障监控
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 10:05
 */
@Data
@ExcelSheet(zh = "设备故障监控报表")
public class HmeEquipmentFaultMonitorVO implements Serializable {

    private static final long serialVersionUID = -7211974927460040461L;

    @ApiModelProperty(value = "资产名称")
    @ExcelColumn(zh = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产编号")
    @ExcelColumn(zh = "资产编号")
    private String assetEncoding;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号")
    private String model;

    @ApiModelProperty(value = "序列号")
    @ExcelColumn(zh = "序列号")
    private String equipmentBodyNum;

    @ApiModelProperty(value = "使用部门")
    @ExcelColumn(zh = "使用部门")
    private String areaName;

    @ApiModelProperty(value = "存放地方")
    @ExcelColumn(zh = "存放地方")
    private String location;

    @ApiModelProperty(value = "使用人")
    @ExcelColumn(zh = "使用人")
    private String user;

    @ApiModelProperty(value = "异常描述")
    @ExcelColumn(zh = "异常描述")
    private String exceptionName;

    @ApiModelProperty(value = "异常发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "异常发生时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date creationDate;

    @ApiModelProperty(value = "异常关闭时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "异常关闭时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date closeTime;

    @ApiModelProperty(value = "异常时间")
    @ExcelColumn(zh = "异常时间")
    private BigDecimal exceptionTime;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注")
    private String respondRemark;

}
