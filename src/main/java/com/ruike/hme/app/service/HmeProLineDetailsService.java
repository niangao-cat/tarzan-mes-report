package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 19:59
 */
public interface HmeProLineDetailsService {

    /**
     * 产量日明细报表
     *
     * @param tenantId
     * @param pageRequest
     * @param params
     * @author sanfeng.zhang@hand-china.com 2021/4/14 20:02
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params);

    /**
     * 产量日明细-班组信息
     *
     * @param tenantId
     * @param pageRequest
     * @param params
     * @return
     */
    Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params);

    /**
     * 投产信息
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param params      查询条件
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 16:16
     */
    Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);

    /**
     * 工段产量日明细报表导出
     *
     * @param tenantId
     * @param detailsVO
     * @author sanfeng.zhang@hand-china.com 2021/4/13 15:03
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    List<HmeProductionLineDetailsDTO> lineStationDetailsExport(Long tenantId, HmeProductionLineDetailsVO detailsVO);
}
