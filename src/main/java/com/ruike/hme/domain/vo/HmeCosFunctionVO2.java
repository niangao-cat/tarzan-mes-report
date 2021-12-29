package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeFunctionReportDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/14 17:38
 * @Version 1.0
 **/
@Data
public class HmeCosFunctionVO2 implements Serializable {
    private static final long serialVersionUID = -5685006340456595259L;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "是否在制")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "mfFlagMeaning")
    private String mfFlag;

    @ApiModelProperty(value = "是否在制含义")
    private String mfFlagMeaning;

    @ApiModelProperty(value = "测试设备")
    private String testEquipment;

    @ApiModelProperty(value = "贴片设备")
    private String patchEquipment;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "位置")
    private String rowCloumn;

    @ApiModelProperty(value = "筛选状态")
    @LovValue(lovCode = "HME.SELECT_STATUS", meaningField = "preStatusMeaning")
    private String preStatus;

    @ApiModelProperty(value = "筛选状态含义")
    private String preStatusMeaning;

    @ApiModelProperty(value = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "测试模式")
    private String a09;

    @ApiModelProperty(value = "阈值电流")
    private BigDecimal a010;

    @ApiModelProperty(value = "阈值电压")
    private BigDecimal a011;

    @ApiModelProperty(value = "测试电流")
    private String current;

    @ApiModelProperty(value = "电压")
    private BigDecimal a06;

    @ApiModelProperty(value = "功率")
    private BigDecimal a02;

    @ApiModelProperty(value = "中心波长")
    private BigDecimal a04;

    @ApiModelProperty(value = "SE")
    private BigDecimal a012;

    @ApiModelProperty(value = "线宽")
    private BigDecimal a013;

    @ApiModelProperty(value = "WPE")
    private BigDecimal a014;

    @ApiModelProperty(value = "波长差")
    private BigDecimal a05;

    @ApiModelProperty(value = "透镜功率")
    private BigDecimal a22;

    @ApiModelProperty(value = "PBS功率")
    private BigDecimal a23;

    @ApiModelProperty(value = "偏振度数")
    private BigDecimal a15;

    @ApiModelProperty(value = "X半宽高")
    private BigDecimal a16;

    @ApiModelProperty(value = "X86能量宽度")
    private BigDecimal a18;

    @ApiModelProperty(value = "Y86能量宽度")
    private BigDecimal a19;

    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal a17;

    @ApiModelProperty(value = "X95能量宽度")
    private BigDecimal a20;

    @ApiModelProperty(value = "Y95能量宽度")
    private BigDecimal a21;

    @ApiModelProperty(value = "功率等级")
    private String a01;

    @ApiModelProperty(value = "波长等级")
    private String a03;

    @ApiModelProperty(value = "电压等级")
    private String a27;

    @ApiModelProperty(value = "发散角等级,没取值逻辑")
    private String divergenceGrade;

    @ApiModelProperty(value = "偏振度等级,没取值逻辑")
    private String polarizationGrade;

    @ApiModelProperty(value = "不良描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String a26;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "是否不良")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "ncFlagMeaning")
    private String ncFlag;

    @ApiModelProperty(value = "是否不良含义")
    private String ncFlagMeaning;

    @ApiModelProperty(value = "热沉条码")
    private String heatSinkMaterialLot;

    @ApiModelProperty(value = "热沉物料ID")
    private String heatSinkMaterialId;

    @ApiModelProperty(value = "热沉物料编码")
    private String heatSinkMaterialCode;

    @ApiModelProperty(value = "热沉供应商批次")
    private String heatSinkSupplierLot;

    @ApiModelProperty(value = "热沉焊料金锡比")
    private String solderAusnRatio;

    @ApiModelProperty(value = "金线条码")
    private String goldWireMaterialLot;

    @ApiModelProperty(value = "金线物料ID")
    private String goldWireMaterialId;

    @ApiModelProperty(value = "金线物料编码")
    private String goldWireMaterialCode;

    @ApiModelProperty(value = "金线供应商批次")
    private String goldWireSupplierLot;

    @ApiModelProperty(value = "测试操作人ID")
    private String userId;

    @ApiModelProperty(value = "测试操作人")
    private String realName;

    @ApiModelProperty(value = "测试时间")
    private String creationDate;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位描述")
    private String workcellName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序描述")
    private String processName;

    @ApiModelProperty(value = "工段ID")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工段编码")
    private String lineWorkcellCode;

    @ApiModelProperty(value = "工段描述")
    private String lineWorkcellName;

    @ApiModelProperty(value = "生产线描述")
    private String prodLineName;

    @ApiModelProperty(value = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    private String freezeFlagMeaning;

    public static Integer currentToNumber(HmeCosFunctionVO2 hmeCosFunctionVO2) {
        return Integer.parseInt(hmeCosFunctionVO2.getCurrent());
    }

    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
}
