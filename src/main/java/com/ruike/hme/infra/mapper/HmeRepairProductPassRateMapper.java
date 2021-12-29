package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO3;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO4;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 返修产品直通率报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-05-19 11:14:12
 */
public interface HmeRepairProductPassRateMapper {

    /**
     * 时间段查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 11:19:07
     * @return com.ruike.hme.api.dto.HmeProdLinePassRateDTO
     */
    HmeProdLinePassRateDTO dateSlotQuery(@Param("tenantId") Long tenantId);

    /**
     * 根据工序查询在此工序上作业的物料集合
     *
     * @param tenantId 租户ID
     * @param processIdList 工序ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/26 02:51:07
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLinePassRateVO9>
     */
    List<HmeProdLinePassRateVO9> materialQuery(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList,
                                               @Param("dto") HmeProdLinePassRateDTO dto);

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
     * 根据EO查询父不良记录
     *
     * @param tenantId
     * @param eoProcessList eo工序Job集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/16 09:45:23
     * @return java.util.List<com.ruike.hme.api.dto.HmeProdLinePassRateDTO4>
     */
    List<HmeProdLinePassRateVO12> ncRecordByEoQuery(@Param("tenantId") Long tenantId, @Param("eoProcessList") List<HmeProdLinePassRateVO11> eoProcessList);

    /**
     * 根据工序+出站时间查询EO
     *
     * @param tenantId 租户ID
     * @param processIdList 工序ID集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 05:12:36
     * @return java.util.List<java.lang.String>
     */
    List<HmeProdLinePassRateVO13> getEoByProcess(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList,
                                                 @Param("dto") HmeProdLinePassRateDTO2 dto, @Param("dateFrom") String dateFrom,
                                                 @Param("dateTo") String dateTo);
}
