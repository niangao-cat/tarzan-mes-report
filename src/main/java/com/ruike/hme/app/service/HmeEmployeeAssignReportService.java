package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.domain.vo.HmeEmployeeAssignExportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;


import java.util.List;

public interface HmeEmployeeAssignReportService {
    /**
     * 员工制造属性查看报表
     * @param tenantId 租户Id
     * @param dto 查询参数
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeEmployeeAssignExportVO> queryList(Long tenantId, HmeEmployeeAssignDTO dto, PageRequest pageRequest);

    /**
     * 导出员工制造属性报表
     * @param tenantId 租户Id
     * @param dto 查询参数
     * @return
     */
    List<HmeEmployeeAssignExportVO> exportReport(Long tenantId, HmeEmployeeAssignDTO dto);
}
