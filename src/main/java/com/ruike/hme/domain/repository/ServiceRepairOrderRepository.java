package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.ServiceRepairOrderQuery;
import com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 维修订单查看报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 11:00
 */
public interface ServiceRepairOrderRepository {

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:09:14
     */
    Page<ServiceRepairOrderRepresentation> pagedList(ServiceRepairOrderQuery query, PageRequest pageRequest);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:09:14
     */
    List<ServiceRepairOrderRepresentation> export(ServiceRepairOrderQuery query);
}
