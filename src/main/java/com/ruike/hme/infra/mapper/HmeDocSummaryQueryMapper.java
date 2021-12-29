package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.hme.domain.vo.HmeDocSummaryQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeDocSummaryQueryMapper {

    /**
     * 单据汇总查询报表
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/15 11:47
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDocSummaryQueryVO>
     */
    List<HmeDocSummaryQueryVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeDocSummaryQueryDTO dto);
}
