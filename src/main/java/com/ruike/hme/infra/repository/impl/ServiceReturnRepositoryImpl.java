package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.query.ServiceReturnQuery;
import com.ruike.hme.api.dto.representation.ServiceReturnRepresentation;
import com.ruike.hme.domain.repository.ServiceReturnRepository;
import com.ruike.hme.domain.vo.SnWorkcellVO;
import com.ruike.hme.infra.mapper.ServiceReturnMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Repository;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 售后退库查询 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 15:35
 */
@Repository
public class ServiceReturnRepositoryImpl implements ServiceReturnRepository {
    private final static MtUserInfo BLANK_USER = new MtUserInfo();
    private final static SnWorkcellVO BLANK_WKC = new SnWorkcellVO();
    private final ServiceReturnMapper mapper;
    private final MtUserClient userClient;

    public ServiceReturnRepositoryImpl(ServiceReturnMapper mapper, MtUserClient userClient) {
        this.mapper = mapper;
        this.userClient = userClient;
    }

    @Override
    @ProcessLovValue
    public Page<ServiceReturnRepresentation> pagedList(ServiceReturnQuery query, PageRequest pageRequest) {
        Page<ServiceReturnRepresentation> page = PageHelper.doPage(pageRequest, () -> mapper.selectList(query));
        displayFieldsCompletion(query.getTenantId(), page.getContent());
        return page;
    }

    @Override
    @ProcessLovValue
    public List<ServiceReturnRepresentation> list(ServiceReturnQuery query) {
        List<ServiceReturnRepresentation> list = mapper.selectList(query);
        displayFieldsCompletion(query.getTenantId(), list);
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, List<ServiceReturnRepresentation> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 批量查询用户
        List<Long> userIdList = list.stream().map(ServiceReturnRepresentation::getReturnCheckUserId).distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, userIdList);

        //  查询翻新条码，查询当前工位
        List<String> materialLotList = list.stream().map(ServiceReturnRepresentation::getRefurbishSnNum).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<SnWorkcellVO> snWorkcellList = CollectionUtils.isEmpty(materialLotList) ? new ArrayList<>(materialLotList.size()) : mapper.selectSnWorkcellList(tenantId, materialLotList);
        Map<String, List<SnWorkcellVO>> snWorkcellMap = CollectionUtils.isEmpty(snWorkcellList) ? new HashMap<>(16) : snWorkcellList.stream().collect(Collectors.groupingBy(SnWorkcellVO::getMaterialLotCode));
        list.forEach(rec -> {
            List<SnWorkcellVO> workcellList = snWorkcellMap.getOrDefault(rec.getRefurbishSnNum(), Collections.emptyList());
            rec.setReturnCheckUserName(userMap.getOrDefault(rec.getReturnCheckUserId(), BLANK_USER).getRealName());
            if (CollectionUtils.isNotEmpty(workcellList)) {
                rec.setWorkcellCode(workcellList.get(0).getWorkcellCode());
                rec.setWorkcellName(workcellList.get(0).getWorkcellName());
            }
        });
    }
}
