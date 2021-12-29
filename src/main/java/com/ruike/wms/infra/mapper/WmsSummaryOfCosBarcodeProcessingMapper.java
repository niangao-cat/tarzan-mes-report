package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description COS条码加工汇总表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
public interface WmsSummaryOfCosBarcodeProcessingMapper {

    List<WmsSummaryOfCosBarcodeProcessingVO> list(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param(value = "dto") WmsSummaryOfCosBarcodeProcessingDTO dto);


    /**
     * 批量查询不良数
     *
     * @param tenantId
     * @param workOrderIdList
     * @param waferNumList
     * @param materialLotIdList
     * @param createByList
     * @param workcellIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     * @author sanfeng.zhang@hand-china.com 2021/1/15 15:23
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchListQueryNg(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "workOrderIdList") List<String> workOrderIdList,
                                                              @Param(value = "waferNumList") List<String> waferNumList,
                                                              @Param(value = "materialLotIdList") List<String> materialLotIdList,
                                                              @Param(value = "createByList") List<String> createByList,
                                                              @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 批量查询不良数
     *
     * @param tenantId 租户ID
     * @param ngQueryList 工单、wafer、物料批、工序集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/5 02:26:40
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchListQueryNgNew(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "ngQueryList") List<WmsSummaryOfCosBarcodeProcessingVO> ngQueryList);

    /**
     * 批量查询设备
     *
     * @param tenantId
     * @param jobIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     * @author sanfeng.zhang@hand-china.com 2021/1/15 15:23
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchListQueryAssetEncoding(@Param(value = "tenantId") Long tenantId,
                                                                         @Param(value = "jobIdList") List<String> jobIdList);

    /**
     * 根据物料批ID批量查询物料批装载部分信息
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/28 04:10:47
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchMaterialLotLoadQuery(@Param(value = "tenantId") Long tenantId,
                                                                       @Param(value = "materialIdList") List<String> materialIdList);

    /**
     * 根据工位查询工序及工段信息
     *
     * @param tenantId
     * @param workcellIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/24 20:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosInProductionVO2>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO2> qeuryProcessAndLineWorkcellByWorkcell(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 批量根据物料批ID查询实验代码
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/28 05:04:00
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2>
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchLabCodeQuery(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialIdList") List<String> materialIdList);

    /**
     * 根据工序查询工位
     *
     * @param tenantId 租户ID
     * @param processId 以逗号分隔的多个工序ID字符串
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/28 18:41:23
     * @return java.util.List<java.lang.String>
     */
    List<String> workcellByProcessQuery(@Param("tenantId") Long tenantId, @Param("processId") String processId);

    /**
     * 根据工段查询工位
     *
     * @param tenantId 租户ID
     * @param lineWorkcellId 以逗号分隔的多个工段ID字符串
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/28 18:41:23
     * @return java.util.List<java.lang.String>
     */
    List<String> workcellByLineWorkcellQuery(@Param("tenantId") Long tenantId, @Param("lineWorkcellId") String lineWorkcellId);
}
