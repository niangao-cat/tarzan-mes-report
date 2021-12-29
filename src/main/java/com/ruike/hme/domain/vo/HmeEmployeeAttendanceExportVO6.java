package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/20 15:33
 */
@Data
public class HmeEmployeeAttendanceExportVO6 implements Serializable {

    private static final long serialVersionUID = -8251498404330386611L;

    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("进站人/出站人")
    private String siteInBy;
    @ApiModelProperty("物料")
    private String snMaterialId;
    @ApiModelProperty("物料版本")
    private String productionVersion;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("EO")
    private String eoId;
    @ApiModelProperty("工位")
    private String workcellId;
    @ApiModelProperty("时长")
    private BigDecimal totalProductionTime;
}
