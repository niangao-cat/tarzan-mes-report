package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.ServiceReturnCheckQueryDTO;
import com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 售后退库检测报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 11:17
 */
public interface ServiceReturnCheckMapper {

    /**
     * 查询列表
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 11:21:32
     */
    List<ServiceReturnCheckRepresentationDTO> selectList(@Param("tenantId") Long tenantId,
                                                         @Param("dto") ServiceReturnCheckQueryDTO dto);
}
