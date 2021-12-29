package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.HmeNcDetailQuery;
import com.ruike.hme.domain.vo.HmeNcDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 工序不良记录 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:20
 */
public interface HmeNcDetailRepository {

    /**
     * 分页列表
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 04:21:30
     */
    Page<HmeNcDetailVO> pagedList(Long tenantId, HmeNcDetailQuery dto, PageRequest pageRequest);


    /**
     * 导出
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 04:22:11
     */
    List<HmeNcDetailVO> export(Long tenantId, HmeNcDetailQuery dto);
}
