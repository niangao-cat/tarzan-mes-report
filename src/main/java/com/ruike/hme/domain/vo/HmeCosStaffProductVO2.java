package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import sun.nio.cs.ext.Big5;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/17 14:46
 */
@Data
@ExcelSheet(zh = "COS员工产量汇总报表")
public class HmeCosStaffProductVO2 implements Serializable {

    private static final long serialVersionUID = 15401090366075142L;

    @ApiModelProperty(value = "员工")
    @ExcelColumn(zh = "员工", order = 1)
    private String realName;
    @ApiModelProperty(value = "工号")
    @ExcelColumn(zh = "工号", order = 2)
    private String loginName;
    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线", order = 3)
    private String prodLineName;
    @ApiModelProperty(value = "工段")
    @ExcelColumn(zh = "工段", order = 4)
    private String lineWordcellName;
    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序", order = 5)
    private String processName;
    @ApiModelProperty(value = "工序Id")
    private String processId;
    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码", order = 6)
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述", order = 7)
    private String materialName;
    @ApiModelProperty(value = "COS类型")
    @ExcelColumn(zh = "COS类型", order = 9)
    private String cosType;
    @ApiModelProperty(value = "实际产出")
    @ExcelColumn(zh = "实际产出", order = 10)
    private BigDecimal totalSnQty;
    @ApiModelProperty(value = "不良数")
    @ExcelColumn(zh = "不良数", order = 11)
    private BigDecimal ncQty;
    @ApiModelProperty(value = "一次合格率")
    @ExcelColumn(zh = "一次合格率", order = 12)
    private BigDecimal passRate;
    @ApiModelProperty(value = "生产总时长")
    @ExcelColumn(zh = "生产总时长", order = 12)
    private BigDecimal duration;
    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本", order = 8)
    private String productionVersion;

    @ApiModelProperty(value = "进站人")
    private Long siteInBy;
}
