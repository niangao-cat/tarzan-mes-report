package com.ruike.common.app.service;

import com.ruike.common.domain.vo.MesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFilePara;

import java.util.List;

public interface GetMesReportFileInfo {
    /**
     * @description:获取文件信息
     * @return:java.util.List<com.ruike.common.domain.vo.MesReportFileInfo>
     * @author: penglin.sui
     * @time: 2021/11/30 16:59
     */
    List<MesReportFileInfo> getFileInfo(Long tenantId , MesReportFilePara mesReportFilePara);
}
