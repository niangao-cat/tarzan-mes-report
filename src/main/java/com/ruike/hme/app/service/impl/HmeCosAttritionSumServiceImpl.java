package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.app.service.HmeCosAttritionSumService;
import com.ruike.hme.domain.repository.HmeCosAttritionSumRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/30 10:03
 */
@Service
public class HmeCosAttritionSumServiceImpl implements HmeCosAttritionSumService {

    @Autowired
    private HmeCosAttritionSumRepository hmeCosAttritionSumRepository;

    @Override
    public Page<HmeCosAttritionSumDTO> page(Long tenantId, WorkOrderAttritionSumQueryDTO query, PageRequest pageRequest) {
        return hmeCosAttritionSumRepository.page(tenantId, query, pageRequest);
    }

    @Override
    public List<HmeCosAttritionSumDTO> export(Long tenantId, WorkOrderAttritionSumQueryDTO query) {
        return hmeCosAttritionSumRepository.export(tenantId, query);
    }
}
