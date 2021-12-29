package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.app.service.HmeProLineDetailsService;
import com.ruike.hme.domain.repository.HmeProLineDetailsRepository;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 19:59
 */
@Service
public class HmeProLineDetailsServiceImpl implements HmeProLineDetailsService {

    @Autowired
    private HmeProLineDetailsRepository hmeProLineDetailsRepository;


    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        // 车间/产线/工段，工单，产品编码查询条件必输其一
        if (StringUtils.isBlank(params.getParentOrganizationId()) && CollectionUtils.isEmpty(params.getProductionLineIds())&& CollectionUtils.isEmpty(params.getLineWorkcellIds()) && StringUtils.isBlank(params.getWorkOrderNum()) && StringUtils.isBlank(params.getMaterialId())) {
            throw new CommonException("车间/产线/工段，工单，产品编码查询条件必输其一");
        }
        return hmeProLineDetailsRepository.queryProductionLineDetails(tenantId, pageRequest, params);
    }

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        return hmeProLineDetailsRepository.queryProductShiftList(tenantId, pageRequest, params);
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        return hmeProLineDetailsRepository.queryProductProcessEoList(tenantId, pageRequest, params);
    }

    @Override
    public List<HmeProductionLineDetailsDTO> lineStationDetailsExport(Long tenantId, HmeProductionLineDetailsVO detailsVO) {
        // 车间/产线/工段，工单，产品编码查询条件必输其一
        if (StringUtils.isBlank(detailsVO.getParentOrganizationId()) && CollectionUtils.isEmpty(detailsVO.getProductionLineIds())&& CollectionUtils.isEmpty(detailsVO.getLineWorkcellIds()) && StringUtils.isBlank(detailsVO.getWorkOrderNum()) && StringUtils.isBlank(detailsVO.getMaterialId())) {
            throw new CommonException("车间/产线/工段，工单，产品编码查询条件必输其一");
        }
        return hmeProLineDetailsRepository.lineStationDetailsExport(tenantId, detailsVO);
    }
}
