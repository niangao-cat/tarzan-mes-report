package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeDistributionDemandQueryDTO;
import com.ruike.hme.domain.vo.HmeDistributionDemandRepresentationVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * <p>
 * 配送需求滚动报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 10:50
 */
public interface HmeDistributionDemandService {

    /**
     * 分页查询
     *
     * @param tenantId    租户
     * @param pageRequest 分页参数
     * @param query       查询
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeDistributionDemandVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 10:51:54
     */
    Page<HmeDistributionDemandRepresentationVO> page(Long tenantId, PageRequest pageRequest, HmeDistributionDemandQueryDTO query);

    /**
     * 导出
     *
     * @param tenantId 租户
     * @param query    查询
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeDistributionDemandVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 10:51:54
     */
    List<HmeDistributionDemandRepresentationVO> export(Long tenantId, HmeDistributionDemandQueryDTO query, ExportParam exportParam);
}
