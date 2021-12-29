package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/30 10:25
 */
public interface HmeCosAttritionSumRepository {

    /**
     * COS损耗汇总
     *
     * @param tenantId
     * @param query
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeCosAttritionSumDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/30
     */
    Page<HmeCosAttritionSumDTO> page(Long tenantId, WorkOrderAttritionSumQueryDTO query, PageRequest pageRequest);

    /**
     * COS损耗汇总导出
     *
     * @param tenantId
     * @param query
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosAttritionSumDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/30
     */
    List<HmeCosAttritionSumDTO> export(Long tenantId, WorkOrderAttritionSumQueryDTO query);
}
