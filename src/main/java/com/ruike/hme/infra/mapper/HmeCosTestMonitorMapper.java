package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosTestMonitorDTO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:52
 */
public interface HmeCosTestMonitorMapper {

    /**
     * COS良率监控报表
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosTestMonitorVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/3
     */
    List<HmeCosTestMonitorVO> queryRecordList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosTestMonitorDTO dto);

    /**
     * wafer和工单查询进站记录
     *
     * @param tenantId
     * @param dtoList
     * @author sanfeng.zhang@hand-china.com 2021/11/8 19:54
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosTestMonitorVO2>
     */
    List<HmeCosTestMonitorVO2> queryMaterialLotCodeByWaferAndWorkOrder(@Param("tenantId") Long tenantId, @Param("dtoList") List<HmeCosTestMonitorVO> dtoList);

    /**
     * 查询进站记录
     *
     * @param tenantId
     * @param materialLotIds
     * @author sanfeng.zhang@hand-china.com 2021/11/8 20:11
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosTestMonitorVO2>
     */
    List<HmeCosTestMonitorVO2> queryEoJobSnByMaterialLotIds(@Param("tenantId") Long tenantId, @Param("materialLotIds") List<String> materialLotIds);
}
