package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeEmployeeAttendanceDTO13
 *
 * @author penglin.sui@hand-china.com 2021-07-12 19:44:00
 **/
@Data
public class HmeEmployeeAttendanceDTO13 implements Serializable {
    private static final long serialVersionUID = 966118084019124333L;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线ID集合,后端自用")
    @JsonIgnore
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "工段ID")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工段ID集合,后端自用")
    @JsonIgnore
    private List<String> lineWorkcellIdList;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序ID集合,后端自用")
    @JsonIgnore
    private List<String> processIdList;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "员工ID集合,后端自用")
    private List<String> userIdList;

    @ApiModelProperty(value = "时间从", required = true)
    private Date dateFrom;

    @ApiModelProperty(value = "时间至", required = true)
    private Date dateTo;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料ID集合,后端自用")
    private List<String> materialIdList;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料版本集合,后端自用")
    private List<String> materialVersionList;

    @ApiModelProperty(value = "工位ID集合,后端自用")
    private List<String> workcellIdList;

    @ApiModelProperty(value = "创建任务")
    private HmeExportTaskVO exportTaskVO;
}
