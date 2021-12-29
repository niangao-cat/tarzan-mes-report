package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.query.HmeLoadJobQuery;
import com.ruike.hme.api.dto.representation.HmeLoadJobRept;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 芯片装载作业服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 18:57
 */
public interface HmeLoadJobService {

    /**
     * 分页查询
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeLoadJobDTO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/3 16:53:00
     */
    Page<HmeLoadJobRept> pageList(Long tenantId, HmeLoadJobQuery dto, PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeLoadJobDTO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/3 16:53:00
     */
    List<HmeLoadJobRept> export(Long tenantId, HmeLoadJobQuery dto);
}
