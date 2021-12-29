package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsDistributionGapDTO;
import com.ruike.wms.api.dto.WmsDistributionGapDTO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGapService
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-19 10:55
 **/
public interface WmsDistributionGapService {

    /**
     * 物料配送缺口
     *
     * @param tenantId
     * @param pageRequest
     * @return
     */
    Page<WmsDistributionGapDTO> selectDelivery(Long tenantId, WmsDistributionGapDTO3 dto3, PageRequest pageRequest);
}
