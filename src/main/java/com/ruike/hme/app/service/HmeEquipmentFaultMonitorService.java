package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEquipmentFaultMonitorDTO;
import com.ruike.hme.domain.vo.HmeEquipmentFaultMonitorVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 设备故障监控
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 11:01
 */
public interface HmeEquipmentFaultMonitorService {

    /**
     * 设备故障监控列表
     *
     * @param pageRequest 分页
     * @param dto         条件
     * @param tenantId    租户
     * @return HmeEquipmentFaultMonitorVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-10 11:07
     */
    Page<HmeEquipmentFaultMonitorVO> pageList(PageRequest pageRequest, HmeEquipmentFaultMonitorDTO dto, Long tenantId);

    /**
     * 设备故障监控列表 导出
     *
     * @param dto      条件
     * @param tenantId 租户
     * @return HmeEquipmentFaultMonitorVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-10 11:07
     */
    List<HmeEquipmentFaultMonitorVO> export(HmeEquipmentFaultMonitorDTO dto, Long tenantId);
}
