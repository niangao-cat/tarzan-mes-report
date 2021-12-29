package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeProdLinePassRateVO2
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:40:31
 **/
@Data
public class HmeProdLinePassRateVO2 implements Serializable {
    private static final long serialVersionUID = 1013412147134104451L;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("合格数")
    private String passNum;

    @ApiModelProperty("不良数")
    private String ncNum;

    @ApiModelProperty("投产数")
    private String productionNum;

    @ApiModelProperty("良率")
    private String rate;

    @ApiModelProperty("良率(小数)")
    private BigDecimal rateBig;

    @ApiModelProperty("合格数据")
    private List<String> identificationList;

    @ApiModelProperty("不良数据")
    private List<HmeProdLinePassRateVO4> ncDataList;

}
