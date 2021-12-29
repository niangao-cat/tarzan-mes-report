package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.HmeLoadJobQuery;
import com.ruike.hme.api.dto.representation.HmeLoadJobRept;
import com.ruike.hme.domain.vo.LoadJobEquipmentVO;
import com.ruike.hme.domain.vo.LoadJobNcVO;
import com.ruike.hme.domain.vo.MaterialLotLabCodeVO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 芯片装载作业 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 18:46
 */
public interface HmeLoadJobMapper {

    List<HmeLoadJobRept> selectList(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeLoadJobQuery dto);

    List<LoadJobNcVO> selectNcList(@Param(value = "tenantId") Long tenantId, @Param(value = "loadJobIds") List<String> loadJobIds);

    List<LoadJobEquipmentVO> selectEquipmentList(@Param(value = "tenantId") Long tenantId, @Param(value = "loadJobIds") List<String> loadJobIds);

    List<MaterialLotLabCodeVO> selectLabCodeList(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotIds") List<String> materialLotIds);

    /**
     * 根据工位查询工序、工段等信息
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/10 01:58:56
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO2> qeuryProcessLineByWorkcell(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    List<MaterialLotLabCodeVO> gpSelectLabCodeList(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotIds") List<String> materialLotIds);

    /**
     * 实验代码
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.hme.domain.vo.MaterialLotLabCodeVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/10
     */
    List<MaterialLotLabCodeVO> selectLabCodeListByMaterialLotIdAndLoadSequence(@Param("tenantId") Long tenantId, @Param(value = "dtoList") List<HmeLoadJobRept> dtoList);
}
