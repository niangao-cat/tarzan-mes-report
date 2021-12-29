package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.wms.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 配送综合查询报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:00
 */
public interface WmsDistributionGeneralRepository {

    /**
     * 查询分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.WmsDistributionGeneralVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 05:03:23
     */
    Page<WmsDistributionGeneralVO> pageList(Long tenantId,
                                            WmsDistributionGeneralQueryDTO dto,
                                            PageRequest pageRequest);

    /**
     * 查询导出列表
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/24 22:37
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionGeneralVO>
     */
    List<WmsDistributionGeneralVO> export(Long tenantId, WmsDistributionGeneralQueryDTO dto);
}
