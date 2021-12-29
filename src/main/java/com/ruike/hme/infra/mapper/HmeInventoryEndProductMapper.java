package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 售后在制品盘点-成品报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:34
 */
public interface HmeInventoryEndProductMapper {

    /**
     * 父层售后拆机记录
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/2 10:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO2>
     */
    List<HmeInventoryEndProductVO2> inventoryEndProductQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeInventoryEndProductVO dto);

    /**
     * 售后在制品盘点-成品报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/1 23:54
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO3>
     */
    List<HmeInventoryEndProductVO3> inventoryEndProductExport(@Param("tenantId") Long tenantId, @Param("dto") HmeInventoryEndProductVO dto);

    /**
     * 查询父层半成品数量
     *
     * @param tenantId
     * @param parentIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/2 1:03
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO4>
     */
    List<HmeInventoryEndProductVO4> queryQtyByParentRecord(@Param("tenantId") Long tenantId, @Param("parentIdList") List<String> parentIdList);

    /**
     * 根据Ids获取售后拆机
     *
     * @param tenantId
     * @param splitRecordIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/2 1:51
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO3>
     */
    List<HmeInventoryEndProductVO3> querySplitRecordByIds(@Param("tenantId") Long tenantId, @Param("splitRecordIdList") List<String> splitRecordIdList);


    /**
     * 批量查询拆机下的工位
     *
     * @param tenantId
     * @param splitRecordIdList
     * @param workcellCodeList
     * @author sanfeng.zhang@hand-china.com 2021/4/2 2:09
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO3>
     */
    List<HmeInventoryEndProductVO3> batchQuerySplitWorkcell(@Param("tenantId") Long tenantId,
                                                            @Param("splitRecordIdList") List<String> splitRecordIdList,
                                                            @Param("workcellCodeList") List<String> workcellCodeList);


    /**
     * 父层售后拆机找子层
     *
     * @param tenantId
     * @param dto
     * @param parentIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/2 9:10
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO2>
     */
    List<HmeInventoryEndProductVO2> batchQuerySubRecord(@Param("tenantId") Long tenantId, @Param("dto") HmeInventoryEndProductVO dto, @Param("parentIdList") List<String> parentIdList);

    /**
     * 查询条码所在入库单号
     *
     * @param tenantId
     * @param materialLotCodeIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/28 0:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO5>
     */
    List<HmeInventoryEndProductVO5> queryDocNumByMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("materialLotCodeIdList") List<String> materialLotCodeIdList);
}
