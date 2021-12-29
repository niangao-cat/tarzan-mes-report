package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * HmeProdLinePassRateVO
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:38:45
 **/
@Data
@ExcelSheet(zh = "投料报表")
public class HmeInputRecordVO implements Serializable {

    private static final long serialVersionUID = -7128004082599408707L;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号", order = 0)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本", order = 1)
    private String productionVersion;

    @ApiModelProperty("工单物料编码")
    @ExcelColumn(zh = "工单物料编码", order = 2)
    private String woMaterialCode;

    @ApiModelProperty("工单物料描述")
    @ExcelColumn(zh = "工单物料描述", order = 3)
    private String woMaterialName;

    @ApiModelProperty("工单数量")
    @ExcelColumn(zh = "工单数量", order = 4)
    private BigDecimal qty;

    @ApiModelProperty("执行作业编码")
    @ExcelColumn(zh = "执行作业编码", order = 5)
    private String eoNum;

    @ApiModelProperty("工序")
    @ExcelColumn(zh = "工序", order = 6)
    private String processName;

    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码", order = 7)
    private String workcellCode;

    @ApiModelProperty("用户")
    @ExcelColumn(zh = "用户", order = 8)
    private String loginName;

    @ApiModelProperty("姓名")
    @ExcelColumn(zh = "姓名", order = 9)
    private String realName;

    @ApiModelProperty("操作平台")
    @LovValue(lovCode = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    private String jobType;

    @ApiModelProperty("操作平台")
    @ExcelColumn(zh = "操作平台", order = 10)
    private String jobTypeMeaning;

    @ApiModelProperty("SN编码")
    @ExcelColumn(zh = "SN编码", order = 11)
    private String identification;

    @ApiModelProperty("投料物料")
    @ExcelColumn(zh = "投料物料", order = 12)
    private String materialCode;

    @ApiModelProperty("物料描述")
    @ExcelColumn(zh = "物料描述", order = 13)
    private String materialName;

    @ApiModelProperty("物料版本")
    @ExcelColumn(zh = "物料版本", order = 14)
    private String materialVersion;

    @ApiModelProperty("BOM用量")
    @ExcelColumn(zh = "BOM用量", order = 15)
    private BigDecimal bomQty;

    @ApiModelProperty("组件损耗率")
    @ExcelColumn(zh = "组件损耗率", order = 16)
    private String attritionChance;

    @ApiModelProperty("单位")
    @ExcelColumn(zh = "单位", order = 17)
    private String uomCode;

    @ApiModelProperty("供应商批次")
    @ExcelColumn(zh = "供应商批次", order = 18)
    private String lot;

    @ApiModelProperty("投料编码")
    @ExcelColumn(zh = "投料编码", order = 19)
    private String materialLotCode;

    @ApiModelProperty("投料数量")
    @ExcelColumn(zh = "投料数量", order = 20)
    private BigDecimal releaseQty;

    @ApiModelProperty("投料时间")
    private Date creationDate;

    @ApiModelProperty("投料时间")
    @ExcelColumn(zh = "投料时间", order = 21)
    private String creationDateStr;

    @ApiModelProperty("物料类型")
    @ExcelColumn(zh = "物料类型", order = 22)
    private String attrValue;

    @ApiModelProperty("物料类型")
    private String attribute14;

    @ApiModelProperty("物料类型")
    private String attribute1;

    @ApiModelProperty("是否已投")
    @ExcelColumn(zh = "是否已投", order = 23)
    private String yN;

    @ApiModelProperty("虚拟件标识")
    @ExcelColumn(zh = "虚拟件标识", order = 24)
    private String virtualFlag;

    @ApiModelProperty("剩余数量")
    @ExcelColumn(zh = "剩余数量", order = 25)
    private BigDecimal primaryUomQty;

    @ApiModelProperty("工位ID")
    private String workcellId;

}
