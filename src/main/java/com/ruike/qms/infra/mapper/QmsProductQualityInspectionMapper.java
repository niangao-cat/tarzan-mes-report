package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.vo.QmsProductQualityInspectionEoVO;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcEoVO;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2021-05-14 10:43
 */
public interface QmsProductQualityInspectionMapper {

    /**
     * 获取工艺id
     *
     * @param tenantId      租户ID
     * @param siteId        站点id
     * @param operationName 工艺编码
     * @return java.lang.String
     * @author faming.yang@hand-china.com 2021/5/17 15:25
     */
    String selectOperationId(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("operationName") String operationName);

    /**
     * 获取所有的eo
     *
     * @param tenantId    租户ID
     * @param operationId 工艺id
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return java.util.List<com.ruike.qms.domain.vo.QmsProductQualityInspectionEoVO>
     * @author faming.yang@hand-china.com 2021/5/17 15:26
     */
    List<QmsProductQualityInspectionEoVO> selectAllEo(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                                                      @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取不良EO
     *
     * @param tenantId    租户ID
     * @param siteId      站点id
     * @param operationId 工艺id
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return java.util.List<com.ruike.qms.domain.vo.QmsProductQualityInspectionNcEoVO>
     * @author faming.yang@hand-china.com 2021/5/17 15:26
     */
    List<QmsProductQualityInspectionNcEoVO> selectNcEo(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                       @Param("operationId") String operationId,
                                                       @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询出站记录
     *
     * @param tenantId     租户ID
     * @param eoId         eoID
     * @param operationId  工艺id
     * @param creationTime EO最新一条不良提交时间
     * @return java.util.List<java.lang.String>
     * @author faming.yang@hand-china.com 2021/5/17 15:27
     */
    List<String> selectSiteOutEo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                 @Param("operationId") String operationId,
                                 @Param("creationTime") String creationTime);

    /**
     * 获取eo_id对应的型号
     *
     * @param tenantId 租户ID
     * @param siteId   站点id
     * @param eoIdList eoIDList
     * @return java.util.List<com.ruike.qms.domain.vo.QmsProductQualityInspectionNcEoVO>
     * @author faming.yang@hand-china.com 2021/5/17 15:29
     */
    List<QmsProductQualityInspectionNcEoVO> selectEoType(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                         @Param("eoIdList") List<String> eoIdList);

    /**
     * 不良记录详细信息
     *
     * @param tenantId    租户ID
     * @param siteId      站点id
     * @param operationId 工艺id
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return java.util.List<com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO>
     * @author faming.yang@hand-china.com 2021/5/17 15:30
     */
    List<QmsProductQualityInspectionNcRecordVO> selectNcRecord(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                               @Param("operationId") String operationId,
                                                               @Param("startTime") String startTime, @Param("endTime") String endTime);
}
