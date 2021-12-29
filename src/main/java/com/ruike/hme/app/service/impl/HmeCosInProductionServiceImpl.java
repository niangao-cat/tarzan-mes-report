package com.ruike.hme.app.service.impl;


import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.app.service.HmeCosInProductionService;
import com.ruike.hme.domain.repository.HmeCosInProductionRepository;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * COS在制报表 服务实现
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:26
 */
@Service
public class HmeCosInProductionServiceImpl implements HmeCosInProductionService {

    @Autowired
    private HmeCosInProductionRepository hmeCosInProductionRepository;

    /**
     * COS在制报表 导出
     *
     * @param tenantId
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 16:07
    */
    @Override
    public void export(Long tenantId, HmeCosInProductionDTO dto, HttpServletResponse response) {
        hmeCosInProductionRepository.export(tenantId, dto, response);
    }

    @Override
    public void asyncExport(Long tenantId, HmeCosInProductionDTO dto) throws IOException {
        hmeCosInProductionRepository.asyncExport(tenantId, dto);
    }

    @Override
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response) {
        return hmeCosInProductionRepository.createTask(tenantId, request, response);
    }
}
