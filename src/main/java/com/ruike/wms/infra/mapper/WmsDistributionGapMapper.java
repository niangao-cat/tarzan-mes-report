package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDistributionGapDTO;
import com.ruike.wms.api.dto.WmsDistributionGapDTO2;
import com.ruike.wms.api.dto.WmsDistributionGapDTO4;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGapMapper
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-19 16:45
 **/
public interface WmsDistributionGapMapper {

    /**
     * 物料配送缺口
     *
     * @param tenantId
     * @param prodLineIdList
     * @return
     */
    List<WmsDistributionGapDTO> selectDelivery(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 查询 仓库非限制库存、质检库存
     *
     * @param locatorCodeList
     * @return
     */
    List<WmsDistributionGapDTO2> selectLotQty(@Param("locatorCodeList") List<String> locatorCodeList);


    /**
     * 次日日期最早的开班时间 - 当前系统时间
     *
     * @return
     */
    BigDecimal selectShiftTime();

    /**
     * 当日生产需求
     * @param tenantId
     * @param resultList
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionGapDTO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/28
     */
    List<WmsDistributionGapDTO4> queryCurrentDemandQty(@Param("tenantId") Long tenantId, @Param("resultList") List<WmsDistributionGapDTO> resultList);

    /**
     * 当日已配送
     * @param tenantId
     * @param resultList
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionGapDTO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/28
     */
    List<WmsDistributionGapDTO4> queryCurrentDeliveryQty(@Param("tenantId") Long tenantId, @Param("resultList") List<WmsDistributionGapDTO> resultList);

    /**
     * 次日生产需求
     * @param tenantId
     * @param resultList
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionGapDTO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/28
     */
    List<WmsDistributionGapDTO4> queryNextDemandQty(@Param("tenantId") Long tenantId, @Param("resultList") List<WmsDistributionGapDTO> resultList);

    /**
     * 次日已配送
     * @param tenantId
     * @param resultList
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionGapDTO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/28
     */
    List<WmsDistributionGapDTO4> queryNextDeliveryQty(@Param("tenantId") Long tenantId, @Param("resultList") List<WmsDistributionGapDTO> resultList);

    /** 
     * 线边仓非限制库存
     *
     * @param tenantId
     * @param resultList
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionGapDTO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/29  
     */
    List<WmsDistributionGapDTO4> queryLineNotLimitQty(@Param("tenantId") Long tenantId, @Param("resultList") List<WmsDistributionGapDTO> resultList, @Param("workcellIdList") List<String> workcellIdList);
}
