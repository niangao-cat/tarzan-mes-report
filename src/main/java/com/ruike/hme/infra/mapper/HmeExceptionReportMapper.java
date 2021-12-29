package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 10:14
 */
public interface HmeExceptionReportMapper {

    /**
     * 异常信息查看报表
     *
     * @param tenantId 租户id
     * @param reportVo 查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     */
    List<HmeExceptionReportVO2> queryExceptionReportList(@Param("tenantId") Long tenantId, @Param("reportVO") HmeExceptionReportVO reportVo);

    /**
     * 批量查询用户信息
     *
     * @param tenantId
     * @param userIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/14 10:42
     * @return java.util.List<tarzan.common.domain.sys.MtUserInfo>
     */
    List<MtUserInfo> userInfoBatchGet(@Param("tenantId") Long tenantId, @Param("userIdList") List<Long> userIdList);

    /**
     * 根据id查询区域信息
     *
     * @param tenantId
     * @param areaId
     * @author sanfeng.zhang@hand-china.com 2021/4/14 10:56
     * @return com.ruike.hme.domain.vo.HmeModAreaVO
     */
    HmeModAreaVO queryAreaByAreaId(@Param("tenantId") Long tenantId, @Param("areaId") String areaId);

    /**
     * 根据车间查制造部
     *
     * @param tenantId
     * @param workshopId
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:01
     * @return com.ruike.hme.domain.vo.HmeModAreaVO
     */
    HmeModAreaVO queryAreaByWorkShopId(@Param("tenantId") Long tenantId, @Param("workshopId") String workshopId);

    /**
     * 根据id查询产线
     *
     * @param tenantId
     * @param prodLineId
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:12
     * @return com.ruike.hme.domain.vo.HmeModProductionLineVO
     */
    HmeModProductionLineVO queryProdLineById(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 根据产线找车间
     *
     * @param tenantId
     * @param prodLineId
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:14
     * @return com.ruike.hme.domain.vo.HmeModAreaVO
     */
    HmeModAreaVO queryWorkShopByProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 根据id批量查询区域信息
     *
     * @param tenantId
     * @param areaAllIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:18
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModAreaVO>
     */
    List<HmeModAreaVO> batchQueryAreaByIds(@Param("tenantId") Long tenantId, @Param("areaAllIdList") List<String> areaAllIdList);

    /**
     * 根据id批量查询产线信息
     * 
     * @param tenantId
     * @param proLineIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:22 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModProductionLineVO>
     */
    List<HmeModProductionLineVO> prodLineBasicPropertyBatchGet(@Param("tenantId") Long tenantId, @Param("proLineIdList") List<String> proLineIdList);

    /**
     * 根据模块及组类型查询类型
     *
     * @param tenantId
     * @param module
     * @param typeGroup
     * @author sanfeng.zhang@hand-china.com 2021/4/14 11:27
     * @return java.util.List<com.ruike.hme.domain.vo.HmeGenTypeVO>
     */
    List<HmeGenTypeVO> queryGenTypeByModuleAndTypeGroup(@Param("tenantId") Long tenantId, @Param("module") String module, @Param("typeGroup") String typeGroup);

    /**
     * 根据用户名、登录名查询用户信息
     * @param tenantId
     * @param loginName
     * @return java.util.List<com.ruike.hme.api.dto.HmeHzeroIamUserDTO>
     * @author sanfeng.zhang@hand-china.com 2021/5/27
     */
    List<HmeHzeroIamUserDTO> queryUserInfo(@Param("tenantId") Long tenantId, @Param("loginName") String loginName);
}
