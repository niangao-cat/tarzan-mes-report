package com.ruike.wms.infra.mapper;


import com.ruike.wms.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.wms.domain.vo.WmsDistributionGeneralVO;
import com.ruike.wms.domain.vo.WmsDistributionListQueryVO1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配送综合查询报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 16:30
 */
public interface WmsDistributionGeneralMapper {

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.WmsDistributionGeneralVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 04:31:47
     */
    List<WmsDistributionGeneralVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                         @Param("dto") WmsDistributionGeneralQueryDTO dto);

    /**
     * @param sourceDocId
     * @return List<HmeDistributionListQueryVO1>
     * @Description 配送单行查询
     * @Date 2020-9-2 15:53:08
     * @Created by yifan.xiong
     */
    List<WmsDistributionListQueryVO1> selectDistribution(@Param(value = "tenantId") Long tenantId, @Param("sourceDocId") String sourceDocId);

}
