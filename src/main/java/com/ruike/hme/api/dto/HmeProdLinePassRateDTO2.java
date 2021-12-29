package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeProdLinePassRateDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021-03-02 10:11:45
 **/
@Data
public class HmeProdLinePassRateDTO2 implements Serializable {
    private static final long serialVersionUID = 1013412147176156451L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("部门ID")
    private String areaId;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料集合")
    private List<String> materialIdList;

    @ApiModelProperty("工单集合")
    private String workOrderId;

    @ApiModelProperty("工单集合")
    private List<String> workOrderIdList;

    @ApiModelProperty("工段集合")
    private String lineWorkCellId;

    @ApiModelProperty("工段集合")
    private List<String> lineWorkCellIdList;

    @ApiModelProperty("工序集合")
    private String processId;

    @ApiModelProperty("工序集合")
    private List<String> processIdList;

    @ApiModelProperty("工位集合")
    private String workcellId;

    @ApiModelProperty("工位集合")
    private List<String> workcellIdList;

    @ApiModelProperty("实验编码")
    private String labCode;

    @ApiModelProperty("产品类型")
    private String productType;

    @ApiModelProperty("开始时间")
    private String dateFrom;

    @ApiModelProperty("结束时间")
    private String dateTo;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;

}
