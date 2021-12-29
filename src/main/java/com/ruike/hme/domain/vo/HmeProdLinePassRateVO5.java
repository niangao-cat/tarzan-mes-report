package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProdLinePassRateVO5
 *
 * @author: chaonan.hu@hand-china.com 2021-03-01 09:45:23
 **/
@Data
public class HmeProdLinePassRateVO5 implements Serializable {
    private static final long serialVersionUID = 1014012147624104451L;

    @ApiModelProperty("表格数据")
    private List<HmeProdLinePassRateVO> resultList;

    @ApiModelProperty("表格最后一行直通率数据")
    private List<String> passRateData;

}
