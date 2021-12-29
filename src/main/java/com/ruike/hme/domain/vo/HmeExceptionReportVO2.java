package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 异常信息查看报表返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 15:03
 */
@Data
@ExcelSheet(zh = "异常信息查看报表")
public class HmeExceptionReportVO2 implements Serializable {

    private static final long serialVersionUID = -8609606311354243584L;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "制造部")
    @ExcelColumn(zh = "制造部", order = 0)
    private String areaName;

    private String areaId;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间", order = 1)
    private String workshopName;

    private String workshopId;

    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线", order = 2)
    private String prodLineName;

    private String prodLineId;

    @ApiModelProperty(value = "工位")
    @ExcelColumn(zh = "工位", order = 3)
    private String workcellName;

    @ApiModelProperty(value = "班组")
    @ExcelColumn(zh = "班组", order = 4)
    private String shiftName;

    private String shiftId;

    @ApiModelProperty(value = "班次")
    @ExcelColumn(zh = "班次", order = 5)
    private String shiftCode;

    @ApiModelProperty(value = "异常产品所属工单")
    @ExcelColumn(zh = "异常产品所属工单", order = 6)
    private String workOrderNum;

    @ApiModelProperty(value = "异常产品序列号")
    @ExcelColumn(zh = "异常产品序列号", order = 7)
    private String identification;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "异常类型描述")
    @ExcelColumn(zh = "异常类型", order = 8)
    private String exceptionTypeName;

    @ApiModelProperty(value = "异常描述")
    @ExcelColumn(zh = "异常描述", order = 9)
    private String exceptionName;

    @ApiModelProperty(value = "异常物料")
    @ExcelColumn(zh = "异常物料", order = 10)
    private String materialName;

    @ApiModelProperty(value = "异常物料条码")
    @ExcelColumn(zh = "异常物料条码", order = 11)
    private String materialLotCode;

    @ApiModelProperty(value = "异常设备")
    @ExcelColumn(zh = "异常设备", order = 12)
    private String assetEncoding;

    @ApiModelProperty(value = "异常备注")
    @ExcelColumn(zh = "异常备注", order = 13)
    private String exceptionRemark;

    @ApiModelProperty(value = "异常状态")
    private String exceptionStatus;

    @ApiModelProperty(value = "异常状态描述")
    @ExcelColumn(zh = "异常状态", order = 14)
    private String exceptionStatusName;

    @ApiModelProperty(value = "异常等级")
    @ExcelColumn(zh = "异常等级", order = 15)
    private String exceptionLevel;

    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;

    @ApiModelProperty(value = "附件名称")
    @ExcelColumn(zh = "附件名称", order = 16)
    private String attachmentName;

    private String createdBy;

    @ApiModelProperty(value = "发起人")
    @ExcelColumn(zh = "发起人", order = 17)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发起时间")
    private Date creationDate;

    @ApiModelProperty(value = "发起时间")
    @ExcelColumn(zh = "发起时间", order = 18)
    private String creationDateStr;

    private String respondedBy;

    @ApiModelProperty(value = "响应人")
    @ExcelColumn(zh = "响应人", order = 19)
    private String respondedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "响应时间")
    private Date respondTime;

    @ApiModelProperty(value = "响应时间")
    @ExcelColumn(zh = "响应时间", order = 20)
    private String respondTimeStr;

    @ApiModelProperty(value = "响应备注")
    @ExcelColumn(zh = "响应备注", order = 21)
    private String respondRemark;

    private String closedBy;

    @ApiModelProperty(value = "关闭人")
    @ExcelColumn(zh = "关闭人", order = 22)
    private String closedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;

    @ApiModelProperty(value = "关闭时间")
    @ExcelColumn(zh = "关闭时间", order = 23)
    private String closeTimeStr;

    @ApiModelProperty(value = "文件列表")
    private List<HmeHzeroFileDTO> fileList;

    @ApiModelProperty("组织类型")
    private String organizationType;

    @ApiModelProperty("组织主键")
    private String organizationId;
}
