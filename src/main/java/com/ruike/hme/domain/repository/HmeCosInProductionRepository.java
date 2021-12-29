package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.domain.vo.HmeCosInNcRecordVO;
import com.ruike.hme.domain.vo.HmeCosInProductionVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description
 *
 * @author 35113 2021/01/27 12:44
 */
public interface HmeCosInProductionRepository {

    /**
     * COS在制报表查询
     *
     * @param tenant
     * @param hmeCosInProductionDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 12:49
    */
    Page<HmeCosInProductionVO> pageList(Long tenant, HmeCosInProductionDTO hmeCosInProductionDTO, PageRequest pagerequest);

    /**
     * COS在制报表导出
     *
     * @param tenant
     * @param hmeCosInProductionDTO
     * @param response
     * @author sanfeng.zhang@hand-china.com 2021/4/23 15:14
     * @return void
     */
    void export(Long tenant, HmeCosInProductionDTO hmeCosInProductionDTO, HttpServletResponse response);

    /**
     * 不良明细
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosInNcRecordVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    Page<HmeCosInNcRecordVO> ncRecordList(Long tenantId, HmeCosInProductionVO dto, PageRequest pageRequest);

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
