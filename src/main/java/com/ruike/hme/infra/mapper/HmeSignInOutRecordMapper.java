package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HmeSignInOutRecordMapper {
    /**
     * 根据工序查询工位
     *
     * @param tenantId  租户ID
     * @param processId 工序ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
     */
    List<String> getWorkcellByProcess(@Param(value = "tenantId") Long tenantId, @Param(value = "processId") String processId);

    /**
     * 根据工段查询工位
     *
     * @param tenantId       租户ID
     * @param lineWorkcellId 工段ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
     */
    List<String> getWorkcellByLineWorkcellId(@Param(value = "tenantId") Long tenantId, @Param(value = "lineWorkcellId") String lineWorkcellId);

    /**
     * 根据产线查询工位
     *
     * @param tenantId   租户ID
     * @param prodLineId 产线ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
     */
    List<String> getWorkcellByProdLine(@Param(value = "tenantId") Long tenantId, @Param(value = "prodLineId") String prodLineId);

    /**
     * 员工产量汇总报表-分组最小维度(员工、工序、物料、物料版本)确定
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:30:39
     */
    List<HmeEmployeeAttendanceExportVO5> sumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO13 dto);

    /**
     * 员工产量汇总报表-汇总表查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/30 14:20
     */
    List<HmeEmployeeAttendanceExportVO5> sumQuery2(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceDTO13 dto);

    /**
     * 工序查询工段 产线
     *
     * @param tenantId
     * @param processIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOrganizationVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeOrganizationVO> queryOrganizationByProcessIds(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param(value = "processIdList") List<String> processIdList);

    /**
     * 批量查询产量
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchSumCountNumber(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 批量查询在制数
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @param dateFrom
     * @param dateTo
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchSumInMakeNum(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 批量查询返修数
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @param dateFrom
     * @param dateTo
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchSumRepairNum(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 批量查询一次合格率
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @param dateFrom
     * @param dateTo
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchSumEoWorkcellGroup(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 批量查询不良的工位及EO
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @param dateFrom
     * @param dateTo
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/24
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchEoWorkcellList(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 批量生产时长
     *
     * @param tenantId
     * @param userIdList
     * @param materialIdList
     * @param materialVersionList
     * @param processIdList
     * @param dateFrom
     * @param dateTo
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeEmployeeAttendanceExportVO6> queryBatchTotalProductionTime(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList, @Param("materialIdList") List<String> materialIdList, @Param("materialVersionList") List<String> materialVersionList, @Param("processIdList") List<String> processIdList, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    /**
     * 员工产量汇总报表-实际产出查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.math.BigDecimal
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:15:22
     */
    BigDecimal getSumActualOutputNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

    /**
     * 员工产量汇总报表-不良数查询
     *
     * @param tenantId 租户ID
     * @param eoIdList 查询条件
     * @param dateFrom 起始日期
     * @param dateTo   截止日期
     * @return java.math.BigDecimal
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:40:19
     */
    BigDecimal getSumDefectsNumbTwo(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "eoIdList") List<String> eoIdList,
                                    @Param(value = "workcellIdList") List<String> workcellIdList,
                                    @Param(value = "dateFrom") Date dateFrom,
                                    @Param(value = "dateTo") Date dateTo);

    /**
     * 员工产量汇总报表-一次合格率除数查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 02:57:15
     */
    List<String> sumEoWorkcellReworkFlagNGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

    /**
     * 员工产量汇总报表-一次合格率除数批量查询
     *
     * @param tenantId 租户ID
     * @param dto  查询条件
     * @param dtoList  查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO10>
     * @author penglin.sui@hand-china.com 2021/7/30 16:48
     */
    List<HmeEmployeeAttendanceExportVO10> batchSumEoWorkcellReworkFlagNGroupQuery(@Param(value = "tenantId") Long tenantId,
                                                                                  @Param(value = "dto") HmeEmployeeAttendanceDTO13 dto,
                                                                                  @Param(value = "dtoList") List<HmeEmployeeAttendanceExportVO5> dtoList);

    /**
     * 员工产量汇总报表-一次合格率除数查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 03:00:13
     */
    List<String> sumEoWorkcellReworkFlagYGroupQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEmployeeAttendanceExportVO5 dto);

    /**
     * 员工产量汇总报表-一次合格率除数批量查询
     *
     * @param tenantId 租户ID
     * @param dto  查询条件
     * @param dtoList  查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO10>
     * @author penglin.sui@hand-china.com 2021/7/30 16:58
     */
    List<HmeEmployeeAttendanceExportVO10> batchSumEoWorkcellReworkFlagYGroupQuery(@Param(value = "tenantId") Long tenantId,
                                                                                  @Param(value = "dto") HmeEmployeeAttendanceDTO13 dto,
                                                                                  @Param(value = "dtoList") List<HmeEmployeeAttendanceExportVO5> dtoList);

    /**
     * 根据车间查询工段
     *
     * @param tenantId   租户ID
     * @param workshopId 车间ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 10:06:03
     */
    List<String> getLineWorkcellByWorkshop(@Param(value = "tenantId") Long tenantId, @Param(value = "workshopId") String workshopId,
                                           @Param(value = "siteId") String siteId);

    /**
     * 根据产线查询工段
     *
     * @param tenantId   租户ID
     * @param prodLineId 产线ID
     * @param siteId     站点ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 10:09:41
     */
    List<String> getLineWorkcellByProdLine(@Param(value = "tenantId") Long tenantId, @Param(value = "prodLineId") String prodLineId,
                                           @Param(value = "siteId") String siteId);

    /**
     * 批量根据工段查询产线
     *
     * @param tenantId           租户ID
     * @param lineWorkcellIdList 工段ID集合
     * @return tarzan.modeling.domain.entity.MtModProductionLine
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 10:27:26
     */
    List<HmeEmployeeAttendanceExportVO9> batchGetProdLineByLineWorkcell(@Param(value = "tenantId") Long tenantId, @Param(value = "lineWorkcellIdList") List<String> lineWorkcellIdList,
                                                                        @Param(value = "siteId") String siteId);

    /**
     * 批量根据班组查询标准人数
     *
     * @param tenantId   租户ID
     * @param unitIdList 班组ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 10:56:06
     */
    List<HmeEmployeeAttendanceExportVO9> batchGetEmployNumberByUnit(@Param(value = "tenantId") Long tenantId,
                                                                    @Param(value = "unitIdList") List<Long> unitIdList);

    /**
     * 批量根据班次编码、班次日期、班组查询出勤数
     *
     * @param tenantId                    租户ID
     * @param employeeAttendanceDTO16List 班次编码、班次日期、班组
     * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceDTO16>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 11:33:47
     */
    List<HmeEmployeeAttendanceDTO16> actualAttendanceBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "employeeAttendanceDTO16List") List<HmeEmployeeAttendanceDTO16> employeeAttendanceDTO16List);

    /**
     * 根据wkcShiftId、工段ID、查询条件批量查询总产量
     *
     * @param tenantId                  租户ID
     * @param employeeAttendanceDtoList wkcShiftId、工段ID集合
     * @param dto                       查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO7>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 02:01:31
     */
    List<HmeEmployeeAttendanceExportVO7> batchGetCountNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "employeeAttendanceDtoList") List<HmeEmployeeAttendanceDto> employeeAttendanceDtoList,
                                                             @Param(value = "dto") HmeEmployeeAttendanceDto1 dto);

    /**
     * 根据wkcShiftId、工段ID、查询条件批量查询实际产出
     *
     * @param tenantId                  租户ID
     * @param employeeAttendanceDtoList wkcShiftId、工段ID集合
     * @param dto                       查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO7>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 02:20:22
     */
    List<HmeEmployeeAttendanceExportVO7> batchGetActualOutputNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "employeeAttendanceDtoList") List<HmeEmployeeAttendanceDto> employeeAttendanceDtoList,
                                                                    @Param(value = "dto") HmeEmployeeAttendanceDto1 dto);

    /**
     * 根据wkcShiftId、工段ID、查询条件批量查询不良数
     *
     * @param tenantId                  租户ID
     * @param employeeAttendanceDtoList wkcShiftId、工段ID集合
     * @param dto                       查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO7>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 02:39:56
     */
    List<HmeEmployeeAttendanceExportVO7> batchGetDefectNumber(@Param(value = "tenantId") Long tenantId, @Param(value = "employeeAttendanceDtoList") List<HmeEmployeeAttendanceDto> employeeAttendanceDtoList,
                                                              @Param(value = "dto") HmeEmployeeAttendanceDto1 dto);

    /**
     * 根据部门查询主岗位下的员工
     *
     * @param tenantId   租户ID
     * @param unitIdList 部门ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 03:28:30
     */
    List<HmeEmployeeAttendanceExportVO9> batchGetEmployeeNameByUnitId(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "unitIdList") List<Long> unitIdList);

    /**
     * 批量根据工位查询工序
     *
     * @param tenantId       租户ID
     * @param workcellIdList 工位ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO6>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 03:50:52
     */
    List<HmeEmployeeAttendanceExportVO9> batchQueryProcessByWorkcell(@Param(value = "tenantId") Long tenantId,
                                                                     @Param(value = "workcellIdList") List<String> workcellIdList,
                                                                     @Param(value = "siteId") String siteId);

    /**
     * 行上数据产量取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组信息
     * @param dto        查询条件
     * @return com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO8
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 04:16:38
     */
    List<HmeEmployeeAttendanceExportVO8> lineMakeNumBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                               @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据产量明细取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO8>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 04:32:35
     */
    List<HmeEmployeeAttendanceExportVO8> lineMakeNumDetailBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                                     @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据在制数取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO8>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 04:44:30
     */
    List<HmeEmployeeAttendanceExportVO8> inMakeNumBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                             @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据在制数明细取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO8>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 04:44:30
     */
    List<HmeEmployeeAttendanceExportVO8> inMakeNumDetailBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                                   @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据不良数取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO8>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 17:03:23
     */
    List<HmeEmployeeAttendanceExportVO8> defectsNumBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                              @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据返修数取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.math.BigDecimal
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 17:05:55
     */
    List<HmeEmployeeAttendanceExportVO8> repairNumBtachQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                             @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据返修数明细取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/19 17:12:55
     */
    List<HmeEmployeeAttendanceExportVO8> repairNumDetailBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                                   @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据一次合格率-被除数取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.math.BigDecimal
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:20:35
     */
    List<HmeEmployeeAttendanceExportVO8> eoWorkcellGroupBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                                   @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 行上数据一次合格率-除数取值逻辑
     *
     * @param tenantId   租户ID
     * @param wkcShiftId 班次ID
     * @param hejs       分组维度信息
     * @param dto        限制条件
     * @return java.math.BigDecimal
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 11:20:35
     */
    List<HmeEmployeeAttendanceExportVO8> eoWorkcellReworkFlagNGroupBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "wkcShiftId") String wkcShiftId,
                                                                              @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 员工上下岗时间查询
     *
     * @param tenantId  租户ID
     * @param hejs      用户ID、工位ID
     * @param unitId    部门ID
     * @param shiftCode 班次编码
     * @param date      班次日期
     * @param operation 上下岗标识
     * @return java.util.Date
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/31 09:21:29
     */
    List<HmeEmployeeAttendanceExportVO8> mountLaidDateBatchQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "hejs") List<HmeEmployeeAttendanceRecordDto> hejs,
                                                                 @Param(value = "unitId") String unitId, @Param(value = "shiftCode") String shiftCode, @Param(value = "date") String date,
                                                                 @Param(value = "operation") String operation);

    /**
     * 头部数据查询 - 第二版逻辑 for ni.xu
     *
     * @param tenantId 租户ID
     * @param dto      查询信息
     * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceDto>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/4 08:54:21
     */
    List<HmeEmployeeAttendanceDto> headDataQuery2(@Param("tenantId") Long tenantId, @Param("dto") HmeEmployeeAttendanceDto1 dto,
                                                  @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据工位名称模糊查询工位ID
     *
     * @param tenantId     租户ID
     * @param workcellName 工位名称
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 10:22:12
     */
    List<String> workcellNameLikeQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellName") String workcellName);

    /**
     * 行表最小查询维度查询-工位、员工、物料
     *
     * @param tenantId       租户ID
     * @param workcellIdList 工段下的所有工位
     * @param wkcShiftId     班次
     * @param dto            限制条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeEmployeeAttendanceRecordDto>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/12 09:44:46
     */
    List<HmeEmployeeAttendanceRecordDto> shiftDataQueryNew(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellIdList") List<String> workcellIdList,
                                                           @Param(value = "wkcShiftId") String wkcShiftId, @Param(value = "dto") HmeEmployeeAttendanceDto5 dto);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO11>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeEmployeeAttendanceExportVO12> querySummarysDetail(@Param("tenantId") Long tenantId,
                                                              @Param("dto") HmeEmployeeAttendanceDTO17 dto);

    /**
     * 不良数据查询
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO12>
     * @author penglin.sui@hand-china.com 2021/8/5
     */
    List<HmeEmployeeAttendanceExportVO12> queryNcQtys(@Param("tenantId") Long tenantId,
                                                      @Param("dto") HmeEmployeeAttendanceDTO17 dto);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param ncRecordIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO13>
     * @author penglin.sui@hand-china.com 2021/8/5
     */
    List<HmeEmployeeAttendanceExportVO13> queryNcCode(@Param("tenantId") Long tenantId,
                                                      @Param("ncRecordIdList") List<String> ncRecordIdList);

    /**
     * 在制数据查询
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO12>
     * @author penglin.sui@hand-china.com 2021/8/5
     */
    List<HmeEmployeeAttendanceExportVO12> queryInMake(@Param("tenantId") Long tenantId,
                                                      @Param("dto") HmeEmployeeAttendanceDTO17 dto);

    /**
     * 进站信息查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/7/28 15:06
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    List<HmeEmployeeOutputSummary> selectDataOfSignIn(@Param("tenantId") Long tenantId,
                                                      @Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime);

    /**
     * 出站信息查询
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/7/28 15:06
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEmployeeOutputSummary>
     */
    List<HmeEmployeeOutputSummary> selectDataOfSignOut(@Param("tenantId") Long tenantId,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param siteId
     * @param startTime
     * @param dtoList
     * @param endTime
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeEmployeeOutputSummaryVO> querySummarys(@Param("tenantId") Long tenantId,
                                                   @Param("siteId") String siteId,
                                                   @Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime,
                                                   @Param(value = "dtoList") List<HmeEmployeeOutputSummary> dtoList);

    /**
     * 汇总数据查询
     *
     * @param tenantId
     * @param startTime
     * @param endTime
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     * @author penglin.sui@hand-china.com 2021/7/28
     */
    List<HmeEmployeeOutputSummaryVO> queryNcQty(@Param("tenantId") Long tenantId,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime,
                                                 @Param(value = "dtoList") List<HmeEmployeeOutputSummaryVO> dtoList);

    /**
     * 查询当前已经汇总成功的最大时间
     *
     * @param tenantId  租户id
     * @author penglin.sui@hand-china.com 2021/7/28 15:41
     * @return java.util.Date
     */
    Date selectMaxJobTime(@Param("tenantId") Long tenantId);
}
