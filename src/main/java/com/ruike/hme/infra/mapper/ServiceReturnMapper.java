package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.ServiceReturnQuery;
import com.ruike.hme.api.dto.representation.ServiceReturnRepresentation;
import com.ruike.hme.domain.vo.SnWorkcellVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 售后退库查询 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 15:07
 */
public interface ServiceReturnMapper {

    /**
     * 查询列表
     *
     * @param query 条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceReturnRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 03:08:42
     */
    List<ServiceReturnRepresentation> selectList(ServiceReturnQuery query);

    /**
     * 查询SN工位
     *
     * @param tenantId     租户
     * @param materialLots 物料批
     * @return java.util.List<com.ruike.hme.domain.vo.SnWorkcellVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 03:32:14
     */
    List<SnWorkcellVO> selectSnWorkcellList(@Param("tenantId") Long tenantId,
                                            @Param("list") List<String> materialLots);
}
