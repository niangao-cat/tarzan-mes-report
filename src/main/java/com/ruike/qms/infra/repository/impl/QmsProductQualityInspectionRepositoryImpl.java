package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.domain.repository.QmsProductQualityInspectionRepository;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionEoVO;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcEoVO;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO;
import com.ruike.qms.infra.mapper.QmsProductQualityInspectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2021-05-14 10:42
 */
@Component
public class QmsProductQualityInspectionRepositoryImpl implements QmsProductQualityInspectionRepository {

    @Autowired
    private QmsProductQualityInspectionMapper qmsProductQualityInspectionMapper;

    @Override
    public String selectOperationId(Long tenantId, String siteId, String operationName) {
        return qmsProductQualityInspectionMapper.selectOperationId(tenantId, siteId, operationName);
    }

    @Override
    public List<QmsProductQualityInspectionEoVO> selectAllEo(Long tenantId, String operationId, String startTime, String endTime) {
        return qmsProductQualityInspectionMapper.selectAllEo(tenantId, operationId, startTime, endTime);
    }

    @Override
    public List<QmsProductQualityInspectionNcEoVO> selectNcEo(Long tenantId, String siteId, String operationId, String startTime, String endTime) {
        return qmsProductQualityInspectionMapper.selectNcEo(tenantId, siteId, operationId, startTime, endTime);
    }

    @Override
    public List<String> selectSiteOutEo(Long tenantId, String eoId, String operationId, String creationTime) {
        return qmsProductQualityInspectionMapper.selectSiteOutEo(tenantId, eoId, operationId, creationTime);
    }

    @Override
    public List<QmsProductQualityInspectionNcEoVO> selectEoType(Long tenantId, String siteId, List<String> eoIdList) {
        return qmsProductQualityInspectionMapper.selectEoType(tenantId, siteId, eoIdList);
    }

    @Override
    public List<QmsProductQualityInspectionNcRecordVO> selectNcRecord(Long tenantId, String siteId,
                                                                      String operationId,
                                                                      String startTime, String endTime) {
        return qmsProductQualityInspectionMapper.selectNcRecord(tenantId, siteId, operationId, startTime, endTime);
    }
}
