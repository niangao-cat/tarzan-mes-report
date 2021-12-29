package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import org.apache.ibatis.annotations.Param;

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
public interface WmsProductionExecutionWholeProcessMonitoringReportMapper {

    List<WmsProductionExecutionWholeProcessMonitoringReportVO> list(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsProductionExecutionWholeProcessMonitoringReportDTO dto);

    /**
     * 根据工单ID批量查询工单在制数量
     *
     * @param tenantId 租户ID
     * @param workOrderIdList 工单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/29 11:20:22
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO>
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryWipQtyByWoIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单ID批量查询工单报废数量
     *
     * @param tenantId 租户ID
     * @param workOrderIdList 工单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/29 11:48:03
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO>
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryAbandonQtyByWoIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单ID批量查询工单已入库数量与工单待入库数量
     *
     * @param tenantId 租户ID
     * @param workOrderIdList 工单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/29 01:56:44
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO>
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryWoinstorkQtyByWoIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单ID、产品物料ID查询产品入库数量、产品待入库数量
     *
     * @param tenantId 租户ID
     * @param dtoList 工单ID、产品物料ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/29 04:33:59
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO>
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryEoinstorkQty(@Param(value = "tenantId") Long tenantId, @Param(value = "dtoList") List<WmsProductionExecutionWholeProcessMonitoringReportVO> dtoList);

    /**
     * 根据已有工单ID查询COS工单ID
     * @param tenantId
     * @param workOrderIdList
     * @return
     */
    List<String> queryCosWorkOrderIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单Id查询COS工单下达的eo数量
     * @param tenantId
     * @param cosWorkOrderIds
     * @return
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryCosReleasedQtyByWorkOrderIds(@Param(value = "tenantId") Long tenantId, @Param(value = "cosWorkOrderIds") List<String> cosWorkOrderIds);

    /**
     * 根据工单Id查询COS工单报废数量
     * @param tenantId
     * @param cosWorkOrderIds
     * @return
     */
    List<WmsProductionExecutionWholeProcessMonitoringReportVO> queryCosAbandonQtyByWorkOrderIds(@Param(value = "tenantId") Long tenantId, @Param(value = "cosWorkOrderIds") List<String> cosWorkOrderIds);
}
