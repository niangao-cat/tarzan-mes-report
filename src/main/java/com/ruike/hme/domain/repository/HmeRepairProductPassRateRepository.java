package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;

import java.text.ParseException;

/**
 * 返修产品直通率报表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-05-19 11:14:12
 */
public interface HmeRepairProductPassRateRepository {

    /**
     * 返修产品直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/19 14:41:13
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO5
     */
    HmeProdLinePassRateVO5 repairProductPassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto);

    /**
     * 返修产品日直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/20 14:03:12
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO8
     */
    HmeProdLinePassRateVO8 repairProductDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException;
}
