package com.ruike.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 获取报表服务文件信息：桶名、目录名、文件类型
 * </p>
 *
 * @author penglin.sui@hand-china.com 2021/12/1 17:02
 */
@Data
public class MesReportFileInfo implements Serializable {

    private static final long serialVersionUID = -6707777392813027720L;

    @ApiModelProperty("桶名")
    private String bucketName;

    @ApiModelProperty("目录名")
    private String directory;

    @ApiModelProperty("文件类型集合")
    private List<String> fileTypeList;
}
