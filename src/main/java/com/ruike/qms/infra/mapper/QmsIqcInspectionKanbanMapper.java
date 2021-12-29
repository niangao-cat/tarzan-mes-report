package com.ruike.qms.infra.mapper;


import com.ruike.qms.api.dto.QmsIqcInspectionKanbanQueryDTO;
import com.ruike.qms.api.dto.QmsSupplierQualityQueryDTO;
import com.ruike.qms.domain.vo.QmsIqcInspectionKanbanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * IQC检验看板 Mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:41
 */
public interface QmsIqcInspectionKanbanMapper {

    /**
     * 查询看板数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:43:31
     */
    List<QmsIqcInspectionKanbanVO> selectKanbanList(@Param("tenantId") Long tenantId,
                                                    @Param("dto") QmsIqcInspectionKanbanQueryDTO dto,
                                                    @Param("siteId") String siteId);

    /**
     * 供应商来料质量数据查询
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:14:34
     */
    List<QmsIqcInspectionKanbanVO> selectQualityList(@Param("tenantId") Long tenantId,
                                                     @Param("dto") QmsSupplierQualityQueryDTO dto,
                                                     @Param("siteId") String siteId);

    /**
     * 用户默认站点
     *
     * @param tenantId
     * @param userId
     * @author sanfeng.zhang@hand-china.com 2021/4/20 16:14
     * @return java.util.List<java.lang.String>
     */
    List<String> queryDefaultSiteByUserId(@Param("tenantId") Long tenantId,
                                          @Param("userId") Long userId);
}
