package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeInputRecordDTO;
import com.ruike.hme.domain.vo.HmeInputRecordVO;
import com.ruike.hme.domain.vo.WorkcellVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeInputRecordMapper {


    List<HmeInputRecordVO> cosFunctionReport(@Param("tenantId")Long tenantId, @Param("dto") HmeInputRecordDTO dto);

    List<HmeInputRecordVO> cosFunctionReportQuery(@Param("tenantId")Long tenantId, @Param("dto") HmeInputRecordDTO dto);

    List<HmeInputRecordVO> cosFunctionReportQueryOfSn(@Param("tenantId")Long tenantId, @Param("dto") HmeInputRecordDTO dto);

    List<HmeInputRecordVO> cosFunctionReportQueryOfLotTime(@Param("tenantId")Long tenantId, @Param("dto") HmeInputRecordDTO dto);

    List<HmeInputRecordVO> cosFunctionReportQueryOfWo(@Param("tenantId")Long tenantId, @Param("dto") HmeInputRecordDTO dto);

    List<WorkcellVO> selectProcess(@Param("tenantId")Long tenantId , @Param("parentWorkcellIdList") List<String> parentWorkcellIdList);

    /**
     * 根据工序查询工位
     *
     * @param tenantId  租户ID
     * @param processIdList 工序ID集合
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/8/2 15:39
     */
    List<String> getWorkcellByProcess(@Param(value = "tenantId") Long tenantId, @Param(value = "processIdList") List<String> processIdList);

    /**
     * 查询执行作业ID
     *
     * @param tenantId  租户ID
     * @param workOrderNumIdList 工单ID集合
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/8/6 16:42
     */
    List<String> getEoIdByWorkOrderId(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderNumIdList") List<String> workOrderNumIdList);
}
