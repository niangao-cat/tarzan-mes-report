package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;
import org.hzero.core.base.AopProxy;

import java.util.List;
import java.util.Map;

/**
 * ICQ检验报表 资源库
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
public interface QmsIqcExamineReportRepository extends AopProxy<QmsIqcExamineReportRepository> {

    /**
     * 分页查询IQC检验报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 11:11:59
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsIqcExamineReportVO>
     */
    Page<QmsIqcExamineReportVO> iqcExamineReportQuery(Long tenantId, QmsIqcExamineReportDTO dto, PageRequest pageRequest);

    /**
     * IQC检验报表-饼状图查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 14:38:05
     * @return com.ruike.qms.domain.vo.QmsIqcExamineReportVO2
     */
    QmsIqcExamineReportVO2 iqcExaminePieChartQuery(Long tenantId, QmsIqcExamineReportDTO dto);

    /**
     * IQC检验报表-折线图查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 15:03:49
     * @return com.ruike.qms.domain.vo.QmsIqcExamineReportVO3
     */
    QmsIqcExamineReportVO3 iqcExamineLineChartQuery(Long tenantId, QmsIqcExamineReportDTO dto);

    /**
     * IQC检验报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/20 14:52
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcExamineReportVO>
     */
    List<QmsIqcExamineReportVO> iqcExamineReportExport(Long tenantId, QmsIqcExamineReportDTO dto);
}
