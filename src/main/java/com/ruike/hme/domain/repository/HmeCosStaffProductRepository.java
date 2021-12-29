package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeCosStaffProductVO;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/17 14:33
 */
public interface HmeCosStaffProductRepository {


    /**
     * COS员工汇总报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosStaffProductVO2>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    Page<HmeCosStaffProductVO2> staffProductQuery(Long tenantId, HmeCosStaffProductVO dto, PageRequest pageRequest);

    /**
     * COS员工汇总报表导出
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosStaffProductVO2>
     * @author sanfeng.zhang@hand-china.com 2021/5/21
     */
    List<HmeCosStaffProductVO2> staffProductExport(Long tenantId, HmeCosStaffProductVO dto);
}
