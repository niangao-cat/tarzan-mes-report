package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:31
 */
public interface HmeCosBarCodeExceptionMapper {

    /**
     * COS条码加工异常汇总报表查询
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-26 15:51
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO
     */
    List<HmeCosBarCodeExceptionVO> queryList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosBarCodeExceptionDTO dto);

    /**
     * COS条码加工异常汇总报表查询
     *
     * @param tenantId
     * @param dto
     * @author penglin.sui@HAND-CHINA.COM 2021-08-03 16:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO>
     */
    List<HmeCosBarCodeExceptionVO> queryList2(@Param("tenantId") Long tenantId, @Param("dto") HmeCosBarCodeExceptionDTO dto);

    /**
     * COS条码加工异常汇总报表查询
     *
     * @param tenantId
     * @param loadSequenceList
     * @author penglin.sui@HAND-CHINA.COM 2021-08-03 18:56
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO>
     */
    List<HmeCosBarCodeExceptionVO> queryNc(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 查询设备
     *
     * @param tenantId
     * @param jobIdList
     * @author penglin.sui@HAND-CHINA.COM 2021-03-29 19:15
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO
     */
    List<HmeCosBarCodeExceptionVO> queryEquipmentList(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 根据工序查询工位
     *
     * @param tenantId  租户ID
     * @param processIdList 工序ID集合
     * @param workcellIdList 工位ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2>
     * @author penglin.sui@hand-china.com 2021/8/3 15:39
     */
    List<HmeCosBarCodeExceptionVO2> getWorkcellByProcess(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "processIdList") List<String> processIdList,
                                                         @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 根据工段查询工位
     *
     * @param tenantId       租户ID
     * @param lineWorkcellIdList 工段ID集合
     * @param workcellIdList 工位ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/15 11:04:04
     */
    List<HmeCosBarCodeExceptionVO2> getWorkcellByLineWorkcellId(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "lineWorkcellIdList") List<String> lineWorkcellIdList,
                                                                @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 根据产线查询工位
     *
     * @param tenantId   租户ID
     * @param prodLineIdList 产线ID集合
     * @param workcellIdList 工位ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2>
     * @author penglin.sui@hand-china.com 2021/8/3 15:43
     */
    List<HmeCosBarCodeExceptionVO2> getWorkcellByProdLine(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "prodLineIdList") List<String> prodLineIdList,
                                                          @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 根据产线查询工位
     *
     * @param tenantId   租户ID
     * @param workcellIdList 工位ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2>
     * @author penglin.sui@hand-china.com 2021/8/4 15:49
     */
    List<HmeCosBarCodeExceptionVO2> queryWkcRel(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "workcellIdList") List<String> workcellIdList);
}
