package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.wms.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 配送综合查询报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:16
 */
public interface WmsDistributionGeneralService {

    /**
     * 导出分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param response
     * @return
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 05:03:23
     */
    void export(Long tenantId, WmsDistributionGeneralQueryDTO dto, HttpServletResponse response);
}
