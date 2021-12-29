package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * COS工位加工异常汇总表 页面展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 10:54
 */
@Data
public class HmeCosQuantityVO implements Serializable {

    private static final long serialVersionUID = 5535650910212878088L;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("不良总数")
    private BigDecimal defectCountQuantity;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工序ID")
    private String workcellProcessId;

    @ApiModelProperty("日期")
    private Date creationDate;

    @ApiModelProperty("不良代码")
    private String ncCode;

    public HmeCosQuantityVO() {
    }

    public HmeCosQuantityVO(String workOrderNum, String waferNum, String workcellProcessId, Date creationDate) {
        this.workOrderNum = workOrderNum;
        this.waferNum = waferNum;
        this.workcellProcessId = workcellProcessId;
        this.creationDate = creationDate;
    }
}