package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO12;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Classname HmeEmployeeAttendanceExportService
 * @Description 员工产量报表
 * @Date  2021/7/12 19:47
 * @Created by penglin.sui
 */
public interface HmeEmployeeAttendanceService {
    /**
     * 员工产量汇总报表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author penglin.sui@hand-china.com 2021/7/12 19:48:00
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    Page<HmeEmployeeAttendanceExportVO5> sumQuery(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest);

    /**
     * 员工产量汇总报表分页查询第二版
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/16 01:51:07
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    Page<HmeEmployeeAttendanceExportVO5> sumQueryNew(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest);

    /**
     * 员工产量汇总Job
     *
     * @param tenantId 租户ID
     * @param startTime 时间起
     * @param endTime   时间至
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/16 02:11:16
     * @return void
     */
    List<HmeEmployeeAttendanceExportVO5> employeeOutPutSummary(Long tenantId, Date startTime, Date endTime,
                                                               HmeEmployeeAttendanceDTO13 queryDto);

    /**
     * 员工产量汇总导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 04:42:43
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    List<HmeEmployeeAttendanceExportVO5> sumExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto);

    /**
     * 员工产量汇总导出第二版
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/17 09:57:35
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    List<HmeEmployeeAttendanceExportVO5> sumExportNew(Long tenantId, HmeEmployeeAttendanceDTO13 dto);

    /**
     * 根据条件查询第一个表数据集
     * @param tenantId 租户id
     * @param dto
     * @author jianfeng.xia01@hand-china.com 2020/7/27 17:13
     * @return [tenantId, dto]
     */
    Page<HmeEmployeeAttendanceDto> headDataQuery(Long tenantId, HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest);

    /**
     * 查找员工考勤明细
     * @param tenantId 租户ID
     * @param dto 头数据信息
     * @pageRequest pageRequest 分页信息
     * @author jianfeng.xia01@hand-china.com 2020/7/29 9:51
     * @return [tenantId, dto]
     */
    Page<HmeEmployeeAttendanceRecordDto> lineDataQuery(Long tenantId, HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest);

    /**
     * 工段产量导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEmployeeAttendanceDto> lineWorkcellProductExport(Long tenantId, HmeEmployeeAttendanceDto1 dto);

    /**
     * 员工产量汇总报表明细查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author penglin.sui@hand-china.com 2021/7/12 19:48:00
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     */
    Page<HmeEmployeeAttendanceExportVO12> querySummarysDetail(Long tenantId, HmeEmployeeAttendanceDTO17 dto, PageRequest pageRequest);

    /**
     * 异步导出
     *
     * @param tenantId
     * @param dto
     * @param request
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/11/10
     */
    void asyncExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId
     * @param request
     * @param response
     * @param fileName
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     * @author sanfeng.zhang@hand-china.com 2021/11/10
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName);
}
