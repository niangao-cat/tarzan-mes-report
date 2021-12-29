package com.ruike.hme.api.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 生产流转查询报表 查询对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 15:27
 */
@Data
public class HmeProductionFlowQuery implements Serializable {
    private static final long serialVersionUID = 9117547277472068799L;

    @ApiModelProperty(value = "工段编码")
    private String workcellLineId;

    @ApiModelProperty(value = "工序编码")
    private String workcellProcessId;

    @ApiModelProperty(value = "工位编码")
    private String workcellStationId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;

    @ApiModelProperty(value = "产品编码")
    private String productionCode;

    @ApiModelProperty(value = "产品序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "实验代码")
    private String identification;

    @ApiModelProperty(value = "是否不良")
    private String badFlag;

    @ApiModelProperty(value = "是否返修")
    private String reFlag;

    @ApiModelProperty(value = "班次日期")
    private String shiftDate;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "进站人")
    private String userInId;

    @ApiModelProperty(value = "出站人")
    private String userOutId;

    @ApiModelProperty(value = "作业平台类型")
    private String workType;

    @ApiModelProperty(value = "加工开始时间起")
    private String workStartFrom;

    @ApiModelProperty(value = "加工开始时间至")
    private String workStartTo;

    @ApiModelProperty(value = "加工结束时间起")
    private String workEndFrom;

    @ApiModelProperty(value = "加工结束时间至")
    private String workEndTo;

    @ApiModelProperty("返修SN")
    private String reworkMaterialLot;

    @ApiModelProperty(value = "工段编码", hidden = true)
    @JsonIgnore
    private List<String> workcellLineIdList;

    @ApiModelProperty(value = "工序编码", hidden = true)
    @JsonIgnore
    private List<String> workcellProcessIdList;

    @ApiModelProperty(value = "工位编码", hidden = true)
    @JsonIgnore
    private List<String> workcellStationIdList;

    @ApiModelProperty(value = "设备编码", hidden = true)
    @JsonIgnore
    private List<String> equipmentIdList;

    @ApiModelProperty(value = "工单号", hidden = true)
    @JsonIgnore
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "产品编码", hidden = true)
    @JsonIgnore
    private List<String> productionCodeList;

    @ApiModelProperty(value = "产品序列号", hidden = true)
    @JsonIgnore
    private List<String> materialLotCodeList;

    @ApiModelProperty("返修SN")
    private List<String> reworkMaterialLotList;

    @ApiModelProperty(value = "工单状态",hidden = true)
    private List<String> workOrderStatusList;

    @ApiModelProperty(value = "创建任务")
    private HmeExportTaskVO exportTaskVO;

    public void initialization() {
        workcellLineIdList = StringUtils.isBlank(workcellLineId) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(workcellLineId, ",")));
        workcellProcessIdList = StringUtils.isBlank(workcellProcessId) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(workcellProcessId, ",")));
        workcellStationIdList = StringUtils.isBlank(workcellStationId) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(workcellStationId, ",")));
        equipmentIdList = StringUtils.isBlank(equipmentId) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(equipmentId, ",")));
        workOrderNumList = StringUtils.isBlank(workOrderNum) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(workOrderNum, ",")));
        productionCodeList = StringUtils.isBlank(productionCode) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(productionCode, ",")));
        materialLotCodeList = StringUtils.isBlank(materialLotCode) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(materialLotCode, ",")));
        reworkMaterialLotList = StringUtils.isBlank(reworkMaterialLot) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(reworkMaterialLot, ",")));
        workOrderStatusList = StringUtils.isBlank(workOrderStatus) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(StringUtils.split(workOrderStatus, ",")));
    }
}
