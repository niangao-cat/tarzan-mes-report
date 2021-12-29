package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeWoPlanRateReportDTO;
import com.ruike.hme.domain.vo.HmeWoPlanRateReportVO3;
import com.ruike.hme.domain.vo.HmeWoPlanRateReportVO5;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface HmeWoPlanRateReportMapper {

    List<HmeWoPlanRateReportVO3> selectAllShift(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") HmeWoPlanRateReportDTO dto);

    List<HmeWoPlanRateReportVO5> seletPlan(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "shiftDateFrom") String shiftDateFrom,
                                           @Param(value = "shiftDateTo") String shiftDateTo);

    List<HmeWoPlanRateReportVO5> seletActualProduction(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "siteId") String siteId,
                                                       @Param(value = "shiftDateFrom") String shiftDateFrom,
                                                       @Param(value = "shiftDateTo") String shiftDateTo);

    List<HmeWoPlanRateReportVO5> seletActualDelivery(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "siteId") String siteId,
                                                     @Param(value = "shiftDateFrom") String shiftDateFrom,
                                                     @Param(value = "shiftDateTo") String shiftDateTo);

    List<HmeWoPlanRateReportVO5> seletStandard(@Param(value = "tenantId") Long tenantId);

    List<HmeWoPlanRateReportVO5> seletQty(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "siteId") String siteId);

}