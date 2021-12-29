package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.domain.vo.HmeEmployeeAssignExportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeEmployeeAssignReportMapper {

    /**
     * 员工制造属性查看报表
     * @param tenantId 租户Id
     * @param dto 查询参数
     * @return
     */
    List<HmeEmployeeAssignExportVO> queryList(@Param("tenantId") Long tenantId, @Param("dto") HmeEmployeeAssignDTO dto);

}
