package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 泵浦源预筛选报表容器应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-11-05 09:17:20
 */
public interface HmePumpSelectionDetailsService {

    /**
     * 泵浦源预筛选报表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 11:43:30
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    Page<HmePumpSelectionDetailsVO> pumpSelectionDetailsPageQuery(Long tenantId, HmePumpSelectionDetailsDTO dto, PageRequest pageRequest);

    /**
     * 泵浦源预筛选报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/5 04:32:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO>
     */
    List<HmePumpSelectionDetailsVO> pumpSelectionDetailsExport(Long tenantId, HmePumpSelectionDetailsDTO dto);
}
