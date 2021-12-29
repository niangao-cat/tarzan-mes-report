package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeProductionLineDetailsVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/04/25 14:35:12
 **/
@Data
public class HmeProductionLineDetailsVO2 implements Serializable {
    private static final long serialVersionUID = -7302352422728327145L;

    @ApiModelProperty(value = "分页表格数据")
    private Page<HmeProductionQueryDTO> pageData;

    @ApiModelProperty(value = "各个工序在制合计数据")
    private List<HmeProductionLineDetailsVO3> workcellQty;

    @ApiModelProperty(value = "在制合计")
    private BigDecimal qty;
}
