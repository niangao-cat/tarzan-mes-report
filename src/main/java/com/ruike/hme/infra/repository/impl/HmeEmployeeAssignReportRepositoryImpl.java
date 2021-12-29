package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.domain.repository.HmeEmployeeAssignReportRepository;
import com.ruike.hme.domain.vo.HmeEmployeeAssignExportVO;
import com.ruike.hme.infra.mapper.HmeEmployeeAssignReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class HmeEmployeeAssignReportRepositoryImpl implements HmeEmployeeAssignReportRepository {

    @Autowired
    private HmeEmployeeAssignReportMapper hmeEmployeeAssignReportMapper;

    @Override
    @ProcessLovValue
    public Page<HmeEmployeeAssignExportVO> queryList(Long tenantId, HmeEmployeeAssignDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, ()->hmeEmployeeAssignReportMapper.queryList(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<HmeEmployeeAssignExportVO> exportReport(Long tenantId, HmeEmployeeAssignDTO dto) {
        List<HmeEmployeeAssignExportVO> employeeAssignExportVOList = hmeEmployeeAssignReportMapper.queryList(tenantId, dto);
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HmeEmployeeAssignExportVO employeeAssignExportVO : employeeAssignExportVOList) {
            if (employeeAssignExportVO.getDateFrom() != null) {
                employeeAssignExportVO.setDateFromStr(sdf.format(employeeAssignExportVO.getDateFrom()));
            }
            if (employeeAssignExportVO.getDateTo() != null) {
                employeeAssignExportVO.setDateToStr(sdf.format(employeeAssignExportVO.getDateTo()));
            }
        }
        return employeeAssignExportVOList;
    }
}
