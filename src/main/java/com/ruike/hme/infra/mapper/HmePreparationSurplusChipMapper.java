package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePreparationSurplusChipDTO;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS筛选剩余芯片统计报表Mapper
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:51:21
 **/
public interface HmePreparationSurplusChipMapper {

    /**
     * 报表数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/7 11:34:25
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO>
     */
    List<HmePreparationSurplusChipVO> listQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePreparationSurplusChipDTO dto);

    /**
     * 报表数据查询-V210804版本
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/4 11:29:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO>
     */
    List<HmePreparationSurplusChipVO> listQueryNew(@Param("tenantId") Long tenantId, @Param("dto") HmePreparationSurplusChipDTO dto);

    /**
     * 根据旧盒号查询selectionDetails的LoadSequence
     *
     * @param tenantId 租户ID
     * @param oldMaterialLotIdList 旧盒号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/7 01:58:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO2>
     */
    List<HmePreparationSurplusChipVO2> selectionDetailsLoadSequenceQuery(@Param("tenantId") Long tenantId, @Param("oldMaterialLotIdList") List<String> oldMaterialLotIdList);

    /**
     * 根据旧盒号查询hme_material_lot_load的LoadSequence
     *
     * @param tenantId 租户ID
     * @param oldMaterialLotIdList 旧盒号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/7 01:58:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO2>
     */
    List<HmePreparationSurplusChipVO2> materialLotLoadSequenceQuery(@Param("tenantId") Long tenantId, @Param("oldMaterialLotIdList") List<String> oldMaterialLotIdList);

    /**
     * 根据筛选批次ID查询筛选批次总数
     *
     * @param tenantId 租户ID
     * @param preSelectionIdList 筛选批次ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/4 03:29:43
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreparationSurplusChipVO>
     */
    List<HmePreparationSurplusChipVO> preSelectionCountQuery(@Param("tenantId") Long tenantId, @Param("preSelectionIdList") List<String> preSelectionIdList);
}
