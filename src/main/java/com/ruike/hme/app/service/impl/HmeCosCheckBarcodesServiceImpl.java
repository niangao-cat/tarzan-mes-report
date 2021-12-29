package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.app.service.HmeCosCheckBarcodesService;
import com.ruike.hme.domain.repository.HmeCosCheckBarcodesRepository;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 12:36
 */
@Service
public class HmeCosCheckBarcodesServiceImpl implements HmeCosCheckBarcodesService {

    @Autowired
    HmeCosCheckBarcodesRepository hmeCosCheckBarcodesRepository;

    @Override
    public List<HmeCosCheckBarcodesVO> exportCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto, ExportParam exportParam) {
        return hmeCosCheckBarcodesRepository.exportCheckBarcodes(tenantId,dto);
    }
}
