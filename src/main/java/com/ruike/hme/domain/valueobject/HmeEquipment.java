package com.ruike.hme.domain.valueobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 设备值对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 10:51
 */
@Data
public class HmeEquipment {
    @ApiModelProperty("租户id")
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录")
    private String equipmentId;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "资产名称")
    private String assetName;
    @ApiModelProperty(value = "资产类别")
    @LovValue(value = "HME.ASSET_CLASS", meaningField = "assetClassDes")
    private String assetClass;
    @ApiModelProperty(value = "设备描述")
    private String descriptions;
    @ApiModelProperty(value = "SAP流水号")
    private String sapNum;
    @ApiModelProperty(value = "机身序列号")
    private String equipmentBodyNum;
    @ApiModelProperty(value = "配置")
    private String equipmentConfig;
    @ApiModelProperty(value = "OA验收单号")
    private String oaCheckNum;
    @ApiModelProperty(value = "设备类别")
    @LovValue(value = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDes")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备类型")
    @LovValue(value = "HME.EQUIPEMNT_TYPE", meaningField = "equipmentTypeDes")
    private String equipmentType;
    @ApiModelProperty(value = "应用类型")
    @LovValue(value = "HME.APPLY_TYPE", meaningField = "applyTypeDes")
    private String applyType;
    @ApiModelProperty(value = "设备状态")
    @LovValue(value = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusDes")
    private String equipmentStatus;
    @ApiModelProperty(value = "处置单号")
    private String dealNum;
    @ApiModelProperty(value = "处置原因")
    private String dealReason;
    @ApiModelProperty(value = "保管部门ID")
    private String businessId;
    @ApiModelProperty(value = "使用人")
    private String user;
    @ApiModelProperty(value = "保管人")
    private String preserver;
    @ApiModelProperty(value = "存放地点")
    private String location;
    @ApiModelProperty(value = "是否计量")
    @LovValue(value = "HME.MEASURE_FLAG", meaningField = "measureFlagDes")
    private String measureFlag;
    @ApiModelProperty(value = "使用频次")
    @LovValue(value = "HME.USE_FREQUENCY", meaningField = "frequencyDes")
    private String frequency;
    @ApiModelProperty(value = "归属权")
    private String belongTo;
    @ApiModelProperty(value = "入账日期")
    private Date postingDate;
    @ApiModelProperty(value = "销售商")
    private String supplier;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "数量")
    private Long quantity;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "币种")
    @LovValue(value = "HME.CURRENCY", meaningField = "currencySymbol")
    private String currency;
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    @ApiModelProperty(value = "募投")
    private String recruitement;
    @ApiModelProperty(value = "募投编号")
    private String recruitementNum;
    @ApiModelProperty(value = "质保期")
    private Date warrantyDate;
    @ApiModelProperty(value = "组织ID")
    private String siteId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
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
