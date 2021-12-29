package com.ruike.hme.app.service;


import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:26
 */
public interface HmeCosInProductionService {

    /**
     * COS在制报表 导出
     *
     * @param tenantId
     * @param dto
     * @param response
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 16:07
    */
    void export(Long tenantId, HmeCosInProductionDTO dto, HttpServletResponse response);

    /**
     * COS在制报表 异步导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/8/1 21:46
     * @return void
     */
    void asyncExport(Long tenantId, HmeCosInProductionDTO dto) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId 租户ID
     * @param request  请求
     * @param response 响应
     * @author penglin.sui@hand-china.com 2021/8/6 21:07
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response);
}
