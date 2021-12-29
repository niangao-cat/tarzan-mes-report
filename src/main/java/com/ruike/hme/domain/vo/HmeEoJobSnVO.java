package com.ruike.hme.domain.vo;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/23 14:30
 */
@Data
public class HmeEoJobSnVO extends AuditDomain implements Serializable {

    private static final long serialVersionUID = 1304791713187400821L;

    @ApiModelProperty(value = "租户id", required = true)
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String jobId;
    @ApiModelProperty(value = "进站日期", required = true)
    private Date siteInDate;
    @ApiModelProperty(value = "出站日期")
    private Date siteOutDate;
    @ApiModelProperty(value = "班次ID", required = true)
    private String shiftId;
    @ApiModelProperty(value = "进站人ID", required = true)
    private Long siteInBy;
    @ApiModelProperty(value = "出站人ID")
    private Long siteOutBy;
    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "SN在制品ID",required = true)
    private String snMaterialId;
    @ApiModelProperty(value = "条码ID",required = true)
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
    @ApiModelProperty(value = "",required = true)
    private Long cid;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
}
