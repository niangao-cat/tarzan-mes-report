package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeWipStocktakeDocDTO15;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocInfoVO;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7;
import feign.Param;

import java.util.List;

/**
 * 在制盘点单Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeDocMapper {

    /**
     * 非COS盘点单投料明细汇总
     *
     * @param tenantId 租户ID
     * @param stocktakeIdList 盘点单ID集合
     * @param itemGroupList COS物料组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 09:36:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7>
     */
    List<HmeWipStocktakeDocVO7> releaseDetailPageQueryNoCos(@Param("tenantId") Long tenantId,
                                                            @Param("stocktakeIdList") List<String> stocktakeIdList,
                                                            @Param("itemGroupList") List<String> itemGroupList,
                                                            @Param("dto") HmeWipStocktakeDocDTO15 dto);

    /**
     * 在制盘点汇总投料导出
     *
     * @param tenantId 租户ID
     * @param stocktakeIdList 盘点单ID集合
     * @param itemGroupList COS物料组集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 11:08:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11>
     */
    List<HmeWipStocktakeDocVO11> releaseDetailExport(@Param("tenantId") Long tenantId,
                                                     @Param("stocktakeIdList") List<String> stocktakeIdList,
                                                     @Param("itemGroupList") List<String> itemGroupList,
                                                     @Param("dto") HmeWipStocktakeDocDTO15 dto);

    /**
     * 根据盘点单ID查询相关信息
     *
     * @param tenantId 租户ID
     * @param stocktakeId 盘点单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/2 03:10:27
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeDocInfoVO
     */
    HmeWipStocktakeDocInfoVO wipStocktakeDocInfoQuery(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);
}
