package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.ServiceRepairOrderQuery;
import com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation;
import com.ruike.hme.domain.vo.RepairWorkOrderDateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 维修订单查看报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:07
 */
public interface ServiceRepairOrderMapper {

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:09:14
     */
    List<ServiceRepairOrderRepresentation> selectList(ServiceRepairOrderQuery query);

    /**
     * 查询客户机维修数据
     *
     * @param workOrders 维修工单号
     * @param siteId     站点
     * @return java.util.List<com.ruike.hme.domain.vo.RepairWorkOrderDateVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:44:02
     */
    List<RepairWorkOrderDateVO> selectCustomerRepairList(@Param("siteId") String siteId,
                                                         @Param("workOrders") List<String> workOrders);

    /**
     * 查询公司级维修数据
     *
     * @param snNums   维修工单号
     * @param tenantId 租户
     * @return java.util.List<com.ruike.hme.domain.vo.RepairWorkOrderDateVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 10:44:02
     */
    List<RepairWorkOrderDateVO> selectOwnRepairList(@Param("tenantId") Long tenantId,
                                                    @Param("snNums") List<String> snNums);
}
