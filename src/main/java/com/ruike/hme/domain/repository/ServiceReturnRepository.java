package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.ServiceReturnQuery;
import com.ruike.hme.api.dto.representation.ServiceReturnRepresentation;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 售后退库查询 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 15:34
 */
public interface ServiceReturnRepository {
    /**
     * 查询列表
     *
     * @param query 条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceReturnRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 03:08:42
     */
    Page<ServiceReturnRepresentation> pagedList(ServiceReturnQuery query, PageRequest pageRequest);

    /**
     * 查询列表
     *
     * @param query 条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceReturnRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 03:08:42
     */
    List<ServiceReturnRepresentation> list(ServiceReturnQuery query);
}
