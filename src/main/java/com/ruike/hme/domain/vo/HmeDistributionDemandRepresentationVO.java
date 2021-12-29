package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.common.HZeroConstant;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 配送需求滚动报表展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 09:33
 */
@Data
@ExcelSheet
public class HmeDistributionDemandRepresentationVO implements Serializable {
    private static final long serialVersionUID = 3602748626625711723L;

    @ApiModelProperty("序号")
    @ExcelColumn(zh = "序号")
    private Integer sequence;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ExcelColumn(zh = "工厂")
    @ApiModelProperty("工厂编码")
    private String siteCode;
    @ApiModelProperty("日期")
    @ExcelColumn(zh = "日期", pattern = BaseConstants.Pattern.DATETIME)
    private Date nowDate;
    @ApiModelProperty("生产线")
    private String prodLineId;
    @ApiModelProperty("生产线编码")
    @ExcelColumn(zh = "生产线")
    private String prodLineCode;
    @ApiModelProperty("工段")
    private String workcellId;
    @ApiModelProperty("工段编码")
    @ExcelColumn(zh = "工段")
    private String workcellCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ExcelColumn(zh = "物料编码")
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ExcelColumn(zh = "物料描述")
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本")
    private String materialVersion;
    @ApiModelProperty("当日需求总数")
    @ExcelColumn(zh = "当日需求总数")
    private BigDecimal currentDemandQty;
    @ApiModelProperty("已配送数量")
    @ExcelColumn(zh = "已配送数量")
    private BigDecimal distributedQty;
    @ApiModelProperty("当日需求缺口")
    @ExcelColumn(zh = "当日需求缺口")
    private BigDecimal demandGapQty;
    @ApiModelProperty("线边数量")
    @ExcelColumn(zh = "线边库存")
    private BigDecimal workcellQty;
    @ApiModelProperty("预计停线时间")
    @ExcelColumn(zh = "预计停线时间")
    private BigDecimal estimatedStopTime;
    @ApiModelProperty("未来需求数量")
    @ExcelColumn(zh = "后三日需求总数")
    private BigDecimal futureDemandQty;
    @ApiModelProperty("库存数量")
    @ExcelColumn(zh = "库存量")
    private BigDecimal inventoryQty;
    @ExcelColumn(zh = "库存空缺")
    @ApiModelProperty("库存空缺")
    private BigDecimal inventoryGapQty;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeDistributionDemandRepresentationVO that = (HmeDistributionDemandRepresentationVO) o;
        return Objects.equals(sequence, that.sequence) && Objects.equals(siteId, that.siteId) && Objects.equals(siteCode, that.siteCode) && Objects.equals(nowDate, that.nowDate) && Objects.equals(prodLineId, that.prodLineId) && Objects.equals(prodLineCode, that.prodLineCode) && Objects.equals(workcellId, that.workcellId) && Objects.equals(workcellCode, that.workcellCode) && Objects.equals(materialId, that.materialId) && Objects.equals(materialCode, that.materialCode) && Objects.equals(materialName, that.materialName) && Objects.equals(materialVersion, that.materialVersion) && Objects.equals(currentDemandQty, that.currentDemandQty) && Objects.equals(distributedQty, that.distributedQty) && Objects.equals(demandGapQty, that.demandGapQty) && Objects.equals(workcellQty, that.workcellQty) && Objects.equals(estimatedStopTime, that.estimatedStopTime) && Objects.equals(futureDemandQty, that.futureDemandQty) && Objects.equals(inventoryQty, that.inventoryQty) && Objects.equals(inventoryGapQty, that.inventoryGapQty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence, siteId, siteCode, nowDate, prodLineId, prodLineCode, workcellId, workcellCode, materialId, materialCode, materialName, materialVersion, currentDemandQty, distributedQty, demandGapQty, workcellQty, estimatedStopTime, futureDemandQty, inventoryQty, inventoryGapQty);
    }
}
