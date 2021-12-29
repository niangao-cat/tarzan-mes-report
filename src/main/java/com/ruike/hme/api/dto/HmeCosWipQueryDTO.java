package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.infra.mapper.HmeCosWipQueryMapper;
import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.Constant.ConstantValue.ALL_PATTERN;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 16:33
 */
@Data
public class HmeCosWipQueryDTO implements Serializable {

    private static final long serialVersionUID = 5016516845992436110L;

    @ApiModelProperty("工厂")
    private String siteId;
    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;
    @ApiModelProperty(value = "产品编码")
    private String productionCode;
    @ApiModelProperty(value = "产品名称")
    private String productionName;
    @ApiModelProperty(value = "芯片盒子")
    private String materialLotCode;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "工位")
    private String workCellId;
    @ApiModelProperty(value = "作业平台")
    private String jobType;
    @ApiModelProperty(value = "进站时间从")
    private String siteInDateFrom;
    @ApiModelProperty(value = "进站时间至")
    private String siteInDateTo;
    @ApiModelProperty(value = "出站时间从")
    private String siteOutDateFrom;
    @ApiModelProperty(value = "出站时间至")
    private String siteOutDateTo;
    @ApiModelProperty(value = "waferNum")
    private String waferNum;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "equipmentId")
    private String equipmentId;
    @ApiModelProperty(value = "工单ID", hidden = true)
    @JsonIgnore
    private List<String> workOrderIds;
    @ApiModelProperty(value = "工单ID", hidden = true)
    @JsonIgnore
    private List<String> materialLotIds;

    public void validObject(Long tenantId, HmeCosWipQueryMapper mapper) {
        if (StringUtils.isBlank(workOrderNum) && StringUtils.isBlank(materialLotCode) && (Objects.isNull(siteInDateFrom) || Objects.isNull(siteInDateTo)) && (Objects.isNull(siteOutDateFrom) || Objects.isNull(siteOutDateTo))) {
            throw new CommonException("工单号，芯片盒子，与进站时间范围或出站时间范围必输其一");
        }

        if (StringUtils.startsWith(workOrderNum, ALL_PATTERN)) {
            throw new CommonException("工单号不能以%开头");
        }

        if (StringUtils.startsWith(materialLotCode, ALL_PATTERN)) {
            throw new CommonException("芯片盒子不能以%开头");
        }

        if (StringUtils.isNotEmpty(workOrderNum) || StringUtils.isNotEmpty(productionCode) || StringUtils.isNotEmpty(productionName)) {
            workOrderIds = mapper.selectWorkOrderList(tenantId, this);
        }
    }
}
