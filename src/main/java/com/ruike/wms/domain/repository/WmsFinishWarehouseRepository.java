package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsFinishWarehouseDTO;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:31
 */
public interface WmsFinishWarehouseRepository {

    /**
     * 工及入库数量汇总报表
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsFinishWarehouseVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/21
     */
    WmsFinishWarehouseVO2 querySummary(Long tenantId, WmsFinishWarehouseDTO dto, PageRequest pageRequest);

    /**
     * 工及入库数量汇总报表-导出
     * @param tenantId
     * @param dto
     * @param response
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsFinishWarehouseVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/21
     */
    void export(Long tenantId, WmsFinishWarehouseDTO dto, HttpServletResponse response) throws IOException;
}
