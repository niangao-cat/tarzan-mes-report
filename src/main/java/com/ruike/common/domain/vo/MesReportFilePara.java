package com.ruike.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author penglin.sui@hand-china.com 2021/12/1 17:35
 */
@Data
public class MesReportFilePara implements Serializable {
    @ApiModelProperty("桶值")
    String bucketNameValue;

    @ApiModelProperty("目录值")
    String directoryValue;

    @ApiModelProperty("文件类型值")
    String fileTypeValue;

    @ApiModelProperty("操作类型：ASYNC_EXPORT-异步导出，DELETE_FILE-删除文件")
    String opType;
}
