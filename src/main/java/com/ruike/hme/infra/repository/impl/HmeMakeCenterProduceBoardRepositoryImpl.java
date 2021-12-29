package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.app.service.HmeMakeCenterProduceBoardService;
import com.ruike.hme.domain.repository.HmeMakeCenterProduceBoardRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.Constant;
import com.ruike.hme.infra.mapper.HmeMakeCenterProduceBoardMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Count;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import tarzan.common.domain.util.DateUtil;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 制造中心看板
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:15
 */
@Component
@Slf4j
public class HmeMakeCenterProduceBoardRepositoryImpl implements HmeMakeCenterProduceBoardRepository {

    @Autowired
    private HmeMakeCenterProduceBoardMapper hmeMakeCenterProduceBoardMapper;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private HmeMakeCenterProduceBoardService hmeMakeCenterProduceBoardService;

    @Autowired
    private LovAdapter lovAdapter;

    private String queryKanbanSiteId(Long tenantId) {
        List<LovValueDTO> siteList = lovAdapter.queryLovValue("HME.KANBAN_SITE", tenantId);
        if (CollectionUtils.isEmpty(siteList)) {
            throw new CommonException("请维护看板站点值集【HME.KANBAN_SITE】");
        }
        return siteList.get(0).getValue();
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateList(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        List<HmeMakeCenterProduceBoardVO2> resultList = new ArrayList<>();
        // 获取计划班次
        List<String> calendarShiftIdList = this.queryCurrentCalendarShiftIdList(tenantId, boardVO);
        if (CollectionUtils.isNotEmpty(calendarShiftIdList)) {
            // 获取实绩班次
            List<String> shiftIdList = hmeMakeCenterProduceBoardMapper.queryShiftIdList(tenantId, calendarShiftIdList);
            // 取COS报表作业类型
            List<LovValueDTO> cosJobTypeLovList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);
            List<String> cosJobTypeList = cosJobTypeLovList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            // 取产线当班所做所有工单
            List<String> workOrderIdList = hmeMakeCenterProduceBoardMapper.queryWorkOrderByShiftIdAndProdLineId(tenantId, shiftIdList, boardVO, cosJobTypeList);
            // 分成派工记录的工单和eoJobSn的工单
            List<String> dispatchWorkOrderIdList = hmeMakeCenterProduceBoardMapper.queryDispatchWorkOrderByShiftIdAndProdLineId(tenantId, boardVO, calendarShiftIdList);
            List<String> allWorkOrderIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                allWorkOrderIdList.addAll(workOrderIdList);
            }
            if (CollectionUtils.isNotEmpty(dispatchWorkOrderIdList)) {
                allWorkOrderIdList.addAll(dispatchWorkOrderIdList);
            }
            // 去重
            allWorkOrderIdList = allWorkOrderIdList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(allWorkOrderIdList)) {
                // 取工单工艺路线及工序所属工段
                List<HmeMakeCenterProduceBoardVO3> boardVO3List = hmeMakeCenterProduceBoardMapper.queryLineWorkcellAndProcess(tenantId, allWorkOrderIdList, boardVO.getSiteId());
                if (CollectionUtils.isNotEmpty(boardVO3List)) {
                    // 按照查询出来的顺序 方便后面根据工段排序
                    List<String> sortLineWorkcellList = boardVO3List.stream().map(HmeMakeCenterProduceBoardVO3::getLineWorkcellId).distinct().collect(Collectors.toList());
                    // 按工单和工段的维度 查询首序和末序(不同工单对应的工段的首序 末序可能不同)
                    LinkedHashMap<String, List<HmeMakeCenterProduceBoardVO3>> lineWorkcellMap = boardVO3List.stream().collect(Collectors.groupingBy(dto -> dto.getWorkOrderId() + "_" + dto.getLineWorkcellId(), LinkedHashMap::new, Collectors.toList()));
                    List<HmeMakeCenterProduceBoardVO4> lineWorkcellList = new ArrayList<>();
                    lineWorkcellMap.entrySet().forEach(map -> {
                        HmeMakeCenterProduceBoardVO4 vo4 = new HmeMakeCenterProduceBoardVO4();
                        List<HmeMakeCenterProduceBoardVO3> valueList = map.getValue();
                        vo4.setWorkOrderId(valueList.get(0).getWorkOrderId());
                        vo4.setLineWorkcellId(valueList.get(0).getLineWorkcellId());
                        vo4.setFirstProcessId(valueList.get(0).getProcessId());
                        vo4.setEndProcessId(valueList.get(valueList.size() - 1).getProcessId());
                        lineWorkcellList.add(vo4);
                    });

                    List<String> firstProcessIdList = lineWorkcellList.stream().map(HmeMakeCenterProduceBoardVO4::getFirstProcessId).distinct().collect(Collectors.toList());
                    List<String> endProcessIdList = lineWorkcellList.stream().map(HmeMakeCenterProduceBoardVO4::getEndProcessId).distinct().collect(Collectors.toList());
                    // 实际投产，在制，实际交付
                    List<HmeMakeCenterProduceBoardVO2> actualQtyList = hmeMakeCenterProduceBoardMapper.queryActualQty(tenantId, shiftIdList, allWorkOrderIdList, firstProcessIdList, boardVO.getSiteId());
                    List<HmeMakeCenterProduceBoardVO2> actualDeliverQtyList = hmeMakeCenterProduceBoardMapper.queryActualDeliverQty(tenantId, shiftIdList, allWorkOrderIdList, endProcessIdList, boardVO.getSiteId());
                    List<HmeMakeCenterProduceBoardVO2> allBoardVO2List = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(actualQtyList)) {
                        allBoardVO2List.addAll(actualQtyList);
                    }
                    if (CollectionUtils.isNotEmpty(actualDeliverQtyList)) {
                        allBoardVO2List.addAll(actualDeliverQtyList);
                    }
                    List<String> lineWorkcellIdList = lineWorkcellList.stream().map(HmeMakeCenterProduceBoardVO4::getLineWorkcellId).distinct().collect(Collectors.toList());
                    // 派工数量
                    List<HmeMakeCenterProduceBoardVO2> dispatchQtyList = hmeMakeCenterProduceBoardMapper.queryDispatchQty(tenantId, calendarShiftIdList, allWorkOrderIdList, lineWorkcellIdList);
                    List<HmeMakeCenterProduceBoardVO2> workingList = new ArrayList<>();

                    if (CollectionUtils.isNotEmpty(allBoardVO2List)) {
                        Map<String, List<HmeMakeCenterProduceBoardVO4>> processMap = lineWorkcellList.stream().collect(Collectors.groupingBy(line -> line.getWorkOrderId() + "_" + line.getLineWorkcellId()));
                        // 根据工单及对应的首末序 取对应的投产、交付数
                        // 过滤数组(过滤掉 不是工单对应工段首末序的投产、交付的数据)
                        List<HmeMakeCenterProduceBoardVO2> filterBoardList = new ArrayList<>();
                        for (HmeMakeCenterProduceBoardVO2 boardVO2 : allBoardVO2List) {
                            List<HmeMakeCenterProduceBoardVO4> boardVO4List = processMap.get(boardVO2.getWorkOrderId() + "_" + boardVO2.getLineWorkcellId());
                            if (CollectionUtils.isNotEmpty(boardVO4List)) {
                                if (boardVO2.getActualQty() != null) {
                                    if (StringUtils.equals(boardVO2.getProcessId(), boardVO4List.get(0).getFirstProcessId())) {
                                        filterBoardList.add(boardVO2);
                                    }
                                }
                                if (boardVO2.getActualDeliverQty() != null) {
                                    if (StringUtils.equals(boardVO2.getProcessId(), boardVO4List.get(0).getEndProcessId())) {
                                        filterBoardList.add(boardVO2);
                                    }
                                }
                            }
                        }
                        // 根据物料及工段 取对应的实际投产、实际交付 在制数 = 实际交付数-实际投产数
                        Map<String, List<HmeMakeCenterProduceBoardVO2>> listMap = filterBoardList.stream().collect(Collectors.groupingBy(vo -> vo.getSnMaterialId() + "_" + vo.getLineWorkcellId()));
                        listMap.entrySet().forEach(lm -> {
                            HmeMakeCenterProduceBoardVO2 boardVO2 = new HmeMakeCenterProduceBoardVO2();
                            List<HmeMakeCenterProduceBoardVO2> valueList = lm.getValue();
                            boardVO2.setSnMaterialId(valueList.get(0).getSnMaterialId());
                            boardVO2.setMaterialName(valueList.get(0).getMaterialName());
                            boardVO2.setMaterialCode(valueList.get(0).getMaterialCode());
                            boardVO2.setLineWorkcellId(valueList.get(0).getLineWorkcellId());
                            boardVO2.setLineWorkcellName(valueList.get(0).getLineWorkcellName());
                            boardVO2.setProdLineId(valueList.get(0).getProdLineId());
                            Double actualQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            Double actualDeliverQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualDeliverQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            boardVO2.setActualQty(actualQty != null ? BigDecimal.valueOf(actualQty) : BigDecimal.ZERO);
                            boardVO2.setActualDeliverQty(actualDeliverQty != null ? BigDecimal.valueOf(actualDeliverQty) : BigDecimal.ZERO);
                            boardVO2.setWipQty(boardVO2.getActualDeliverQty().subtract(boardVO2.getActualQty()));
                            workingList.add(boardVO2);
                        });
                    }
                    if (CollectionUtils.isNotEmpty(dispatchWorkOrderIdList)) {
                        // 派工记录的 取派工工单的物料 及通过工单找的工段 进行汇总
                        List<HmeMakeCenterProduceBoardVO4> dispatchLineWorkcellList = hmeMakeCenterProduceBoardMapper.queryDispatchLineWorkcell(tenantId, dispatchWorkOrderIdList, boardVO.getSiteId());
                        for (HmeMakeCenterProduceBoardVO4 hmeMakeCenterProduceBoardVO4 : dispatchLineWorkcellList) {
                            HmeMakeCenterProduceBoardVO2 boardVO2 = new HmeMakeCenterProduceBoardVO2();
                            boardVO2.setSnMaterialId(hmeMakeCenterProduceBoardVO4.getMaterialId());
                            boardVO2.setMaterialName(hmeMakeCenterProduceBoardVO4.getMaterialName());
                            boardVO2.setMaterialCode(hmeMakeCenterProduceBoardVO4.getMaterialCode());
                            boardVO2.setLineWorkcellId(hmeMakeCenterProduceBoardVO4.getLineWorkcellId());
                            boardVO2.setLineWorkcellName(hmeMakeCenterProduceBoardVO4.getLineWorkcellName());
                            boardVO2.setProdLineId(hmeMakeCenterProduceBoardVO4.getProdLineId());
                            boardVO2.setActualQty(BigDecimal.ZERO);
                            boardVO2.setActualDeliverQty(BigDecimal.ZERO);
                            boardVO2.setWipQty(BigDecimal.ZERO);
                            workingList.add(boardVO2);
                        }
                    }
                    // 根据工段和物料 对数据进行汇总
                    Map<String, List<HmeMakeCenterProduceBoardVO2>> resultListMap = workingList.stream().collect(Collectors.groupingBy(vo -> vo.getSnMaterialId() + "_" + vo.getLineWorkcellId(), LinkedHashMap::new, Collectors.toList()));
                    resultListMap.entrySet().forEach(rs -> {
                        List<HmeMakeCenterProduceBoardVO2> valueList = rs.getValue();
                        HmeMakeCenterProduceBoardVO2 vo2 = new HmeMakeCenterProduceBoardVO2();
                        vo2.setSnMaterialId(valueList.get(0).getSnMaterialId());
                        vo2.setMaterialName(valueList.get(0).getMaterialName());
                        vo2.setMaterialCode(valueList.get(0).getMaterialCode());
                        vo2.setLineWorkcellId(valueList.get(0).getLineWorkcellId());
                        vo2.setLineWorkcellName(valueList.get(0).getLineWorkcellName());
                        vo2.setProdLineId(valueList.get(0).getProdLineId());
                        vo2.setSortNum(sortLineWorkcellList.indexOf(valueList.get(0).getLineWorkcellId()));
                        Double actualQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        Double actualDeliverQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualDeliverQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        vo2.setActualQty(actualQty != null ? BigDecimal.valueOf(actualQty) : BigDecimal.ZERO);
                        vo2.setActualDeliverQty(actualDeliverQty != null ? BigDecimal.valueOf(actualDeliverQty) : BigDecimal.ZERO);
                        vo2.setWipQty(vo2.getActualQty().subtract(vo2.getActualDeliverQty()));
                        Optional<HmeMakeCenterProduceBoardVO2> firstOpt = dispatchQtyList.stream().filter(dpq -> StringUtils.equals(dpq.getLineWorkcellId(), vo2.getLineWorkcellId()) && StringUtils.equals(dpq.getSnMaterialId(), vo2.getSnMaterialId())).findFirst();
                        vo2.setDispatchQty(firstOpt.isPresent() ? firstOpt.get().getDispatchQty() : BigDecimal.ZERO);
                        // 计划达成率 实际交付/派工数量*100%，保留整数，四舍五入
                        BigDecimal planReachRate = BigDecimal.ZERO;
                        if (BigDecimal.ZERO.compareTo(vo2.getDispatchQty()) != 0) {
                            planReachRate = vo2.getActualDeliverQty().divide(vo2.getDispatchQty(), 6, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                        }
                        vo2.setPlanReachRate(planReachRate + "%");
                        // 过滤掉派工 实际交付 实际投产都为0的数据
                        if (BigDecimal.ZERO.compareTo(vo2.getActualQty()) != 0 || BigDecimal.ZERO.compareTo(vo2.getActualDeliverQty()) != 0 || BigDecimal.ZERO.compareTo(vo2.getDispatchQty()) != 0) {
                            resultList.add(vo2);
                        }
                    });
                }
            }
        }
        return resultList.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO2::getMaterialCode).thenComparing(HmeMakeCenterProduceBoardVO2::getProdLineId).thenComparing(HmeMakeCenterProduceBoardVO2::getSortNum)).collect(Collectors.toList());
    }

    @Override
    public HmeMakeCenterProduceBoardVO5 queryMonthPlan(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        // 获取纵轴信息(产品-产品描述)
        List<HmeMakeCenterProduceBoardVO7> materialList = hmeMakeCenterProduceBoardMapper.queryMaterialAndMaterialName(tenantId, boardVO);
        // 根据物料去重 再根据产品描述和产品排序
        materialList = materialList.stream().distinct().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO7::getMaterialName).thenComparing(HmeMakeCenterProduceBoardVO7::getMaterialCode)).collect(Collectors.toList());
        return this.handleChartsData(tenantId, materialList, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetails(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        List<HmeMakeCenterProduceBoardVO9> resultList = new ArrayList<>();
        // 根据产线取产品组及工单
        List<HmeMakeCenterProduceBoardVO10> productionGroupList = hmeMakeCenterProduceBoardMapper.queryProductionGroupByProdLineId(tenantId, boardVO);
        if (CollectionUtils.isNotEmpty(productionGroupList)) {
            // 获取今天在做的工单(eoJobSn 现在进站时间为今天)
            Date currentDate = CommonUtils.currentTimeGet();
            List<String> currentWorkOrderIdList = hmeMakeCenterProduceBoardMapper.queryCurrentWorkOrderIdList(tenantId, DateUtil.date2String(currentDate, "yyyy-MM-dd 00:00:00"), DateUtil.date2String(currentDate, "yyyy-MM-dd 23:59:59"));
            // 去重
            List<String> distinctCurrentWorkOrderIdList = currentWorkOrderIdList.stream().distinct().collect(Collectors.toList());
            // COS产线的看板基础数据
            List<String> centerKanbanLineByHeaderIds = productionGroupList.stream().map(HmeMakeCenterProduceBoardVO10::getCenterKanbanHeaderId).collect(Collectors.toList());
            List<HmeMakeCenterProduceBoardVO12> kanbanLineByHeaderList = hmeMakeCenterProduceBoardMapper.batchQueryCenterKanbanLineByHeaderIds(tenantId, centerKanbanLineByHeaderIds);
            Map<String, List<HmeMakeCenterProduceBoardVO12>> kanbanLineByHeaderMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(kanbanLineByHeaderList)) {
                kanbanLineByHeaderMap = kanbanLineByHeaderList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO12::getCenterKanbanHeaderId));
            }
            List<String> firstWorkOrderIdList = new ArrayList<>();
            List<String> nonCosWorkOrderIdList = new ArrayList<>();
            List<String> cosWorkOrderIdList = new ArrayList<>();
            productionGroupList.forEach(ptg -> {
                // 获取产品组下所有工单
                List<String> workOrderIdList = ptg.getWorkOrderIdList();
                // 取两者的交集
                List<String> filterWorkOrderIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    filterWorkOrderIdList = distinctCurrentWorkOrderIdList.stream().filter(vo -> workOrderIdList.contains(vo)).collect(Collectors.toList());
                    if (Constant.ConstantValue.YES.equals(ptg.getCosFlag())) {
                        cosWorkOrderIdList.addAll(workOrderIdList);
                    } else {
                        nonCosWorkOrderIdList.addAll(workOrderIdList);
                    }
                }
                if (CollectionUtils.isNotEmpty(filterWorkOrderIdList)) {
                    firstWorkOrderIdList.add(filterWorkOrderIdList.get(0));
                }
            });
            Map<String, List<HmeMakeCenterProduceBoardVO11>> firstWorkOrderMap = new HashMap<>();
            List<HmeMakeCenterProduceBoardVO19> boardVO19List = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> reworkEoList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> cosBoardList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> cosTestBoardList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO20> reInspectionOkQtyList = new ArrayList<>();
            Integer ncRecordNum = 0;
            if (CollectionUtils.isNotEmpty(firstWorkOrderIdList)) {
                // 查询工单对应工序及工位信息
                List<HmeMakeCenterProduceBoardVO11> boardVO11List = hmeMakeCenterProduceBoardMapper.batchQueryProcessByWorkOrders(tenantId, boardVO.getSiteId(), firstWorkOrderIdList);
                if (CollectionUtils.isNotEmpty(boardVO11List)) {
                    firstWorkOrderMap = boardVO11List.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO11::getWorkOrderId, LinkedHashMap::new, Collectors.toList()));
                }
                List<String> processIds = boardVO11List.stream().map(HmeMakeCenterProduceBoardVO11::getProcessId).distinct().collect(Collectors.toList());
                // 更据工单和工位查询当天作业的eo8
                if (CollectionUtils.isNotEmpty(processIds) && CollectionUtils.isNotEmpty(nonCosWorkOrderIdList)) {
                    //获取前一天的时间
                    Date date = new Date();
                    Calendar now = Calendar.getInstance();
                    now.setTime(date);
                    now.add(Calendar.DAY_OF_MONTH, -1);
                    Date cdate=now.getTime();
                    boardVO19List = hmeMakeCenterProduceBoardMapper.queryEoListByWorkOrderAndWorkcell(tenantId, nonCosWorkOrderIdList, processIds, boardVO.getSiteId(),DateUtil.date2String(cdate, "yyyy-MM-dd 00:00:00"), DateUtil.date2String(cdate, "yyyy-MM-dd 23:59:59"));
                    List<String> allEoIdList = boardVO19List.stream().map(HmeMakeCenterProduceBoardVO19::getEoId).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(allEoIdList)) {
                        // 多线程获取返修的eo
                        // 根据eoId查询返修的eo
                        List<List<String>> eoIdList = this.splitSqlList(allEoIdList, 4000, 10);

                        List<Future<List<HmeMakeCenterProduceBoardVO19>>> BoardVO19FutureList = new ArrayList<>();
                        // 最多开启10个进程 判断数据拆分数是否大于10
                        for (List<String> eoIds : eoIdList) {
                            Future<List<HmeMakeCenterProduceBoardVO19>> BoardVO19Future = poolExecutor.submit(() -> {
                                SecurityTokenHelper.close();
                                List<HmeMakeCenterProduceBoardVO19> makeCenterProduceBoardVO19s = hmeMakeCenterProduceBoardMapper.batchQueryReworkRecordEoList(tenantId, eoIds, processIds, boardVO.getSiteId());
                                return makeCenterProduceBoardVO19s;
                            });
                            BoardVO19FutureList.add(BoardVO19Future);
                        }
                        if (CollectionUtils.isNotEmpty(BoardVO19FutureList)) {
                            //将异步计算结果加载到返回值中
                            BoardVO19FutureList.forEach(bf -> {
                                try {
                                    List<HmeMakeCenterProduceBoardVO19> boardVO19s = bf.get();
                                    if (CollectionUtils.isNotEmpty(boardVO19s)) {
                                        reworkEoList.addAll(boardVO19s);
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    bf.cancel(true);
                                    Thread.currentThread().interrupt();
                                }
                            });
                        }
                    }
                }
                // COS 汇总SN数量和出站数量
                if (CollectionUtils.isNotEmpty(cosWorkOrderIdList)) {
                    if (CollectionUtils.isNotEmpty(processIds)) {
                        cosBoardList = hmeMakeCenterProduceBoardMapper.queryCosQtyByWorkOrderAndProcess(tenantId, cosWorkOrderIdList, processIds, boardVO.getSiteId());
                        List<String> materialLotIdList = cosBoardList.stream().map(HmeMakeCenterProduceBoardVO19::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                            // 复检合格数
                            reInspectionOkQtyList = hmeMakeCenterProduceBoardMapper.queryReInspectionOkQty(tenantId, materialLotIdList);
                        }
                    }
                    // 查询当天COS的不良数
                    ncRecordNum = hmeMakeCenterProduceBoardMapper.queryCosNcRecordNum(tenantId);
                    cosTestBoardList = hmeMakeCenterProduceBoardMapper.queryCos015SnQty(tenantId, cosWorkOrderIdList);
                }
            }
            Map<String, List<HmeMakeCenterProduceBoardVO19>> cosBoardMap = cosBoardList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO19::getProcessId));

            for (HmeMakeCenterProduceBoardVO10 boardVO10 : productionGroupList) {
                List<HmeMakeCenterProduceBoardVO12> boardVO12List = new ArrayList<>();
                BigDecimal throughRate = BigDecimal.ONE;
                // 获取产品组下所有工单
                List<String> workOrderIdList = boardVO10.getWorkOrderIdList();
                // 取两者的交集
                List<String> filterWorkOrderIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    filterWorkOrderIdList = distinctCurrentWorkOrderIdList.stream().filter(vo -> workOrderIdList.contains(vo)).collect(Collectors.toList());
                }
                // 根据第一个工单 获取对应工序及工位
                if (CollectionUtils.isNotEmpty(filterWorkOrderIdList)) {
                    List<HmeMakeCenterProduceBoardVO11> boardVO11List = firstWorkOrderMap.get(filterWorkOrderIdList.get(0));
                    // 按工序计算直通率
                    for (HmeMakeCenterProduceBoardVO11 boardVO11 : boardVO11List) {
                        HmeMakeCenterProduceBoardVO12 boardVO12 = new HmeMakeCenterProduceBoardVO12();
                        boardVO12.setProcessId(boardVO11.getProcessId());
                        boardVO12.setProcessName(boardVO11.getProcessName());
                        BigDecimal processThroughRate = BigDecimal.ONE;
                        if (Constant.ConstantValue.YES.equals(boardVO10.getCosFlag())) {
                            // COS 工序直通率 产出数/投产数  取片工序 = 产出数+复检合格数/投产数
                            List<HmeMakeCenterProduceBoardVO19> processCosBoardList = cosBoardMap.getOrDefault(boardVO11.getProcessId(), new ArrayList<>());
                            List<HmeMakeCenterProduceBoardVO19> cosBoardVO19s = processCosBoardList.stream().filter(vo -> workOrderIdList.contains(vo.getWorkOrderId())).collect(Collectors.toList());
                            Double siteOutQty = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getSiteOutQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            Double snQty = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getSnQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            Double reInspectionOkQty = 0.0D;
                            if (Constant.ConstantValue.YES.equals(boardVO11.getQpFlag())) {
                                List<String> materialLotIds = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                                reInspectionOkQty = reInspectionOkQtyList.stream().filter(vo -> materialLotIds.contains(vo.getMaterialLotId())).map(HmeMakeCenterProduceBoardVO20::getReInspectionOkQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            }
                            if (snQty.compareTo(Double.valueOf(0)) != 0) {
                                processThroughRate = BigDecimal.valueOf(siteOutQty).add(BigDecimal.valueOf(reInspectionOkQty)).divide(BigDecimal.valueOf(snQty), 2, BigDecimal.ROUND_HALF_DOWN);
                            }
                        } else {
                            List<HmeMakeCenterProduceBoardVO19> boardVO19s = boardVO19List.stream().filter(vo -> StringUtils.equals(boardVO11.getProcessId(), vo.getProcessId()) && workOrderIdList.contains(vo.getWorkOrderId())).collect(Collectors.toList());
                            // 获取所有的EO
                            List<String> eoList = boardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getEoId).distinct().collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(eoList)) {
                                // 获取没有返修记录的EO
                                List<HmeMakeCenterProduceBoardVO19> reworkList = reworkEoList.stream().filter(rework -> eoList.contains(rework.getEoId()) && StringUtils.equals(boardVO11.getProcessId(), rework.getProcessId())).collect(Collectors.toList());
                                List<String> reworkEoIdList = reworkList.stream().map(HmeMakeCenterProduceBoardVO19::getEoId).collect(Collectors.toList());
                                List<String> nonReworkEoList = eoList.stream().filter(eo -> !reworkEoIdList.contains(eo)).collect(Collectors.toList());
                                processThroughRate = BigDecimal.valueOf(nonReworkEoList.size()).divide(BigDecimal.valueOf(eoList.size()), 2, BigDecimal.ROUND_HALF_DOWN);
                            }
                        }
                        boardVO12.setProcessThroughRate(processThroughRate);
                        boardVO12List.add(boardVO12);
                        if (BigDecimal.ZERO.compareTo(processThroughRate) != 0) {
                            throughRate = throughRate.multiply(processThroughRate).setScale(6, BigDecimal.ROUND_HALF_DOWN);
                        }
                    }
                    // 查询横轴对应的工序
                    List<HmeMakeCenterProduceBoardVO12> centerProduceBoardVO12List = kanbanLineByHeaderMap.get(boardVO10.getCenterKanbanHeaderId());
                    HmeMakeCenterProduceBoardVO12 boardVO12 = new HmeMakeCenterProduceBoardVO12();
                    if (Constant.ConstantValue.YES.equals(boardVO10.getCosFlag())) {
                        // COS测试工序 直通率为 SN数/(SN数+不良数)
                        List<HmeMakeCenterProduceBoardVO19> cosTestBoards = cosTestBoardList.stream().filter(vo -> workOrderIdList.contains(vo.getWorkOrderId())).collect(Collectors.toList());
                        Double snQty = cosTestBoards.stream().map(HmeMakeCenterProduceBoardVO19::getSnQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        boardVO12.setProcessThroughRate(BigDecimal.ONE);
                        if (snQty.compareTo(Double.valueOf(0)) != 0 && ncRecordNum.compareTo(Integer.valueOf(0)) != 0) {
                            boardVO12.setProcessThroughRate(BigDecimal.valueOf(snQty).divide(BigDecimal.valueOf(snQty + ncRecordNum), 2, BigDecimal.ROUND_HALF_DOWN));
                        }
                        throughRate = throughRate.multiply(boardVO12.getProcessThroughRate()).setScale(6, BigDecimal.ROUND_HALF_DOWN);
                        boardVO12.setThroughRate(throughRate);
                    }
                    BigDecimal finalThroughRate = throughRate;
                    centerProduceBoardVO12List.stream().forEach(cpb -> {
                        //  为COS测试工序 即cosTestFlag = Y 另外取值
                        if (Constant.ConstantValue.YES.equals(cpb.getCosTestFlag())) {
                            cpb.setProcessThroughRate(boardVO12.getProcessThroughRate());
                        } else {
                            Optional<HmeMakeCenterProduceBoardVO12> firstOpt = boardVO12List.stream().filter(vo -> StringUtils.equals(vo.getProcessId(), cpb.getProcessId())).findFirst();
                            if (firstOpt.isPresent()) {
                                cpb.setProcessThroughRate(firstOpt.get().getProcessThroughRate());
                            } else {
                                cpb.setProcessThroughRate(BigDecimal.ONE);
                            }
                        }
                        cpb.setThroughRate(finalThroughRate);
                    });
                    // 组装数据返回
                    HmeMakeCenterProduceBoardVO9 boardVO9 = new HmeMakeCenterProduceBoardVO9();
                    boardVO9.setChartTitle(boardVO10.getProductionGroupCode() + "-" + boardVO10.getDescription());
                    boardVO9.setTargetThroughRate(boardVO10.getTargetThroughRate());
                    // 目标直通率为空 默认1 小于目标直通率 则红色显示 即N 大于等于则Y
                    BigDecimal targetThroughRate = boardVO10.getTargetThroughRate() != null ? boardVO10.getTargetThroughRate() : BigDecimal.ONE;
                    if (finalThroughRate.compareTo(targetThroughRate) >= 0) {
                        boardVO9.setOverFlag(Constant.ConstantValue.YES);
                    } else {
                        boardVO9.setOverFlag(Constant.ConstantValue.NO);
                    }
                    boardVO9.setChartValueList(centerProduceBoardVO12List);
                    resultList.add(boardVO9);
                }
            }
        }
        return resultList;
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO13> queryProcessNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        // 根据选择产线取工序及工位信息
        List<HmeMakeCenterProduceBoardVO11> processList = hmeMakeCenterProduceBoardMapper.queryProcessByProdLineId(tenantId, boardVO.getSiteId(), boardVO.getProdLineIdList());
        List<HmeMakeCenterProduceBoardVO13> resultList = new ArrayList<>();
        List<String> processIdList = processList.stream().filter(vo -> !StringUtils.equals(vo.getCosFlag(), "Y")).map(HmeMakeCenterProduceBoardVO11::getProcessId).distinct().collect(Collectors.toList());
        Map<String, List<HmeMakeCenterProduceBoardVO14>> boardVO14Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(processIdList)) {
            List<HmeMakeCenterProduceBoardVO14> boardVO14List = hmeMakeCenterProduceBoardMapper.queryNcCodeDataList(tenantId, processIdList, boardVO.getSiteId());
            if (CollectionUtils.isNotEmpty(boardVO14List)) {
                 boardVO14Map = boardVO14List.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO14::getProcessId));
            }
        }
        // COS产线工序 不良汇总明细记录数
        List<String> cosProcessIdList = processList.stream().filter(vo -> StringUtils.equals(vo.getCosFlag(), "Y")).map(HmeMakeCenterProduceBoardVO11::getProcessId).distinct().collect(Collectors.toList());
        Map<String, List<HmeMakeCenterProduceBoardVO14>> boardVO14CosMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(cosProcessIdList)) {
            List<HmeMakeCenterProduceBoardVO14> boardVO14List = hmeMakeCenterProduceBoardMapper.queryCosNcCodeDataList(tenantId, cosProcessIdList, boardVO.getSiteId());
            if (CollectionUtils.isNotEmpty(boardVO14List)) {
                boardVO14CosMap = boardVO14List.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO14::getProcessId));
            }
        }
        for (HmeMakeCenterProduceBoardVO11 processVO : processList) {
            HmeMakeCenterProduceBoardVO13 boardVO13 = new HmeMakeCenterProduceBoardVO13();
            boardVO13.setChartsTitle(processVO.getProcessName());
            List<HmeMakeCenterProduceBoardVO14> boardVO14List = new ArrayList<>();
            // COS产线的工序
            if (StringUtils.equals(processVO.getCosFlag(), Constant.ConstantValue.YES)) {
                boardVO14List = boardVO14CosMap.get(processVO.getProcessId());
            } else {
                // 取今天所有非放行不良数据
                boardVO14List = boardVO14Map.get(processVO.getProcessId());
            }

            List<HmeMakeCenterProduceBoardVO14> sortList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(boardVO14List)) {
                sortList = boardVO14List.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO14::getNcCount).reversed()).collect(Collectors.toList());
                // 超过5个的截取
                if (sortList.size() > 5) {
                    sortList = sortList.subList(0, 5);
                }
            }
            boardVO13.setChartsValueList(sortList);
            resultList.add(boardVO13);
        }
        return resultList;
    }
    @Override
    public HmeMakeCenterProduceBoardVO15 queryInspectionNc(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        HmeMakeCenterProduceBoardVO15 boardVO15 = new HmeMakeCenterProduceBoardVO15();
        // 获取产线的部门
        List<String> areaIdList = hmeMakeCenterProduceBoardMapper.queryAreaIdByProdLineId(tenantId, boardVO.getProdLineIdList(), boardVO.getSiteId());
        // 获取部门下所有产线
        List<String> prodLineIdList = hmeMakeCenterProduceBoardMapper.queryDefaultAreaProdLineIdList(tenantId, areaIdList, boardVO.getSiteId());
        BigDecimal ncRate = BigDecimal.ZERO;
        // 月度发生不良次数
        Integer monthNcCount = hmeMakeCenterProduceBoardMapper.queryMonthNcCount(tenantId, boardVO.getSiteId(), boardVO.getProdLineIdList());
        if (CollectionUtils.isNotEmpty(prodLineIdList)) {
            // 查询看板维护的物料
            List<String> materialIdList = hmeMakeCenterProduceBoardMapper.queryMaterialFromKanban(tenantId, areaIdList, boardVO.getSiteId());
            // 物料及产线取出工单
            List<String> workOrderIdList = hmeMakeCenterProduceBoardMapper.queryWOrkOrderByMaterialAndProdLine(tenantId, materialIdList, prodLineIdList, boardVO.getSiteId());
            // 月度产品完工数
            Integer monthFinishCount = 0;
            if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                List<List<String>> workOrderSplitIdList = CommonUtils.splitSqlList(workOrderIdList, 3000);
                for (List<String> workOrderIds : workOrderSplitIdList) {
                    Integer finishCount = hmeMakeCenterProduceBoardMapper.queryMonthFinishCount(tenantId, workOrderIds);
                    monthFinishCount += finishCount;
                }
            }
            // 巡检不良率=月度发生不良次数/月度产品完工数*100%
            if (monthFinishCount.compareTo(Integer.valueOf(0)) != 0) {
                ncRate = BigDecimal.valueOf(monthNcCount).divide(BigDecimal.valueOf(monthFinishCount), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        // 稽核时间 限制为本月两周内
        Date startDate = this.queryTwoWeekDate();
        List<HmeMakeCenterProduceBoardVO16> boardVO16List = hmeMakeCenterProduceBoardMapper.queryInspectionNc(tenantId, startDate, boardVO.getProdLineIdList());
        Integer indexNum = 0;
        for (HmeMakeCenterProduceBoardVO16 boardVO16 : boardVO16List) {
            if (CollectionUtils.isNotEmpty(boardVO16.getProblemDescList())) {
                boardVO16.setProblemDesc(StringUtils.join(boardVO16.getProblemDescList(), ","));
            }
            boardVO16.setSerialNumber(indexNum++);
        }
        boardVO15.setInspectionNcList(CollectionUtils.isNotEmpty(boardVO16List) ? boardVO16List : Collections.EMPTY_LIST);
        boardVO15.setInspectionNcRate(ncRate + "%");
        return boardVO15;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    @Override
    public HmeMakeCenterProduceBoardVO5 queryMonthPlanByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        // 根据部门获取纵轴信息(产品-产品描述)
        List<HmeMakeCenterProduceBoardVO7> materialLists = hmeMakeCenterProduceBoardMapper.queryMaterialAndMaterialNameByAreaId(tenantId, boardVO);
        List<HmeMakeCenterProduceBoardVO7> materialList = materialLists.stream().filter(distinctByKey(HmeMakeCenterProduceBoardVO7::getMonthlyPlanId)).collect(Collectors.toList());

        List<String> materialIdList = new ArrayList<>();
        materialList.forEach(s ->{
            materialIdList.add(s.getMaterialId());
        });
        //过滤掉当前无派工的机型
        //List<String> ids= hmeMakeCenterProduceBoardMapper.queryAttrValueByMaterialIdList(materialIdList);

        // 根据产品描述和产品排序
        //materialList = materialList.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO7::getMaterialName).thenComparing(HmeMakeCenterProduceBoardVO7::getMaterialCode)).collect(Collectors.toList());
        // 根据部门查询产线
        List<String> prodLineIdList = hmeMakeCenterProduceBoardMapper.queryProdLineByAreaId(tenantId, boardVO.getAreaId(), boardVO.getSiteId());
        boardVO.setProdLineIdList(prodLineIdList);
        return this.handleChartsData(tenantId, materialList, boardVO);
    }

    private HmeMakeCenterProduceBoardVO5 handleChartsData(Long tenantId, List<HmeMakeCenterProduceBoardVO7> materialList, HmeMakeCenterProduceBoardVO boardVO) {
        HmeMakeCenterProduceBoardVO5 boardVO5 = new HmeMakeCenterProduceBoardVO5();
        boardVO5.setYAxisList(Collections.EMPTY_LIST);
        boardVO5.setXAxisList(Collections.EMPTY_LIST);
        if (CollectionUtils.isNotEmpty(materialList)) {
            List<String> materialIdList = materialList.stream().map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
            //获取选取部门的ID
            List<String> areaIdList  = new ArrayList<>();
            areaIdList.add(boardVO.getAreaId());
            // 计划完成 用户默认部门，按部门+月份+物料取计划数量  -->取所选部门，按部门+月份+机型(相同机型的物料进行汇总)
            //一个月内 一个部门下面 一个站点内的数量
            List<HmeMakeCenterProduceBoardVO8> planFinishQtyList = hmeMakeCenterProduceBoardMapper.
                    queryPlanFinishQty(tenantId, boardVO.getSiteId(), areaIdList, materialIdList);
            Map<String, BigDecimal> planFinishQtyMap =
                    planFinishQtyList.stream().collect(Collectors.toMap(HmeMakeCenterProduceBoardVO8::getMaterialId, HmeMakeCenterProduceBoardVO8::getPlanFinishQty));

            // 已完成(分COS及非COS)
            List<String> cosMaterialIdList = materialList.stream().filter(mm -> StringUtils.equalsIgnoreCase(mm.getMaterialType(), "COS")).map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
            List<String> nonCosMaterialIdList = materialList.stream().filter(mm -> !StringUtils.equalsIgnoreCase(mm.getMaterialType(), "COS")).map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
            Map<String, BigDecimal> cosFinishQtyMap = new HashMap<>();
            Map<String, BigDecimal> nonCosFinishQtyMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(cosMaterialIdList)) {
                List<HmeMakeCenterProduceBoardVO8> cosFinishQtyList =
                        hmeMakeCenterProduceBoardMapper.queryCosMaterialFinishedQty(tenantId, cosMaterialIdList);
                if (CollectionUtils.isNotEmpty(cosFinishQtyList)) {
                    cosFinishQtyMap = cosFinishQtyList.stream().collect(Collectors.toMap(HmeMakeCenterProduceBoardVO8::getMaterialId, HmeMakeCenterProduceBoardVO8::getFinishQty));
                }
            }
            if (CollectionUtils.isNotEmpty(nonCosMaterialIdList)) {
                List<HmeMakeCenterProduceBoardVO8> nonCosFinishQtyLis = hmeMakeCenterProduceBoardMapper.queryNonCosMaterialFinishedQty(tenantId, nonCosMaterialIdList, boardVO.getProdLineIdList());
                if (CollectionUtils.isNotEmpty(nonCosFinishQtyLis)) {
                    nonCosFinishQtyMap = nonCosFinishQtyLis.stream().collect(Collectors.toMap(HmeMakeCenterProduceBoardVO8::getMaterialId, HmeMakeCenterProduceBoardVO8::getFinishQty));
                }
            }

            List<HmeMakeCenterProduceBoardVO6> xAxisList = new ArrayList<>();
            HmeMakeCenterProduceBoardVO6 planFinishDto = new HmeMakeCenterProduceBoardVO6();
            planFinishDto.setXAxisName("计划完成");
            planFinishDto.setColor("#2B5989`");
            planFinishDto.setXAxisType(Constant.ConstantValue.STRING_ZERO);
            HmeMakeCenterProduceBoardVO6 finishDto = new HmeMakeCenterProduceBoardVO6();
            finishDto.setXAxisName("已完成");
            finishDto.setColor("#2B92FD");
            finishDto.setNoticeColor("#FFB848");
            finishDto.setXAxisType(Constant.ConstantValue.STRING_ONE);
            List<BigDecimal> planFinishValues = new ArrayList<>();
            List<BigDecimal> finishValues = new ArrayList<>();
            List<Integer> noticeIndexList = new ArrayList<>();
            List<BigDecimal> proportionList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Integer monthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Integer currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            Integer indexNum = 0;

            Map<String, List<HmeMakeCenterProduceBoardVO7>> materilaLmap = materialList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO7::getYAxis));
            Map<String, List<HmeMakeCenterProduceBoardVO8>> planFinishQtyMap1 = new HashMap<>();
            // 设置Y轴
            List<String> yAxisList = new ArrayList<>();
            for (Map.Entry<String, List<HmeMakeCenterProduceBoardVO7>> dataMap : materilaLmap.entrySet()) {
                yAxisList.add(dataMap.getKey());
                List<HmeMakeCenterProduceBoardVO7> dataMapValueList = dataMap.getValue();
                BigDecimal planFinishQty = BigDecimal.ZERO;
                BigDecimal finishQty = BigDecimal.ZERO;
                for (HmeMakeCenterProduceBoardVO7 hmeMakeCenterProduceBoardVO7 : dataMapValueList) {
                    planFinishQty =  planFinishQty.add(planFinishQtyMap.getOrDefault(hmeMakeCenterProduceBoardVO7.getMaterialId(), BigDecimal.ZERO));
                    if (StringUtils.equals(hmeMakeCenterProduceBoardVO7.getMaterialType(), "COS")) {
                        finishQty = finishQty.add(cosFinishQtyMap.getOrDefault(hmeMakeCenterProduceBoardVO7.getMaterialId(), BigDecimal.ZERO));
                    } else {
                        finishQty = finishQty.add(nonCosFinishQtyMap.getOrDefault(hmeMakeCenterProduceBoardVO7.getMaterialId(), BigDecimal.ZERO));
                    }
                }
                //计算完成占比
                BigDecimal proportion = finishQty.divide(planFinishQty, 4, BigDecimal.ROUND_HALF_DOWN);
                proportionList.add(proportion);
                // 计划完成
                planFinishValues.add(planFinishQty);
                // 已完成
                finishValues.add(finishQty);

                // 计算当日应完成数：当天日期/当月天数*计划完成数 大于已完成数 则红色警示
                BigDecimal dailyWorkQty = BigDecimal.valueOf(currentDay).divide(BigDecimal.valueOf(monthMaxDay), 6, BigDecimal.ROUND_HALF_DOWN).multiply(planFinishQty).setScale(3, BigDecimal.ROUND_HALF_DOWN);
                if (dailyWorkQty.compareTo(finishQty) > 0) {
                    noticeIndexList.add(indexNum);
                }
                indexNum++;
            }

            finishDto.setProportionList(proportionList);
            planFinishDto.setValueList(planFinishValues);
            xAxisList.add(planFinishDto);
            finishDto.setNoticeIndexList(noticeIndexList);
            finishDto.setValueList(finishValues);
            xAxisList.add(finishDto);
            boardVO5.setXAxisList(xAxisList);
            boardVO5.setYAxisList(yAxisList);
            //计算当月达成率 总计划完成数
            BigDecimal planFinishValue = new BigDecimal(0);
            for (BigDecimal planFinishVa: planFinishValues) {
                planFinishValue = planFinishValue.add(planFinishVa);
            }
            //已完成的
            BigDecimal finishValue = new BigDecimal(0);
            for (BigDecimal finishVa : finishValues) {
                finishValue = finishValue.add(finishVa);
            }
            BigDecimal monthRate = finishValue.divide(planFinishValue,4,BigDecimal.ROUND_HALF_DOWN);
            boardVO5.setMonthRate(monthRate);

        }
        return boardVO5;
    }

//    private HmeMakeCenterProduceBoardVO5 handleChartsData(Long tenantId, List<HmeMakeCenterProduceBoardVO7> materialList, HmeMakeCenterProduceBoardVO boardVO) {
//        HmeMakeCenterProduceBoardVO5 boardVO5 = new HmeMakeCenterProduceBoardVO5();
//        boardVO5.setYAxisList(Collections.EMPTY_LIST);
//        boardVO5.setXAxisList(Collections.EMPTY_LIST);
//        if (CollectionUtils.isNotEmpty(materialList)) {
//            //设置y轴
//            //boardVO5.setYAxisList(materialList.stream().map(HmeMakeCenterProduceBoardVO7::getYAxis).collect(Collectors.toList()));
//            List<String> materialIdList = materialList.stream().map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
//            //获取选取部门的ID
//            List<String> areaIdList  = new ArrayList<>();
//            areaIdList.add(boardVO.getAreaId());
//            // 计划完成 用户默认部门，按部门+月份+物料取计划数量  -->取所选部门，按部门+月份+机型(相同机型的物料进行汇总)
//            //一个月内 一个部门下面 一个站点内的数量
//            List<HmeMakeCenterProduceBoardVO8> planFinishQtyList = hmeMakeCenterProduceBoardMapper.
//                    queryPlanFinishQty(tenantId, boardVO.getSiteId(), areaIdList, materialIdList);
//            Map<String, List<HmeMakeCenterProduceBoardVO8>> planFinishQtyMap =
//                    planFinishQtyList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO8::getMaterialId));
//
//            Map<String, List<HmeMakeCenterProduceBoardVO7>> materilaLmap = materialList.stream().collect(Collectors.
//                    groupingBy(HmeMakeCenterProduceBoardVO7::getYAxis));
//            Map<String, BigDecimal> planFinishQtyMap1 = new HashMap<>();
//            List<String> yAxisList = new ArrayList<>();
//            materilaLmap.entrySet().forEach(dataMap -> {
//
//                yAxisList.add(dataMap.getKey());
//                List<HmeMakeCenterProduceBoardVO7> dataMapValueList = dataMap.getValue();
//                BigDecimal planFinishQty =BigDecimal.ZERO;
//                List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVOList = new ArrayList<>();
//                for (HmeMakeCenterProduceBoardVO7 hmeMakeCenterProduceBoardVO7 : dataMapValueList) {
//                    List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVO8List =
//                            planFinishQtyMap.get(hmeMakeCenterProduceBoardVO7.getMaterialId());
//                    for (HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO8 : hmeMakeCenterProduceBoardVO8List) {
//                        HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO = new HmeMakeCenterProduceBoardVO8();
//                        hmeMakeCenterProduceBoardVO.setMaterialId(hmeMakeCenterProduceBoardVO8.getMaterialId());
//                        planFinishQty = planFinishQty.add(hmeMakeCenterProduceBoardVO8.getPlanFinishQty());
//                        hmeMakeCenterProduceBoardVO.setPlanFinishQty(hmeMakeCenterProduceBoardVO8.getPlanFinishQty());
//                        hmeMakeCenterProduceBoardVOList.add(hmeMakeCenterProduceBoardVO);
//                    }
//                    planFinishQtyMap1.put(dataMap.getKey(),planFinishQty);
//                    boardVO5.setYAxisList(yAxisList);
//                }
//            });
//
//            //已完成(分COS及非COS)
//            Map<String, List<HmeMakeCenterProduceBoardVO8>> cosFinishQtyMap = new HashMap<>();
//            Map<String, List<HmeMakeCenterProduceBoardVO8>> nonCosFinishQtyMap = new HashMap<>();
//            Map<String, BigDecimal> cosFinishQtyMap1 = new HashMap<>();
//            Map<String, BigDecimal> nonCosFinishQtyMap1 = new HashMap<>();
//
//
//            List<String> cosMaterialIdList = materialList.stream().filter(mm -> StringUtils.equalsIgnoreCase(mm.getMaterialType(), "COS")).map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
//            List<String> nonCosMaterialIdList = materialList.stream().filter(mm -> !StringUtils.equalsIgnoreCase(mm.getMaterialType(), "COS")).map(HmeMakeCenterProduceBoardVO7::getMaterialId).collect(Collectors.toList());
//            if (CollectionUtils.isNotEmpty(cosMaterialIdList)) {
//                List<HmeMakeCenterProduceBoardVO8> cosFinishQtyList = hmeMakeCenterProduceBoardMapper.queryCosMaterialFinishedQty(tenantId, cosMaterialIdList);
//                if (CollectionUtils.isNotEmpty(cosFinishQtyList)) {
//                    cosFinishQtyMap =
//                            cosFinishQtyList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO8::getMaterialId));
//                    for (Map.Entry<String, List<HmeMakeCenterProduceBoardVO7>> dataMap : materilaLmap.entrySet()) {
//                        List<HmeMakeCenterProduceBoardVO7> dataMapValueList = dataMap.getValue();
//                        BigDecimal finishQty = BigDecimal.ZERO;
//                        List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVOList = new ArrayList<>();
//                        for (HmeMakeCenterProduceBoardVO7 hmeMakeCenterProduceBoardVO7 : dataMapValueList) {
//                            List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVO8List =
//                                    cosFinishQtyMap.get(hmeMakeCenterProduceBoardVO7.getMaterialId());
//                            for (HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO8 : hmeMakeCenterProduceBoardVO8List) {
//                                HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO = new HmeMakeCenterProduceBoardVO8();
//                                finishQty = finishQty.add(hmeMakeCenterProduceBoardVO8.getPlanFinishQty());
//                                hmeMakeCenterProduceBoardVO.setPlanFinishQty(finishQty);
//                                hmeMakeCenterProduceBoardVOList.add(hmeMakeCenterProduceBoardVO);
//                            }
//                        }
//                        cosFinishQtyMap1.put(dataMap.getKey(), finishQty);
//                    }
//                }
//            }
//
//            if (CollectionUtils.isNotEmpty(nonCosMaterialIdList)) {
//                List<HmeMakeCenterProduceBoardVO8> nonCosFinishQtyLis =
//                        hmeMakeCenterProduceBoardMapper.queryNonCosMaterialFinishedQty(tenantId, nonCosMaterialIdList, boardVO.getProdLineIdList());
//                if (CollectionUtils.isNotEmpty(nonCosFinishQtyLis)) {
//                    nonCosFinishQtyMap =
//                            nonCosFinishQtyLis.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO8::getMaterialId));
//                    for (Map.Entry<String, List<HmeMakeCenterProduceBoardVO7>> dataMap : materilaLmap.entrySet()) {
//                        List<HmeMakeCenterProduceBoardVO7> dataMapValueList = dataMap.getValue();
//                        BigDecimal finishQty = BigDecimal.ZERO;
//                        List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVOList = new ArrayList<>();
//                        for (HmeMakeCenterProduceBoardVO7 hmeMakeCenterProduceBoardVO7 : dataMapValueList) {
//                            List<HmeMakeCenterProduceBoardVO8> hmeMakeCenterProduceBoardVO8List =
//                                    nonCosFinishQtyMap.get(hmeMakeCenterProduceBoardVO7.getMaterialId());
//                            for (HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO8 : hmeMakeCenterProduceBoardVO8List) {
//                                HmeMakeCenterProduceBoardVO8 hmeMakeCenterProduceBoardVO = new HmeMakeCenterProduceBoardVO8();
//                                finishQty = finishQty.add(hmeMakeCenterProduceBoardVO8.getPlanFinishQty());
//                                hmeMakeCenterProduceBoardVO.setPlanFinishQty(finishQty);
//                                hmeMakeCenterProduceBoardVOList.add(hmeMakeCenterProduceBoardVO);
//                            }
//                        }
//                        nonCosFinishQtyMap1.put(dataMap.getKey(), finishQty);
//                    }
//                }
//            }
//
//            List<HmeMakeCenterProduceBoardVO6> xAxisList = new ArrayList<>();
//            HmeMakeCenterProduceBoardVO6 planFinishDto = new HmeMakeCenterProduceBoardVO6();
//            planFinishDto.setXAxisName("计划完成");
//            planFinishDto.setColor("yellow");
//            planFinishDto.setXAxisType(Constant.ConstantValue.STRING_ZERO);
//            HmeMakeCenterProduceBoardVO6 finishDto = new HmeMakeCenterProduceBoardVO6();
//            finishDto.setXAxisName("已完成");
//            finishDto.setColor("blue");
//            finishDto.setNoticeColor("red");
//            finishDto.setXAxisType(Constant.ConstantValue.STRING_ONE);
//            List<BigDecimal> planFinishValues = new ArrayList<>();
//            List<BigDecimal> finishValues = new ArrayList<>();
//            List<Integer> noticeIndexList = new ArrayList<>();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            Integer monthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            Integer currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//            Integer indexNum = 0;
//            List<String> yAxisList1 = boardVO5.getYAxisList();
//            for (String s : yAxisList1) {
//                //获取计划完成数
//                BigDecimal plan = new BigDecimal(0);
//                if(planFinishQtyMap1.size()!= 0){
//                    plan = planFinishQtyMap1.get(s);
//                }
//                BigDecimal cos = new BigDecimal(0);
//                if(nonCosFinishQtyMap1.size() != 0){
//                    cos = nonCosFinishQtyMap1.get(s);
//                }
//                if(cosFinishQtyMap1.size() != 0){
//                    cos = cosFinishQtyMap1.get(s);
//                }
//                finishValues.add(cos);
//                planFinishValues.add(plan);
//                // 计算当日应完成数：当天日期/当月天数*计划完成数 大于已完成数 则红色警示
//                BigDecimal dailyWorkQty = BigDecimal.valueOf(currentDay).divide(BigDecimal.valueOf(monthMaxDay), 6,
//                        BigDecimal.ROUND_HALF_DOWN).multiply(plan).setScale(3, BigDecimal.ROUND_HALF_DOWN);
//                if (dailyWorkQty.compareTo(plan) > 0) {
//                    noticeIndexList.add(indexNum);
//                }
//                indexNum++;
//            }
//
////            for (HmeMakeCenterProduceBoardVO7 boardVO7 : materialList) {
////                // 计划完成
////                //List<HmeMakeCenterProduceBoardVO8> planFinishList = planFinishQtyMap1.get(boardVO7.getYAxis());
////                BigDecimal planFinish = BigDecimal.ZERO;
////                if (CollectionUtils.isNotEmpty(planFinishList)) {
////                    planFinish = planFinishList.get(0).getPlanFinishQty();
////                }
////                planFinishValues.add(planFinish);
////                // 已完成
////                BigDecimal finishQty = BigDecimal.ZERO;
////                if (StringUtils.equals(boardVO7.getMaterialType(), "COS")) {
////                    List<HmeMakeCenterProduceBoardVO8> cosFinishList = cosFinishQtyMap1.get(boardVO7.getYAxis());
////                    if (CollectionUtils.isNotEmpty(cosFinishList)) {
////                        finishQty = cosFinishList.get(0).getFinishQty();
////                    }
////                } else {
////                    List<HmeMakeCenterProduceBoardVO8> nonCosFinishList = nonCosFinishQtyMap1.get(boardVO7.getYAxis());
////                    if (CollectionUtils.isNotEmpty(nonCosFinishList)) {
////                        finishQty = nonCosFinishList.get(0).getFinishQty();
////                    }
////                }
////                finishValues.add(finishQty);
////                // 计算当日应完成数：当天日期/当月天数*计划完成数 大于已完成数 则红色警示
////                BigDecimal dailyWorkQty = BigDecimal.valueOf(currentDay).divide(BigDecimal.valueOf(monthMaxDay), 6, BigDecimal.ROUND_HALF_DOWN).multiply(planFinish).setScale(3, BigDecimal.ROUND_HALF_DOWN);
////                if (dailyWorkQty.compareTo(finishQty) > 0) {
////                    noticeIndexList.add(indexNum);
////                }
////                indexNum++;
////            }
//            planFinishDto.setValueList(planFinishValues);
//            xAxisList.add(planFinishDto);
//            finishDto.setNoticeIndexList(noticeIndexList);
//            finishDto.setValueList(finishValues);
//            xAxisList.add(finishDto);
//            boardVO5.setXAxisList(xAxisList);
//        }
//        return boardVO5;
//    }

    @Override
    public List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetailsByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));

         //所选制造部下，每个产线一个柱状图，
        //1、获取车间 取表mt_mod_organization_rel.【ORGANIZATION_id】
        List<String> organizationIdList = hmeMakeCenterProduceBoardMapper.queryCarByAreaId(boardVO.getAreaId());
        //2、获取产线  再根据车间找产线
        List<String> prodLineIdLists = hmeMakeCenterProduceBoardMapper.queryProdLineByOrganizationIdList(organizationIdList);
        //去重
        List<String> prodLineIdList = prodLineIdLists.stream().distinct().collect(Collectors.toList());
        //3、获取柱状图名称  一个部门对应多个产线 一条产线一个图
        //List<HmeMakeCenterProdLineNameVO> prodLineNameList = hmeMakeCenterProduceBoardMapper.queryProdLineNameByProdlineId(prodLineIdList);
        //4、查询产品组  一条产线一个图  产品组就是横坐标
        List<HmeMakeCenterProdLineNameVO> productionGroupCodeList = hmeMakeCenterProduceBoardMapper.
                        queryProductionGroupCodeByProdLineIdList(prodLineIdList,this.queryKanbanSiteId(tenantId));
        Map<String, List<HmeMakeCenterProdLineNameVO>> productionGroupCodeMap =
                productionGroupCodeList.stream().collect(Collectors.
                        groupingBy(HmeMakeCenterProdLineNameVO::getProdLineId));
        // attribute1-产线id  attribute2-产品组id
        //通过产线id  查询产品组的直通率
        List<HmeMakeCenterProduceBoardVO22> hmeMakeCenterProduceBoardVO22s =
                hmeMakeCenterProduceBoardMapper.queryThroughRateByProdLineIdList(prodLineIdList,tenantId);

        List<HmeMakeCenterProduceBoardVO9> resultList = new ArrayList<>();
        productionGroupCodeMap.entrySet().forEach(dataMap -> {
            HmeMakeCenterProduceBoardVO9 hmeMakeCenterProduceBoardVO9 = new HmeMakeCenterProduceBoardVO9();
            List<HmeMakeCenterProdLineNameVO> hmeMakeCenterProdLineNameVOList = dataMap.getValue();
            List<HmeMakeCenterProduceBoardVO12> chartValueList = new ArrayList<>();
            for (HmeMakeCenterProdLineNameVO hmeMakeCenterProdLineNameVO : hmeMakeCenterProdLineNameVOList) {
                hmeMakeCenterProduceBoardVO9.setProdLineName(hmeMakeCenterProdLineNameVO.getProdLineName());
                hmeMakeCenterProduceBoardVO9.setProdLineId(hmeMakeCenterProdLineNameVO.getProdLineId());
                hmeMakeCenterProduceBoardVO9.setProdLineOrder(hmeMakeCenterProdLineNameVO.getProdLineOrder());
                HmeMakeCenterProduceBoardVO12 hmeMakeCenterProduceBoardVO12 = new HmeMakeCenterProduceBoardVO12();
                // 取描述显示
                hmeMakeCenterProduceBoardVO12.setProductionGroupCode(hmeMakeCenterProdLineNameVO.getProductionGroupDesc());
//                hmeMakeCenterProduceBoardVO12.setProductionGroupCode(hmeMakeCenterProdLineNameVO.getProductionGroupCode());
                hmeMakeCenterProduceBoardVO22s.forEach(v->{
                    if(v.getAttribute1().equals(hmeMakeCenterProdLineNameVO.getProdLineId())
                            &&v.getAttribute2().equals(hmeMakeCenterProdLineNameVO.getProductionGroupId())){
                        hmeMakeCenterProduceBoardVO12.setThroughRate(v.getThroughRate());
                    }
                });
                if(Objects.isNull(hmeMakeCenterProduceBoardVO12.getThroughRate())){
                    continue;
                }
                chartValueList.add(hmeMakeCenterProduceBoardVO12);
            }
            if(!Objects.isNull(chartValueList)){
                hmeMakeCenterProduceBoardVO9.setChartValueList(chartValueList);
            }

            resultList.add(hmeMakeCenterProduceBoardVO9);
        });
        //过滤没有直通率的产线 及没有排序的产线
        List<HmeMakeCenterProduceBoardVO9> resultLists = resultList.stream().
                filter(s -> (s.getChartValueList().size()) != 0).collect(Collectors.toList());
        //按照产线排序
        List<HmeMakeCenterProduceBoardVO9> resultLi = resultLists.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO9::getProdLineId)).
                collect(Collectors.toList());
        return resultLi;
    }

    private String spliceStr (HmeMakeCenterProduceBoardVO22 vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getChartTitle());
        sb.append(vo.getOverFlag());
        sb.append(vo.getTargetHeaderThroughRate());
        return sb.toString();
    }

    /***
     * 拆分数据 拆分个数大于指定数 则将剩余数据放到最后一列
     * @param sqlList
     * @param splitNum
     * @return java.util.List<java.util.List<T>>
     * @author sanfeng.zhang@hand-china.com 2021/6/21
     */
    private static <T> List<List<T>> splitSqlList(List<T> sqlList, Integer splitNum, Integer maxNum) {
        if (maxNum == null) {
            maxNum = 10;
        }
        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            if (splitCount > maxNum - 1) {
                splitCount = maxNum - 1;
            }
            int splitRest = sqlList.size() - splitCount * splitNum;
            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO13> queryMaterialNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        List<HmeMakeCenterProduceBoardVO13> resultList = new ArrayList<>();
        // 根据部门查询产线(区分COS产线)
        List<HmeMakeCenterProduceBoardVO21> prodLineList =  hmeMakeCenterProduceBoardMapper.queryProdLineInfoByAreaId(tenantId, boardVO.getAreaId(), boardVO.getSiteId());
        Map<String, HmeMakeCenterProduceBoardVO21> prodLineOrderMap = prodLineList.stream().collect(Collectors.toMap(HmeMakeCenterProduceBoardVO21::getProdLineId, Function.identity()));
        List<HmeMakeCenterProduceBoardVO21> cosProdLineList = prodLineList.stream().filter(prodLine -> StringUtils.equals(Constant.ConstantValue.YES, prodLine.getCosFlag())).collect(Collectors.toList());
        List<HmeMakeCenterProduceBoardVO21> nonCosProdLineList = prodLineList.stream().filter(prodLine -> !StringUtils.equals(Constant.ConstantValue.YES, prodLine.getCosFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nonCosProdLineList)) {
            // 获取产线
            List<String> nonCosProdLineIdList = nonCosProdLineList.stream().map(HmeMakeCenterProduceBoardVO21::getProdLineId).collect(Collectors.toList());
            //List<String> workcellIdList = hmeMakeCenterProduceBoardMapper.queryWorkcellByProdLineIds(tenantId, nonCosProdLineIdList, boardVO.getSiteId());
            if (CollectionUtils.isNotEmpty(nonCosProdLineIdList)) {
                // 拆分sql
                List<List<String>> splitProdLineIdList = CommonUtils.splitSqlList(nonCosProdLineIdList, 1000);
                List<HmeMakeCenterProduceBoardVO14> boardAllList = new ArrayList<>();
                for (List<String> splitProdLineId : splitProdLineIdList) {
                    List<HmeMakeCenterProduceBoardVO14> boardVO14List = hmeMakeCenterProduceBoardMapper.queryMaterialNcCodeDataList(tenantId, splitProdLineId, boardVO.getSiteId());
                    if (CollectionUtils.isNotEmpty(boardVO14List)) {
                        boardAllList.addAll(boardVO14List);
                    }
                }
                List<HmeMakeCenterProduceBoardVO13> boardVO13List = assembleNcTopFiveList(boardAllList);
                if (CollectionUtils.isNotEmpty(boardVO13List)) {
                    resultList.addAll(boardVO13List);
                }
            }
        }
        // COS 不良top5
        if (CollectionUtils.isNotEmpty(cosProdLineList)) {
            String startTime = DateUtil.date2String(new Date(), "yyyy-MM-dd 00:00:00");
            String endTime = DateUtil.date2String(new Date(), "yyyy-MM-dd 23:59:59");
            // 获取产线
            List<String> cosProdLineIdList = cosProdLineList.stream().map(HmeMakeCenterProduceBoardVO21::getProdLineId).collect(Collectors.toList());
            //List<String> workcellIdList = hmeMakeCenterProduceBoardMapper.queryWorkcellByProdLineIds(tenantId, cosProdLineIdList, boardVO.getSiteId());
            if (CollectionUtils.isNotEmpty(cosProdLineIdList)) {
                // 拆分sql
                List<List<String>> splitProdLineIdList = CommonUtils.splitSqlList(cosProdLineIdList, 1000);
                List<HmeMakeCenterProduceBoardVO14> boardAllList = new ArrayList<>();
                for (List<String> prodLineIdList : splitProdLineIdList) {
                    List<HmeMakeCenterProduceBoardVO14> boardVO14List = hmeMakeCenterProduceBoardMapper.queryCosMaterialNcCodeDataList(tenantId, prodLineIdList, boardVO.getSiteId(), startTime, endTime);
                    if (CollectionUtils.isNotEmpty(boardVO14List)) {
                        boardAllList.addAll(boardVO14List);
                    }
                }
                List<HmeMakeCenterProduceBoardVO13> boardVO13List = assembleNcTopFiveList(boardAllList);
                if (CollectionUtils.isNotEmpty(boardVO13List)) {
                    resultList.addAll(boardVO13List);
                }
            }
        }

        List<HmeMakeCenterProduceBoardVO9> hmeMakeCenterProduceBoardVO9sList =
                hmeMakeCenterProduceBoardService.queryThroughRateDetailsByArea(tenantId, boardVO);
        List<HmeMakeCenterProduceBoardVO13> resultLists = new ArrayList<>();
        //谁的长度大遍历谁
        if(hmeMakeCenterProduceBoardVO9sList.size()>=resultList.size()){
            for (HmeMakeCenterProduceBoardVO9 hmeMakeCenterProduceBoardVO9 : hmeMakeCenterProduceBoardVO9sList) {
                HmeMakeCenterProduceBoardVO13 vo = new HmeMakeCenterProduceBoardVO13();
                vo.setChartValueList(hmeMakeCenterProduceBoardVO9.getChartValueList());
                vo.setProdLineId(hmeMakeCenterProduceBoardVO9.getProdLineId());
                vo.setProLineName(hmeMakeCenterProduceBoardVO9.getProdLineName());
                HmeMakeCenterProduceBoardVO21 orderObj = prodLineOrderMap.get(hmeMakeCenterProduceBoardVO9.getProdLineId());
                if (orderObj != null) {
                    vo.setProdLineOrder(orderObj.getProdLineOrder());
                }
                for (HmeMakeCenterProduceBoardVO13 hmeMakeCenterProduceBoardVO13 : resultList) {
                    if(hmeMakeCenterProduceBoardVO13.getProdLineId().equals(hmeMakeCenterProduceBoardVO9.getProdLineId())){
                        vo.setChartsValueList(hmeMakeCenterProduceBoardVO13.getChartsValueList());
                        vo.setMCount(hmeMakeCenterProduceBoardVO13.getMCount());
                    }
                }
                resultLists.add(vo);
            }
        }else {
            for (HmeMakeCenterProduceBoardVO13 hmeMakeCenterProduceBoardVO13 : resultList) {
                HmeMakeCenterProduceBoardVO13 vo = new HmeMakeCenterProduceBoardVO13();
                vo.setChartsValueList(hmeMakeCenterProduceBoardVO13.getChartsValueList());
                vo.setMCount(hmeMakeCenterProduceBoardVO13.getMCount());
                for (HmeMakeCenterProduceBoardVO9 hmeMakeCenterProduceBoardVO9 : hmeMakeCenterProduceBoardVO9sList) {
                    if (hmeMakeCenterProduceBoardVO13.getProdLineId().equals(hmeMakeCenterProduceBoardVO9.getProdLineId())) {
                        vo.setChartValueList(hmeMakeCenterProduceBoardVO9.getChartValueList());
                        vo.setProdLineId(hmeMakeCenterProduceBoardVO9.getProdLineId());
                        vo.setProLineName(hmeMakeCenterProduceBoardVO9.getProdLineName());
                    }
                }
                // 如果直通率为空 则取不良对应的产线
                if (CollectionUtils.isEmpty(vo.getChartValueList())) {
                    vo.setProdLineId(hmeMakeCenterProduceBoardVO13.getProdLineId());
                }
                HmeMakeCenterProduceBoardVO21 orderObj = prodLineOrderMap.get(vo.getProdLineId());
                if (orderObj != null) {
                    vo.setProdLineOrder(orderObj.getProdLineOrder());
                }
                resultLists.add(vo);
            }
        }
        // 排除产线排序为空的数据
        List<HmeMakeCenterProduceBoardVO13> filterResultList = resultLists.stream().filter(dto -> dto.getProdLineOrder() != null).collect(Collectors.toList());
        // 按排序正序排序
        filterResultList = filterResultList.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO13::getProdLineOrder)).collect(Collectors.toList());
        return filterResultList;
    }

    private List<HmeMakeCenterProduceBoardVO13> assembleNcTopFiveList(List<HmeMakeCenterProduceBoardVO14> boardAllList) {
        List<HmeMakeCenterProduceBoardVO13> resultList = new ArrayList<>();
        // 根据产线维度进行汇总
        //List<HmeMakeCenterProduceBoardVO14> filterBoardList = boardAllList.stream().filter(vo -> vo.getMaterialType() != null).collect(Collectors.toList());
        Map<String, List<HmeMakeCenterProduceBoardVO14>> boardVO14Map = boardAllList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO14::getProdLineId));
        boardVO14Map.entrySet().forEach(map -> {
            HmeMakeCenterProduceBoardVO13 boardVO13 = new HmeMakeCenterProduceBoardVO13();
            boardVO13.setChartsTitle(map.getKey());
            //根据不良代码进行分组
            Map<String, List<HmeMakeCenterProduceBoardVO14>> ncCodeMap = map.getValue().stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO14::getNcCodeId));
            List<HmeMakeCenterProduceBoardVO14> ncCodeList = new ArrayList<>();
            ncCodeMap.forEach((ncKey, ncValue) -> {
                HmeMakeCenterProduceBoardVO14 vo14 = new HmeMakeCenterProduceBoardVO14();
                vo14.setNcCodeId(ncKey);
                vo14.setDescription(ncValue.get(0).getDescription());
                vo14.setNcCount(ncValue.size());
                vo14.setProdLineName(ncValue.get(0).getProdLineName());
                ncCodeList.add(vo14);
            });
            List<HmeMakeCenterProduceBoardVO14> sortList = ncCodeList.stream().
                    sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO14::getNcCount).reversed()).collect(Collectors.toList());
            Long count = 0L;
            for (HmeMakeCenterProduceBoardVO14 hmeMakeCenterProduceBoardVO14 : sortList) {
                count = count + hmeMakeCenterProduceBoardVO14.getNcCount();
            }
            for (HmeMakeCenterProduceBoardVO14 hmeMakeCenterProduceBoardVO14 : sortList) {//计算不良率
                BigDecimal NcCount = new BigDecimal(hmeMakeCenterProduceBoardVO14.getNcCount());
                BigDecimal count1 = new BigDecimal(count);
                BigDecimal defectiveRate= NcCount.divide(count1,4,BigDecimal.ROUND_HALF_DOWN);
                hmeMakeCenterProduceBoardVO14.setDefectiveRate(defectiveRate);
            }

            List<HmeMakeCenterProduceBoardVO14> sortList1 = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO14> sortList2 = new ArrayList<>();

            // 超过5个的截取
            if (sortList.size() > 5) {
                sortList1 = sortList.subList(0, 5);
                sortList2 = sortList.subList(5,sortList.size());
            }

            Long sortCount2 = 0L;
            for (HmeMakeCenterProduceBoardVO14 s : sortList2) {
                sortCount2 = sortCount2 + s.getNcCount();
            }
            if(sortList2.size()>0){
                //统计超过5个的
                HmeMakeCenterProduceBoardVO14 hmeMakeCenterProduceBoardVO14 = new HmeMakeCenterProduceBoardVO14();

                BigDecimal NcCount = new BigDecimal(sortCount2);
                BigDecimal count1 = new BigDecimal(count);
                BigDecimal defectiveRate= NcCount.divide(count1,4,BigDecimal.ROUND_HALF_DOWN);

                hmeMakeCenterProduceBoardVO14.setDefectiveRate(defectiveRate);
                hmeMakeCenterProduceBoardVO14.setDescription("其他");
                hmeMakeCenterProduceBoardVO14.setNcCount(Math.toIntExact(sortCount2));
                sortList1.add(hmeMakeCenterProduceBoardVO14);
            }
            boardVO13.setMCount(count);
            if(sortList.size() > 5){
                boardVO13.setChartsValueList(sortList1);
            }else{
                boardVO13.setChartsValueList(sortList);
            }

            boardVO13.setProdLineId(map.getKey());
            resultList.add(boardVO13);
        });
        //通过产线排序
        List<HmeMakeCenterProduceBoardVO13> resultLists = resultList.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO13::getProdLineId)).
                collect(Collectors.toList());
        return resultLists;
    }

    private List<String> queryCurrentCalendarShiftIdList(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        List<String> calendarShiftIdList = new ArrayList<>();
        // 获取当天产线对应工段下的班次(考虑跨天的情况 班次日期往后推一天)
        List<HmeCalendarShiftVO> calendarShiftList = hmeMakeCenterProduceBoardMapper.queryCalendarShiftList(tenantId, boardVO);
        // 根据当前时间取最近的班次日历
        Map<String, List<HmeCalendarShiftVO>> calendarShiftMap = calendarShiftList.stream().collect(Collectors.groupingBy(HmeCalendarShiftVO::getCalendarId));
        for (Map.Entry<String, List<HmeCalendarShiftVO>> calendarShiftEntry : calendarShiftMap.entrySet()) {
            List<HmeCalendarShiftVO> valueList = calendarShiftEntry.getValue();
            List<HmeCalendarShiftVO> sortValueList = valueList.stream().sorted(Comparator.comparing(HmeCalendarShiftVO::getShiftStartTime).reversed()).collect(Collectors.toList());
            calendarShiftIdList.add(sortValueList.get(0).getCalendarShiftId());
        }
        return calendarShiftIdList;
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateListByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.setSiteId(this.queryKanbanSiteId(tenantId));
        return hmeMakeCenterProduceBoardMapper.queryDayPlanReachRateListByArea(tenantId, boardVO.getAreaId(), boardVO.getSiteId());
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO18> queryKanbanAreaList(Long tenantId) {
        return hmeMakeCenterProduceBoardMapper.queryKanbanAreaList(tenantId);
    }

    @Override
    public HmeKanbanConfigVO queryKanbanConfig(Long tenantId) {
        HmeKanbanConfigVO hmeKanbanConfigVO = new HmeKanbanConfigVO();
        List<LovValueDTO> centerConfig = lovAdapter.queryLovValue("HME.CENTER_KANBAN_CONFIG", tenantId);
        hmeKanbanConfigVO.setCenterConfig(CollectionUtils.isNotEmpty(centerConfig) ? centerConfig : Collections.EMPTY_LIST);
        List<LovValueDTO> areaList = lovAdapter.queryLovValue("HME.AREA_SYB_KANBAN", tenantId);
        hmeKanbanConfigVO.setAreaList(CollectionUtils.isNotEmpty(areaList) ? areaList : Collections.EMPTY_LIST);
        List<LovValueDTO> productInspectionConfig = lovAdapter.queryLovValue("HME.PRODUCT_INSPECTION_KANBAN_CONFIG", tenantId);
        hmeKanbanConfigVO.setProductInspectionConfig(CollectionUtils.isNotEmpty(productInspectionConfig) ? productInspectionConfig : Collections.EMPTY_LIST);
        List<LovValueDTO> siteInfo = lovAdapter.queryLovValue("HME.KANBAN_SITE", tenantId);
        if (CollectionUtils.isEmpty(siteInfo)) {
            throw new CommonException("请维护看板站点值集【HME.KANBAN_SITE】");
        }
        HmeModSiteVO siteVO = new HmeModSiteVO();
        siteVO.setSiteId(siteInfo.get(0).getValue());
        siteVO.setSiteCode(siteInfo.get(0).getMeaning());
        siteVO.setSiteName(siteInfo.get(0).getDescription());
        hmeKanbanConfigVO.setSiteInfo(siteVO);
        return hmeKanbanConfigVO;
    }

    @Override
    public Page<HmeModProductionLineVO> queryKanbanProdLine(Long tenantId, HmeModProductionLineVO lineVO, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeMakeCenterProduceBoardMapper.queryProductionInfoList(tenantId, lineVO));
    }

    @Override
    public HmeMakeCenterProduceBoardVO queryAreaIdByAreaCode(String areaCode) {
        return hmeMakeCenterProduceBoardMapper.queryAreaIdByAreaCode(areaCode);
    }

    /**
     * 获取本月前两周数据 不足两周的取本月第一天
     * @param
     * @return java.util.Date
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    private Date queryTwoWeekDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day > 14) {
            calendar.set(Calendar.DAY_OF_MONTH, -14);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY),calendar.get(Calendar.DAY_OF_MONTH),00,00,00);
        return calendar.getTime();
    }
}
