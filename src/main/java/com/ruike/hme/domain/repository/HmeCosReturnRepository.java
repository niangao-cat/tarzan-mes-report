package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.domain.vo.HmeCosReturnVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/3 16:36
 */
public interface HmeCosReturnRepository {

    /***
     * COS退料报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosReturnVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/3
     */
    Page<HmeCosReturnVO> queryRecordList(Long tenantId, HmeCosReturnDTO dto, PageRequest pageRequest);

    /**
     * COS退料导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/4 8:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnVO>
     */
    List<HmeCosReturnVO> export(Long tenantId, HmeCosReturnDTO dto);

    /**
     * COS退料异步导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/4 8:52
     * @return void
     */
    void asyncExport(Long tenantId, HmeCosReturnDTO dto) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId
     * @param request
     * @param response
     * @author sanfeng.zhang@hand-china.com 2021/11/4 8:45
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response);
}
