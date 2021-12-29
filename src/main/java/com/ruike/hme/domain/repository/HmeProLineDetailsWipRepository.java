package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * HmeProLineDetailsRepository
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:30
 */
public interface HmeProLineDetailsWipRepository {


    /**
     * 批量查询汇总工序的运行和库存数
     *
     * @param tenantId
     * @param siteId
     * @param prodLineId
     * @param materialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/8 18:46
     */
    List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(Long tenantId, String siteId, String prodLineId, List<String> materialIdList);

    /**
     * 查询待上线数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param siteId
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectQueueNumByMaterialList(Long tenantId,
                                               String prodLineId,
                                               String siteId,
                                               List<String> materialIdList);

    /**
     * 查询未入库库存数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectUnCountByMaterialList(Long tenantId,
                                              String prodLineId,
                                              List<String> materialIdList);


    /**
     * Description: 在制查询报表 查询
     * @param params
     * @author bao.xu@hand-china.com 2020/7/13 11:38
     * @return
     */
    List<HmeProductionQueryDTO> queryProductDetails(HmeProductionQueryVO params);


    /**
     * 在制报表-eo信息
     *
     * @param tenantId          租户id
     * @param pageRequest       分页参数
     * @param params            查询参数
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:47
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);


    /**
     * 在制报表-导出
     *
     * @param tenantId
     * @param params
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/13 18:29
     */
    void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException;

}
