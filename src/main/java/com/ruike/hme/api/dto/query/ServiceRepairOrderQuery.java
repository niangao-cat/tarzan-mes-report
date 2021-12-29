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
 * 维修订单查看报表 查询对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 09:53
 */
@Data
public class ServiceRepairOrderQuery implements Serializable {
    private static final long serialVersionUID = 3145049523296077496L;

    @ApiModelProperty(value = "站点", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("接收SN")
    private String snNum;
    @ApiModelProperty("产品编码")
    private String materialCode;
    @ApiModelProperty("型号")
    private String model;
    @ApiModelProperty("登记时间起")
    private Date receiveDateStart;
    @ApiModelProperty("登记时间止")
    private Date receiveDateEnd;
    @ApiModelProperty("SAP信息返回时间起")
    private Date sapReturnDateStart;
    @ApiModelProperty("SAP信息返回时间至")
    private Date sapReturnDateEnd;
    @ApiModelProperty("维修订单号")
    private String repairWorkOrderNum;
    @ApiModelProperty("SAP创建时间起")
    private Date sapCreationDateStart;
    @ApiModelProperty("SAP创建时间至")
    private Date sapCreationDateEnd;

    @ApiModelProperty(value = "产品编码列表", hidden = true)
    @JsonIgnore
    private List<String> materialCodeList;
    @ApiModelProperty(value = "接收SN列表", hidden = true)
    @JsonIgnore
    private List<String> snNumList;

    public void initParam(Long tenantId) {
        this.tenantId = tenantId;
        materialCodeList = StringCommonUtils.splitIntoList(materialCode);
        snNumList = StringCommonUtils.splitIntoList(snNum);
    }
}
