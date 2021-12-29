package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeAfterSalesQuotationDTO;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO2;
import com.ruike.hme.domain.vo.HmeItfSnSapIfaceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description 售后报价单
 *
 * @author wengang.qiang@hand-china 2021/10/14 20:55
 */
public interface HmeAfterSalesQuotationMapper {

    /**
     * 售后报价单 查询
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeAfterSalesQuotationVO> querySalesQuotation(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeAfterSalesQuotationDTO dto);

    /**
     * 查询接口表
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeItfSnSapIfaceVO> queryItfSnSapIface(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeItfSnSapIfaceVO dto);

    /**
     * 查询报价单行
     *
     * @param tenantId
     * @param quotationHeaderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO2>
     * @author sanfeng.zhang@hand-china.com 2021/11/9
     */
    List<HmeAfterSalesQuotationVO2> querySalesQuotationLine(@Param("tenantId") Long tenantId, @Param("quotationHeaderIdList") List<String> quotationHeaderIdList);
}
