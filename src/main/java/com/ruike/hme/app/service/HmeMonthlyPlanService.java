package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeMonthlyPlanDTO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

public interface HmeMonthlyPlanService {

    Page<HmeMonthlyPlanVO> monthlyPlanQuery(Long tenantId, HmeMonthlyPlanDTO dto, PageRequest pageRequest);

    /**
     * 月底计划达成率报表新版查询-通过中间表查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 04:32:27
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     */
    Page<HmeMonthlyPlanVO> monthlyPlanQueryNew(Long tenantId, HmeMonthlyPlanDTO dto, PageRequest pageRequest);

    /**
     * 月度计划导出
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     * @author sanfeng.zhang@hand-china.com 2021/6/10
     */
    List<HmeMonthlyPlanVO> monthlyPlanExport(Long tenantId, HmeMonthlyPlanDTO dto);

    /**
     * 月度计划导出-新版 通过中间表查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 04:57:39
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     */
    List<HmeMonthlyPlanVO> monthlyPlanExportNew(Long tenantId, HmeMonthlyPlanDTO dto);
}
