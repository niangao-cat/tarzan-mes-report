package com.ruike.hme.api.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 售后退库查询 查询对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 14:46
 */
@Data
public class ServiceReturnQuery implements Serializable {
    private static final long serialVersionUID = -9130425045703167342L;

    @ApiModelProperty(value = "站点", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty("接收SN")
    private String snNum;
    @ApiModelProperty("翻新SN")
    private String refurbishSnNum;
    @ApiModelProperty("产品编码")
    private String materialCode;
    @ApiModelProperty("退库检测人")
    private Long returnCheckUserId;
    @ApiModelProperty("退库检测工位")
    private String returnCheckWorkcellCode;
    @ApiModelProperty("当前工位")
    private String workcellCode;
    @ApiModelProperty("退库检测时间起")
    private Date returnCheckDateStart;
    @ApiModelProperty("退库检测时间至")
    private Date returnCheckDateEnd;

    @ApiModelProperty(value = "租户ID", hidden = true)
    @JsonIgnore
    private Long tenantId;
    @ApiModelProperty(value = "接收SN", hidden = true)
    @JsonIgnore
    private List<String> snNumList;
    @JsonIgnore
    @ApiModelProperty(value = "翻新SN", hidden = true)
    private List<String> refurbishSnNumList;
    @ApiModelProperty(value = "产品编码", hidden = true)
    @JsonIgnore
    private List<String> materialCodeList;
    @ApiModelProperty(value = "退库检测工位", hidden = true)
    @JsonIgnore
    private List<String> returnCheckWorkcellCodeList;
    @ApiModelProperty(value = "当前工位", hidden = true)
    @JsonIgnore
    private List<String> workcellCodeList;

    public void initParam(Long tenantId) {
        this.tenantId = tenantId;
        snNumList = StringCommonUtils.splitIntoList(snNum);
        refurbishSnNumList = StringCommonUtils.splitIntoList(refurbishSnNum);
        materialCodeList = StringCommonUtils.splitIntoList(materialCode);
        returnCheckWorkcellCodeList = StringCommonUtils.splitIntoList(returnCheckWorkcellCode);
        workcellCodeList = StringCommonUtils.splitIntoList(workcellCode);
    }
}
