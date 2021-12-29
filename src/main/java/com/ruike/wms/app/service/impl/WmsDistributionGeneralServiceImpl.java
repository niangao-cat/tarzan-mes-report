package com.ruike.wms.app.service.impl;


import com.ruike.qms.domain.vo.QmsIqcInspectionKanbanVO;
import com.ruike.wms.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.wms.app.service.WmsDistributionGeneralService;
import com.ruike.wms.domain.repository.WmsDistributionGeneralRepository;
import com.ruike.wms.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;
import utils.ExportUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 配送综合查询报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:16
 */
@Service
public class WmsDistributionGeneralServiceImpl implements WmsDistributionGeneralService {

    private final WmsDistributionGeneralRepository wmsDistributionGeneralRepository;

    public WmsDistributionGeneralServiceImpl(WmsDistributionGeneralRepository wmsDistributionGeneralRepository) {
        this.wmsDistributionGeneralRepository = wmsDistributionGeneralRepository;
    }

    private static final String FILE_NAME = "配送综合查询报表";
    private static final String SHEET_NAME = "配送综合查询报表";

    @Override
    public void export(Long tenantId, WmsDistributionGeneralQueryDTO dto, HttpServletResponse response) {
        List<WmsDistributionGeneralVO> exportList = wmsDistributionGeneralRepository.export(tenantId, dto);
        int i = 1;
        for (WmsDistributionGeneralVO wmsDistributionGeneralVO : exportList) {
            wmsDistributionGeneralVO.setSequence(i++);
        }
        // 写入数据,文件流会自动关闭
        ExportUtil.writeExcelOneSheet(response, exportList, FILE_NAME, SHEET_NAME, WmsDistributionGeneralVO.class);
    }
}
