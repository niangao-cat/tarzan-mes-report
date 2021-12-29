package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.domain.vo.BomComponentWorkcellVO;
import com.ruike.hme.domain.vo.HmeCosAttritionSumVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/30 10:26
 */
public interface HmeCosAttritionSumMapper {

    /**
     * COS损耗报表
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosAttritionSumDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/30
     */
    List<HmeCosAttritionSumDTO> selectList(@Param("tenantId") Long tenantId, @Param("dto") WorkOrderAttritionSumQueryDTO dto);


    /**
     * 物料工段 工单分组查询报废数量
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosAttritionSumVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/27
     */
    List<HmeCosAttritionSumVO> processNcScrapGet(@Param("tenantId") Long tenantId,
                                                 @Param("dtoList") List<HmeCosAttritionSumDTO> dtoList,
                                                 @Param("siteId") String siteId);

    /**
     * 物料工段 工单分组查询报废数量
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosAttritionSumVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/27
     */
    List<HmeCosAttritionSumVO> processNcScrapGet2(@Param("tenantId") Long tenantId,
                                                 @Param("dtoList") List<HmeCosAttritionSumDTO> dtoList);

    /**
     * 物料工段 工单分组查询不良数量
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosAttritionSumVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/27
     */
    List<HmeCosAttritionSumVO> queryNcQuantity(@Param("tenantId") Long tenantId,
                                               @Param("dtoList") List<HmeCosAttritionSumDTO> dtoList,
                                               @Param("siteId") String siteId);

    /**
     * 根据产线及bom组件查询工段
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.BomComponentWorkcellVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/2
     */
    List<BomComponentWorkcellVO> queryLineWorkcellByBomComponentIdAndProdLineId(@Param("tenantId") Long tenantId,
                                                                                @Param("dtoList") List<BomComponentWorkcellVO> dtoList);
}
