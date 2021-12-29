package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.io.Serializable;
import java.util.List;


/**
 * @author sanfeng.zhang@hand-china.com 2021/8/5 10:30
 */
@Data
public class HmeKanbanConfigVO implements Serializable {

    private static final long serialVersionUID = 1378219488581649939L;

    @ApiModelProperty("制造中心综合看板配置")
    private List<LovValueDTO> centerConfig;
    @ApiModelProperty("成品检验看板配置")
    private List<LovValueDTO> productInspectionConfig;
    @ApiModelProperty("站点信息")
    private HmeModSiteVO siteInfo;
    @ApiModelProperty("制造中心综合看板-制造部列表")
    private List<LovValueDTO> areaList;
}
