package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import com.ruike.hme.api.dto.WorkOrderSubstituteGroupDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单损耗汇总报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 10:41
 */
public interface WorkOrderAttritionSumMapper {

    /**
     * 查询列表
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.api.dto.WorkOrderLossSumRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 10:43:32
     */
    List<WorkOrderAttritionSumRepresentationDTO> selectList(@Param("tenantId") Long tenantId,
                                                            @Param("dto") WorkOrderAttritionSumQueryDTO dto);

    /**
     * 查询工单联产品损耗
     *
     * @param tenantId     租户
     * @param workOrderIds 组件行
     * @return java.util.List<com.ruike.hme.domain.vo.BomComponentWorkcellVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 08:32:33
     */
    List<WorkOrderQtyVO> selectCoproductScrappedList(@Param("tenantId") Long tenantId,
                                                     @Param("ids") Iterable<String> workOrderIds);

    /**
     * 查询bom组件行
     *
     * @param tenantId 租户
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.BomComponentWorkcellVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 08:32:33
     */
    List<BomComponentWorkcellVO> selectBomComponentStationList(@Param("tenantId") Long tenantId,
                                                               @Param("dtoList") List<WorkOrderAttritionSumRepresentationDTO> dtoList);

    /**
     * 查询bom组件行
     *
     * @param tenantId 租户
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.BomComponentWorkcellVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 08:32:33
     */
    List<BomComponentWorkcellVO> selectBomComponentStationList2(@Param("tenantId") Long tenantId,
                                                                @Param("dtoList") List<HmeCosAttritionSumDTO> dtoList);

    /**
     * 根据工位找工段
     *
     * @param tenantId   租户
     * @param stationIds 工位
     * @return java.util.List<com.ruike.hme.domain.vo.WorkcellVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/15 08:43:23
     */
    List<WorkcellVO> selectWorkcellByStation(@Param("tenantId") Long tenantId,
                                             @Param("ids") Iterable<String> stationIds);

    /**
     * 查询替代列表
     *
     * @param tenantId 租户
     * @param list     查询条件
     * @return java.util.List<com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/16 08:54:30
     */
    List<WorkOrderAttritionSumRepresentationDTO> selectSubstituteList(@Param("tenantId") Long tenantId,
                                                                      @Param("list") List<BomComponentWorkcellVO> list);

    /**
     * 查询不良待审数量
     *
     * @param tenantId 租户
     * @param list     列表
     * @return java.util.List<com.ruike.hme.domain.vo.PendingNcQueryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/16 09:46:01
     */
    List<PendingNcQueryVO> selectPendingNcList(@Param("tenantId") Long tenantId,
                                               @Param("list") List<PendingNcQueryVO> list);

    /**
     * 查询工单报废的EO
     *
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/7/27
     */
    List<String> processNcEoListGet(@Param("tenantId") Long tenantId,
                                    @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 物料 组件 工单分组查询eo装配数量
     *
     * @param tenantId
     * @param eoIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderAttritionSumVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/27
     */
    List<HmeWorkOrderAttritionSumVO> processNcScrapGet(@Param("tenantId") Long tenantId,
                                                       @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据产线及bom组件查询工段
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.BomComponentWorkcellVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/2
     */
    List<BomComponentWorkcellVO> queryLineWorkcellByBomComponentIdAndProdLineId(@Param("tenantId") Long tenantId,
                                                                                @Param("dtoList") List<BomComponentWorkcellVO> dtoList);
    /**
     * 查询物料替代组
     *
     * @param tenantId
     * @param materialCodeList
     * @return java.util.List<com.ruike.hme.api.dto.WorkOrderSubstituteGroupDTO>
     * @author xin.t@raycuslaser.com 2021/9/18
     */
    List<WorkOrderSubstituteGroupDTO> querySubstituteGroup(@Param("tenantId") Long tenantId, @Param("materialCodeList") List<String> materialCodeList);
}
