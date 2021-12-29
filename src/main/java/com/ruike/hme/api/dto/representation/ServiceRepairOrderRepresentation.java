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
 * 维修订单查看报表 展示对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 09:33
 */
@Data
@ExcelSheet(zh = "维修订单查看报表")
public class ServiceRepairOrderRepresentation implements Serializable {
    private static final long serialVersionUID = -7975289069742714290L;

    @JsonIgnore
    private String workOrderId;

    @ApiModelProperty(value = "接收SN")
    @ExcelColumn(zh = "接收SN")
    private String snNum;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号")
    private String model;

    @ApiModelProperty(value = "登记时间")
    @ExcelColumn(zh = "登记时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date receiveDate;

    @ApiModelProperty(value = "返回类型")
    @LovValue(lovCode = "HME.BACK_TYPE", meaningField = "backTypeMeaning")
    private String backType;

    @ApiModelProperty(value = "返回类型含义")
    @ExcelColumn(zh = "返回类型")
    private String backTypeMeaning;

    @ApiModelProperty(value = "SAP信息返回时间")
    @ExcelColumn(zh = "SAP信息返回时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date sapReturnDate;

    @ApiModelProperty(value = "维修订单号")
    @ExcelColumn(zh = "维修订单号")
    private String repairWorkOrderNum;

    @ApiModelProperty(value = "维修订单类型")
    private String internalOrderType;

    @ApiModelProperty(value = "维修订单类型含义")
    @ExcelColumn(zh = "维修订单类型")
    private String internalOrderTypeMeaning;

    @ApiModelProperty(value = "SAP创建时间")
    @ExcelColumn(zh = "SAP创建时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date sapCreationDate;

    @ApiModelProperty(value = "MES接收时间")
    @ExcelColumn(zh = "MES接收时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date mesReceiveDate;
}
