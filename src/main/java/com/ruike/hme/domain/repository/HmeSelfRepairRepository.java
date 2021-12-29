package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeSelfRepairDTO;
import com.ruike.hme.domain.vo.HmeSelfRepairVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 自制件返修统计报表
 *
 * @author xin.t@raycuslaser 2021/7/5 15:47
 */
public interface HmeSelfRepairRepository {
    /**
     * 自制件返修统计报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author xin.t@raycuslaser 2021/7/5 15:47
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeSelfRepairVO>
     */
    Page<HmeSelfRepairVO> query(Long tenantId, HmeSelfRepairDTO dto, PageRequest pageRequest);

    /**
     * 自制件返修统计报表导出
     *
     * @param tenantId
     * @param dto
     * @author xin.t@raycuslaser 2021/7/5 15:47
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSelfRepairVO2>
     */
    List<HmeSelfRepairVO> export(Long tenantId, HmeSelfRepairDTO dto);
}
