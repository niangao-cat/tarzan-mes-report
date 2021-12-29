package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName HmeDocSummaryQueryVO
 * @Description
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:27
 * @Version 1.0
 **/
@Data
@ExcelSheet(zh = "单据汇总查询报表")
public class HmeDocSummaryQueryVO implements Serializable {
    private static final long serialVersionUID = 8454318175347797379L;

    @ApiModelProperty("单据类型编码")
    @LovValue(value = "HME.INSTRUCTION_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("单据类型含义")
    @ExcelColumn(zh = "单据类型", order = 0)
    private String instructionDocTypeMeaning;

    @ApiModelProperty("单据号")
    @ExcelColumn(zh = "单据号", order = 1)
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    @LovValue(value = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态")
    @ExcelColumn(zh = "单据状态", order = 2)
    private String instructionDocStatusMeaning;

    @ApiModelProperty("成本中心")
    @ExcelColumn(zh = "成本中心", order = 3)
    private String costcenterCode;

    @ApiModelProperty("成本中心描述")
    @ExcelColumn(zh = "成本中心描述", order = 4)
    private String description;

    @ApiModelProperty("备注")
    @ExcelColumn(zh = "备注", order = 5)
    private String remark;

    @ApiModelProperty("单据行号")
    @ExcelColumn(zh = "单据行号", order = 6)
    private String lineNum;

    @ApiModelProperty("行类型")
    @LovValue(value = "HME.INSTRUCTION_TYPE", meaningField = "instructionTypeMeaning")
    private String instructionType;

    @ApiModelProperty("行类型")
    @ExcelColumn(zh = "行类型", order = 7)
    private String instructionTypeMeaning;

    @ApiModelProperty("行状态")
    @LovValue(value = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态")
    @ExcelColumn(zh = "行状态", order = 8)
    private String instructionStatusMeaning;

    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码", order = 9)
    private String materialCode;

    @ApiModelProperty("物料名称")
    @ExcelColumn(zh = "物料名称", order = 10)
    private String materialName;

    @ApiModelProperty("物料版本")
    @ExcelColumn(zh = "物料版本", order = 11)
    private String materialVersion;

    @ApiModelProperty("单位")
    @ExcelColumn(zh = "单位", order = 12)
    private String uomCode;

    @ApiModelProperty("物料组")
    @ExcelColumn(zh = "物料组", order = 13)
    private String itemGroup;

    @ApiModelProperty("物料组描述")
    @ExcelColumn(zh = "物料组描述", order = 14)
    private String itemGroupDescription;

    @ApiModelProperty("需求数量")
    @ExcelColumn(zh = "需求数量", order = 15)
    private String quantity;

    @ApiModelProperty("执行数量")
    @ExcelColumn(zh = "执行数量", order = 16)
    private String actualQuantity;

    @ApiModelProperty("发料仓库")
    @ExcelColumn(zh = "发料仓库", order = 17)
    private String fromWarehouseCode;

    @ApiModelProperty("发料货位")
    @ExcelColumn(zh = "发料货位", order = 18)
    private String fromLocatorCode;

    @ApiModelProperty("收料仓库")
    @ExcelColumn(zh = "收料仓库", order = 19)
    private String toWarehouseCode;

    @ApiModelProperty("收料货位")
    @ExcelColumn(zh = "收料货位", order = 20)
    private String toLocatorCode;

    @ApiModelProperty("超发设置")
    @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty("超发设置")
    @ExcelColumn(zh = "超发设置", order = 21)
    private String excessSettingMeaning;

    @ApiModelProperty("超发值")
    @ExcelColumn(zh = "超发值", order = 22)
    private String excessValue;

    @ApiModelProperty("供应商编码")
    @ExcelColumn(zh = "供应商编码", order = 23)
    private String supplierCode;

    @ApiModelProperty("供应商描述")
    @ExcelColumn(zh = "供应商描述", order = 24)
    private String supplierName;

    @ApiModelProperty("销单号")
    @ExcelColumn(zh = "销单号", order = 25)
    private String soNum;

    @ApiModelProperty("销单行号")
    @ExcelColumn(zh = "销单行号", order = 26)
    private String soLine;

    @ApiModelProperty("订单类型")
    @LovValue(value = "WMS.PO_LINE.TYPE", meaningField = "poTypeMeaning")
    private String poType;

    @ApiModelProperty("订单类型")
    @ExcelColumn(zh = "订单类型", order = 27)
    private String poTypeMeaning;

    @ApiModelProperty("制单人")
    @ExcelColumn(zh = "制单人", order = 28)
    private String realName;

    @ApiModelProperty("制单时间")
    private Date creationDate;

    @ApiModelProperty("制单时间")
    @ExcelColumn(zh = "制单时间", order = 29)
    private String creationDateStr;

    @ApiModelProperty("执行人")
    @ExcelColumn(zh = "执行人", order = 30)
    private String excuteRealName;

    @ApiModelProperty("执行时间")
    private Date lastUpdateDate;

    @ApiModelProperty("执行时间")
    @ExcelColumn(zh = "执行时间", order = 31)
    private String lastUpdateDateStr;
}
