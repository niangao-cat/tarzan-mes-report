package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.wms.app.service.WmsTransferSummaryService;
import com.ruike.wms.domain.repository.WmsTransferSummaryRepository;
import com.ruike.wms.domain.vo.WmsTransferSummaryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调拨汇总报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:28
 */
@Service
public class WmsTransferSummaryServiceImpl implements WmsTransferSummaryService {

    private final WmsTransferSummaryRepository wmsTransferSummaryRepository;

    public WmsTransferSummaryServiceImpl(WmsTransferSummaryRepository wmsTransferSummaryRepository) {
        this.wmsTransferSummaryRepository = wmsTransferSummaryRepository;
    }

    @Override
    public Page<WmsTransferSummaryVO> export(Long tenantId, WmsTransferSummaryQueryDTO dto, ExportParam exportParam, PageRequest pageRequest) {
        return wmsTransferSummaryRepository.pageList(tenantId, dto, pageRequest);
    }
}
