package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产线日明细报表Mapper
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:20
 */
public interface HmeProLineDetailsWipMapper {

    /**
     * 获取当前用户对应站点id
     *
     * @param userId
     * @return
     */
    String getSiteIdByUserId(@Param("userId") Long userId);

    /**
     * 功能描述: 在制查询报表 查询
     *
     * @param siteId     工厂
     * @param prodLineId 产线编号
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionQueryDTO>
     * @author bao.xu@hand-china.com 2020/7/13 11:32
     */
    List<HmeProductionQueryDTO> queryProductDetails(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineId") String prodLineId,
                                                    @Param("productType") String productType, @Param("productClassification") String productClassification,
                                                    @Param("productCode") String productCode, @Param("productModel") String productModel,
                                                    @Param("materialId") String materialId);
    /**
     * 查询待上线数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectQueueNumByMaterialList(@Param("tenantId") Long tenantId,
                                               @Param("prodLineId") String prodLineId,
                                               @Param("siteId") String siteId,
                                               @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询未入库库存数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectUnCountByMaterialList(@Param("tenantId") Long tenantId,
                                              @Param("prodLineId") String prodLineId,
                                              @Param("materialIdList") List<String> materialIdList);

    /**
     * 批量查询运行数量和完成数量 查询
     *
     * @param tenantId
     * @param prodLineId
     * @param siteId
     * @param materialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/8 19:05
     */
    List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(@Param(value = "tenantId") Long tenantId,
                                                                  @Param("siteId") String siteId,
                                                                  @Param(value = "prodLineId") String prodLineId,
                                                                  @Param(value = "materialIdList") List<String> materialIdList);

    /**
     * 在制报表-eo信息(运行)
     *
     * @param tenantId         租户id
     * @param workcellId       工序id
     * @param materialId       物料id
     * @param eoIdentification eo标识
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:12
     */
    List<HmeProductEoInfoVO> queryProductEoListByRun(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "workcellId") String workcellId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);

    /**
     * 在制报表-eo信息（库存）
     *
     * @param tenantId          租户id
     * @param workcellId        工序id
     * @param materialId        物料id
     * @param eoIdentification  eo标识
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductEoListByFinish(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "workcellId") String workcellId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);
    /**
     * 在制报表-eo信息（待上线）
     *
     * @param tenantId         租户id
     * @param productionLineId 产线id
     * @param materialId       物料id
     * @param eoIdentification eo标识
     * @param siteId           站点id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:10
     */
    List<HmeProductEoInfoVO> queryProductEoListByQueueQty(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "productionLineId") String productionLineId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);

    /**
     * 在制报表-eo信息（未入库）
     *
     * @param tenantId
     * @param productionLineId
     * @author sanfeng.zhang@hand-china.com 2020/9/4 15:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductEoListByNoCount(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "productionLineId") String productionLineId, @Param(value = "materialId") String materialId);

    /**
     * 工序在制合计数量查询
     *
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/25 02:58:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionLineDetailsVO3>
     */
    List<HmeProductionLineDetailsVO3> processQtyQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                      @Param("prodLineId") String prodLineId, @Param("materialId") String materialId);

    /**
     * 获取工单单元的计划属性
     *
     * @param tenantId
     * @param workcellId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/25 05:26:55
     * @return HmeWorkcellScheduleVO
     */
    HmeWorkcellScheduleVO getWorkcellScheduleVO(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);
}
