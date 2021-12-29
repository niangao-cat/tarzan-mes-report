package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeProdLinePassRateVO7
 *
 * @author: chaonan.hu@hand-china.com 2021-03-01 10:08:56
 **/
@Data
public class HmeProdLinePassRateVO7 implements Serializable {
    private static final long serialVersionUID = 1013412147134106351L;

    @ApiModelProperty("班次ID")
    private String wkcShiftId;

    @ApiModelProperty("班次名称")
    private String shiftName;

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
