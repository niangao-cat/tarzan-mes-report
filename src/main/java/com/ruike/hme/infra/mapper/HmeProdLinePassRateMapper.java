package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO3;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO4;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品直通率报表Mapper
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:22:12
 **/
public interface HmeProdLinePassRateMapper {

    /**
     * 时间段查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 11:19:07
     * @return com.ruike.hme.api.dto.HmeProdLinePassRateDTO
     */
    HmeProdLinePassRateDTO dateSlotQuery(@Param("tenantId") Long tenantId);

    /**
     * 工序查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 02:11:49
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeProdLinePassRateDTO dto);

    /**
     * 根据工序查询所属产线
     * 
     * @param tenantId 租户ID
     * @param processIdList 工序ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 02:32:49 
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO3
     */
    List<HmeProdLinePassRateVO10> prodLineQuery(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    /**
     * 根据工序查询在此工序上作业的物料集合
     *
     * @param tenantId 租户ID
     * @param processIdList 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 02:51:07
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO9> materialQuery(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList,
                                               @Param("dto")  HmeProdLinePassRateDTO dto);

    /**
     * 投产数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param materialId 物料ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 04:00:00
     * @return java.lang.Long
     */
    Long productionNumQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                            @Param("materialId") String materialId, @Param("dto")  HmeProdLinePassRateDTO dto);

    /**
     * 合格数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param materialId 物料ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 04:16:58
     * @return java.util.List<java.lang.String>
     */
    List<String> passNumQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                         @Param("materialId") String materialId, @Param("dto")  HmeProdLinePassRateDTO dto);

    /**
     * 不良数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param materialId 物料ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 05:44:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO4>
     */
    List<HmeProdLinePassRateVO4> ncNumQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                                            @Param("materialId") String materialId, @Param("dto")  HmeProdLinePassRateDTO dto);

    /**
     * 产品日直通率报表-工序查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:36:35
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processDayQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeProdLinePassRateDTO2 dto,
                                                 @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

    /**
     * 产品日直通率报表-根据工序查询在此工序上作业的班次集合
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:42:56
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> shiftQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                                            @Param("dto")  HmeProdLinePassRateDTO2 dto);

    /**
     * 产品日直通率报表-投产数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 11:12:34
     * @return java.lang.Long
     */
    Long productionNumDayQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                               @Param("dto")  HmeProdLinePassRateDTO2 dto, @Param("dateFrom") String dateFrom,
                               @Param("dateTo") String dateTo);

    /**
     * 产品日直通率报表-合格数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 11:18:45
     * @return java.util.List<java.lang.String>
     */
    List<String> passNumDayQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                                 @Param("dto")  HmeProdLinePassRateDTO2 dto, @Param("dateFrom") String dateFrom,
                                 @Param("dateTo") String dateTo);

    /**
     * 产品日直通率报表-不良数查询
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 11:25:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO4>
     */
    List<HmeProdLinePassRateVO4> ncNumDayQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId,
                                               @Param("dto")  HmeProdLinePassRateDTO2 dto, @Param("dateFrom") String dateFrom,
                                               @Param("dateTo") String dateTo);

    /**
     * 根据工序ID查询工序信息
     *
     * @param tenantId 租户ID
     * @param processId 多个工序ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 11:31:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processByIdQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId);

    /**
     * 根据工段ID查询工序信息
     *
     * @param tenantId 租户ID
     * @param lineWorkcellId 多个工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 11:31:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processByLineWorkcellQuery(@Param("tenantId") Long tenantId, @Param("lineWorkcellId") String lineWorkcellId);

    /**
     * 根据产线ID查询工序信息
     *
     * @param tenantId 租户ID
     * @param prodLineId 多个产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 11:31:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processByProdLineQuery(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 根据部门ID查询工序信息
     *
     * @param tenantId 租户ID
     * @param departmentId 多个部门ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 11:31:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO3>
     */
    List<HmeProdLinePassRateVO3> processByDepartmentQuery(@Param("tenantId") Long tenantId, @Param("departmentId") String departmentId);

    /**
     * 根据工序+物料+出站时间查询EO
     *
     * @param tenantId 租户ID
     * @param processMaterialList 工序物料集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 05:12:36
     * @return java.util.List<java.lang.String>
     */
    List<HmeProdLinePassRateVO11> getEoByProcessAndMaterial(@Param("tenantId") Long tenantId, @Param("processMaterialList") List<HmeProdLinePassRateVO9> processMaterialList,
                                                            @Param("dto") HmeProdLinePassRateDTO dto);

    /**
     * 根据EO与工序查询父不良记录
     *
     * @param tenantId
     * @param eoProcessList eo工序集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/16 09:45:23
     * @return java.util.List<com.ruike.hme.api.dto.HmeProdLinePassRateDTO4>
     */
    List<HmeProdLinePassRateVO12> ncRecordByEoQuery(@Param("tenantId") Long tenantId, @Param("eoProcessList") List<HmeProdLinePassRateVO11> eoProcessList);

    /**
     * 根据父不良记录ID查询不良信息
     *
     * @param tenantId 租户ID
     * @param ncRecordIdList 父不良记录ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/16 10:23:33
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO4>
     */
    List<HmeProdLinePassRateVO4> ncRecordDetailQuery(@Param("tenantId") Long tenantId, @Param("ncRecordIdList") List<String> ncRecordIdList);

    /**
     * 根据工序+出站时间查询EO
     *
     * @param tenantId 租户ID
     * @param processIdList 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 05:12:36
     * @return java.util.List<java.lang.String>
     */
    List<HmeProdLinePassRateVO13> getEoByProcess(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList,
                                                 @Param("dto")  HmeProdLinePassRateDTO2 dto, @Param("dateFrom") String dateFrom,
                                                 @Param("dateTo") String dateTo);
}
