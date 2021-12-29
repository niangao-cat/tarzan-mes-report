package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/3 10:00
 */
@Data
public class HmeMakeCenterProduceBoardVO16 implements Serializable {

    private static final long serialVersionUID = 5300151443135770023L;

    @ApiModelProperty("序号")
    private Integer serialNumber;
    @ApiModelProperty("产线")
    private String prodLineName;
    @ApiModelProperty("稽核时间")
    private String inspectionFinishDate;
    @ApiModelProperty("工序")
    private String processName;
    @ApiModelProperty("问题描述")
    private String problemDesc;
    @ApiModelProperty("问题描述列表")
    @JsonIgnore
    private List<String> problemDescList;
    @ApiModelProperty("巡检员")
    private String inspectionName;
}
