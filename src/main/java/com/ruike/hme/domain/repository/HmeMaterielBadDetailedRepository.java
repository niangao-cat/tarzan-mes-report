package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:03
 */
public interface HmeMaterielBadDetailedRepository {
    /**
     * 材料不良明细报表查询
     *
     * @param tenant
     * @param hmeMaterielBadDetailedDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.ItfMaterielBadDetailedVO>
     * @auther wenqiang.yin@hand-china.com 2021/2/2 12:57
     */
    Page<HmeMaterielBadDetailedVO> pageList(Long tenant, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO, PageRequest pagerequest);

    /**
     * 材料不良明细报表导出
     *
     * @param tenantId
     * @param hmeMaterielBadDetailedDTO
     * @author sanfeng.zhang@hand-china.com 2021/4/14 17:27
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO>
     */
    List<HmeMaterielBadDetailedVO> materialNcExport(Long tenantId, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO);
}
