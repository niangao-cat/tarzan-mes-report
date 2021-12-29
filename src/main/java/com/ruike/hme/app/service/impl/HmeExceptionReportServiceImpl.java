package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeExceptionReportService;
import com.ruike.hme.domain.repository.HmeExceptionReportRepository;
import com.ruike.hme.domain.vo.HmeExceptionReportVO;
import com.ruike.hme.domain.vo.HmeExceptionReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 10:02
 */
@Service
public class HmeExceptionReportServiceImpl implements HmeExceptionReportService {

    @Autowired
    private HmeExceptionReportRepository hmeExceptionReportRepository;

    @Override
    public Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest) {
        return hmeExceptionReportRepository.queryExceptionReportList(tenantId,reportVO,pageRequest);
    }

    @Override
    public List<HmeExceptionReportVO2> queryExceptionReportExport(Long tenantId, HmeExceptionReportVO reportVO) {
        return hmeExceptionReportRepository.queryExceptionReportExport(tenantId, reportVO);
    }
}
