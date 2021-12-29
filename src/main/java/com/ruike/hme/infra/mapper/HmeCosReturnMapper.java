package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.domain.vo.HmeCosReturnVO;
import com.ruike.hme.domain.vo.HmeCosReturnVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/3 16:38
 */
public interface HmeCosReturnMapper {

    /**
     * COS退料报表
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/3
     */
    List<HmeCosReturnVO> queryRecordList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosReturnDTO dto);

    /**
     * 查询目标条码对应数量
     *
     * @param tenantId
     * @param targetMaterialLotIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnVO2>
     * @author sanfeng.zhang@hand-china.com 2021/11/11
     */
    List<HmeCosReturnVO2> queryCosReturnList(@Param("tenantId") Long tenantId, @Param("targetMaterialLotIdList") List<String> targetMaterialLotIdList);
}
