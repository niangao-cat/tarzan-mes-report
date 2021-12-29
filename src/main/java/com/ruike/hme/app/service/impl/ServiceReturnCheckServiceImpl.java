package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.ServiceReturnCheckQueryDTO;
import com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO;
import com.ruike.hme.app.service.ServiceReturnCheckService;
import com.ruike.hme.infra.mapper.ServiceReturnCheckMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 售后退库检测报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 14:11
 */
@Service
public class ServiceReturnCheckServiceImpl implements ServiceReturnCheckService {
    private final ServiceReturnCheckMapper mapper;

    public ServiceReturnCheckServiceImpl(ServiceReturnCheckMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @ProcessLovValue
    public Page<ServiceReturnCheckRepresentationDTO> page(Long tenantId, ServiceReturnCheckQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mapper.selectList(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<ServiceReturnCheckRepresentationDTO> export(Long tenantId, ServiceReturnCheckQueryDTO dto, ExportParam exportParam) {
        return mapper.selectList(tenantId, dto);
    }
}
