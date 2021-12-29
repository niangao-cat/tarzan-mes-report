package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:07
 */
public interface HmeMaterielBadDetailedMapper {

    /**
     * 材料不良明细报表
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/14 17:08
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO>
     */
    List<HmeMaterielBadDetailedVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                         @Param("dto") HmeMaterielBadDetailedDTO dto);

    /**
     * 查询
     * @param tenantId
     * @param materialLotIdList
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/19
     */
    List<HmeMaterielBadDetailedVO> queryReleaseQty(@Param("tenantId") Long tenantId,
                           @Param("materialLotIdList") List<String> materialLotIdList,
                           @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据条码ID、工单ID在表hme_eo_job_material中批量查询投料数量
     *
     * @param tenantId 租户ID
     * @param dtoList 条码ID、工单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/3 02:31:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO>
     */
    List<HmeMaterielBadDetailedVO> queryReleaseQtyInEoJobMaterial(@Param("tenantId") Long tenantId,
                                                   @Param("dtoList") List<HmeMaterielBadDetailedVO> dtoList);

    /**
     * 根据条码ID、工单ID在表hme_eo_job_sn_lot_material中批量查询投料数量
     *
     * @param tenantId 租户ID
     * @param dtoList 条码ID、工单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/3 02:56:57
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO>
     */
    List<HmeMaterielBadDetailedVO> queryReleaseQtyInEoJobSnLotMaterial(@Param("tenantId") Long tenantId,
                                                                  @Param("dtoList") List<HmeMaterielBadDetailedVO> dtoList);
}
