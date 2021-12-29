package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeModProductionLineVO;
import com.ruike.hme.domain.vo.HmeModWorkcellVO;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author xin.tian@raycuslaser 2021/06/18 14:31
 */
public interface HmeWorkCellDetailsReportRepository {

    /**
     * 工位产量明细报表查询
     *
     * @param tenantId    租户di
     * @param reportVO    报表参数
     * @param pageRequest 分页参数
     * @return : 报表数据
     * @author xin.tian
     * @date 2021/06/18 14:31
     **/
    Page<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest);

    /**
     * 工位产量明细报表导出
     *
     * @param tenantId    租户di
     * @param reportVO    报表参数
     * @return : 报表数据
     * @author xin.tian
     * @date 2021/06/18 14:31
     **/
    List<HmeWorkCellDetailsReportVO2> exportWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO);

    /**
     * workcellBasicPropertyBatchGet-批量获取工作单元基础属性
     *
     * @author xin.tian
     * @date 2021.06.18
     * @param tenantId
     * @param workcellIds
     * @return
     */
    List<HmeModWorkcellVO> workcellBasicPropertyBatchGet(Long tenantId, List<String> workcellIds);

    /**
     * prodLineBasicPropertyBatchGet-批量获取生产线基础属性
     *
     * @author xin.tian
     * @date 2021.06.18
     * @param tenantId
     * @param prodLineIds
     * @return
     */
    List<HmeModProductionLineVO> prodLineBasicPropertyBatchGet(Long tenantId, List<String> prodLineIds);
}
