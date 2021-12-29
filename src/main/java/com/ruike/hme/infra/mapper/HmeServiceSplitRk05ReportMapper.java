package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
import org.apache.ibatis.annotations.Param;

/**
 *  售后在制品盘点-半成品报表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-03-31 16:19:00
 */
public interface HmeServiceSplitRk05ReportMapper {
    /**
     * 售后在制品盘点-半成品查询
     *
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO>
     * @author penglin.sui@hand-china.com 2021/03/31 17:01
     */
    List<HmeServiceSplitRk05ReportVO> selectSplitRecord(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeServiceSplitRk05ReportDTO dto);

    /**
     * 售后在制品盘点-半成品工位查询
     *
     * @param tenantId 租户ID
     * @param splitRecordIdList 主键ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO>
     * @author penglin.sui@hand-china.com 2021/03/31 18:41
     */
    List<HmeServiceSplitRk05ReportVO> selectSplitWorkcell(@Param("tenantId") Long tenantId,
                                                          @Param("splitRecordIdList") List<String> splitRecordIdList,
                                                          @Param("dto") HmeServiceSplitRk05ReportDTO dto);

    /**
     * 售后在制品盘点-半成品导出查询
     *
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO>
     * @author penglin.sui@hand-china.com 2021/04/01 14:09
     */
    List<HmeServiceSplitRk05ReportVO> selectExportSplitRecord(@Param("tenantId") Long tenantId,
                                                              @Param("dto") HmeServiceSplitRk05ReportDTO dto);
}
