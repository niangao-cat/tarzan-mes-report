package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.api.dto.HmeInputRecordDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeInputRecordVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 投料汇总报表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-02-26 10:22:12
 */
public interface HmeInputRecordService {


    Page<HmeInputRecordVO> inputRecord(Long tenantId, HmeInputRecordDTO dto, PageRequest pageRequest);

    /**
     * 投料报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/13 15:56
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInputRecordVO>
     */
    List<HmeInputRecordVO> inputRecordExport(Long tenantId, HmeInputRecordDTO dto);

    /**
     * 投料报表异步导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/4 8:52
     * @return void
     */
    void asyncExport(Long tenantId, HmeInputRecordDTO dto) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId
     * @param request
     * @param response
     * @author sanfeng.zhang@hand-china.com 2021/11/4 8:45
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName);
}
