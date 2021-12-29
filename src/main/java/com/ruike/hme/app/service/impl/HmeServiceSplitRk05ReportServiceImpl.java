package com.ruike.hme.app.service.impl;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.app.service.HmeServiceSplitRk05ReportService;
import com.ruike.hme.domain.repository.HmeServiceSplitRk05ReportRepository;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 售后在制品盘点-半成品报表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
@Service
public class HmeServiceSplitRk05ReportServiceImpl implements HmeServiceSplitRk05ReportService {

    @Autowired
    private HmeServiceSplitRk05ReportRepository hmeServiceSplitRk05ReportRepository;

    @Override
    public Page<HmeServiceSplitRk05ReportVO> selectSplitRecordList(Long tenantId, HmeServiceSplitRk05ReportDTO dto, PageRequest pageRequest) {
        return hmeServiceSplitRk05ReportRepository.selectSplitRecordList(tenantId,dto,pageRequest);
    }

    @Override
    public List<HmeServiceSplitRk05ReportVO> serviceSplitRk05Export(Long tenantId, HmeServiceSplitRk05ReportDTO dto) {
        return hmeServiceSplitRk05ReportRepository.serviceSplitRk05Export(tenantId,dto);
    }
}
