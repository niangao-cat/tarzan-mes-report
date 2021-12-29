package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeStockInDetailsDTO;
import com.ruike.hme.domain.vo.HmeStockInDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 入库明细查询报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 12:21
 */
public interface HmeStockInDetailsService {
    /**
     * 入库明细查询报表列表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-04 12:32
     * @return HmeStockInDetailsVO
     */
    Page<HmeStockInDetailsVO> queryList(Long tenantId, HmeStockInDetailsDTO dto, PageRequest pageRequest);

    /**
     * 入库明细查询报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/26 10:27:50
     * @return List<HmeStockInDetailsVO>
     */
    List<HmeStockInDetailsVO> queryListExport(Long tenantId, HmeStockInDetailsDTO dto);
}
