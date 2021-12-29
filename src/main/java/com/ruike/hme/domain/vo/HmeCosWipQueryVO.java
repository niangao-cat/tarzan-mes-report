package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 16:32
 */
@Data
@ExcelSheet(zh = "COS履历")
public class HmeCosWipQueryVO implements Serializable {

    private static final long serialVersionUID = -7968065824857905819L;

    @ApiModelProperty("工厂")
    private String siteName;
    @ApiModelProperty(value = "工单Id")
    private String workOrderId;
    @ApiModelProperty(value = "工单编号")
    @ExcelColumn(zh = "工单编号", order = 0)
    private String workOrderNum;
    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码", order = 1)
    private String productionCode;
    @ApiModelProperty(value = "产品名称")
    @ExcelColumn(zh = "产品描述", order = 2)
    private String productionName;
    @ApiModelProperty(value = "芯片盒子")
    @ExcelColumn(zh = "芯片盒子", order = 3)
    private String materialLotCode;
    @ApiModelProperty(value = "wafer")
    @ExcelColumn(zh = "WAFER", order = 4)
    private String waferNum;
    @ApiModelProperty(value = "芯片类型编码")
    @ExcelColumn(zh = "芯片类型编码", order = 5)
    private String cosType;
    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码", order = 6)
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述", order = 7)
    private String materialName;
    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量", order = 8)
    private String primaryUomQty;
    @ApiModelProperty(value = "仓库")
    private String parentLocatorCode;
    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位", order = 9)
    private String locatorCode;
    @ApiModelProperty(value = "工艺步骤加工次数")
    @ExcelColumn(zh = "工艺步骤加工次数", order = 11)
    private String eoStepNum;
    @ApiModelProperty(value = "当前工位")
    @ExcelColumn(zh = "当前工位", order = 12)
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位名称", order = 13)
    private String workcellName;

    @ApiModelProperty(value = "设备编码")
    @ExcelColumn(zh = "设备编码", order = 10)
    private String assetEncoding;
    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @LovValue(value = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    @ApiModelProperty(value = "作业平台")
    private String jobType;

    @ApiModelProperty(value = "作业平台含义")
    @ExcelColumn(zh = "作业平台", order = 14)
    private String jobTypeMeaning;

    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;
    @ApiModelProperty(value = "进站时间")
    @ExcelColumn(zh = "进站时间", order = 15)
    private String siteInDateStr;
    @ApiModelProperty(value = "进站人员ID")
    private Long siteInById;
    @ApiModelProperty(value = "进站人员")
    @ExcelColumn(zh = "进站人员", order = 16)
    private String siteInBy;
    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;
    @ApiModelProperty(value = "出站时间")
    @ExcelColumn(zh = "出站时间", order = 17)
    private String siteOutDateStr;
    @ApiModelProperty(value = "出站人员")
    @ExcelColumn(zh = "出站人员", order = 18)
    private String siteOutBy;
    @ApiModelProperty(value = "出站人员ID")
    private Long siteOutById;
    @ApiModelProperty(value = "是否返修标识")
    @ExcelColumn(zh = "是否返修标识", order = 19)
    private String reworkflag;
}
