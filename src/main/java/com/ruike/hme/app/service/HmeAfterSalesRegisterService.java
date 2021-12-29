package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeAfterSalesRegisterDTO;
import com.ruike.hme.domain.vo.HmeAfterSalesRegisterVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

public interface HmeAfterSalesRegisterService {

    Page<HmeAfterSalesRegisterVO> query(Long tenantId, HmeAfterSalesRegisterDTO dto, PageRequest pageRequest);

    List<HmeAfterSalesRegisterVO> export(Long tenantId, HmeAfterSalesRegisterDTO dto);
}
