package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS在制报表 mapper
 *
 * @author 35113 2021/01/27 12:53
 */
public interface HmeCosInProductionMapper {

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 12:28:33
     */
    List<HmeCosInProductionVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 批量查询工单实际按工单汇总
     *
     * @param ids 工单ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderActual>
     */
    List<HmeWorkOrderActual> selectWoActualList(@Param("ids") List<String> ids);

    /**
     * 批量查询cos数量
     *
     * @param woIds    工单
     * @param wafers   wafer
     * @param wkcCodes 来料工位
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosOperationRecordVO>
     */
    List<HmeCosOperationRecordVO> selectCosRecordList(@Param("woIds") List<String> woIds,
                                                      @Param("wafers") List<String> wafers,
                                                      @Param("wkcCodes") List<String> wkcCodes);

    /**
     * 查询作业数据
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/23 14:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO>
     */
    List<HmeEoJobSnVO> queryJobSnList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 根据工序找工位
     *
     * @param tenantId
     * @param processIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 20:11
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkcellByProcessIds(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    /**
     * 根据工段找工位
     *
     * @param tenantId
     * @param lineWorkcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 20:14
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkcellBylineWorkcellIdList(@Param("tenantId") Long tenantId, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 根据工位查询工序及工段信息
     *
     * @param tenantId
     * @param workcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 20:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO2>
     */
    List<HmeCosInProductionVO2> qeuryProcessAndLineWorkcellByWorkcell(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 实验代码
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 21:10
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO3>
     */
    List<HmeCosInProductionVO3> queryLabCodeByMaterialLotIdList(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询不良信息
     *
     * @param tenantId
     * @param materialLotIdList
     * @param workcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 21:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO4>
     */
    List<HmeCosInProductionVO4> queryNcRecode(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据作业对应的工艺查询工艺步骤
     *
     * @param tenantId
     * @param jobIdList
     * @return
     */
    List<HmeRouterStepVO> queryRouterStepByJob(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 根据工单的步骤查询工艺步骤
     *
     * @param tenantId
     * @param workOrderIdList
     * @return
     */
    List<HmeRouterStepVO> queryRouterStepByWorkOrderId(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 不良明细
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInNcRecordVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<HmeCosInNcRecordVO> ncRecordList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosInProductionVO dto);

    /**
     * 是否不良
     * @param tenantId
     * @param materialLotList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInNcRecordVO2>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInNcRecordVO2> ncRecordCountList(@Param("tenantId") Long tenantId,
                                                @Param("materialLotList") List<String> materialLotList);

    /**
     * 查询工单
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInProductionVO> selectWoId(@Param("tenantId") Long tenantId,
                                          @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 根据物料查询工单
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInProductionVO> selectWoIdOfMaterial(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 查询条码
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInProductionVO> selectMaterialLotId(@Param("tenantId") Long tenantId,
                                                   @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 根据WAFER查询条码
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInProductionVO> selectMaterialLotIdOfWafer(@Param("tenantId") Long tenantId,
                                                   @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 根据实验代码查询条码
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosInProductionVO> selectMaterialLotIdOfLabCode(@Param("tenantId") Long tenantId,
                                                            @Param("dto") HmeCosInProductionDTO dto);

    /**
     * 当前工序
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/11/18 16:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO>
     */
    List<HmeRouterStepVO> queryCurrentProcessByCodes(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
