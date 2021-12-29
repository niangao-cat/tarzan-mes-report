package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO2;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO4;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.core.domain.Page;

import java.util.List;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringService
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-13 16:55
 **/
public interface WmsDeliveryMonitoringService {

    /**
     * 日配送任务
     *
     * @param tenantId
     * @return
     */
    List<WmsDeliveryMonitoringDTO> selectDailyDelivery(Long tenantId);

    /**
     * 日产线配送任务
     *
     * @param tenantId
     * @param pageRequest
     * @return
     */
    Page<WmsDeliveryMonitoringDTO2> selectProLineDelivery(Long tenantId, PageRequest pageRequest);

    /**
     * 每月配送任务统计
     *
     * @param tenantId
     * @return
     */
    WmsDeliveryMonitoringDTO4 selectMonthDelivery(Long tenantId);
}
