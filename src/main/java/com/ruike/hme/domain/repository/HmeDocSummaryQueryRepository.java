package com.ruike.hme.domain.repository;


import com.ruike.hme.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.hme.domain.vo.HmeDocSummaryQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

public interface HmeDocSummaryQueryRepository {

    /**
     * 单据汇总查询报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author sanfeng.zhang@hand-china.com 2021/4/15 11:47
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeDocSummaryQueryVO>
     */
    Page<HmeDocSummaryQueryVO> pageList(Long tenantId, HmeDocSummaryQueryDTO dto, PageRequest pageRequest);

    /**
     * 单据汇总查询报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/15 12:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDocSummaryQueryVO>
     */
    List<HmeDocSummaryQueryVO> reportExport(Long tenantId, HmeDocSummaryQueryDTO dto);
}
