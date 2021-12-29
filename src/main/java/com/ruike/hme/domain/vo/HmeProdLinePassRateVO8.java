package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateVO8
 *
 * @author: chaonan.hu@hand-china.com 2021-03-02 10:10:34
 **/
@Data
public class HmeProdLinePassRateVO8 implements Serializable {
    private static final long serialVersionUID = 1014012139624104451L;

    @ApiModelProperty("表格数据")
    private List<HmeProdLinePassRateVO6> resultList;

    @ApiModelProperty("表格最后一行直通率数据")
    private List<String> passRateData;
}
