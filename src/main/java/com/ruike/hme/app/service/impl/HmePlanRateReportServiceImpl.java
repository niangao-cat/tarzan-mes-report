package com.ruike.hme.app.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.beust.jcommander.internal.Lists;
import com.ruike.hme.api.dto.HmePlanRateReportRateDTO;
import com.ruike.hme.api.dto.HmePlanRateReportRequestDTO;
import com.ruike.hme.api.dto.HmePlanRateReportResponseDTO;
import com.ruike.hme.api.dto.RowRangeDto;
import com.ruike.hme.app.assembler.BizMergeStrategy;
import com.ruike.hme.app.service.HmePlanRateReportService;
import com.ruike.hme.domain.vo.HmePlanRateDetailVO;
import com.ruike.hme.infra.mapper.HmePlanRateReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * description 计划达成率报表
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:31
 */
@Service
public class HmePlanRateReportServiceImpl implements HmePlanRateReportService {

    @Autowired
    private HmePlanRateReportMapper hmePlanRateReportMapper;

    @Override
    public List<HmePlanRateReportResponseDTO> planRateReportQuery(Long tenantId, HmePlanRateReportRequestDTO hmePlanRateReportRequestDTO) {
        List<HmePlanRateReportResponseDTO> hmePlanRateReportResponseDtoList = new ArrayList<>();
        String defaultSiteId = hmePlanRateReportMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        hmePlanRateReportRequestDTO.setSiteId(defaultSiteId);
        List<HmePlanRateReportRateDTO> hmePlanRateReportRateDtoList = hmePlanRateReportMapper.planRateReportQuery(tenantId, hmePlanRateReportRequestDTO);
        if (CollectionUtils.isEmpty(hmePlanRateReportRateDtoList)) {
            return Collections.emptyList();
        }

        //再单独查询实际投产数据，并按照日期分组 modify by yuchao.wang for fang.pan at 2021.1.21
        Map<LocalDate, List<HmePlanRateReportRateDTO>> actualProductionShiftDateMap = new HashMap<>();
        List<String> workcellIdList = hmePlanRateReportRateDtoList.stream().map(HmePlanRateReportRateDTO::getWorkcellId).distinct().collect(Collectors.toList());
        List<String> shiftCodeList = hmePlanRateReportRateDtoList.stream().map(HmePlanRateReportRateDTO::getShiftCode).distinct().collect(Collectors.toList());
        List<HmePlanRateReportRateDTO> actualProductionList = hmePlanRateReportMapper.actualProductionQuery(tenantId, workcellIdList, shiftCodeList, hmePlanRateReportRequestDTO);
        if (CollectionUtils.isNotEmpty(actualProductionList)) {
            actualProductionShiftDateMap = actualProductionList.stream().collect(Collectors.groupingBy(HmePlanRateReportRateDTO::getShiftDate));
        }

        //再单独查询实际交付数据，并按照日期分组 modify by yuchao.wang for fang.pan at 2021.1.21
        Map<LocalDate, List<HmePlanRateReportRateDTO>> actualAeliveryShiftDateMap = new HashMap<>();
        List<HmePlanRateReportRateDTO> actualAeliveryList = hmePlanRateReportMapper.actualAeliveryQuery(tenantId, workcellIdList, shiftCodeList, hmePlanRateReportRequestDTO);
        if (CollectionUtils.isNotEmpty(actualAeliveryList)) {
            actualAeliveryShiftDateMap = actualAeliveryList.stream().collect(Collectors.groupingBy(HmePlanRateReportRateDTO::getShiftDate));
        }

        // 按时间分组  之后再按wkc分组进行班次的重写
        Map<LocalDate, List<HmePlanRateReportRateDTO>> dateListMap = hmePlanRateReportRateDtoList.stream()
                .collect(Collectors.groupingBy(HmePlanRateReportRateDTO::getShiftDate));
        for (Map.Entry<LocalDate, List<HmePlanRateReportRateDTO>> dateListEntry : dateListMap.entrySet()) {
            List<HmePlanRateReportRateDTO> dateListEntryValue = dateListEntry.getValue();
            Map<String, List<HmePlanRateReportRateDTO>> wkcIdMaps = dateListEntryValue.stream().collect(Collectors.groupingBy(HmePlanRateReportRateDTO::getWorkcellId));
            for (Map.Entry<String, List<HmePlanRateReportRateDTO>> wkcIdListEntry : wkcIdMaps.entrySet()) {
                AtomicInteger index = new AtomicInteger(0);
                wkcIdListEntry.getValue().forEach(rec -> {
                    rec.setOriginalShiftCode(rec.getShiftCode());
                    rec.setShiftCode(String.valueOf(index.incrementAndGet()));
                });
            }
            if (StringUtils.isNotBlank(hmePlanRateReportRequestDTO.getShiftCode())) {
                String shiftCodeFilter = getShiftCodeFilter(hmePlanRateReportRequestDTO.getShiftCode());
                if (shiftCodeFilter != null) {
                    dateListEntryValue = dateListEntryValue.stream().filter(hmePlanRateReportRateDTO -> shiftCodeFilter.equals(hmePlanRateReportRateDTO.getShiftCode())).collect(Collectors.toList());
                }
            }

            //取当前日期下的实际投产，并按照班次-工位分组 add by yuchao.wang for fang.pan at 2021.1.21
            Map<String, BigDecimal> actualProductionMap = new HashMap<>();
            List<HmePlanRateReportRateDTO> actualProductionShiftDateList = actualProductionShiftDateMap.getOrDefault(dateListEntry.getKey(), new ArrayList<HmePlanRateReportRateDTO>());
            if (CollectionUtils.isNotEmpty(actualProductionShiftDateList)) {
                actualProductionShiftDateList.forEach(item -> actualProductionMap.put(item.getShiftCode() + "," + item.getWorkcellId(), item.getActualProduction()));
            }

            //取当前日期下的实际交付，并按照班次-工位分组 add by yuchao.wang for fang.pan at 2021.1.21
            Map<String, BigDecimal> actualAeliveryMap = new HashMap<>();
            List<HmePlanRateReportRateDTO> actualAeliveryShiftDateList = actualAeliveryShiftDateMap.getOrDefault(dateListEntry.getKey(), new ArrayList<HmePlanRateReportRateDTO>());
            if (CollectionUtils.isNotEmpty(actualAeliveryShiftDateList)) {
                actualAeliveryShiftDateList.forEach(item -> actualAeliveryMap.put(item.getShiftCode() + "," + item.getWorkcellId(), item.getActualAelivery()));
            }

            Map<String, List<HmePlanRateReportRateDTO>> shiftCodeMaps = dateListEntryValue.stream().collect(Collectors.groupingBy(HmePlanRateReportRateDTO::getShiftCode));
            for (Map.Entry<String, List<HmePlanRateReportRateDTO>> shiftEntry : shiftCodeMaps.entrySet()) {
                HmePlanRateReportResponseDTO hmePlanRateReportResponseDTO = new HmePlanRateReportResponseDTO();
                List<HmePlanRateReportRateDTO> reportRateDtoList = new ArrayList<>();
                hmePlanRateReportResponseDTO.setDataTime(dateListEntry.getKey());
                hmePlanRateReportResponseDTO.setShiftCode(getShiftCode(shiftEntry.getKey()) + "班");

                for (HmePlanRateReportRateDTO hmePlanRateReportRateDTO : shiftEntry.getValue()) {
                    HmePlanRateReportRateDTO planRateReportRateDTO = new HmePlanRateReportRateDTO();
                    BeanUtils.copyProperties(hmePlanRateReportRateDTO, planRateReportRateDTO);

                    //取到当前的实际投产 add by yuchao.wang for fang.pan at 2021.1.21
                    planRateReportRateDTO.setActualProduction(actualProductionMap.getOrDefault(hmePlanRateReportRateDTO.getOriginalShiftCode()
                            + "," + hmePlanRateReportRateDTO.getWorkcellId(), BigDecimal.ZERO));

                    //取到当前的实际交付 add by yuchao.wang for fang.pan at 2021.1.21
                    planRateReportRateDTO.setActualAelivery(actualAeliveryMap.getOrDefault(hmePlanRateReportRateDTO.getOriginalShiftCode()
                            + "," + hmePlanRateReportRateDTO.getWorkcellId(), BigDecimal.ZERO));

                    // 组装百分比数据
                    if (planRateReportRateDTO.getPlannedProduction().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal actualProductionRatio = planRateReportRateDTO.getActualProduction()
                                .divide(planRateReportRateDTO.getPlannedProduction(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        planRateReportRateDTO.setActualProductionRatio(actualProductionRatio.toString() + "%");
                    }
                    if (planRateReportRateDTO.getPlannedDelivery().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal actualAeliveryRatio = planRateReportRateDTO.getActualAelivery()
                                .divide(planRateReportRateDTO.getPlannedDelivery(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        planRateReportRateDTO.setActualAeliveryRatio(actualAeliveryRatio.toString() + "%");
                    }
                    if (planRateReportRateDTO.getInProcessStandards().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal percentageInProduction = planRateReportRateDTO.getQuantityUnderProduction()
                                .divide(planRateReportRateDTO.getInProcessStandards(), 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                        planRateReportRateDTO.setPercentageInProduction(percentageInProduction.toString() + "%");
                    }
                    reportRateDtoList.add(planRateReportRateDTO);
                }
                hmePlanRateReportResponseDTO.setWorkcells(reportRateDtoList);
                hmePlanRateReportResponseDtoList.add(hmePlanRateReportResponseDTO);
            }
        }
        // 按照时间排序
        return hmePlanRateReportResponseDtoList.stream().sorted(Comparator.comparing(HmePlanRateReportResponseDTO::getDataTime)).collect(Collectors.toList());
    }

    @Override
    public Page<HmePlanRateDetailVO> detailQuery(Long tenantId, String siteId, LocalDate shiftDate, String shiftCode, String workcellId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmePlanRateReportMapper.selectDetailList(tenantId, siteId, shiftDate, shiftCode, workcellId));
    }

    @Override
    public Page<HmePlanRateDetailVO> detailDeliveryQuery(Long tenantId, String siteId, LocalDate shiftDate, String shiftCode, String workcellId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmePlanRateReportMapper.queryDeliveryDetailList(tenantId, siteId, shiftDate, shiftCode, workcellId));
    }

    @Override
    public void planRateReportExport(Long tenantId, HmePlanRateReportRequestDTO dto, HttpServletResponse response) throws IOException {
        List<HmePlanRateReportResponseDTO> reportList = this.planRateReportQuery(tenantId, dto);
        //确定表格内容数据
        List<List<Object>> data = Lists.newArrayList();
        //确定表头的动态标题
        List<String> lineWorkcellNameList = new ArrayList<>();
        for (HmePlanRateReportResponseDTO hmePlanRateReportResponseDTO:reportList) {
            List<Object> singleData = Lists.newArrayList();
            singleData.add(hmePlanRateReportResponseDTO.getDataTime().toString());
            singleData.add(hmePlanRateReportResponseDTO.getShiftCode());
            for (HmePlanRateReportRateDTO hmePlanRateReportRateDTO:hmePlanRateReportResponseDTO.getWorkcells()) {
                singleData.add(hmePlanRateReportRateDTO.getPlannedProduction());
                singleData.add(hmePlanRateReportRateDTO.getActualProduction());
                singleData.add(hmePlanRateReportRateDTO.getActualProductionRatio());
                singleData.add(hmePlanRateReportRateDTO.getPlannedDelivery());
                singleData.add(hmePlanRateReportRateDTO.getActualAelivery());
                singleData.add(hmePlanRateReportRateDTO.getActualAeliveryRatio());
                singleData.add(hmePlanRateReportRateDTO.getQuantityUnderProduction());
                singleData.add(hmePlanRateReportRateDTO.getInProcessStandards());
                singleData.add(hmePlanRateReportRateDTO.getPercentageInProduction());
                lineWorkcellNameList.add(hmePlanRateReportRateDTO.getDescription());
            }
            data.add(singleData);
        }
        if(CollectionUtils.isNotEmpty(lineWorkcellNameList)){
            lineWorkcellNameList = lineWorkcellNameList.stream().distinct().collect(Collectors.toList());
        }
        try {
            Map<String, List<RowRangeDto>> strategyMap = BizMergeStrategy.addAnnualMerStrategy(data);
            String filename = URLEncoder.encode("计划达成率报表", "UTF-8").replaceAll("\\+", "%20");

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(head(lineWorkcellNameList))
                    .registerWriteHandler(new BizMergeStrategy(strategyMap)) // 注册合并策略
                    .registerWriteHandler(BizMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet("计划达成率")
                    .doWrite(data);
        }catch (Exception ex){
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().println("计划达成率导出失败" + ex.getMessage());
        }
    }

    private static List <List<String>> head(List<String> lineWorkcellNameList){
        List<List<String>> headTitles = Lists.newArrayList();
        String basicInfo = "日期",skuInfo = "班次";
        headTitles.add( Lists.newArrayList(basicInfo ,basicInfo));
        headTitles.add( Lists.newArrayList(skuInfo ,skuInfo));
        List<String>  skuTitles = Lists.newArrayList("计划投产", "实际投产", "实际投产比例", "计划交付",
                "实际交付", "实际交付比例", "在制数量", "在制标准", "在制百分比");
        for (String lineWorkcellName:lineWorkcellNameList) {
            skuTitles.forEach(title->{
                headTitles.add( Lists.newArrayList(lineWorkcellName,title) );
            });
        }
        return headTitles;
    }

    /**
     * 班次改为A、B、C
     *
     * @param shiftCode 班次
     * @return A、B、C
     */
    private String getShiftCode(String shiftCode) {
        int shiftCodeNum = Integer.parseInt(shiftCode);
        int num = 0;
        if (shiftCodeNum <= 26) {
            num = shiftCodeNum;
        } else {
            num = shiftCodeNum % 26;
        }
        char sl = (char) (num - 1 + (int) 'A');
        return "" + sl;
    }

    /**
     * 查询条件转为1、2、3
     *
     * @param shiftCode 班次
     * @return 1、2、3
     */
    private String getShiftCodeFilter(String shiftCode) {
        if (shiftCode.contains("A")) {
            return "1";
        } else if (shiftCode.contains("B")) {
            return "2";
        } else if (shiftCode.contains("C")) {
            return "3";
        }
        return null;
    }
}
