package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeTagCheckQueryVO;
import com.ruike.hme.domain.vo.HmeTagCheckVO;
import com.ruike.hme.domain.vo.HmeTagCheckVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:44
 */
public interface HmeTagCheckRepository {


    /**
     * 数据项展示报表
     *
     * @param tenantId
     * @param queryVO
     * @param pageRequest
     * @author sanfeng.zhang@hand-china.com 2021/9/1 12:05
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    Page<HmeTagCheckVO2> queryList(Long tenantId, HmeTagCheckQueryVO queryVO, PageRequest pageRequest);

    /**
     * 数据项展示报表 异步导出
     *
     * @param tenantId
     * @param queryVO
     * @author sanfeng.zhang@hand-china.com 2021/8/1 21:46
     * @return void
     */
    void asyncExport(Long tenantId, HmeTagCheckQueryVO queryVO) throws IOException;

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
