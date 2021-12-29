package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeAfterSalesRegisterDTO;
import com.ruike.hme.app.service.HmeAfterSalesRegisterService;
import com.ruike.hme.domain.vo.HmeAfterSalesRegisterVO;
import com.ruike.hme.infra.mapper.HmeAfterSalesRegisterMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName HmeAfterSalesRegisterServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/17 11:32
 * @Version 1.0
 **/
@Service
public class HmeAfterSalesRegisterServiceImpl implements HmeAfterSalesRegisterService {

    @Autowired
    private HmeAfterSalesRegisterMapper hmeAfterSalesRegisterMapper;

    @Override
    @ProcessLovValue
    public Page<HmeAfterSalesRegisterVO> query(Long tenantId, HmeAfterSalesRegisterDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest,
                () -> hmeAfterSalesRegisterMapper.query(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<HmeAfterSalesRegisterVO> export(Long tenantId, HmeAfterSalesRegisterDTO dto) {
        return hmeAfterSalesRegisterMapper.query(tenantId, dto);
    }

}
