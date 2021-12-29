package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWoPlanRateReportDTO;
import com.ruike.hme.app.service.HmeWoPlanRateReportService;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeWoPlanRateReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName HmeWoPlanRateReportServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 15:16
 * @Version 1.0
 **/
@Service
public class HmeWoPlanRateReportServiceImpl implements HmeWoPlanRateReportService {

    @Autowired
    private HmeWoPlanRateReportMapper hmeWoPlanRateReportMapper;

    private final List<String> SHIFT_SEQUENCE = Arrays.asList("A组", "B组", "C组");


    @Override
    public List<HmeWoPlanRateReportVO> woPlanRateReportQuery(Long tenantId, HmeWoPlanRateReportDTO dto) {
        List<HmeWoPlanRateReportVO> resultList = new ArrayList<>();
        //获取基本数据
        List<HmeWoPlanRateReportVO3> hmeWoPlanRateReportVO3List = hmeWoPlanRateReportMapper.selectAllShift(tenantId, dto);

        //将数据转为返回的格式
        //按照日期分组
        Map<String, List<HmeWoPlanRateReportVO3>> hmeWoPlanRateReportVO3MapByShiftDate = hmeWoPlanRateReportVO3List.stream().collect(Collectors.groupingBy(t -> t.getShiftDate()));
        for (Map.Entry<String, List<HmeWoPlanRateReportVO3>> entry : hmeWoPlanRateReportVO3MapByShiftDate.entrySet()) {
            List<HmeWoPlanRateReportVO3> hmeWoPlanRateReportVO3Temp = entry.getValue();
            Map<String, List<String>> chipLocationMap = hmeWoPlanRateReportVO3Temp.stream().collect(Collectors.groupingBy(HmeWoPlanRateReportVO3::getCalendarId,
                    Collectors.mapping(HmeWoPlanRateReportVO3::getSequence, Collectors.collectingAndThen(Collectors.toList(), lists -> lists.stream().distinct().sorted().collect(Collectors.toList())))));
            Map<String, List<HmeWoPlanRateReportVO3>> hmeWoPlanRateReportVO3Map = new HashMap<>();
            chipLocationMap.entrySet().forEach(entryTemp -> {
                int i = 0;
                List<String> sequences = entryTemp.getValue();
                for (String sequence :
                        sequences) {
                    int finalI = i;
                    hmeWoPlanRateReportVO3Temp.stream()
                            .filter(t -> t.getCalendarId().equals(entryTemp.getKey()) && t.getSequence().equals(sequence))
                            .forEach(t -> t.setSequence(SHIFT_SEQUENCE.get(finalI)));
                    i++;
                }
            });
            for (String shiftCode :
                    SHIFT_SEQUENCE) {
                List<HmeWoPlanRateReportVO3> collect = hmeWoPlanRateReportVO3Temp.stream().filter(t -> t.getSequence().equals(shiftCode)).collect(Collectors.toList());
                for (HmeWoPlanRateReportVO3 temp :
                        collect) {
                    HmeWoPlanRateReportVO hmeWoPlanRateReportVO = new HmeWoPlanRateReportVO();
                    long count = resultList.stream().filter(t -> t.getShiftCode().equals(temp.getSequence()) && t.getDataTime().equals(temp.getShiftDate()) && t.getWorkOrderNum().equals(t.getWorkOrderNum())).count();
                    if (count == 0L) {
                        hmeWoPlanRateReportVO.setDataTime(temp.getShiftDate());
                        hmeWoPlanRateReportVO.setShiftCode(temp.getSequence());
                        hmeWoPlanRateReportVO.setWorkOrderNum(temp.getWorkOrderNum());
                        hmeWoPlanRateReportVO.setQty(temp.getQty());
                        hmeWoPlanRateReportVO.setPublishDate(temp.getAttrValue());
                        hmeWoPlanRateReportVO.setPlanEndTime(temp.getPlanEndTime());
                        hmeWoPlanRateReportVO.setWoEndTime(temp.getActualEndDate());
                        hmeWoPlanRateReportVO.setSourceList(collect.stream().filter(t -> t.getWorkOrderid().equals(temp.getWorkOrderid())).collect(Collectors.toList()));
                        resultList.add(hmeWoPlanRateReportVO);
                    }
                }
            }
        }
        //获取工段
        List<HmeWoPlanRateReportVO4> newList = hmeWoPlanRateReportVO3List.stream()
                .map(e -> new HmeWoPlanRateReportVO4(e.getWorkcellId(), e.getWorkcellCode(), e.getWorkcellName(), e.getWorkcellSequence()))
                .distinct().collect(Collectors.toList());
        //计划投产，计划交付
        List<HmeWoPlanRateReportVO5> hmeWoPlanRateReportVO5Plan = hmeWoPlanRateReportMapper.seletPlan(tenantId, dto.getShiftDateFrom(), dto.getShiftDateTo());
        //实际投产
        List<HmeWoPlanRateReportVO5> hmeWoPlanRateReportVO5ActualProduction = hmeWoPlanRateReportMapper.seletActualProduction(tenantId, dto.getSiteId(), dto.getShiftDateFrom(), dto.getShiftDateTo());
        ;

        //实际交付
        List<HmeWoPlanRateReportVO5> hmeWoPlanRateReportVO5ActualDelivery = hmeWoPlanRateReportMapper.seletActualDelivery(tenantId, dto.getSiteId(), dto.getShiftDateFrom(), dto.getShiftDateTo());
        ;

        //在制标准
        List<HmeWoPlanRateReportVO5> hmeWoPlanRateReportVO5Standard = hmeWoPlanRateReportMapper.seletStandard(tenantId);
        ;

        //在制数量
        List<HmeWoPlanRateReportVO5> hmeWoPlanRateReportVO5 = hmeWoPlanRateReportMapper.seletQty(tenantId, dto.getSiteId());

        for (HmeWoPlanRateReportVO hmeWoPlanRateReportVOTemp :
                resultList) {
            List<HmeWoPlanRateReportVO2> HmeWoPlanRateReportVO2List = new ArrayList<>();
            for (HmeWoPlanRateReportVO4 hmeWoPlanRateReportVO4 :
                    newList) {
                HmeWoPlanRateReportVO2 hmeWoPlanRateReportVO2 = new HmeWoPlanRateReportVO2();
                hmeWoPlanRateReportVO2.setWorkCellId(hmeWoPlanRateReportVO4.getWorkcellId());
                hmeWoPlanRateReportVO2.setWorkCellName(hmeWoPlanRateReportVO4.getWorkcellName());
                hmeWoPlanRateReportVO2.setWorkcellSequence(hmeWoPlanRateReportVO4.getWorkcellSequence());

                hmeWoPlanRateReportVO2.setPlannedProduction(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setActualProduction(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setActualProductionRatio("0.00%");
                hmeWoPlanRateReportVO2.setPlannedAelivery(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setActualAelivery(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setActualAeliveryRatio("0.00%");
                hmeWoPlanRateReportVO2.setInProcessStandards(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setQuantityUnderProduction(BigDecimal.ZERO);
                hmeWoPlanRateReportVO2.setPercentageInProduction("0.00%");

                if (!CollectionUtils.isEmpty(hmeWoPlanRateReportVO5Standard)) {
                    List<HmeWoPlanRateReportVO5> collect1 = hmeWoPlanRateReportVO5Standard.stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect1) && !Objects.isNull(collect1.get(0).getQty())) {
                        hmeWoPlanRateReportVO2.setInProcessStandards(collect1.get(0).getQty());
                    }
                }
                List<HmeWoPlanRateReportVO3> collect = hmeWoPlanRateReportVOTemp.getSourceList().stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    HmeWoPlanRateReportVO3 hmeWoPlanRateReportVO3 = collect.get(0);

                    //计划投产，计划交付
                    if (!CollectionUtils.isEmpty(hmeWoPlanRateReportVO5Plan)) {
                        List<HmeWoPlanRateReportVO5> collect1 = hmeWoPlanRateReportVO5Plan.stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())
                                && t.getShiftCode().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getShiftDate().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getWorkOrderId().equals(hmeWoPlanRateReportVO3.getWorkOrderid())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect1) && !Objects.isNull(collect1.get(0).getQty())) {
                            hmeWoPlanRateReportVO2.setPlannedProduction(collect1.get(0).getQty());
                            hmeWoPlanRateReportVO2.setPlannedAelivery(collect1.get(0).getQty());
                        }
                    }
                    //实际投产
                    if (!CollectionUtils.isEmpty(hmeWoPlanRateReportVO5ActualProduction)) {
                        List<HmeWoPlanRateReportVO5> collect1 = hmeWoPlanRateReportVO5ActualProduction.stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())
                                && t.getShiftCode().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getShiftDate().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getWorkOrderId().equals(hmeWoPlanRateReportVO3.getWorkOrderid())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect1) && !Objects.isNull(collect1.get(0).getQty())) {
                            hmeWoPlanRateReportVO2.setActualProduction(collect1.get(0).getQty());
                        }
                    }

                    //实际交付
                    if (!CollectionUtils.isEmpty(hmeWoPlanRateReportVO5ActualDelivery)) {
                        List<HmeWoPlanRateReportVO5> collect1 = hmeWoPlanRateReportVO5ActualDelivery.stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())
                                && t.getShiftCode().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getShiftDate().equals(hmeWoPlanRateReportVO3.getShiftDate())
                                && t.getWorkOrderId().equals(hmeWoPlanRateReportVO3.getWorkOrderid())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect1) && !Objects.isNull(collect1.get(0).getQty())) {
                            hmeWoPlanRateReportVO2.setActualAelivery(collect1.get(0).getQty());
                        }
                    }

                    //在制数量
                    if (!CollectionUtils.isEmpty(hmeWoPlanRateReportVO5)) {
                        List<HmeWoPlanRateReportVO5> collect1 = hmeWoPlanRateReportVO5.stream().filter(t -> t.getWorkcellId().equals(hmeWoPlanRateReportVO4.getWorkcellId())
                                && t.getWorkOrderId().equals(hmeWoPlanRateReportVO3.getWorkOrderid())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect1) && !Objects.isNull(collect1.get(0).getQty())) {
                            hmeWoPlanRateReportVO2.setQuantityUnderProduction(collect1.get(0).getQty());
                        }
                    }
                    if (hmeWoPlanRateReportVO2.getPlannedProduction().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal actualProductionRatio = hmeWoPlanRateReportVO2.getPlannedProduction()
                                .divide(hmeWoPlanRateReportVO2.getPlannedProduction(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        hmeWoPlanRateReportVO2.setActualProductionRatio(actualProductionRatio.toString() + "%");
                    }
                    if (hmeWoPlanRateReportVO2.getPlannedAelivery().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal actualAeliveryRatio = hmeWoPlanRateReportVO2.getActualAelivery()
                                .divide(hmeWoPlanRateReportVO2.getPlannedAelivery(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        hmeWoPlanRateReportVO2.setActualAeliveryRatio(actualAeliveryRatio.toString() + "%");
                    }
                    if (hmeWoPlanRateReportVO2.getInProcessStandards().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal percentageInProductionRatio = hmeWoPlanRateReportVO2.getQuantityUnderProduction()
                                .divide(hmeWoPlanRateReportVO2.getInProcessStandards(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        hmeWoPlanRateReportVO2.setPercentageInProduction(percentageInProductionRatio.toString() + "%");
                    }
                }
                HmeWoPlanRateReportVO2List.add(hmeWoPlanRateReportVO2);
            }
            hmeWoPlanRateReportVOTemp.setResultList(HmeWoPlanRateReportVO2List);
        }

        //V20210322  modify by penglin.sui for fang.pan 排序
        if(!CollectionUtils.isEmpty(resultList)){
            resultList = resultList.stream()
                    .sorted(Comparator.comparing(HmeWoPlanRateReportVO::getDataTime))
                    .collect(Collectors.toList());
        }

        return resultList;
    }
}
