package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosTestMonitorDTO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:41
 */
public interface HmeCosTestMonitorRepository {

    /***
     * COS良率监控报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosTestMonitorVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/8
     */
    Page<HmeCosTestMonitorVO> queryRecordList(Long tenantId, HmeCosTestMonitorDTO dto, PageRequest pageRequest);

    /**
     * COS良率监控导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/8 8:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosTestMonitorVO>
     */
    List<HmeCosTestMonitorVO> export(Long tenantId, HmeCosTestMonitorDTO dto);

    /**
     * COS良率监控异步导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/8 8:52
     * @return void
     */
    void asyncExport(Long tenantId, HmeCosTestMonitorDTO dto) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId
     * @param request
     * @param response
     * @author sanfeng.zhang@hand-china.com 2021/11/8 8:45
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response);
}
