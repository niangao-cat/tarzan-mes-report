package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 工单损耗汇总报表 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 10:17
 */
@Data
public class WorkOrderAttritionSumQueryDTO implements Serializable {
    private static final long serialVersionUID = -2268111631601515146L;

    @ApiModelProperty(value = "产线ID", notes = "传入逗号隔开字符串")
    private String prodLineIds;

    @ApiModelProperty(value = "工段ID", notes = "传入逗号隔开字符串")
    private String workcellIds;

    @ApiModelProperty(value = "工单", notes = "传入逗号隔开字符串")
    private String workOrderNums;

    @ApiModelProperty(value = "工单状态", notes = "传入逗号隔开字符串")
    private String woStatuses;

    @ApiModelProperty(value = "产品物料ID", notes = "传入逗号隔开字符串")
    private String assemblyMaterialIds;

    @ApiModelProperty(value = "BOM版本")
    private String bomVersion;

    @ApiModelProperty(value = "组件物料ID", notes = "传入逗号隔开字符串")
    private String componentMaterialIds;

    @ApiModelProperty(value = "是否超工单报损")
    private String scrappedOverFlag;

    @ApiModelProperty(value = "损耗率差值大于")
    private BigDecimal attritionRateFrom;

    @ApiModelProperty(value = "损耗率差值小于")
    private BigDecimal attritionRateTo;

    @ApiModelProperty(value = "计划开始时间从")
    private Date planStartTimeFrom;

    @ApiModelProperty(value = "计划开始时间至")
    private Date planStartTimeTo;

    @ApiModelProperty(value = "产线ID列表", notes = "前端勿传", hidden = true)
    private Set<String> prodLineIdList;

    @ApiModelProperty(value = "工段ID列表", notes = "前端勿传", hidden = true)
    private Set<String> workcellIdSet;

    @ApiModelProperty(value = "工单列表", notes = "前端勿传", hidden = true)
    private Set<String> workOrderNumList;

    @ApiModelProperty(value = "工单状态列表", notes = "前端勿传", hidden = true)
    private Set<String> woStatusList;

    @ApiModelProperty(value = "产品物料ID列表", notes = "前端勿传", hidden = true)
    private Set<String> assemblyMaterialIdList;

    @ApiModelProperty(value = "组件物料ID列表", notes = "前端勿传", hidden = true)
    private Set<String> componentMaterialIdList;

    public void paramInit() {
        this.setProdLineIdList(StringUtils.isBlank(this.getProdLineIds()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getProdLineIds(), ","))));
        this.setWorkcellIdSet(StringUtils.isBlank(this.getWorkcellIds()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getWorkcellIds(), ","))));
        this.setWorkOrderNumList(StringUtils.isBlank(this.getWorkOrderNums()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getWorkOrderNums(), ","))));
        this.setWoStatusList(StringUtils.isBlank(this.getWoStatuses()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getWoStatuses(), ","))));
        this.setAssemblyMaterialIdList(StringUtils.isBlank(this.getAssemblyMaterialIds()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getAssemblyMaterialIds(), ","))));
        this.setComponentMaterialIdList(StringUtils.isBlank(this.getComponentMaterialIds()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getComponentMaterialIds(), ","))));
    }
}
