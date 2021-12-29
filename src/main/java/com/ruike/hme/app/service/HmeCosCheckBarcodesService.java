package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 12:36
 */
public interface HmeCosCheckBarcodesService {

    /**
     * 导出分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param exportParam 导出参数
     * @return HmeCosCheckBarcodesVO
     * @author li.zhang 2021/01/19 12:36
     */
    List<HmeCosCheckBarcodesVO> exportCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto, ExportParam exportParam);
}
