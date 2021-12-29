package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeDistributionDemandQueryDTO;
import com.ruike.hme.domain.vo.HmeDistributionDemandQtyVO;
import com.ruike.hme.domain.vo.HmeDistributionDemandVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配送需求滚动报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 10:53
 */
public interface HmeDistributionDemandMapper {

    /**
     * 查询需求列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDistributionDemandVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 01:57:07
     */
    List<HmeDistributionDemandVO> selectDemandList(@Param("tenantId") Long tenantId,
                                                   @Param("dto") HmeDistributionDemandQueryDTO dto);

    /**
     * 查询线边数量列表
     *
     * @param tenantId 租户
     * @param dtos     条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDistributionDemandQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 03:34:06
     */
    List<HmeDistributionDemandQtyVO> selectWorkcellQtyList(@Param("tenantId") Long tenantId,
                                                           @Param("dtos") Iterable<HmeDistributionDemandQtyVO> dtos);

    /**
     * 查询库存数量列表
     *
     * @param tenantId 租户
     * @param dtos     条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDistributionDemandQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 03:34:06
     */
    List<HmeDistributionDemandQtyVO> selectInventoryQtyList(@Param("tenantId") Long tenantId,
                                                            @Param("dtos") Iterable<HmeDistributionDemandQtyVO> dtos);
}
