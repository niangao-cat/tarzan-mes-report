package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeAfterSalesRegisterDTO;
import com.ruike.hme.domain.vo.HmeAfterSalesRegisterVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeAfterSalesRegisterMapper {

    List<HmeAfterSalesRegisterVO> query(@Param(value = "tenantId") Long tenantId, @Param(value = "dto")HmeAfterSalesRegisterDTO dto);
}
