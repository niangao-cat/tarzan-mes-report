package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.query.ServiceRepairOrderQuery;
import com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation;
import com.ruike.hme.domain.repository.ServiceRepairOrderRepository;
import com.ruike.hme.domain.vo.RepairWorkOrderDateVO;
import com.ruike.hme.infra.mapper.ServiceRepairOrderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.stereotype.Repository;
import utils.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.Constant.BackType.CUSTOMER;
import static com.ruike.hme.infra.constant.Constant.BackType.OWN;

/**
 * <p>
 * 维修订单查看报表 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 11:01
 */
@Repository
public class ServiceRepairOrderRepositoryImpl implements ServiceRepairOrderRepository {
    private final static RepairWorkOrderDateVO BLANK_REPAIR_DATE = new RepairWorkOrderDateVO();
    private final ServiceRepairOrderMapper mapper;
    private final LovAdapter lovAdapter;

    public ServiceRepairOrderRepositoryImpl(ServiceRepairOrderMapper mapper, LovAdapter lovAdapter) {
        this.mapper = mapper;
        this.lovAdapter = lovAdapter;
    }

    @Override
    @ProcessLovValue
    public Page<ServiceRepairOrderRepresentation> pagedList(ServiceRepairOrderQuery query, PageRequest pageRequest) {
        return Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), getList(query));
    }

    @Override
    @ProcessLovValue
    public List<ServiceRepairOrderRepresentation> export(ServiceRepairOrderQuery query) {
        return getList(query);
    }

    private List<ServiceRepairOrderRepresentation> getList(ServiceRepairOrderQuery query) {
        List<ServiceRepairOrderRepresentation> list = mapper.selectList(query);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<String, String> backTypeMap = lovAdapter.queryLovValue("HME.BACK_TYPE", query.getTenantId()).stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getTag, (a, b) -> a));
        // 分类查询字段
        Map<String, List<ServiceRepairOrderRepresentation>> map = list.stream().filter(rec -> Objects.nonNull(rec.getBackType())).collect(Collectors.groupingBy(t -> backTypeMap.getOrDefault(t.getBackType(), null)));
        map.forEach((tag, sub) -> {
            if (CUSTOMER.equals(tag)) {
                getCustomerList(query.getTenantId(), query.getSiteId(), sub);
            } else if (OWN.equals(tag)) {
                getOwnList(query.getTenantId(), sub);
            }
        });

        // 二次筛选
        if (StringUtils.isNotBlank(query.getRepairWorkOrderNum()) || Objects.nonNull(query.getSapCreationDateStart()) || Objects.nonNull(query.getSapCreationDateEnd())) {
            list = list.stream().filter(rec -> (
                    StringUtils.isBlank(query.getRepairWorkOrderNum()) || query.getRepairWorkOrderNum().equals(rec.getRepairWorkOrderNum()))
                    && (Objects.isNull(query.getSapCreationDateStart()) || (Objects.nonNull(rec.getSapCreationDate()) && (query.getSapCreationDateStart().before(rec.getSapCreationDate()) || query.getSapCreationDateStart().equals(rec.getSapCreationDate()))))
                    && (Objects.isNull(query.getSapCreationDateEnd()) || (Objects.nonNull(rec.getSapCreationDate()) && (query.getSapCreationDateEnd().after(rec.getSapCreationDate()) || query.getSapCreationDateEnd().equals(rec.getSapCreationDate()))))
            ).collect(Collectors.toList());
        }

        return list;
    }

    private void getOwnList(Long tenantId, List<ServiceRepairOrderRepresentation> sub) {
        List<String> snNums = sub.stream().map(ServiceRepairOrderRepresentation::getSnNum).filter(Objects::nonNull).collect(Collectors.toList());
        List<RepairWorkOrderDateVO> repairList = CollectionUtils.isEmpty(snNums) ? new ArrayList<>() : mapper.selectOwnRepairList(tenantId, snNums);
        Map<String, RepairWorkOrderDateVO> repairMap = new HashMap<>(16);
        for (RepairWorkOrderDateVO repairWorkOrderDateVO:repairList) {
            if(!repairMap.containsKey(repairWorkOrderDateVO.getSnNum())){
                repairMap.put(repairWorkOrderDateVO.getSnNum(), repairWorkOrderDateVO);
            }
        }
        //Map<String, RepairWorkOrderDateVO> repairMap = CollectionUtils.isEmpty(repairList) ? new HashMap<>(16) : repairList.stream().collect(Collectors.toMap(RepairWorkOrderDateVO::getSnNum, Function.identity()));
        sub.forEach(rec -> {
            RepairWorkOrderDateVO repairWorkOrderDate = repairMap.getOrDefault(rec.getSnNum(), BLANK_REPAIR_DATE);
            rec.setInternalOrderType(repairWorkOrderDate.getInternalOrderType());
            rec.setInternalOrderTypeMeaning(repairWorkOrderDate.getInternalOrderTypeMeaning());
            rec.setRepairWorkOrderNum(repairWorkOrderDate.getRepairWorkOrderNum());
            rec.setSapCreationDate(repairWorkOrderDate.getSapCreationDate());
            rec.setMesReceiveDate(repairWorkOrderDate.getMesReceiveDate());
        });
    }

    private void getCustomerList(Long tenantId, String siteId, List<ServiceRepairOrderRepresentation> sub) {
        List<String> workOrders = sub.stream().map(ServiceRepairOrderRepresentation::getWorkOrderId).filter(Objects::nonNull).collect(Collectors.toList());
        List<RepairWorkOrderDateVO> repairList = CollectionUtils.isEmpty(workOrders) ? new ArrayList<>() : mapper.selectCustomerRepairList(siteId, workOrders);
        Map<String, RepairWorkOrderDateVO> repairMap = CollectionUtils.isEmpty(repairList) ? new HashMap<>(16) : repairList.stream().collect(Collectors.toMap(RepairWorkOrderDateVO::getRepairWorkOrderNum, Function.identity()));
        Map<String, String> internalOrderTypeMap = lovAdapter.queryLovValue("WMS.INTERNAL_ORDER_TYPE", tenantId).stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning, (a, b) -> a));
        sub.forEach(rec -> {
            RepairWorkOrderDateVO repairWorkOrderDate = repairMap.getOrDefault(rec.getWorkOrderId(), BLANK_REPAIR_DATE);
            rec.setInternalOrderType(repairWorkOrderDate.getInternalOrderType());
            rec.setInternalOrderTypeMeaning(internalOrderTypeMap.getOrDefault(rec.getInternalOrderType(), ""));
            rec.setRepairWorkOrderNum(repairWorkOrderDate.getRepairWorkOrderNum());
            rec.setSapCreationDate(repairWorkOrderDate.getSapCreationDate());
            rec.setMesReceiveDate(repairWorkOrderDate.getMesReceiveDate());
        });
    }
}
