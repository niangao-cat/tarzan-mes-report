package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 16:29
 */
public interface HmeCosWipQueryService {

    /**
     * 获取COS在制信息
     *
     * @param tenantId
     * @param dto
     * @author yifan.xiong
     * @date 2020-9-28 14:45:16
     */
    Page<HmeCosWipQueryVO> propertyCosWipQuery(Long tenantId, HmeCosWipQueryDTO dto, PageRequest pageRequest);

    /**
     * COS在制信息导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/14 16:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosWipQueryVO>
     */
    List<HmeCosWipQueryVO> cosWipExport(Long tenantId, HmeCosWipQueryDTO dto);
}

