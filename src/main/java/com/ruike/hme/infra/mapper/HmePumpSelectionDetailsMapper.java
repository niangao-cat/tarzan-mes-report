package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;
import com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 泵浦源预筛选报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-05-19 11:14:12
 */
public interface HmePumpSelectionDetailsMapper {

    /**
     * 分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 01:55:27
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    List<HmePumpSelectionDetailsVO> pumpSelectionDetailsPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePumpSelectionDetailsDTO dto);

    /**
     * 根据投料工单查询组合件SN
     *
     * @param tenantId 租户ID
     * @param releaseWorkOrderNumList 投料工单集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 02:55:28
     * @return java.util.List<java.lang.String>
     */
    List<String> combMaterialLotCodeQueryByReleaseWo(@Param("tenantId") Long tenantId, @Param("releaseWorkOrderNumList") List<String> releaseWorkOrderNumList);

    /**
     * 根据泵浦源SN查询实验代码
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 泵浦源SN
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 03:20:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    List<HmePumpSelectionDetailsVO> pumpMaterialLotLabCodeQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据泵浦源SN查询组合件SN
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 泵浦源SN
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 03:42:02
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    List<HmePumpSelectionDetailsVO> combMaterialLotCodeQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据组合件SN查询投料工单
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 组合件SN
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 03:51:57
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    List<HmePumpSelectionDetailsVO> releaseWoQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
