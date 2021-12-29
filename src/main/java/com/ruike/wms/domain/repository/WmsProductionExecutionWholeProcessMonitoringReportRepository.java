package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
public interface WmsProductionExecutionWholeProcessMonitoringReportRepository {

    /**
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @description 生产执行全过程监控报表
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     */
    Page<WmsProductionExecutionWholeProcessMonitoringReportVO> list(Long tenantId, PageRequest pageRequest, WmsProductionExecutionWholeProcessMonitoringReportDTO dto);

    /**
     * 生产执行全过程监控报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/26 02:01:55
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO>
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> listExport(Long tenantId, WmsProductionExecutionWholeProcessMonitoringReportDTO dto);
}
