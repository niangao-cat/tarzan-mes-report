package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;

import java.text.ParseException;
import java.util.List;

/**
 * 产品直通率报表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-02-26 10:22:12
 */
public interface HmeProdLinePassRateRepository {

    /**
     * 产品直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:12:02
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO5
     */
    HmeProdLinePassRateVO5 prodLinePassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto);

    /**
     * 产品日直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:13:49
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO8
     */
    HmeProdLinePassRateVO8 prodLineDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException;
}
