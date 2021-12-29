package com.ruike.hme.api.dto.query;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工序采集项报表 查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 09:36
 */
@Data
public class HmeProcessCollectQuery implements Serializable {
    private static final long serialVersionUID = -7042562219713494069L;
    private static final String DEFAULT_MATCH = "____";

    @ApiModelProperty(value = "工单代码")
    private String workOrderNum;

    @ApiModelProperty(value = "工单代码列表")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "产品代码")
    private String materialCode;

    @ApiModelProperty(value = "产品代码列表")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "序列号")
    private String sn;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "序列号列表")
    private List<String> snList;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "车间")
    private String workShopId;

    @ApiModelProperty(value = "产线")
    private String productLineId;

    @ApiModelProperty(value = "工段")
    private String lineWorkCellId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序编码列表")
    private List<String> processCodeList;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位编码列表")
    private List<String> workcellCodeList;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "eo状态")
    private String eoStatus;

    @ApiModelProperty(value = "eo状态列表")
    private List<String> eoStatusList;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "是否转型")
    private String transformFlag;

    @ApiModelProperty(value = "加工人员")
    private String userId;

    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "产品类型匹配")
    private String productMatch;

    @ApiModelProperty(value = "工序下工位ID列表")
    private List<String> workcellIdList;

    @ApiModelProperty(value = "任务")
    HmeExportTaskVO exportTaskVO;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "最大结果列")
    private Integer seqNum;

    @ApiModelProperty(value = "结果列名集合")
    private List<String> resultFieldList;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    public void validParam() {
        // 工单列表
        this.setWorkOrderNumList(StringUtils.isNotBlank(this.getWorkOrderNum()) ? Arrays.asList(this.getWorkOrderNum().split(",")) : null);
        // 物料编码列表
        this.setMaterialCodeList(StringUtils.isNotBlank(this.getMaterialCode()) ? Arrays.asList(this.getMaterialCode().split(",")) : null);
        // sn列表
        this.setSnList(StringUtils.isNotBlank(this.getSn()) ? Arrays.asList(this.getSn().split(",")) : null);
        // 工序列表
        this.setProcessCodeList(StringUtils.isNotBlank(this.getProcessCode()) ? Arrays.asList(this.getProcessCode().split(",")) : null);
        // 工位列表
        this.setWorkcellCodeList(StringUtils.isNotBlank(this.getWorkcellCode()) ? Arrays.asList(this.getWorkcellCode().split(",")) : null);
        // eo状态列表
        this.setEoStatusList(StringUtils.isNotBlank(this.getEoStatus()) ? Arrays.asList(this.getEoStatus().split(",")) : null);
        if (DEFAULT_MATCH.equals(this.productMatch)) {
            this.productMatch = null;
        }
    }
}