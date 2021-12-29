package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.HmeCosWorkcellSummaryQuery;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * COS工位加工汇总 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 16:08
 */
public interface HmeCosWorkcellSummaryRepository {

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         参数
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    Page<HmeCosWorkcellSummaryRepresentation> pageList(Long tenantId,
                                                       HmeCosWorkcellSummaryQuery dto,
                                                       PageRequest pageRequest);

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    List<HmeCosWorkcellSummaryRepresentation> export(Long tenantId,
                                                     HmeCosWorkcellSummaryQuery dto);
}
