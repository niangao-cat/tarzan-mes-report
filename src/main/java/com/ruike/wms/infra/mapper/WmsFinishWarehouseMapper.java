package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsFinishWarehouseDTO;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:32
 */
public interface WmsFinishWarehouseMapper {

    /**
     * 完工数量汇总查询
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsFinishWarehouseVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/21
     */
    List<WmsFinishWarehouseVO> queryFinishSummary(@Param("tenantId") Long tenantId, @Param("dto") WmsFinishWarehouseDTO dto);

    /**
     * 入库数量汇总查询
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsFinishWarehouseVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/21
     */
    List<WmsFinishWarehouseVO> queryWarehousingSummary(@Param("tenantId") Long tenantId, @Param("dto") WmsFinishWarehouseDTO dto);


}
