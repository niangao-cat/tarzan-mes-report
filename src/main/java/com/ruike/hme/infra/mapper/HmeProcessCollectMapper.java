package com.ruike.hme.infra.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工序采集项报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 09:35
 */
public interface HmeProcessCollectMapper {

    /**
     * 工序采集项报表查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessReportVo2>
     */
    List<HmeProcessCollectVO> selectList(@Param("tenantId") Long tenantId,
                                         @Param("dto") HmeProcessCollectQuery dto);

    /**
     * 查询采集项job详情列表
     *
     * @param tenantId 租户
     * @param jobId    job
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessJobDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 02:03:14
     */
    List<HmeProcessJobDetailVO> selectProcessJobDetailList(@Param("tenantId") Long tenantId,
                                                           @Param("jobId") String jobId);

    /**
     * 批量获取工序采集项
     *
     * @param tenantId
     * @param jobIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessCollectVO>
     * @author sanfeng.zhang@hand-china.com 2020/11/6 14:24
     */
    List<HmeProcessCollectProVO> selectCollectProBatchList(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 批量查询不良
     *
     * @param tenantId 租户
     * @param eoIdList eo
     * @return java.util.List<com.ruike.hme.domain.vo.EoNcRecordVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/19 12:49:56
     */
    List<EoNcRecordVO> selectEoNcBatchList(@Param("tenantId") Long tenantId,
                                           @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据工序查询工位
     *
     * @param tenantId 租户
     * @param dto dto
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/7/12 16:17:00
     */
    List<String> selectWorkcellIdList(@Param("tenantId") Long tenantId,
                                            @Param("dto") HmeProcessCollectQuery dto);

    /**
     * 查询表名-GP
     *
     * @param tenantId 租户
     * @param processId 工序ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessCollectVO3>
     * @author penglin.sui@hand-china.com 2021/9/10 14:16:00
     */
    @DS("gp")
    HmeProcessCollectVO3 gpSelectTableName(@Param("tenantId") Long tenantId,
                                           @Param("processId") String processId);

    /**
     * 工序采集项报表查询-GP
     *
     * @param tenantId 租户
     * @param dto
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/9/10 14:16:00
     */
    @DS("gp")
    List<HmeProcessCollectVO2> gpSelectList(@Param("tenantId") Long tenantId,
                                            @Param("dto") HmeProcessCollectQuery dto);

    /**
     * 查询最大的结果字段-GP
     *
     * @param tenantId 租户ID
     * @param operationId 工艺ID集合
     * @return java.lang.Integer
     * @author penglin.sui@hand-china.com 2021/9/13 14:54:00
     */
    @DS("gp")
    Integer gpSelectMaxSeqNum(@Param("tenantId") Long tenantId,
                              @Param("operationId") String operationId);

    /**
     * 查询最大的结果字段-GP
     *
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/9/13 14:54:00
     */
    @DS("gp")
    List<String> gpSelectDynamicColDesc(@Param("tenantId") Long tenantId,
                                        @Param("operationId") String operationId);
}
