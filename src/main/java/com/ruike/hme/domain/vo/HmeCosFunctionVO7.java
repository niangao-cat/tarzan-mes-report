package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeFunctionReportDTO6
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/14 17:38
 * @Version 1.0
 **/
@Data
public class HmeCosFunctionVO7 implements Serializable {
    private static final long serialVersionUID = -3700789983304573217L;

    @ApiModelProperty(value = "表格数据")
    private List<HmeCosFunctionVO6> hmeCosFunctionVO6List;

    @ApiModelProperty(value = "动态列标题")
    private List<String> title;
}
