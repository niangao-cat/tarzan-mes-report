package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeWoPlanRateReportDTO;
import com.ruike.hme.domain.vo.HmeWoPlanRateReportVO;

import java.util.List;

public interface HmeWoPlanRateReportService {

    List<HmeWoPlanRateReportVO> woPlanRateReportQuery(Long tenantId, HmeWoPlanRateReportDTO dto);
}
