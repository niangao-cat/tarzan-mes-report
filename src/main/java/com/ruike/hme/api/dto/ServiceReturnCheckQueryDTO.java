package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 售后退库检测报表 查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 10:40
 */
@Data
public class ServiceReturnCheckQueryDTO implements Serializable {
    private static final long serialVersionUID = 4003026060384646895L;

    @ApiModelProperty("站点ID")
    @NotBlank
    private String siteId;

    @ApiModelProperty("SN")
    private String snNum;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("机型，支持模糊查询")
    private String machineType;

    @ApiModelProperty("物流公司，对应值集【HME.LOGISTICS】")
    private String logistics;

    @ApiModelProperty("物流单号，支持模糊查询")
    private String logisticsNumber;

    @ApiModelProperty("批次号，支持模糊查询")
    private String batchNumber;

    @ApiModelProperty("检验项目编码")
    private String tagGroupCode;

    @ApiModelProperty("当前状态，对应值集【HME.RECEIVE_STATUS】")
    private String receiveStatus;

    @ApiModelProperty("收货时间起")
    private Date receiptDateFrom;

    @ApiModelProperty("收货时间至")
    private Date receiptDateTo;

    @ApiModelProperty("拆箱时间起")
    private Date splitDateFrom;

    @ApiModelProperty("拆箱时间至")
    private Date splitDateTo;

    @ApiModelProperty("最后更新时间起")
    private Date lastUpdateDateFrom;

    @ApiModelProperty("最后更新时间至")
    private Date lastUpdateDateTo;


    @ApiModelProperty(value = "SN", hidden = true)
    private Set<String> snNums;
    @ApiModelProperty(value = "物料编码", hidden = true)
    private Set<String> materialCodes;
    @ApiModelProperty(value = "检验项目编码", hidden = true)
    private Set<String> tagGroupCodes;

    public void paramInit() {
        this.setSnNums(StringUtils.isBlank(this.getSnNum()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getSnNum(), ","))));
        this.setMaterialCodes(StringUtils.isBlank(this.getMaterialCode()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getMaterialCode(), ","))));
        this.setTagGroupCodes(StringUtils.isBlank(this.getTagGroupCode()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getTagGroupCode(), ","))));

    }
}
