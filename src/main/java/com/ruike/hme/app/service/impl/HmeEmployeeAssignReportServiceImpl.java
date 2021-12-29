package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.app.service.HmeEmployeeAssignReportService;
import com.ruike.hme.domain.repository.HmeEmployeeAssignReportRepository;
import com.ruike.hme.domain.vo.HmeEmployeeAssignExportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HmeEmployeeAssignReportServiceImpl implements HmeEmployeeAssignReportService {

    @Autowired
    private HmeEmployeeAssignReportRepository employeeAssignReportRepository;

    @Override
    public Page<HmeEmployeeAssignExportVO> queryList(Long tenantId, HmeEmployeeAssignDTO dto, PageRequest pageRequest) {
        return employeeAssignReportRepository.queryList(tenantId,dto,pageRequest);
    }

    @Override
    public List<HmeEmployeeAssignExportVO> exportReport(Long tenantId, HmeEmployeeAssignDTO dto) {
        return employeeAssignReportRepository.exportReport(tenantId,dto);
    }
}
