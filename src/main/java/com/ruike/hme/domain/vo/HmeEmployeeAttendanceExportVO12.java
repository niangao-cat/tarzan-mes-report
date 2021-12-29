package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HmeEmployeeAttendanceExportVO12 implements Serializable {
    private static final long serialVersionUID = 7025958342901998286L;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("执行作业号")
    private String eoNum;

    @ApiModelProperty("执行作业标识")
    private String identification;

    @ApiModelProperty("进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @ApiModelProperty("出站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteOutDate;

    @ApiModelProperty("返修标识")
    private String reworkFlag;

    @ApiModelProperty("加工时长")
    private BigDecimal processTime;

    @ApiModelProperty("不良记录")
    private String ncRecordId;

    @ApiModelProperty("提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    @ApiModelProperty("用户")
    private String realName;

    @ApiModelProperty("不良代码组ID")
    private String ncGroupId;

    @ApiModelProperty("不良代码组")
    private String ncGroupDescription;

    @ApiModelProperty("不良代码ID集合")
    private List<String> ncCodeIdList;

    @ApiModelProperty("不良代码集合")
    private List<String> ncCodeDescriptionList;
}
