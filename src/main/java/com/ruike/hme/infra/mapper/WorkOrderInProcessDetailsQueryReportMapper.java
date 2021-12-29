package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.hme.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description 工单在制明细查询报表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
public interface WorkOrderInProcessDetailsQueryReportMapper {

    /**
     * 列表查询
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     */
    List<WorkOrderInProcessDetailsQueryReportVO> list(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") WorkOrderInProcessDetailsQueryReportDTO dto);
}
