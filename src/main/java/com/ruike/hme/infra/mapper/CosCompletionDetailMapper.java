package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.CosCompletionDetailQuery;
import com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation;
import com.ruike.hme.domain.vo.HmeCosCompletionVO;
import com.ruike.hme.domain.vo.HmeCosCompletionVO2;
import com.ruike.hme.domain.vo.HmeCosCompletionVO3;
import com.ruike.hme.domain.vo.HmeCosFunctionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * COS完工芯片明细报表 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 14:20
 */
public interface CosCompletionDetailMapper {

    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 03:28:19
     */
    List<CosCompletionDetailRepresentation> selectList(@Param("dto") CosCompletionDetailQuery dto);

    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation>
     * @author penglin.sui@hand-china.com 2021/7/14 16:32
     */
    List<CosCompletionDetailRepresentation> selectList2(@Param("dto") CosCompletionDetailQuery dto);

    /**
     * 实验代码
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/5/6 17:19
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO>
     */
    List<HmeCosCompletionVO> queryLabCodeByMaterialLotIds(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 电流点
     *
     * @param tenantId
     * @param selectionRuleCodeList
     * @author sanfeng.zhang@hand-china.com 2021/5/6 18:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO2>
     */
    List<HmeCosCompletionVO2> queryCurrentByRuleCode(@Param("tenantId") Long tenantId, @Param("selectionRuleCodeList") List<String> selectionRuleCodeList);

    /**
     * 性能数据
     *
     * @param tenantId
     * @param siteId
     * @param loadSequenceList
     * @param currentList
     * @author sanfeng.zhang@hand-china.com 2021/5/6 19:11
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionVO>
     */
    List<HmeCosFunctionVO> queryCosFunctionByCurrentAndLoadSequence(@Param("tenantId") Long tenantId,
                                                                    @Param("siteId") String siteId,
                                                                    @Param("loadSequenceList") List<String> loadSequenceList,
                                                                    @Param("currentList") List<String> currentList);

    /**
     * 查询条码
     *
     * @param dto
     * @author penglin.sui@hand-china.com 2021/7/14 19:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO>
     */
    List<HmeCosCompletionVO> queryMaterialLot(@Param("dto") CosCompletionDetailQuery dto);

    /**
     * 根据实验代码查询查询条码
     *
     * @param dto
     * @author penglin.sui@hand-china.com 2021/7/14 21:18
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO>
     */
    List<HmeCosCompletionVO> queryMaterialLotOfLabCode(@Param("dto") CosCompletionDetailQuery dto);

    /**
     * 查询EO
     *
     * @param dto
     * @author penglin.sui@hand-china.com 2021/7/14 19:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO3>
     */
    List<HmeCosCompletionVO3> queryEo(@Param("dto") CosCompletionDetailQuery dto);

    /**
     * 查询EO
     *
     * @param dto
     * @author penglin.sui@hand-china.com 2021/7/14 19:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCompletionVO3>
     */
    List<HmeCosCompletionVO3> queryLocator(@Param("dto") CosCompletionDetailQuery dto);
}
