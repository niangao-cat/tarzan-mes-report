package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeExceptionReportVO;
import com.ruike.hme.domain.vo.HmeExceptionReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 9:39
 */
public interface HmeExceptionReportService {

    /**
     * 异常信息查看报表
     *
     * @param tenantId    租户的ID
     * @param reportVO    查询数据
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     * @author sanfeng.zhang 2020/7/14 15:03
     */
    Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest);

    /**
     * 异常信息查看报表导出
     *
     * @param tenantId      租户
     * @param reportVO      查询条件
     * @author sanfeng.zhang@hand-china.com 2021/4/13 9:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     */
    List<HmeExceptionReportVO2> queryExceptionReportExport(Long tenantId, HmeExceptionReportVO reportVO);
}
