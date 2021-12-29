package com.ruike.hme.infra.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 芯片性能表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
public interface HmeCosFunctionMapper extends BaseMapper<HmeCosFunction> {

    List<HmeCosFunctionVO2> cosFunctionReport(@Param("tenantId") Long tenantId, @Param("dto") HmeCosFunctionDTO2 dto);

    List<HmeCosFunctionVO2> cosFunctionReport2(@Param("tenantId") Long tenantId, @Param("dto") HmeCosFunctionDTO2 dto);

    List<HmeCosFunctionVO9> cosFunctionReport3(@Param("tenantId") Long tenantId, @Param("dto") HmeCosFunctionDTO2 dto);

    List<HmeCosFunctionVO3> getLabCodeByMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    List<HmeCosFunctionVO5> getWorkcellByMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    List<HmeCosFunctionVO4> workcellInfoQuery(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    List<String> getWorkcellByProcess(@Param("tenantId") Long tenantId, @Param("processId") String processId);

    List<String> getWorkcellByProcess2(@Param("tenantId") Long tenantId, @Param("processIdList") List<String> processIdList);

    List<String> getWorkcellByLineWorkcell(@Param("tenantId") Long tenantId, @Param("lineWorkcellId") String lineWorkcellId);

    List<String> getWorkcellByLineWorkcell2(@Param("tenantId") Long tenantId, @Param("lineWorkcellIdList") List<String> lineWorkcellIdList);

    /**
     * 主查询-GP
     *
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @param tableName 表名
     * @return
     * @author penglin.sui@hand-china.com 2021/8/26 20:10
     */
    @DS("gp")
    List<HmeCosFunctionVO2> gpCosFunctionReport(@Param("tenantId") Long tenantId,
                                                @Param("dto") HmeCosFunctionDTO2 dto,
                                                @Param("tableName") String tableName);

    /**
     * 条码当前工位信息查询-GP
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID集合
     * @param materialLotIdList 条码ID集合
     * @return
     * @author penglin.sui@hand-china.com 2021/8/26 20:51
     */
    @DS("gp")
    List<HmeCosFunctionVO4> gpWorkcellInfoQuery(@Param("tenantId") Long tenantId,
                                                @Param("workcellIdList") List<String> workcellIdList,
                                                @Param("materialLotIdList") List<String> materialLotIdList);

    List<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosFunctionHeadDTO dto);

    /**
     * 条码当前工位信息查询-GP
     *
     * @param tenantId 租户ID
     * @param materialLotCodeList 条码集合
     * @return
     * @author penglin.sui@hand-china.com 2021/10/22 14:46
     */
    List<String> selectMaterialLot(@Param("tenantId") Long tenantId,
                                              @Param("materialLotCodeList") List<String> materialLotCodeList);

    /**
     * 主查询-GP
     *
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @param tableName 表名
     * @return
     * @author penglin.sui@hand-china.com 2021/10/25 14:24
     */
    @DS("gp")
    List<HmeCosFunctionVO2> gpCosFunctionReport2(@Param("tenantId") Long tenantId,
                                                 @Param("dto") HmeCosFunctionDTO2 dto,
                                                 @Param("tableName") String tableName);
}
