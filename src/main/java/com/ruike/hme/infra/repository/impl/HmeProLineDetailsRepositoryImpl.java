package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.domain.repository.HmeProLineDetailsRepository;
import com.ruike.hme.domain.vo.HmeModWorkcellVO;
import com.ruike.hme.domain.vo.HmeProcessInfoVO;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import com.ruike.hme.infra.mapper.HmeProLineDetailsMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 20:13
 */
@Component
public class HmeProLineDetailsRepositoryImpl implements HmeProLineDetailsRepository {

    @Autowired
    private HmeProLineDetailsMapper hmeProLineDetailsMapper;

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        //获取默认组织ID
        String defaultSiteId = hmeProLineDetailsMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        if (StringUtils.isNotBlank(params.getStartTime())) {
            params.setStartTime(CommonUtils.dateStrFormat(params.getStartTime(), "yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            params.setEndTime(CommonUtils.dateStrFormat(params.getEndTime(), "yyyy-MM-dd 23:59:59"));
        }
        Page<HmeProductionLineDetailsDTO> resultList = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryDetails(tenantId, params));
        resultList.forEach(e -> {
            String shiftDate = e.getShiftDate();
            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                //工段下所有工序
                List<HmeModWorkcellVO> processList = hmeProLineDetailsMapper.queryProcessByLineWorkcellId(tenantId, e.getLineWorkcellId());
                if (CollectionUtils.isNotEmpty(processList)) {
                    //开班时间
                    e.setShiftStartTime(hmeProLineDetailsMapper.queryMinShiftStart(tenantId, shiftDate, e.getLineWorkcellId()));
                    //结班时间
                    List<Date> dateList = hmeProLineDetailsMapper.queryMaxShiftEnd(tenantId, shiftDate, e.getLineWorkcellId());
                    if (CollectionUtils.isNotEmpty(dateList)) {
                        Boolean flag = false;
                        for (Date date : dateList) {
                            if (date == null) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            e.setShiftEndTime(dateList.get(0));
                        }
                    }

                    //根据工单id找所有的工序
                    List<HmeProcessInfoVO> processInfoVOList = hmeProLineDetailsMapper.queryProcessByWorkOderId(tenantId, e.getWorkOrderId());

                    List<HmeModWorkcellVO> resultProcessList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(processList)) {
                        processInfoVOList.forEach(pro -> {
                            processList.forEach(c -> {
                                if (pro != null) {
                                    if (StringUtils.equals(pro.getWorkcellId(), c.getWorkcellId())) {
                                        HmeModWorkcellVO mtModWorkcell = new HmeModWorkcellVO();
                                        mtModWorkcell.setWorkcellId(c.getWorkcellId());
                                        mtModWorkcell.setWorkcellName(c.getWorkcellName());
                                        resultProcessList.add(mtModWorkcell);
                                    }
                                }
                            });
                        });
                    } else {
                        resultProcessList.addAll(processList);
                    }

                    if (CollectionUtils.isNotEmpty(resultProcessList)) {
                        Date endDate = new Date();
                        if (e.getShiftEndTime() != null) {
                            endDate = e.getShiftEndTime();
                        }
                        //投产(首道)
                        // 根据工序查找工位
                        List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(0).getWorkcellId());
                        BigDecimal shiftProduction = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(workcellVOList)) {
                            List<String> materialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            shiftProduction = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                        }
                        e.setPutData(shiftProduction.intValue());
                        e.setFirstProcessId(resultProcessList.get(0).getWorkcellId());

                        //完工(末道)
                        List<HmeModWorkcellVO> endProcessList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                        BigDecimal shiftEndProduction = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(endProcessList)) {
                            List<String> endMaterialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), endProcessList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            shiftEndProduction  = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, endMaterialLotIdList);
                        }
                        e.setFinishedData(shiftEndProduction.intValue());
                        e.setEndProcessId(resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                    }

                }

                //不良数
                List<String> eoIds = hmeProLineDetailsMapper.queryEoIdByWorkOrderId(tenantId, e.getWorkOrderId());
                BigDecimal ncNumber = BigDecimal.ZERO;
                List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByLineWorkcellId(tenantId, e.getLineWorkcellId(), defaultSiteId);
                List<String> workcellIds = workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(eoIds) && CollectionUtils.isNotEmpty(workcellIds)) {
                    List<String> materialLotIdList = hmeProLineDetailsMapper.getMaterialLotId5(tenantId, e.getShiftStartTime(), e.getShiftEndTime(), workcellIds, eoIds);
                    materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                    ncNumber = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                }
                e.setNcNumber(ncNumber);
            }
        });
        return resultList;
    }

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        //获取默认组织ID
        String defaultSiteId = hmeProLineDetailsMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        if (StringUtils.isNotBlank(params.getStartTime())) {
            params.setStartTime(CommonUtils.dateStrFormat(params.getStartTime(), "yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            params.setEndTime(CommonUtils.dateStrFormat(params.getEndTime(), "yyyy-MM-dd 23:59:59"));
        }
        Page<HmeProductionLineDetailsDTO> resultList = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductShiftList(tenantId, params));
        resultList.forEach(e -> {
            //判断工段 为空 根据工段id 查询工段 生产线 车间id
            if (StringUtils.isBlank(e.getLineWorkcellId())) {
                HmeProductionLineDetailsDTO hmeProductionLineDetailsDTO = hmeProLineDetailsMapper.queryLineWorkcellUpIdInfo(tenantId, e.getShiftWorkcellId());
                if (hmeProductionLineDetailsDTO != null) {
                    e.setWorkshopId(hmeProductionLineDetailsDTO.getWorkshopId());
                    e.setProductionLineId(hmeProductionLineDetailsDTO.getProductionLineId());
                    e.setLineWorkcellId(hmeProductionLineDetailsDTO.getLineWorkcellId());
                }
            }

            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                //工段下所有工序
                List<HmeModWorkcellVO> processList = hmeProLineDetailsMapper.queryProcessByLineWorkcellId(tenantId, e.getLineWorkcellId());

                if (CollectionUtils.isNotEmpty(processList)) {
                    List<String> processIdList = processList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList());
                    //根据工单id找所有的工序
                    List<HmeProcessInfoVO> processInfoVOList = hmeProLineDetailsMapper.queryProcessByWorkOderId(tenantId, e.getWorkOrderId());

                    List<HmeModWorkcellVO> resultProcessList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(processList)) {
                        processInfoVOList.forEach(pro -> {
                            if (pro != null) {
                                processList.forEach(c -> {
                                    if (StringUtils.equals(pro.getWorkcellId(), c.getWorkcellId())) {
                                        HmeModWorkcellVO mtModWorkcell = new HmeModWorkcellVO();
                                        mtModWorkcell.setWorkcellId(pro.getWorkcellId());
                                        resultProcessList.add(mtModWorkcell);
                                    }
                                });
                            }
                        });
                    } else {
                        resultProcessList.addAll(processList);
                    }

                    if (CollectionUtils.isNotEmpty(resultProcessList)) {
                        //投产(首道)
                        List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(0).getWorkcellId());
                        if (CollectionUtils.isNotEmpty(workcellVOList)) {
                            List<String> materialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), e.getShiftEndTime());
                            //本班投产
                            BigDecimal shiftProduction = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                            e.setPutData(shiftProduction.intValue());
                        }

                        //完工(末道)
                        List<HmeModWorkcellVO> endWorkcellList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                        if (CollectionUtils.isNotEmpty(endWorkcellList)) {
                            List<String> endMaterialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), endWorkcellList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), e.getShiftEndTime());
                            //本班投产
                            BigDecimal shiftProduction = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, endMaterialLotIdList);
                            e.setFinishedData(shiftProduction.intValue());
                        }
                    }
                }

                //不良数
                List<String> eoIds = hmeProLineDetailsMapper.queryEoIdByWorkOrderId(tenantId, e.getWorkOrderId());
                List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByLineWorkcellId(tenantId, e.getLineWorkcellId(), defaultSiteId);
                BigDecimal ncNumber = BigDecimal.ZERO;
                List<String> workcellIds = workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(eoIds) && CollectionUtils.isNotEmpty(workcellIds)) {
                    List<String> materialLotIdList = hmeProLineDetailsMapper.getMaterialLotId5(tenantId, e.getShiftStartTime(), e.getShiftEndTime(), workcellIds, eoIds);
                    materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                    ncNumber = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                }
                e.setNcNumber(ncNumber);
            }
        });
        return resultList;
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        //获取工序下 工位id
        List<HmeModWorkcellVO> processOrg = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, params.getProcessId());
        List<String> workcellIdList = processOrg.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(workcellIdList)) {
            return new Page<HmeProductEoInfoVO>();
        }
        if (StringUtils.isBlank(params.getShiftEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.setShiftEndTime(sdf.format(new Date()));
        }

        Page<HmeProductEoInfoVO> page = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductProcessEoList(tenantId, params.getMaterialId(), params.getWorkOrderId(), workcellIdList, params.getShiftStartTime(), params.getShiftEndTime()));
        // 过滤非COS作业的(COS 没有eo)
        List<String> eoIdentificationList = page.getContent().stream().filter(nonCos -> StringUtils.isNotBlank(nonCos.getEoNum())).map(HmeProductEoInfoVO::getEoIdentification).filter(Objects::nonNull).collect(Collectors.toList());
        List<HmeProductEoInfoVO> eoInfoVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIdentificationList)) {
            eoInfoVOList = hmeProLineDetailsMapper.batchReworkFlagQuery(tenantId, eoIdentificationList);
        }
        Map<String, List<HmeProductEoInfoVO>> reworkFlagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoInfoVOList)) {
            reworkFlagMap = eoInfoVOList.stream().collect(Collectors.groupingBy(dto -> dto.getEoIdentification()));
        }
        Map<String, List<HmeProductEoInfoVO>> reworkFlagFinalMap = reworkFlagMap;
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            page.getContent().forEach(e -> {
                if (StringUtils.isNotBlank(e.getEoNum())) {
                    List<HmeProductEoInfoVO> infoVOList = reworkFlagFinalMap.get(e.getEoIdentification());
                    if (CollectionUtils.isNotEmpty(infoVOList)) {
                        //返修标识
                        e.setValidateFlag(infoVOList.get(0).getValidateFlag());
                        //操作时间
                        e.setLastUpdateDate(infoVOList.get(0).getLastUpdateDate());
                    }
                }
            });
        }
        return page;
    }

    @Override
    public List<HmeProductionLineDetailsDTO> lineStationDetailsExport(Long tenantId, HmeProductionLineDetailsVO detailsVO) {
        //获取默认组织ID
        String defaultSiteId = hmeProLineDetailsMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        if (StringUtils.isNotBlank(detailsVO.getStartTime())) {
            detailsVO.setStartTime(CommonUtils.dateStrFormat(detailsVO.getStartTime(), "yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(detailsVO.getEndTime())) {
            detailsVO.setEndTime(CommonUtils.dateStrFormat(detailsVO.getEndTime(), "yyyy-MM-dd 23:59:59"));
        }

        List<HmeProductionLineDetailsDTO> resultList = hmeProLineDetailsMapper.queryDetails(tenantId, detailsVO);
        resultList.forEach(e -> {
            String shiftDate = e.getShiftDate();
            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                //工段下所有工序
                List<HmeModWorkcellVO> processList = hmeProLineDetailsMapper.queryProcessByLineWorkcellId(tenantId, e.getLineWorkcellId());
                if (CollectionUtils.isNotEmpty(processList)) {
                    //开班时间
                    e.setShiftStartTime(hmeProLineDetailsMapper.queryMinShiftStart(tenantId, shiftDate, e.getLineWorkcellId()));
                    //结班时间
                    List<Date> dateList = hmeProLineDetailsMapper.queryMaxShiftEnd(tenantId, shiftDate, e.getLineWorkcellId());
                    if (CollectionUtils.isNotEmpty(dateList)) {
                        Boolean flag = false;
                        for (Date date : dateList) {
                            if (date == null) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            e.setShiftEndTime(dateList.get(0));
                        }
                    }
                    //根据工单id找所有的工序
                    List<HmeProcessInfoVO> processInfoVOList = hmeProLineDetailsMapper.queryProcessByWorkOderId(tenantId, e.getWorkOrderId());

                    List<HmeModWorkcellVO> resultProcessList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(processList)) {
                        processInfoVOList.forEach(pro -> {
                            processList.forEach(c -> {
                                if (pro != null) {
                                    if (StringUtils.equals(pro.getWorkcellId(), c.getWorkcellId())) {
                                        HmeModWorkcellVO mtModWorkcell = new HmeModWorkcellVO();
                                        mtModWorkcell.setWorkcellId(c.getWorkcellId());
                                        mtModWorkcell.setWorkcellName(c.getWorkcellName());
                                        resultProcessList.add(mtModWorkcell);
                                    }
                                }
                            });
                        });
                    } else {
                        resultProcessList.addAll(processList);
                    }

                    if (CollectionUtils.isNotEmpty(resultProcessList)) {
                        Date endDate = new Date();
                        if (e.getShiftEndTime() != null) {
                            endDate = e.getShiftEndTime();
                        }
                        //投产(首道)
                        List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(0).getWorkcellId());
                        if (CollectionUtils.isNotEmpty(workcellVOList)) {
                            List<String> materialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            BigDecimal shiftProduction = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                            e.setPutData(shiftProduction.intValue());
                        }
                        e.setFirstProcessId(resultProcessList.get(0).getWorkcellId());

                        //完工(末道)
                        List<HmeModWorkcellVO> endWorkcellList = hmeProLineDetailsMapper.queryWorkcellByProcess(tenantId, resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                        if (CollectionUtils.isNotEmpty(endWorkcellList)) {
                            List<String> endMaterialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), endWorkcellList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            BigDecimal shiftProduction = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, endMaterialLotIdList);
                            e.setFinishedData(shiftProduction.intValue());
                        }
                        e.setEndProcessId(resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                    }

                }

                //不良数
                List<String> eoIds = hmeProLineDetailsMapper.queryEoIdByWorkOrderId(tenantId, e.getWorkOrderId());
                List<HmeModWorkcellVO> workcellVOList = hmeProLineDetailsMapper.queryWorkcellByLineWorkcellId(tenantId, e.getLineWorkcellId(), defaultSiteId);
                BigDecimal ncNumber = BigDecimal.ZERO;
                List<String> workcellIds = workcellVOList.stream().map(HmeModWorkcellVO::getWorkcellId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(eoIds) && CollectionUtils.isNotEmpty(workcellIds)) {
                    List<String> materialLotIdList = hmeProLineDetailsMapper.getMaterialLotId5(tenantId, e.getShiftStartTime(), e.getShiftEndTime(), workcellIds, eoIds);
                    materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                    ncNumber = hmeProLineDetailsMapper.sumMaterialLotQty(tenantId, materialLotIdList);
                }
                e.setNcNumber(ncNumber);
            }
            // 开班时间
            if (e.getShiftStartTime() != null) {
                e.setShiftStartTimeStr(DateUtils.format(e.getShiftStartTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            // 结班时间
            if (e.getShiftEndTime() != null) {
                e.setShiftEndTimeStr(DateUtils.format(e.getShiftEndTime(), "yyyy-MM-dd HH:mm:ss"));
            }
        });
        return resultList;
    }
}
