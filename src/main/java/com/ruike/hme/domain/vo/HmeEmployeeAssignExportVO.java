package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

@Data
@ExcelSheet(zh = "员工制造属性查看报表")
public class HmeEmployeeAssignExportVO implements Serializable {
    private static final long serialVersionUID = 6116851677510385935L;

    @ExcelColumn(zh = "员工编码", order = 0)
    @ApiModelProperty("员工编码")
    private String employeeNum;

    @ExcelColumn(zh = "员工姓名", order = 1)
    @ApiModelProperty("员工姓名")
    private String name;

    @ExcelColumn(zh = "资质编码", order = 2)
    @ApiModelProperty("资质编码")
    private String qualityCode;

    @LovValue(value = "HME.QUALITY_TYPE", meaningField = "qualityTypeMeaning")
    @ApiModelProperty("资质类型")
    private String qualityType;

    @ExcelColumn(zh = "资质类型", order = 3)
    @ApiModelProperty("资质类型")
    private String qualityTypeMeaning;

    @ExcelColumn(zh = "资质名称", order = 4)
    @ApiModelProperty("资质名称")
    private String qualityName;

    @ExcelColumn(zh = "资质备注", order = 5)
    @ApiModelProperty("资质备注")
    private String remark;

    @LovValue(value = "HME.PROFICIENCY", meaningField = "proficiencyMeaning")
    @ApiModelProperty("资质熟练度")
    private String proficiency;

    @ExcelColumn(zh = "资质熟练度", order = 6)
    @ApiModelProperty("资质熟练度")
    private String proficiencyMeaning;

    @ExcelColumn(zh = "物料编码", order = 7)
    @ApiModelProperty("物料编码")
    private String materialCode;

    @ExcelColumn(zh = "物料名称", order = 8)
    @ApiModelProperty("物料名称")
    private String materialName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("有效时间起")
    private Date dateFrom;
    @ExcelColumn(zh = "有效时间起", order = 9)
    @ApiModelProperty("有效时间起")
    private String dateFromStr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("有效时间止")
    private Date dateTo;
    @ExcelColumn(zh = "有效时间止", order = 10)
    @ApiModelProperty("有效时间止")
    private String dateToStr;

}
