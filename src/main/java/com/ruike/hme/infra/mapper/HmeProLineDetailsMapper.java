package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.domain.vo.HmeModWorkcellVO;
import com.ruike.hme.domain.vo.HmeProcessInfoVO;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 20:14
 */
public interface HmeProLineDetailsMapper {

    /**
     * 获取当前用户对应站点id
     *
     * @param userId
     * @return
     */
    String getSiteIdByUserId(@Param("userId") Long userId);


    /**
     * 产线日明细信息 查询
     *
     * @param tenantId
     * @param params
     * @author sanfeng.zhang@hand-china.com 2021/4/14 20:34
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    List<HmeProductionLineDetailsDTO> queryDetails(@Param("tenantId") Long tenantId, @Param("params") HmeProductionLineDetailsVO params);

    /**
     * 获取最小的开班时间
     *
     * @param tenantId          租户id
     * @param shiftDate         班次时间
     * @param lineWorkcellId    工段
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:08
     * @return java.util.Date
     */
    Date queryMinShiftStart(@Param(value = "tenantId") Long tenantId, @Param(value = "shiftDate") String shiftDate, @Param("lineWorkcellId") String lineWorkcellId);


    /**
     * 获取最大的结班时间
     *
     * @param tenantId          租户id
     * @param shiftDate         班次时间
     * @param lineWorkcellId    工段
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:08
     * @return java.util.Date
     */
    List<Date> queryMaxShiftEnd(@Param(value = "tenantId") Long tenantId, @Param(value = "shiftDate") String shiftDate, @Param("lineWorkcellId") String lineWorkcellId);

    /**
     * 查询工段下工序
     *
     * @param tenantId
     * @param lineWorkcellId
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:40
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModWorkcellVO>
     */
    List<HmeModWorkcellVO> queryProcessByLineWorkcellId(@Param(value = "tenantId") Long tenantId, @Param("lineWorkcellId") String lineWorkcellId);

    /**
     * 查询工段下工位
     *
     * @param tenantId
     * @param lineWorkcellId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:40
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModWorkcellVO>
     */
    List<HmeModWorkcellVO> queryWorkcellByLineWorkcellId(@Param(value = "tenantId") Long tenantId, @Param("lineWorkcellId") String lineWorkcellId, @Param("siteId") String siteId);


    /**
     * 根据工单查询工序
     *
     * @param tenantId
     * @param workOrderId
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:45
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessInfoVO>
     */
    List<HmeProcessInfoVO> queryProcessByWorkOderId(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId);

    /**
     * 根据工序查询工位
     *
     * @param tenantId
     * @param processId
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:49
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModWorkcellVO>
     */
    List<HmeModWorkcellVO> queryWorkcellByProcess(@Param(value = "tenantId") Long tenantId, @Param(value = "processId") String processId);

    /**
     * 获取工序的物料批Id
     *
     * @param tenantId          租户id
     * @param workOrderId       生产指令
     * @param materialId        物料id
     * @param processIdList     工序
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:07
     * @return java.lang.Integer
     */
    List<String> queryProcessQty(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderId") String workOrderId, @Param(value = "materialId") String materialId, @Param(value = "processIdList") List<String> processIdList, @Param(value = "siteInDateFrom") Date siteInDateFrom, @Param(value = "siteInDateTo") Date siteInDateTo);

    /**
     * 统计条码数量
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:54
     * @return java.math.BigDecimal
     */
    BigDecimal sumMaterialLotQty(@Param(value = "tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据工单获取Eo
     *
     * @param tenantId
     * @param workOrderId
     * @author sanfeng.zhang@hand-china.com 2021/4/15 6:58
     * @return java.util.List<java.lang.String>
     */
    List<String> queryEoIdByWorkOrderId(@Param(value = "tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 批量查询物料批ID，用于完工统计-不良数
     *
     * @param tenantId 租户ID
     * @param dateTimeFrom NC记录时间起
     * @param dateTimeTo NC记录时间至
     * @param workcellIds 工位ID集合
     * @param eoIds eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 14:49:40
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotId5(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTimeFrom") Date dateTimeFrom,
                                   @Param(value = "dateTimeTo") Date dateTimeTo, @Param(value = "workcellIds") List<String> workcellIds,
                                   @Param(value = "eoIds") List<String> eoIds);

    /**
     * 工段查询产线 车间数据
     *
     * @param tenantId
     * @param lineWorkcellId
     * @author sanfeng.zhang@hand-china.com 2020/8/3 11:11
     * @return com.ruike.hme.api.dto.HmeProductionLineDetailsDTO
     */
    HmeProductionLineDetailsDTO queryLineWorkcellUpIdInfo(@Param(value = "tenantId") Long tenantId, @Param(value = "lineWorkcellId") String lineWorkcellId);

    /**
     * 产量日明细-班次
     *
     * @param tenantId      租户id
     * @param params        查询参数
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:06
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    List<HmeProductionLineDetailsDTO> queryProductShiftList(@Param("tenantId") Long tenantId, @Param("params") HmeProductionLineDetailsVO params);

    /**
     * 投产信息
     *
     * @param tenantId          租户id
     * @param materialId        物料id
     * @param workOrderId       生产指令
     * @param workcellIdList    工位
     * @param shiftStartTime    开始时间
     * @param shiftEndTime      结班时间
     * @author sanfeng.zhang@hand-china.com 2020/7/31 16:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductProcessEoList(@Param(value = "tenantId") Long tenantId, @Param(value = "materialId") String materialId, @Param("workOrderId") String workOrderId, @Param(value = "workcellIdList") List<String> workcellIdList, @Param("shiftStartTime") String shiftStartTime, @Param("shiftEndTime") String shiftEndTime);

    /**
     * 批量查询返修状态
     *
     * @param tenantId
     * @param eoIdentificationList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/2 16:42
     */
    List<HmeProductEoInfoVO> batchReworkFlagQuery(@Param("tenantId") Long tenantId, @Param("eoIdentificationList") List<String> eoIdentificationList);
}
