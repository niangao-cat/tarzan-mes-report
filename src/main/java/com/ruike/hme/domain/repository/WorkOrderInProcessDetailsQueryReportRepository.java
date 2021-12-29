package com.ruike.hme.domain.repository;


import com.ruike.hme.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.hme.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @description 工单在制明细查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
public interface WorkOrderInProcessDetailsQueryReportRepository {

    /**
     * 工单在制明细查询报表
     *
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     */
    Page<WorkOrderInProcessDetailsQueryReportVO> list(Long tenantId, PageRequest pageRequest, WorkOrderInProcessDetailsQueryReportDTO dto);

    /**
     * 工单在制明细查询报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/15 14:10
     * @return java.util.List<com.ruike.hme.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     */
    List<WorkOrderInProcessDetailsQueryReportVO> processDetailsReportExport(Long tenantId, WorkOrderInProcessDetailsQueryReportDTO dto);
}
