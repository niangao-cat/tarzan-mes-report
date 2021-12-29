package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:16
 */
public interface HmeMakeCenterProduceBoardMapper {

    /**
     * 获取当天计划班次
     *
     * @param tenantId
     * @param boardVO
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCalendarShiftVO>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<HmeCalendarShiftVO> queryCalendarShiftList(@Param("tenantId") Long tenantId, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 获取当天实绩班次
     * @param tenantId
     * @param calendarShiftIdList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<String> queryShiftIdList(@Param("tenantId") Long tenantId, @Param("calendarShiftIdList") List<String> calendarShiftIdList);
    /**
     * 取产线当班所做所有工单
     *
     * @param tenantId
     * @param shiftIdList
     * @param boardVO
     * @param cosJobTypeList
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:06
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkOrderByShiftIdAndProdLineId(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO, @Param("cosJobTypeList") List<String> cosJobTypeList);

    /**
     * 取产线当班派工的所有工单
     *
     * @param tenantId
     * @param boardVO
     * @param calendarShiftIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:06
     * @return java.util.List<java.lang.String>
     */
    List<String> queryDispatchWorkOrderByShiftIdAndProdLineId(@Param("tenantId") Long tenantId, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO, @Param("calendarShiftIdList") List<String> calendarShiftIdList);


    /**
     * 取工单工艺路线的工序及所属工段
     *
     * @param tenantId
     * @param workOrderIdList
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/5/30 22:55
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO3>
     */
    List<HmeMakeCenterProduceBoardVO3> queryLineWorkcellAndProcess(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList, @Param("siteId") String siteId);

    /**
     * 实际投产
     *
     * @param tenantId
     * @param shiftIdList
     * @param workOrderIdList
     * @param firstProcessIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/30 23:22
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     */
    List<HmeMakeCenterProduceBoardVO2> queryActualQty(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("workOrderIdList") List<String> workOrderIdList, @Param("firstProcessIdList") List<String> firstProcessIdList, @Param("siteId") String siteId);

    /**
     * 实际交付
     *
     * @param tenantId
     * @param shiftIdList
     * @param workOrderIdList
     * @param endProcessId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/5/30 23:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     */
    List<HmeMakeCenterProduceBoardVO2> queryActualDeliverQty(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("workOrderIdList") List<String> workOrderIdList, @Param("endProcessId") List<String> endProcessId, @Param("siteId") String siteId);

    /**
     * 派工数量
     *
     * @param tenantId
     * @param shiftIdList
     * @param workOrderIdList
     * @param lineWorkcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/31 0:25
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     */
    List<HmeMakeCenterProduceBoardVO2> queryDispatchQty(@Param("tenantId") Long tenantId, @Param("shiftIdList") List<String> shiftIdList, @Param("workOrderIdList") List<String> workOrderIdList, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 产品-产品描述
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO7>
     * @author sanfeng.zhang@hand-china.com 2021/5/31
     */
    List<HmeMakeCenterProduceBoardVO7> queryMaterialAndMaterialName(@Param("tenantId") Long tenantId, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO);

    /** 
     * 计划完成
     *
     * @param tenantId
     * @param siteId
     * @param areaIdList
     * @param materialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO8>
     * @author sanfeng.zhang@hand-china.com 2021/5/31  
     */
    List<HmeMakeCenterProduceBoardVO8> queryPlanFinishQty(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("areaIdList") List<String> areaIdList, @Param("materialIdList") List<String> materialIdList);

    /**
     * COS已完成数
     * @param tenantId
     * @param cosMaterialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO8>
     * @author sanfeng.zhang@hand-china.com 2021/5/31
     */
    List<HmeMakeCenterProduceBoardVO8> queryCosMaterialFinishedQty(@Param("tenantId") Long tenantId,
                                                                   @Param("cosMaterialIdList") List<String> cosMaterialIdList);

    /**
     * 非COS已完成数
     * @param tenantId
     * @param nonCosMaterialIdList
     * @param prodLineIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO8>
     * @author sanfeng.zhang@hand-china.com 2021/5/31
     */
    List<HmeMakeCenterProduceBoardVO8> queryNonCosMaterialFinishedQty(@Param("tenantId") Long tenantId, @Param("nonCosMaterialIdList") List<String> nonCosMaterialIdList, @Param("prodLineIdList") List<String> prodLineIdList);

    /** 
     * 根据产线取产品组
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO10>
     * @author sanfeng.zhang@hand-china.com 2021/6/1  
     */
    List<HmeMakeCenterProduceBoardVO10> queryProductionGroupByProdLineId(@Param("tenantId") Long tenantId, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 根据产线查询工序
     *
     * @param tenantId
     * @param siteId
     * @param prodLineIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO11>
     * @author sanfeng.zhang@hand-china.com 2021/6/2
     */
    List<HmeMakeCenterProduceBoardVO11> queryProcessByProdLineId(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 派工工单查询工段及物料
     * @param tenantId
     * @param dispatchWorkOrderIdList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/2
     */
    List<HmeMakeCenterProduceBoardVO4> queryDispatchLineWorkcell(@Param("tenantId") Long tenantId, @Param("dispatchWorkOrderIdList") List<String> dispatchWorkOrderIdList , @Param("siteId") String siteId);

    /**
     * 取今天所有非放行不良数据
     * @param tenantId
     * @param processIdList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO13>
     * @author sanfeng.zhang@hand-china.com 2021/6/2
     */
    List<HmeMakeCenterProduceBoardVO14> queryNcCodeDataList(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList, @Param("siteId") String siteId);

    /**
     * 查询部门下产线
     * @param tenantId
     * @param areaIdList
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    List<String> queryDefaultAreaProdLineIdList(@Param("tenantId") Long tenantId, @Param("areaIdList") List<String> areaIdList, @Param("siteId") String siteId);

    /**
     * 月度发生不良次数
     * @param tenantId
     * @param siteId
     * @param prodLineIdList
     * @return java.lang.Integer
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    Integer queryMonthNcCount(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 月度发生不良次数
     * @param tenantId
     * @param workOrderIdList
     * @return java.lang.Integer
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    Integer queryMonthFinishCount(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 巡检不良列表
     *
     * @param tenantId
     * @param startDate
     * @param prodLineIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO16>
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    List<HmeMakeCenterProduceBoardVO16> queryInspectionNc(@Param("tenantId") Long tenantId, @Param("startDate") Date startDate, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 查询看板维护的物料
     * @param tenantId
     * @param areaIdList
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    List<String> queryMaterialFromKanban(@Param("tenantId") Long tenantId, @Param("areaIdList") List<String> areaIdList, @Param("siteId") String siteId);

    /**
     * 获取工单
     * @param tenantId
     * @param materialIdList
     * @param prodLineIdList
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    List<String> queryWOrkOrderByMaterialAndProdLine(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList, @Param("prodLineIdList") List<String> prodLineIdList, @Param("siteId") String siteId);

    /**
     * 获取当天在做的工单
     * @param tenantId
     * @param currentStartDate
     * @param currentEndDate
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/4
     */
    List<String> queryCurrentWorkOrderIdList(@Param("tenantId") Long tenantId, @Param("currentStartDate") String currentStartDate, @Param("currentEndDate") String currentEndDate);

    /**
     * 根据产线查找部门
     * @param tenantId
     * @param prodLineIdList
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<String> queryAreaIdByProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("siteId") String siteId);

    /**
     * 根据部门查找产线
     * @param tenantId
     * @param areaId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<String> queryProdLineByAreaId(@Param("tenantId") Long tenantId, @Param("areaId") String areaId, @Param("siteId") String siteId);

    /**
     * 部门找工位
     * @param tenantId
     * @param areaId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<String> queryWorkcellByAreaId(@Param("tenantId") Long tenantId, @Param("areaId") String areaId, @Param("siteId") String siteId);

    /**
     * 根据产线查询对应的不良数
     * @param tenantId
     * @param
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO14>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<HmeMakeCenterProduceBoardVO14> queryMaterialNcCodeDataList(@Param("tenantId") Long tenantId, @Param("prodLineList") List<String> prodLineList, @Param("siteId") String siteId);

    /**
     * 根据产线查询COS对应的不良数
     * @param tenantId
     * @param
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO14>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<HmeMakeCenterProduceBoardVO14> queryCosMaterialNcCodeDataList(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("siteId") String siteId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 派工工单查询物料
     * @param tenantId
     * @param dispatchWorkOrderIdList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/8
     */
    List<HmeMakeCenterProduceBoardVO4> queryDispatchMaterialList(@Param("tenantId") Long tenantId, @Param("dispatchWorkOrderIdList") List<String> dispatchWorkOrderIdList, @Param("siteId") String siteId);

    /**
     * 查询看板制造部
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO18>
     * @author sanfeng.zhang@hand-china.com 2021/6/8
     */
    List<HmeMakeCenterProduceBoardVO18> queryKanbanAreaList(@Param("tenantId") Long tenantId);

    /**
     * 批量查询看板行信息
     *
     * @param tenantId
     * @param centerKanbanLineByHeaderIds
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO12>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO12> batchQueryCenterKanbanLineByHeaderIds(@Param("tenantId") Long tenantId, @Param("centerKanbanLineByHeaderIds") List<String> centerKanbanLineByHeaderIds);

    /**
     * 工单查询工序
     *
     * @param tenantId
     * @param siteId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO11>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO11> batchQueryProcessByWorkOrders(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单和工序查询eo
     *
     * @param tenantId
     * @param allWorkOrderIdList
     * @param processIds
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO19> queryEoListByWorkOrderAndWorkcell(@Param("tenantId") Long tenantId, @Param("allWorkOrderIdList") List<String> allWorkOrderIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId, @Param("currentStartDate") String currentStartDate, @Param("currentEndDate") String currentEndDate);

    /**
     * 根据工单和工序查询COSSN数量和出站数量
     *
     * @param tenantId
     * @param allWorkOrderIdList
     * @param processIds
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/6/22 0:45
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     */
    List<HmeMakeCenterProduceBoardVO19> queryCosQtyByWorkOrderAndProcess(@Param("tenantId") Long tenantId, @Param("allWorkOrderIdList") List<String> allWorkOrderIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId);

    /**
     * 根据工单和工序查询返修的eo
     * @param tenantId
     * @param eoIdList
     * @param processIds
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO19> batchQueryReworkRecordEoList(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId);

    /**
     * 根据制造部查询物料
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO7>
     * @author sanfeng.zhang@hand-china.com 2021/6/10
     */
    List<HmeMakeCenterProduceBoardVO7> queryMaterialAndMaterialNameByAreaId(@Param("tenantId") Long tenantId, @Param("boardVO") HmeMakeCenterProduceBoardVO boardVO);

    /**
     * COS不良代码统计
     * @param tenantId
     * @param processIdList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO14>
     * @author sanfeng.zhang@hand-china.com 2021/6/21
     */
    List<HmeMakeCenterProduceBoardVO14> queryCosNcCodeDataList(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList, @Param("siteId") String siteId);

    /**
     * 查询当天不良数
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/6/22 1:14
     * @return java.lang.Integer
     */
    Integer queryCosNcRecordNum(@Param("tenantId") Long tenantId);

    /**
     * COS测试工序的SN数量
     *
     * @param tenantId
     * @param workOrderIdList
     * @author sanfeng.zhang@hand-china.com 2021/6/22 1:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     */
    List<HmeMakeCenterProduceBoardVO19> queryCos015SnQty(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 条码复检合格数
     *
     * @param tenantId
     * @param materialLotIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO20>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<HmeMakeCenterProduceBoardVO20> queryReInspectionOkQty(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 制造部下产线
     * @param tenantId
     * @param areaId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO21>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<HmeMakeCenterProduceBoardVO21> queryProdLineInfoByAreaId(@Param("tenantId") Long tenantId, @Param("areaId") String areaId,  @Param("siteId") String siteId);

    /**
     * 产线下工位
     * @param tenantId
     * @param prodLineIdList
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<String> queryWorkcellByProdLineIds(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("siteId") String siteId);

    /**
     * 制造部-直通率
     *
     * @param tenantId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/6/25 1:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO22>
     */
    List<HmeMakeCenterProduceBoardVO22> hmeMakeCenterProduceBoardMapper(@Param("tenantId") Long tenantId, @Param("areaId") String siteId);

    /**
     * 制造部-日计划达成率
     *
     * @param tenantId
     * @param areaId
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     * @author sanfeng.zhang@hand-china.com 2021/7/2
     */
    List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateListByArea(@Param("tenantId") Long tenantId, @Param("areaId") String areaId, @Param("siteId") String siteId);

    /**
     * 看板产线信息
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModProductionLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/5
     */
    List<HmeModProductionLineVO> queryProductionInfoList(@Param("tenantId") Long tenantId, @Param("dto") HmeModProductionLineVO dto);

    List<String> queryAttrValueByMaterialIdList(List<String> materialIdList);

    List<String> queryCarByAreaId(String areaId);

    List<String> queryProdLineByOrganizationIdList(@Param("organizationIdList") List<String> organizationIdList);

    List<HmeMakeCenterProdLineNameVO> queryProdLineNameByProdlineId(List<String> prodLineIdList);


    List<HmeMakeCenterProdLineNameVO> queryProductionGroupCodeByProdLineIdList(@Param("prodLineIdList") List<String> prodLineIdList,@Param("siteId") String siteId);

    List<HmeMakeCenterProduceBoardVO22> queryThroughRateByProdLineIdList(@Param("prodLineIdList") List<String> prodLineIdList,@Param("tenantId") Long tenantId);

    /**
     * 通过部门编码查询部门ID
     * @param areaCode
     * @return
     */
    HmeMakeCenterProduceBoardVO queryAreaIdByAreaCode(String areaCode);
}
