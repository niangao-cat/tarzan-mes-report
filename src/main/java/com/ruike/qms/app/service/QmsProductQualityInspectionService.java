package com.ruike.qms.app.service;

import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:30
 */
public interface QmsProductQualityInspectionService {


    /**
     * 成品质量检验看板
     *
     * @param tenantId 租户ID
     * @return com.ruike.qms.domain.vo.QmsProductQualityInspectionVO
     * @author faming.yang@hand-china.com 2021/5/17 15:32
     */
    QmsProductQualityInspectionVO qualityInspection(Long tenantId);

    /**
     * 不良情况说明
     *
     * @param tenantId    租户Id
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO>
     * @author faming.yang@hand-china.com 2021/5/17 15:32
     */
    List<QmsProductQualityInspectionNcRecordVO> ncRecord(Long tenantId);
}
