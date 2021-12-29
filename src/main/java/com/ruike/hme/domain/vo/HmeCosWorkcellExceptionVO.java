package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * COS工位加工异常汇总表 页面展示
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 12:38
 */
@Data
@ExcelSheet(zh = "COS工位加工异常汇总表")
public class HmeCosWorkcellExceptionVO implements Serializable {

    private static final long serialVersionUID = 743107993983845239L;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号")
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    @ExcelColumn(zh = "工单版本")
    private String productionVersion;

    @ApiModelProperty("版本描述")
    @ExcelColumn(zh = "版本描述")
    private String productionVersionDescription;

    @ApiModelProperty("jobId")
    @JsonIgnore
    private String jobId;

    @ApiModelProperty("jobIdList")
    private List<String> jobIdList;

    @ApiModelProperty("工序ID")
    @JsonIgnore
    private String workcellProcessId;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty("WAFER")
    @ExcelColumn(zh = "WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    @ExcelColumn(zh = "COS类型")
    private String cosType;

    @ApiModelProperty("工单芯片数")
    @ExcelColumn(zh = "工单芯片数")
    private BigDecimal qty;

    @ApiModelProperty("工单工序不良总数")
    @ExcelColumn(zh = "工单工序不良总数")
    private BigDecimal woProcessNcQty;

    @ApiModelProperty("不良总数")
    @ExcelColumn(zh = "不良总数")
    private BigDecimal ncTotalQuantity;

    @ApiModelProperty("不良数量")
    @ExcelColumn(zh = "不良数量")
    private BigDecimal ncQuantity;

    @ApiModelProperty("不良类型")
    private String description;

    @ApiModelProperty("不良代码")
    @ExcelColumn(zh = "不良代码")
    private String ncCode;

    @ApiModelProperty("不良代码描述")
    @ExcelColumn(zh = "不良代码描述")
    private String ncCodeDescription;

    @ApiModelProperty("操作者ID")
    private String createBy;

    @ApiModelProperty("操作者")
    @ExcelColumn(zh = "操作者")
    private String realName;

    @ApiModelProperty("日期")
    @ExcelColumn(zh = "日期", pattern = BaseConstants.Pattern.DATE)
    private Date creationDate;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码")
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(zh = "工位描述")
    private String workcellName;

    @ApiModelProperty("工序描述")
    @ExcelColumn(zh = "工序描述")
    private String workcellProcessName;

    @ApiModelProperty("工段描述")
    @ExcelColumn(zh = "工段描述")
    private String workcellLineName;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(zh = "生产线描述")
    private String prodLineName;

    @ApiModelProperty("设备编码")
    @ExcelColumn(zh = "设备编码")
    private String assetEncoding;

    @ApiModelProperty("设别描述")
    @ExcelColumn(zh = "设别描述")
    private String assetName;

    public static HmeCosWorkcellExceptionVO group(HmeCosWorkcellExceptionVO obj) {
        HmeCosWorkcellExceptionVO result = new HmeCosWorkcellExceptionVO();
        result.setWorkOrderNum(obj.getWorkOrderNum());
        result.setProductionVersion(obj.getProductionVersion());
        result.setProductionVersionDescription(obj.getProductionVersionDescription());
        result.setMaterialCode(obj.getMaterialCode());
        result.setMaterialName(obj.getMaterialName());
        result.setWaferNum(obj.getWaferNum());
        result.setCosType(obj.getCosType());
        result.setQty(obj.getQty());
        result.setNcCode(obj.getNcCode());
        result.setNcCodeDescription(obj.getNcCodeDescription());
        result.setRealName(obj.getRealName());
        result.setCreationDate(obj.getCreationDate());
        result.setWorkcellCode(obj.getWorkcellCode());
        result.setWorkcellName(obj.getWorkcellName());
        result.setWorkcellProcessId(obj.getWorkcellProcessId());
        result.setWorkcellProcessName(obj.getWorkcellProcessName());
        result.setWorkcellLineName(obj.getWorkcellLineName());
        result.setProdLineName(obj.getProdLineName());
        result.setDescription(obj.getDescription());
        return result;
    }
}
