package com.ruike.hme.domain.repository;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 售后在制品盘点-半成品报表资源库
 *
 * @author penglin.sui@hand-china.com 2021-03-31 16:18:00
 */
public interface HmeServiceSplitRk05ReportRepository {
    /**
     * 售后在制品盘点-半成品查询
     *
     * @param tenantId    租户di
     * @param dto          报表参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO>
     * @author penglin.sui
     * @date 2021/3/31 18:12
     **/
    Page<HmeServiceSplitRk05ReportVO> selectSplitRecordList(Long tenantId, HmeServiceSplitRk05ReportDTO dto, PageRequest pageRequest);

    /**
     * 获取导出数据
     *
     * @param tenantId
     * @param dto
     * @author penglin.sui@hand-china.com 2021/4/1 13:54
     * @return
     */
    List<HmeServiceSplitRk05ReportVO> serviceSplitRk05Export(Long tenantId, HmeServiceSplitRk05ReportDTO dto);
}
