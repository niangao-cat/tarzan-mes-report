package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.app.service.HmeCosWipQueryService;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO2;
import com.ruike.hme.domain.vo.WorkOrderProductionVO;
import com.ruike.hme.infra.mapper.HmeCosWipQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruike.hme.infra.constant.Constant.ConstantValue.MAP_DEFAULT_CAPACITY;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 16:30
 */
@Service
public class HmeCosWipQueryServiceImpl implements HmeCosWipQueryService {

    private final static MtUserInfo BLANK_USER = new MtUserInfo();
    private final static WorkOrderProductionVO BLANK_WO = new WorkOrderProductionVO();
    private final HmeCosWipQueryMapper mapper;
    private final MtUserClient userClient;

    public HmeCosWipQueryServiceImpl(HmeCosWipQueryMapper mapper, MtUserClient userClient) {
        this.mapper = mapper;
        this.userClient = userClient;
    }

    private List<String> queryMaterialLot(Long tenantId, HmeCosWipQueryDTO dto){
        List<String> materialLotIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getMaterialLotCode())){
            List<HmeCosWipQueryVO2> hmeCosWipQueryVO2List = mapper.selectMaterialLot(tenantId , dto);
            if(CollectionUtils.isNotEmpty(hmeCosWipQueryVO2List)){
                materialLotIdList = hmeCosWipQueryVO2List.stream()
                        .map(HmeCosWipQueryVO2::getMaterialLotId)
                        .filter(Objects::nonNull)
                        .distinct()
                .collect(Collectors.toList());
            }
        }

        if(StringUtils.isNotBlank(dto.getWaferNum())){
            List<HmeCosWipQueryVO2> hmeCosWipQueryVO2List = mapper.selectMaterialLotIdOfWafer(tenantId , dto);
            if(CollectionUtils.isNotEmpty(hmeCosWipQueryVO2List)){
                List<String> subMaterialLotIdList = hmeCosWipQueryVO2List.stream()
                                .map(HmeCosWipQueryVO2::getMaterialLotId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(subMaterialLotIdList)){
                    //取交集
                    materialLotIdList.retainAll(subMaterialLotIdList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
        }

        return materialLotIdList;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosWipQueryVO> propertyCosWipQuery(Long tenantId, HmeCosWipQueryDTO dto, PageRequest pageRequest) {

        List<String> materialLotIdList = queryMaterialLot(tenantId,dto);
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            dto.setMaterialLotIds(materialLotIdList);
        }

        Page<HmeCosWipQueryVO> page = PageHelper.doPage(pageRequest, () -> mapper.cosWipQuery(tenantId, dto));
        displayFieldsCompletion(tenantId, page.getContent());
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosWipQueryVO> cosWipExport(Long tenantId, HmeCosWipQueryDTO dto) {

        List<String> materialLotIdList = queryMaterialLot(tenantId,dto);
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            dto.setMaterialLotIds(materialLotIdList);
        }

        List<HmeCosWipQueryVO> list = mapper.cosWipQuery(tenantId, dto);
        displayFieldsCompletion(tenantId, list);
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeCosWipQueryVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 批量查用户
        List<Long> userIds = Stream.concat(list.stream().map(HmeCosWipQueryVO::getSiteInById), list.stream().map(HmeCosWipQueryVO::getSiteOutById)).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, userIds);

        // 批量查工单
        List<String> workOrderIds = list.stream().map(HmeCosWipQueryVO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, WorkOrderProductionVO> workOrderMap = CollectionUtils.isNotEmpty(workOrderIds) ? mapper.selectWorkOrderProduction(tenantId, workOrderIds).stream().collect(Collectors.toMap(WorkOrderProductionVO::getWorkOrderId, Function.identity())) : new HashMap<>(MAP_DEFAULT_CAPACITY);

        list.forEach(rec -> {
            WorkOrderProductionVO workOrderProduction = workOrderMap.getOrDefault(rec.getWorkOrderId(), BLANK_WO);
            rec.setSiteInBy(userMap.getOrDefault(rec.getSiteInById(), BLANK_USER).getRealName());
            rec.setSiteOutBy(userMap.getOrDefault(rec.getSiteOutById(), BLANK_USER).getRealName());
            rec.setWorkOrderNum(workOrderProduction.getWorkOrderNum());
            rec.setProductionCode(workOrderProduction.getMaterialCode());
            rec.setProductionName(workOrderProduction.getMaterialName());
        });
    }
}
