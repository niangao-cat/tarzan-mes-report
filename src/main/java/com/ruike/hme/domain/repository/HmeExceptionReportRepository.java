package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeExceptionReportVO;
import com.ruike.hme.domain.vo.HmeExceptionReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 10:06
 */
public interface HmeExceptionReportRepository {

    /**
     * 异常信息查看报表
     *
     * @param tenantId    租户ID
     * @param reportVO    查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     * @author sanfeng.zhang
     * @date 020/7/14 15:37
     **/
    Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest);


    /**
     * 异常信息查看报表导出
     *
     * @param tenantId
     * @param reportVO
     * @author sanfeng.zhang@hand-china.com 2021/4/13 9:36
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     */
    List<HmeExceptionReportVO2> queryExceptionReportExport(Long tenantId, HmeExceptionReportVO reportVO);
}
