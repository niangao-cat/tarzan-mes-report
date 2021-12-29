package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeCosStaffProductVO;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO2;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工汇总报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/17 14:44
 */
public interface HmeCosStaffProductMapper {

    /**
     * 员工汇总报表
     *
     * @param tenantId
     * @param dto
     * @param jobTypeList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosStaffProductVO2>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    List<HmeCosStaffProductVO2> staffProductQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosStaffProductVO dto, @Param("jobTypeList") List<String> jobTypeList);

    /**
     * 工序查询工段、产线
     *
     * @param tenantId
     * @param processIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosStaffProductVO3>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    List<HmeCosStaffProductVO3> queryOrganizationInfo(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    /**
     * 工序找工位
     * @param tenantId
     * @param processIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    List<String> queryWorkcellByProcessId(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    /**
     * 工段找工位
     * @param tenantId
     * @param lineWorkcellIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    List<String> queryWorkcellByLineWorkcellId(@Param("tenantId") Long tenantId, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 产线找工位
     * @param tenantId
     * @param prodLineIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/5/17
     */
    List<String> queryWorkcellByProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList);
}
