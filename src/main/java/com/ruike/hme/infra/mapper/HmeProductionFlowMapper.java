package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.HmeProductionFlowQuery;
import com.ruike.hme.api.dto.representation.HmeProductionFlowRepresentation;
import com.ruike.hme.domain.vo.EoWorkcellStationNcInfoVO;
import com.ruike.hme.domain.vo.JobEquipmentVO;
import com.ruike.hme.domain.vo.SnStepLabCodeVO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 生产流转查询报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 15:51
 */
public interface HmeProductionFlowMapper {
    /**
     * 查询工序流转信息
     *
     * @param tenantId  租户ID
     * @param topSiteId 顶层站点ID
     * @param dto       查询信息
     * @return
     */
    List<HmeProductionFlowRepresentation> selectList(@Param("tenantId") Long tenantId,
                                                     @Param("topSiteId") String topSiteId,
                                                     @Param("dto") HmeProductionFlowQuery dto);

    /**
     * 查询实验代码
     *
     * @param list 条件
     * @return java.util.List<com.ruike.hme.domain.vo.MaterialLotLabCodeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 05:20:16
     */
    List<SnStepLabCodeVO> selectLabCodeList(@Param("list") List<SnStepLabCodeVO> list);

    /**
     * 查询实验代码
     *
     * @param list 条件
     * @return java.util.List<com.ruike.hme.domain.vo.MaterialLotLabCodeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 05:20:16
     */
    List<JobEquipmentVO> selectEquipmentList(@Param("list") List<String> list);

    /**
     * 查询不良信息点击标识
     *
     * @param tenantId 租户Id
     * @param list     条件
     * @return java.lang.Long
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/15 15:07:45
     */
    List<EoWorkcellStationNcInfoVO> ncInfoFlagQuery(@Param("tenantId") Long tenantId,
                                                    @Param("list") List<EoWorkcellStationNcInfoVO> list);

    /**
     * 根据工序查询工位
     *
     * @param tenantId 租户ID
     * @param processIdList 工序ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/28 11:09:11
     * @return java.util.List<java.lang.String>
     */
    List<String> workcellByProcessBatchQuery(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    /**
     * 根据工段查询工位
     *
     * @param tenantId 租户ID
     * @param lineWorkcellIdList 工段ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/28 11:20:03
     * @return java.util.List<java.lang.String>
     */
    List<String> workcellByLineWorkcellBatchQuery(@Param("tenantId") Long tenantId, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 根据产线查询工位
     *
     * @param tenantId 租户ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/28 11:30:24
     * @return java.util.List<java.lang.String>
     */
    List<String> workcellByProdLineQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "prodLineId") String prodLineId);

    /**
     * 根据工位查询工序、工段、产线信息
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/28 11:44:36
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO2> qeuryProcessLineProdByWorkcell(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);
}
