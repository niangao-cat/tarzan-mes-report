package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmeCosFunctionHeadDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/17 15:58
 * @Version 1.0
 **/
@Data
public class HmeCosFunctionDTO2 implements Serializable {
    private static final long serialVersionUID = 275453059757665187L;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "测试开始时间", required = true)
    private Date startDate;

    @ApiModelProperty(value = "测试结束时间", required = true)
    private Date endDate;

    @ApiModelProperty(value = "测试电流,以逗号分隔的字符串", required = true)
    private String current;

    @ApiModelProperty(value = "测试电流集合")
    private List<String> currentList;

    @ApiModelProperty(value = "工单号,以逗号分隔的字符串")
    private String workOrderNum;

    @ApiModelProperty(value = "工单号集合")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "产品编码,以逗号分隔的ID字符串")
    private String materialId;

    @ApiModelProperty(value = "产品编码集合")
    private List<String> materialIdList;

    @ApiModelProperty(value = "筛选状态,以逗号分隔的字符串")
    private String preStatus;

    @ApiModelProperty(value = "筛选状态集合")
    private List<String> preStatusList;

    @ApiModelProperty(value = "测试设备")
    private String testEquipment;

    @ApiModelProperty(value = "贴片设备")
    private String patchEquipment;

    @ApiModelProperty(value = "WAFER,以逗号分隔的字符串")
    private String wafer;

    @ApiModelProperty(value = "WAFER集合")
    private List<String> waferList;

    @ApiModelProperty(value = "COS类型,以逗号分隔的字符串")
    private String cosType;

    @ApiModelProperty(value = "COS类型集合")
    private List<String> cosTypeList;

    @ApiModelProperty(value = "条码,以逗号分隔的字符串")
    private String materialLotCode;

    @ApiModelProperty(value = "条码集合")
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "条码ID集合")
    private List<String> materialLotIdList;

    @ApiModelProperty(value = "热沉编码,以逗号分隔的字符串")
    private String hotSinkCode;

    @ApiModelProperty(value = "热沉编码集合")
    private List<String> hotSinkCodeList;

    @ApiModelProperty(value = "实验代码,以逗号分隔的字符串")
    private String labCode;

    @ApiModelProperty(value = "实验代码集合")
    private List<String> labCodeList;

    @ApiModelProperty(value = "不良代码,以逗号分隔的字符串,注意这里传LOV值的编码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码集合")
    private List<String> ncCodeList;

    @ApiModelProperty(value = "是否不良")
    private String ncFlag;

    @ApiModelProperty(value = "热沉条码,以逗号分隔的字符串")
    private String heatSinkMaterialLot;

    @ApiModelProperty(value = "热沉条码集合")
    private List<String> heatSinkMaterialLotList;

    @ApiModelProperty(value = "热沉物料")
    private String heatSinkMaterialId;

    @ApiModelProperty(value = "热沉物料集合")
    private List<String> heatSinkMaterialIdList;

    @ApiModelProperty(value = "热沉供应商批次,以逗号分隔的字符串")
    private String heatSinkSupplierLot;

    @ApiModelProperty(value = "热沉供应商批次集合")
    private List<String> heatSinkSupplierLotList;

    @ApiModelProperty(value = "金线条码,以逗号分隔的字符串")
    private String goldWireMaterialLot;

    @ApiModelProperty(value = "金线条码集合")
    private List<String> goldWireMaterialLotList;

    @ApiModelProperty(value = "金线物料,以逗号分隔的ID字符串")
    private String goldWireMaterialId;

    @ApiModelProperty(value = "金线物料集合")
    private List<String> goldWireMaterialIdList;

    @ApiModelProperty(value = "金线供应商批次,以逗号分隔的字符串")
    private String goldWireSupplierLot;

    @ApiModelProperty(value = "金线供应商批次集合")
    private List<String> goldWireSupplierLotList;

    @ApiModelProperty(value = "测试人")
    private String userId;

    @ApiModelProperty(value = "仓库,以逗号分隔的ID字符串")
    private String warehouseId;

    @ApiModelProperty(value = "仓库集合")
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "货位,以逗号分隔的ID字符串")
    private String locatorId;

    @ApiModelProperty(value = "货位集合")
    private List<String> locatorIdList;

    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

    @ApiModelProperty(value = "工位,以逗号分隔的ID字符串")
    private String workcellId;

    @ApiModelProperty(value = "工序,以逗号分隔的ID字符串")
    private String processId;

    @ApiModelProperty(value = "工段,以逗号分隔的ID字符串")
    private String lineWorkcellId;

    @ApiModelProperty(value = "产线,以逗号分隔的ID字符串")
    private String prodLineId;

    @ApiModelProperty(value = "产线集合")
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "工位ID集合,后端自用")
    private List<String> workcellIdList;

    @ApiModelProperty(value = "芯片序列号集合,后端自用")
    private List<String> loadSequenceList;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;
}
