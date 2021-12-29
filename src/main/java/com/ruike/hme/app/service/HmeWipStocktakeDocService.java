package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeWipStocktakeDocDTO15;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 在制盘点单应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeDocService {

    /**
     * 盘点投料明细汇总
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 09:41:26
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7>
     */
    Page<HmeWipStocktakeDocVO7> releaseDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest);

    /**
     * 在制盘点投料汇总
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 11:06:10
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11>
     */
    List<HmeWipStocktakeDocVO11> releaseDetailExport(Long tenantId, HmeWipStocktakeDocDTO15 dto);
}
