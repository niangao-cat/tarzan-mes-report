package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;

/**
 * 泵浦源预筛选报表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-11-05 09:17:20
 */
public interface HmePumpSelectionDetailsRepository {

    /**
     * 分页查询条件转换
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 11:45:31
     * @return void
     */
    void pageQueryParamVerify(Long tenantId, HmePumpSelectionDetailsDTO dto);
}
