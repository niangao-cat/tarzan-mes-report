package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
@Data
@ExcelSheet(title = "生产执行全过程监控")
public class WmsProductionExecutionWholeProcessMonitoringReportVO implements Serializable {

    private static final long serialVersionUID = 6902684344192813265L;

    @ApiModelProperty("站点")
    @ExcelColumn(title = "站点", order = 0)
    private String siteCode;

    @ApiModelProperty("制造部")
    @ExcelColumn(title = "制造部", order = 1)
    private String areaCode;

    @ApiModelProperty("生产线")
    @ExcelColumn(title = "生产线", order = 2)
    private String prodLineCode;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单编号")
    @ExcelColumn(title = "工单编号", order = 3)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(title = "工单版本", order = 4)
    private String productionVersion;

    @ApiModelProperty("工单类型")
    @ExcelColumn(title = "工单类型", order = 5)
    private String workoderType;

    @ApiModelProperty("工单状态")
    @ExcelColumn(title = "工单状态", order = 6)
    private String workoderStatus;

    @ApiModelProperty("工单物料编码")
    @ExcelColumn(title = "工单物料编码", order = 7)
    private String materialCode;

    @ApiModelProperty("工单物料描述")
    @ExcelColumn(title = "工单物料描述", order = 8)
    private String materialName;

    @ApiModelProperty("工单物料组")
    @ExcelColumn(title = "工单物料组", order = 9)
    private String itemGroup;

    @ApiModelProperty("工单物料组描述")
    @ExcelColumn(title = "工单物料组描述", order = 10)
    private String itemGroupDescription;

    @ApiModelProperty("工单数量")
    @ExcelColumn(title = "工单数量", order = 11)
    private Long qty;

    @ApiModelProperty("下达EO数量")
    @ExcelColumn(title = "下达EO数量", order = 12)
    private Long releasedQty;

    @ApiModelProperty("工单在制数量")
    @ExcelColumn(title = "工单在制数量", order = 13)
    private Long wipQty;

    @ApiModelProperty("工单报废数量")
    @ExcelColumn(title = "工单报废数量", order = 14)
    private Long abandonQty;

    @ApiModelProperty("工单完工数量")
    @ExcelColumn(title = "工单完工数量", order = 15)
    private Long completedQty;

    @ApiModelProperty("工单完工率")
    @ExcelColumn(title = "工单完工率", order = 16)
    private String woCompleteRate;

    @ApiModelProperty("EO完工率")
    @ExcelColumn(title = "EO完工率", order = 17)
    private String eoCompleteRate;

    @ApiModelProperty("工单已入库数量")
    @ExcelColumn(title = "工单已入库数量", order = 18)
    private Long woinstorkQty;

    @ApiModelProperty("工单待入库数量")
    @ExcelColumn(title = "工单待入库数量", order = 19)
    private Long wonotinstorkQty;

    @ApiModelProperty("工单入库率")
    @ExcelColumn(title = "工单入库率", order = 20)
    private String wonotinstorkRate;

    @ApiModelProperty("产品物料Id")
    private String proMaterialId;

    @ApiModelProperty("产品物料编码")
    @ExcelColumn(title = "产品物料编码", order = 21)
    private String proMaterialCode;

    @ApiModelProperty("是否主产品")
    @ExcelColumn(title = "是否主产品", order = 22)
    private String flag;

    @ApiModelProperty("产品完工数量")
    @ExcelColumn(title = "产品完工数量", order = 23)
    private Long countQty;

    @ApiModelProperty("产品完工率")
    @ExcelColumn(title = "产品完工率", order = 24)
    private String countQtyRate;

    @ApiModelProperty("产品入库数量")
    @ExcelColumn(title = "产品入库数量", order = 25)
    private Long eonotinstorkQty;

    @ApiModelProperty("产品待入库数量")
    @ExcelColumn(title = "产品待入库数量", order = 26)
    private Long eotinstorkQty;

    @ApiModelProperty("产品物料组")
    @ExcelColumn(title = "产品物料组", order = 27)
    private String proItemGroup;

    @ApiModelProperty("产品物料组描述")
    @ExcelColumn(title = "产品物料组描述", order = 28)
    private String proItemGroupDescription;

    @ApiModelProperty("工作令号")
    @ExcelColumn(title = "工作令号", order = 29)
    private String workNum;

    @ApiModelProperty("销售订单号")
    @ExcelColumn(title = "销售订单号", order = 30)
    private String soNum;

    @ApiModelProperty("销售订单行号")
    @ExcelColumn(title = "销售订单行号", order = 31)
    private String soLineNum;

    @ApiModelProperty("工单实际完成时间")
    private Date actualEndDate;

    @ApiModelProperty("工单实际完成时间")
    @ExcelColumn(title = "工单实际完成时间", order = 32)
    private String actualEndDateStr;

    @ApiModelProperty("ERP创建日期")
    @ExcelColumn(title = "ERP创建日期", order = 33)
    private String erpCreateDate;

    @ApiModelProperty("ERP下达日期")
    @ExcelColumn(title = "ERP下达日期", order = 34)
    private String erpRealeseDate;

    @ApiModelProperty("工单备注")
    @ExcelColumn(title = "工单备注", order = 35)
    private String remark;

    @ApiModelProperty("长文本")
    @ExcelColumn(title = "长文本", order = 36)
    private String longText;

    @ApiModelProperty("生产管理员")
    @ExcelColumn(title = "生产管理员", order = 37)
    private String userCode;

    @ApiModelProperty("仓库类型")
    private String warehouseType;
}
