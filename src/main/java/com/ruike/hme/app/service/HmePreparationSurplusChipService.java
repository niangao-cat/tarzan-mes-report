package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmePreparationSurplusChipDTO;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * COS筛选剩余芯片统计报表应用服务
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:51:21
 **/
public interface HmePreparationSurplusChipService {

    /**
     * COS筛选剩余芯片统计报表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/7 01:42:25
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO>
     */
    Page<HmePreparationSurplusChipVO> listQuery(Long tenantId, HmePreparationSurplusChipDTO dto, PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/7 02:47:30
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO>
     */
    List<HmePreparationSurplusChipVO> export(Long tenantId, HmePreparationSurplusChipDTO dto);
}
