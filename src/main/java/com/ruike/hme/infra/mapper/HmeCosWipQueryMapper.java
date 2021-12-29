package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.domain.vo.HmeCosInProductionVO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO2;
import com.ruike.hme.domain.vo.WorkOrderProductionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 16:31
 */
public interface HmeCosWipQueryMapper {

    /**
     * 查询COS在制信息
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<HmeCosWipQueryVO>
     * @author yifan.xiong@hand-china.com 2020-9-28 14:43:54
     */
    List<HmeCosWipQueryVO> cosWipQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosWipQueryDTO dto);

    /**
     * 查询工单信息
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 12:29:56
     */
    List<String> selectWorkOrderList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosWipQueryDTO dto);

    /**
     * 批量查询工单及产品信息
     *
     * @param tenantId     租户
     * @param workOrderIds 工单
     * @return java.util.List<com.ruike.hme.domain.vo.WorkOrderProductionVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 11:38:21
     */
    List<WorkOrderProductionVO> selectWorkOrderProduction(@Param("tenantId") Long tenantId,
                                                          @Param("ids") List<String> workOrderIds);

    /**
     * 批量查询工单及产品信息
     *
     * @param tenantId     租户
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.WorkOrderProductionVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 11:38:21
     */
    List<HmeCosWipQueryVO2> selectMaterialLot(@Param("tenantId") Long tenantId,
                                              @Param("dto") HmeCosWipQueryDTO dto);

    /**
     * 根据WAFER查询条码
     * @param tenantId 租户ID
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO>
     * @author penglin.sui@hand-china.com 2021/7/13
     */
    List<HmeCosWipQueryVO2> selectMaterialLotIdOfWafer(@Param("tenantId") Long tenantId,
                                                       @Param("dto") HmeCosWipQueryDTO dto);
}
