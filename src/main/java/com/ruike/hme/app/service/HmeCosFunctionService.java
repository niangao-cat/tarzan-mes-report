package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.vo.HmeCosFunctionVO2;
import com.ruike.hme.domain.vo.HmeCosFunctionVO7;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 芯片性能表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
public interface HmeCosFunctionService {

    Page<HmeCosFunctionVO2> cosFunctionReport(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest);

    HmeCosFunctionVO7 cosFunctionListQuery(Long tenantId, HmeCosFunctionDTO2 dto);

    void cosFunctionReportExport(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException;

    void cosFunctionReportAsyncExport(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException;

    /**
     * 创建异步导出任务
     *
     * @param tenantId 租户ID
     * @param request  请求
     * @param response 响应
     * @param taskName 任务名称
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/10 04:02:11
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String taskName);

    Page<HmeCosFunctionVO2> gpCosFunctionReport(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest);

    void gpCosFunctionReportExport(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException;

    void gpCosFunctionReportAsyncExport(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException;

    Page<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest);

    Page<HmeCosFunction> cosFunctionQuery(Long tenantId, String loadSequence, PageRequest pageRequest);

    Page<HmeCosFunctionVO2> gpCosFunctionReport2(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest);

    void gpCosFunctionReportExport2(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException;

    void gpCosFunctionReportAsyncExport2(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException;
}
