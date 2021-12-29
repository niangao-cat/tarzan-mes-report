package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeAfterSalesQuotationDTO;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * description  报表查询业务
 *
 * @author wengang.qiang@hand-chian.com 2021/10/15 10:45
 */
public interface HmeAfterSalesQuotationService {

    /**
     * 报表查询接口
     *
     * @param tenantId    租户id
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeAfterSalesQuotationVO> query(Long tenantId, HmeAfterSalesQuotationDTO dto, PageRequest pageRequest);

    /**
     * 报表导出
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeAfterSalesQuotationVO> export(Long tenantId, HmeAfterSalesQuotationDTO dto);

}
