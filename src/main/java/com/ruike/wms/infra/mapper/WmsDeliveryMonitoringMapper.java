package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO3;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO5;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringMapper
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-13 17:05
 **/
public interface WmsDeliveryMonitoringMapper {

    /**
     * 日配送任务
     *
     * @param tenantId
     * @return
     */
    List<WmsDeliveryMonitoringDTO> selectDailyDelivery(@Param("tenantId") Long tenantId);

    /**
     * 日产线配送任务
     *
     * @param tenantId
     * @return
     */
    List<WmsDeliveryMonitoringDTO3> selectProLineDelivery(@Param("tenantId") Long tenantId);

    /**
     * 每月配送任务统计
     *
     * @param tenantId
     * @return
     */
    List<WmsDeliveryMonitoringDTO5> selectMonthDelivery(@Param("tenantId") Long tenantId);
}
