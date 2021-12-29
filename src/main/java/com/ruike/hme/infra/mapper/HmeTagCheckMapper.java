package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:45
 */
public interface HmeTagCheckMapper {

    /**
     * 查询Sn数据的进站信息
     *
     * @param tenantId
     * @param queryVO
     * @author sanfeng.zhang@hand-china.com 2021/9/1 15:11
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO>
     */
    List<HmeTagCheckVO> querySnMaterialLotCodeJobList(@Param("tenantId") Long tenantId, @Param("queryVO") HmeTagCheckQueryVO queryVO, @Param("siteId") String siteId);

    /**
     * 数据项规则展示信息
     *
     * @param tenantId
     * @param queryVO
     * @author sanfeng.zhang@hand-china.com 2021/9/1 16:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO3>
     */
    List<HmeTagCheckVO3> queryTagCheckList(@Param("tenantId") Long tenantId, @Param("queryVO") HmeTagCheckQueryVO queryVO);

    /**
     * 查询采集项记录
     *
     * @param tenantId
     * @param tagCheckVOList
     * @author sanfeng.zhang@hand-china.com 2021/9/1 18:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO5>
     */
    List<HmeTagCheckVO5> queryRecordResult(@Param("tenantId") Long tenantId, @Param("tagCheckVOList") List<HmeTagCheckVO> tagCheckVOList);

    /**
     * 查询数据项展示维护头下物料组下所有物料
     *
     * @param tenantId
     * @param RuleHeaderIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/2 1:56
     * @return java.util.List<java.lang.String>
     */
    List<String> queryItemGroupMaterialByHeaderId(@Param("tenantId") Long tenantId, @Param("ruleHeaderIdList") List<String> RuleHeaderIdList);

    /**
     * 查询Sn投料的组件条码信息
     *
     * @param tenantId
     * @param queryVO
     * @param materialIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/2 9:38
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO>
     */
    List<HmeTagCheckVO> queryCmbMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("queryVO") HmeTagCheckQueryVO queryVO, @Param("materialIdList") List<String> materialIdList);

    /**
     * 组合SN的进站记录
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/2 10:25
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO>
     */
    List<HmeTagCheckVO> queryCmbMaterialLotCodeJobList(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList, @Param("siteId") String siteId);
}
