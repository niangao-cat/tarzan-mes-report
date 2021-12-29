package com.ruike.hme.app.service;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 售后在制品盘点-半成品报表应用服务
 *
 * @author penglin.sui@hand-china.com 2020-03-31 16:31:01
 */
public interface HmeServiceSplitRk05ReportService {
    /**
     * 售后在制品盘点-半成品报表
     *
     * @param tenantId    租户ID
     * @param dto    查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO>
     * @author penglin.sui 2021/3/31 18:14
     */
    Page<HmeServiceSplitRk05ReportVO> selectSplitRecordList(Long tenantId, HmeServiceSplitRk05ReportDTO dto, PageRequest pageRequest);

    /**
     * 售后在制品盘点-半成品报表导出EXCEL
     *
     * @param tenantId
     * @param dto
     * @author penglin.sui@hand-china.com 2021/4/1 13:52
     * @return void
     */

    List<HmeServiceSplitRk05ReportVO> serviceSplitRk05Export(Long tenantId, HmeServiceSplitRk05ReportDTO dto);
}
