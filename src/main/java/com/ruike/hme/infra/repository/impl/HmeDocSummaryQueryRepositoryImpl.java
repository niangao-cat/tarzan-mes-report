package com.ruike.hme.infra.repository.impl;


import com.ruike.hme.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.hme.domain.repository.HmeDocSummaryQueryRepository;
import com.ruike.hme.domain.vo.HmeDocSummaryQueryVO;
import com.ruike.hme.infra.mapper.HmeDocSummaryQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName HmeDocSummaryQueryRepositoryImpl
 * @Description
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:29
 * @Version 1.0
 **/
@Component
public class HmeDocSummaryQueryRepositoryImpl implements HmeDocSummaryQueryRepository {

    @Autowired
    private HmeDocSummaryQueryMapper hmeDocSummaryQueryMapper;

    @Override
    @ProcessLovValue
    public Page<HmeDocSummaryQueryVO> pageList(Long tenantId, HmeDocSummaryQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeDocSummaryQueryMapper.selectListByCondition(tenantId, dto));

    }

    @Override
    @ProcessLovValue
    public List<HmeDocSummaryQueryVO> reportExport(Long tenantId, HmeDocSummaryQueryDTO dto) {
        return hmeDocSummaryQueryMapper.selectListByCondition(tenantId, dto);
    }
}
