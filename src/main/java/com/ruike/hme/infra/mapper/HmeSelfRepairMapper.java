package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeSelfRepairDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自制件返修统计报表
 *
 * @author xin.t@raycuslaser 2021/7/5 15:17
 */
public interface HmeSelfRepairMapper {
    List<HmeSelfRepairVO> fetchList(@Param("tenantId") Long tenantId, @Param("dto") HmeSelfRepairDTO dto);

    List<HmeCosWorkcellExceptionVO> queryWorkcell(@Param("tenantId") Long tenantId, @Param("workOrderNumList") List<String> workOrderNumList);
}
