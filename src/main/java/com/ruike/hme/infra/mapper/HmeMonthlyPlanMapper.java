package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.HmeMonthlyPlanDTO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeMonthlyPlanMapper {

    List<HmeMonthlyPlanVO> monthlyPlanQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 根据月份 部门 查询月度计划
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO2>
     * @author sanfeng.zhang@hand-china.com 2021/6/15
     */
    List<HmeMonthlyPlanVO2> queryMonthPlanByAreaId(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 查询COS物料的完工数
     *
     * @param tenantId
     * @param cosMaterialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     * @author sanfeng.zhang@hand-china.com 2021/6/17
     */
    List<HmeMonthlyPlanVO> queryFinishQtyByCosMaterialIds(@Param(value = "tenantId") Long tenantId, @Param(value = "cosMaterialIdList") List<String> cosMaterialIdList, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 月底计划达成率报表新版查询-通过中间表查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 04:36:30
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     */
    List<HmeMonthlyPlanVO> monthlyPlanQueryNew(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeMonthlyPlanDTO dto);
}
