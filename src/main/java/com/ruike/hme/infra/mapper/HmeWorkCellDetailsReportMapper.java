package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:48
 */
public interface HmeWorkCellDetailsReportMapper {

    /**
     * 工位产量明细报表
     *
     * @param tenantId  租户ID
     * @param reportVO  查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2>
     */
    List<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(@Param("tenantId") Long tenantId, @Param("reportVO") HmeWorkCellDetailsReportVO reportVO);

    List<HmeModWorkcellVO> selectByIdsCustomWorkcell(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "workcellIds") List<String> workcellIds);

    List<HmeModProductionLineVO> selectByIdsCustomProdLine(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "prodLineIds") List<String> prodLineIds);
}
