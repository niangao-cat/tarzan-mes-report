package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEquipmentFaultMonitorDTO;
import com.ruike.hme.app.service.HmeEquipmentFaultMonitorService;
import com.ruike.hme.domain.vo.HmeEquipmentFaultMonitorVO;
import com.ruike.hme.infra.mapper.HmeEquipmentFaultMonitorMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 设备故障监控
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 11:01
 */
@Service
public class HmeEquipmentFaultMonitorServiceImpl implements HmeEquipmentFaultMonitorService {
    private final HmeEquipmentFaultMonitorMapper hmeEquipmentFaultMonitorMapper;

    public HmeEquipmentFaultMonitorServiceImpl(HmeEquipmentFaultMonitorMapper hmeEquipmentFaultMonitorMapper) {
        this.hmeEquipmentFaultMonitorMapper = hmeEquipmentFaultMonitorMapper;
    }

    @Override
    public Page<HmeEquipmentFaultMonitorVO> pageList(PageRequest pageRequest, HmeEquipmentFaultMonitorDTO dto, Long tenantId) {
        Page<HmeEquipmentFaultMonitorVO> list = PageHelper.doPage(pageRequest, () -> hmeEquipmentFaultMonitorMapper.pageList(tenantId, dto));
        this.displayFieldsCompletion(list.getContent());
        return list;
    }

    private void displayFieldsCompletion(List<HmeEquipmentFaultMonitorVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (HmeEquipmentFaultMonitorVO vo : list) {
                if (Objects.nonNull(vo.getCreationDate()) && Objects.nonNull(vo.getCloseTime())) {
                    vo.setExceptionTime(BigDecimal.valueOf((double) (vo.getCloseTime().getTime() - vo.getCreationDate().getTime()) / (1000 * 60 * 60)).setScale(1, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }

    @Override
    public List<HmeEquipmentFaultMonitorVO> export(HmeEquipmentFaultMonitorDTO dto, Long tenantId) {
        List<HmeEquipmentFaultMonitorVO> list = hmeEquipmentFaultMonitorMapper.pageList(tenantId, dto);
        this.displayFieldsCompletion(list);
        return list;
    }
}
