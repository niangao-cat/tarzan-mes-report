package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEquipmentFaultMonitorDTO;
import com.ruike.hme.domain.vo.HmeEquipmentFaultMonitorVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备故障监控mapper
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 11:02
 */
public interface HmeEquipmentFaultMonitorMapper {
    /**
     * 设备故障监控列表
     *
     * @param dto
     * @param tenantId
     * @return
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-10 11:15
     */
    List<HmeEquipmentFaultMonitorVO> pageList(@Param("tenantId") Long tenantId, @Param("dto") HmeEquipmentFaultMonitorDTO dto);
}
