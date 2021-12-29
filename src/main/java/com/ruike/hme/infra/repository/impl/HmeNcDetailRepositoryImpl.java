package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.query.HmeNcDetailQuery;
import com.ruike.hme.domain.repository.HmeNcDetailRepository;
import com.ruike.hme.domain.vo.HmeNcDetailVO;
import com.ruike.hme.domain.vo.MaterialLotLabCodeVO;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import com.ruike.hme.infra.mapper.HmeNcDetailMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工序不良记录 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:22
 */
@Repository
public class HmeNcDetailRepositoryImpl implements HmeNcDetailRepository {
    private final HmeNcDetailMapper hmeNcDetailMapper;
    private final HmeLoadJobMapper loadJobMapper;

    public HmeNcDetailRepositoryImpl(HmeNcDetailMapper hmeNcDetailMapper, HmeLoadJobMapper loadJobMapper) {
        this.hmeNcDetailMapper = hmeNcDetailMapper;
        this.loadJobMapper = loadJobMapper;
    }


    @Override
    @ProcessLovValue
    public Page<HmeNcDetailVO> pagedList(Long tenantId, HmeNcDetailQuery dto, PageRequest pageRequest) {
        Page<HmeNcDetailVO> page = PageHelper.doPage(pageRequest, () -> hmeNcDetailMapper.selectList(tenantId, dto));
        displayFieldsCompletion(tenantId, page.getContent());
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeNcDetailVO> export(Long tenantId, HmeNcDetailQuery dto) {
        List<HmeNcDetailVO> list = hmeNcDetailMapper.selectList(tenantId, dto);
        displayFieldsCompletion(tenantId, list);
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeNcDetailVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> materialLotIds = list.stream().map(HmeNcDetailVO::getMaterialLotId).collect(Collectors.toList());
        List<MaterialLotLabCodeVO> labCodeList = loadJobMapper.selectLabCodeList(tenantId, materialLotIds);
        Map<String, String> labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(MaterialLotLabCodeVO::getMaterialLotId, Collectors.mapping(MaterialLotLabCodeVO::getLabCode, Collectors.joining("/"))));
        list.forEach(rec -> rec.setLabCode(labCodeMap.getOrDefault(rec.getMaterialId(), "")));
    }
}
