package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import utils.BeanCopierUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 配送需求
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 11:28
 */
@Data
public class HmeDistributionDemandVO {
    @ApiModelProperty("配送需求ID")
    private String distDemandId;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ApiModelProperty("工厂编码")
    private String siteCode;
    @ApiModelProperty("生产线")
    private String prodLineId;
    @ApiModelProperty("生产线编码")
    private String prodLineCode;
    @ApiModelProperty("工段")
    private String workcellId;
    @ApiModelProperty("工段编码")
    private String workcellCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty("需求数量")
    private BigDecimal demandQty;
    @ApiModelProperty("已配送数量")
    private BigDecimal distributedQty;
    @ApiModelProperty("配送需求日期")
    private Date demandDate;

    public HmeDistributionDemandRepresentationVO toRepresentation() {
        HmeDistributionDemandRepresentationVO representation = new HmeDistributionDemandRepresentationVO();
        BeanCopierUtil.copy(this, representation);
        return representation;
    }
}
