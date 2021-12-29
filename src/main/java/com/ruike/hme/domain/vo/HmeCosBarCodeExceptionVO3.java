package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class HmeCosBarCodeExceptionVO3 implements Serializable {
    private static final long serialVersionUID = -3715763564424783774L;

    @ApiModelProperty("工序工位关系")
    private Map<String,String> processWorkcellMap;

    @ApiModelProperty("工段工位关系")
    private Map<String,String> lineWorkcellMap;

    @ApiModelProperty("产线工位关系")
    private Map<String,String> prodLineWorkcellMap;
}
