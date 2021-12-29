package com.ruike.hme.domain.valueobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * SN作业
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:18
 */
@Data
public class HmeEoJobSn {
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String jobId;
    @ApiModelProperty(value = "进站日期")
    private Date siteInDate;
    @ApiModelProperty(value = "出站日期")
    private Date siteOutDate;
    @ApiModelProperty(value = "班次ID")
    private String shiftId;
    @ApiModelProperty(value = "进站人ID")
    private Long siteInBy;
    @ApiModelProperty(value = "出站人ID")
    private Long siteOutBy;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "SN在制品ID")
    private String snMaterialId;
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "SN数量")
    private BigDecimal snQty;
    @ApiModelProperty(value = "EO")
    private String eoId;
    @ApiModelProperty(value = "工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty(value = "工艺步骤加工次数")
    private Integer eoStepNum;
    @ApiModelProperty(value = "作业容器ID")
    private String jobContainerId;
    @ApiModelProperty(value = "是否返修标识")
    private String reworkFlag;
    @ApiModelProperty(value = "作业平台类型")
    private String jobType;
    @ApiModelProperty(value = "来源容器ID")
    private String sourceContainerId;
    @ApiModelProperty(value = "来源作业ID")
    private String sourceJobId;
}
