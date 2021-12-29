package com.ruike.hme.api.dto.representation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 售后退库查询 展示对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 14:45
 */
@Data
@ExcelSheet(zh = "售后退库查询")
public class ServiceReturnRepresentation implements Serializable {
    private static final long serialVersionUID = 4732848460415755353L;
    @ApiModelProperty("接收SN")
    @ExcelColumn(zh = "接收SN")
    private String snNum;

    @ApiModelProperty("翻新SN")
    @ExcelColumn(zh = "翻新SN")
    private String refurbishSnNum;

    @ApiModelProperty("当前SN状态")
    @LovValue(lovCode = "HME.RECEIVE_STATUS", meaningField = "receiveStatusMeaning")
    private String receiveStatus;

    @ApiModelProperty("当前SN状态含义")
    @ExcelColumn(zh = "当前SN状态")
    private String receiveStatusMeaning;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty("型号")
    @ExcelColumn(zh = "型号")
    private String model;

    @ApiModelProperty("退库检测人ID")
    @JsonIgnore
    private Long returnCheckUserId;

    @ApiModelProperty("退库检测人")
    @ExcelColumn(zh = "退库检测人")
    private String returnCheckUserName;

    @ApiModelProperty("退库检测时间")
    @ExcelColumn(zh = "退库检测时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date returnCheckDate;

    @ApiModelProperty("退库检测工位编码")
    @ExcelColumn(zh = "退库检测工位编码")
    private String returnCheckWorkcellCode;

    @ApiModelProperty("退库检测工位描述")
    @ExcelColumn(zh = "退库检测工位描述")
    private String returnCheckWorkcellName;

    @ApiModelProperty("当前工位编码")
    @ExcelColumn(zh = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty("当前工位描述")
    @ExcelColumn(zh = "当前工位描述")
    private String workcellName;
}
