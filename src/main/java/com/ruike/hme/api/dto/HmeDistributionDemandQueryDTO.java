package com.ruike.hme.api.dto;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.infra.util.DatetimeUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 配送需求滚动报表 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 09:33
 */
@Data
public class HmeDistributionDemandQueryDTO implements Serializable {
    private static final long serialVersionUID = -5403774906040349334L;

    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("生产线")
    private String prodLineId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "开始日期", hidden = true)
    private Date startDate;
    @ApiModelProperty(value = "结束日期", hidden = true)
    private Date endDate;

    public void paramInit() {
        this.setStartDate(DatetimeUtils.getBeginOfDate(new Date()));
        this.setEndDate(DateUtils.addDays(this.getStartDate(), 3));
    }
}
