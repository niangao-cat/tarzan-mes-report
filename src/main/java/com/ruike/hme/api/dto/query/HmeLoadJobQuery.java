package com.ruike.hme.api.dto.query;

import com.ruike.hme.infra.util.StringCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 芯片装载作业 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 18:44
 */
@Data
public class HmeLoadJobQuery implements Serializable {
    private static final long serialVersionUID = 8063529543240240718L;

    @ApiModelProperty(value = "作业类型")
    private String loadJobType;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "工段ID")
    private String workcellLineId;

    @ApiModelProperty(value = "工序ID")
    private String workcellProcessId;

    @ApiModelProperty(value = "工位ID")
    private String workcellStationId;

    @ApiModelProperty(value = "工单")
    private String workNum;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "投料物料条码")
    private String bomMaterialLotCode;

    @ApiModelProperty(value = "创建人")
    private String id;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "物料编码列表", hidden = true)
    @JsonIgnore
    private List<String> materialCodeList;

    @ApiModelProperty(value = "条码列表", hidden = true)
    @JsonIgnore
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "热沉编码列表", hidden = true)
    @JsonIgnore
    private List<String> hotSinkCodeList;

    @ApiModelProperty(value = "生产线ID列表", hidden = true)
    @JsonIgnore
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "工段ID列表", hidden = true)
    @JsonIgnore
    private List<String> workcellLineIdList;

    @ApiModelProperty(value = "工序ID列表", hidden = true)
    @JsonIgnore
    private List<String> workcellProcessIdList;

    @ApiModelProperty(value = "工位ID列表", hidden = true)
    @JsonIgnore
    private List<String> workcellStationIdList;

    @ApiModelProperty(value = "工单列表", hidden = true)
    @JsonIgnore
    private List<String> workNumList;

    @ApiModelProperty(value = "wafer列表", hidden = true)
    @JsonIgnore
    private List<String> waferList;

    @ApiModelProperty(value = "实验代码列表", hidden = true)
    @JsonIgnore
    private List<String> labCodeList;

    public void initParam() {
        this.materialCodeList = StringCommonUtils.splitIntoList(this.materialCode);
        this.materialLotCodeList = StringCommonUtils.splitIntoList(this.materialLotCode);
        this.hotSinkCodeList = StringCommonUtils.splitIntoList(this.hotSinkCode);
        this.prodLineIdList = StringCommonUtils.splitIntoList(this.prodLineId);
        this.workcellLineIdList = StringCommonUtils.splitIntoList(this.workcellLineId);
        this.workcellProcessIdList = StringCommonUtils.splitIntoList(this.workcellProcessId);
        this.workcellStationIdList = StringCommonUtils.splitIntoList(this.workcellStationId);
        this.workNumList = StringCommonUtils.splitIntoList(this.workNum);
        this.waferList = StringCommonUtils.splitIntoList(this.wafer);
        this.labCodeList = StringCommonUtils.splitIntoList(this.labCode);
    }
}
