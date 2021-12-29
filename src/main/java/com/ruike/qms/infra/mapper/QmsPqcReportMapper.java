package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
public interface QmsPqcReportMapper {

    /**
     * 巡检报表-根据事业部查询头部数据
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:59:31
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> pqcReportHeadDataQueryByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部+车间查询头部数据
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 10:36:24
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> pqcReportHeadDataQueryByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-新版查询逻辑
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/14 02:43:06
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> pqcReportHeadDataQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 根据车间Id查询车间不合格数
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/14 02:53:50
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> workshopNcNumQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 根据工序Id查询工序不合格数
     *
     * @param tenantId 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/14 02:53:50
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> processNcNumQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-明细数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 13:29:37
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    List<QmsPqcReportVO2> pgcReportDetailDataQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);


    /**
     * 巡检报表-根据事业部查询头部数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:59:31
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO3>
     */
    List<QmsPqcReportVO3> pqcReportHeadDataExportByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部+车间查询头部数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 10:36:24
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO4>
     */
    List<QmsPqcReportVO4> pqcReportHeadDataExportByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表新版导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/14 03:42:58
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO6>
     */
    List<QmsPqcReportVO6> pqcReportHeadDataExport(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-明细数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 13:29:37
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    List<QmsPqcReportVO5> pgcReportDetailDataExport(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部按时间查询-折线图 -周
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportChartVO> queryWeekByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部按时间查询-折线图 -月
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> queryMonthByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部按时间查询-折线图 -季
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> querySeasonByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部按时间查询-折线图-年
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> queryYearByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据生产线按时间查询-折线图 -周
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> queryWeekByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据生产线按时间查询-折线图 -月
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> queryMonthByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据生产线按时间查询-折线图 -季
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> querySeasonByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据生产线按时间查询-折线图 -年
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-27 15:36
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportChartVO>
     */
    List<QmsPqcReportChartVO> queryYearByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);
}
