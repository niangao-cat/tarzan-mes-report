package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO3;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO4;
import com.ruike.hme.domain.repository.HmeRepairProductPassRateRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeProdLinePassRateMapper;
import com.ruike.hme.infra.mapper.HmeRepairProductPassRateMapper;
import com.ruike.hme.infra.util.WmsCommonUtils;
import com.ruike.qms.infra.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 返修产品直通率报表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-05-19 11:14:12
 */
@Component
public class HmeRepairProductPassRateRepositoryImpl implements HmeRepairProductPassRateRepository {

    @Autowired
    private HmeProdLinePassRateMapper hmeProdLinePassRateMapper;

    @Autowired
    private HmeRepairProductPassRateMapper hmeRepairProductPassRateMapper;

    @Override
    public HmeProdLinePassRateVO5 repairProductPassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto) {
        HmeProdLinePassRateVO5 hmeProdLinePassRateVO5 = new HmeProdLinePassRateVO5();
        List<HmeProdLinePassRateVO> resultList = new ArrayList<>();
        //确定时间段
        if (Objects.isNull(dto.getDateFrom()) && Objects.isNull(dto.getDateTo())) {
            HmeProdLinePassRateDTO hmeProdLinePassRateDTO = hmeRepairProductPassRateMapper.dateSlotQuery(tenantId);
            dto.setDateFrom(hmeProdLinePassRateDTO.getDateFrom());
            dto.setDateTo(hmeProdLinePassRateDTO.getDateTo());
        } else if (Objects.nonNull(dto.getDateFrom()) && Objects.isNull(dto.getDateTo())) {
            dto.setDateTo(new Date());
        } else if (Objects.isNull(dto.getDateFrom()) && Objects.nonNull(dto.getDateTo())) {
            HmeProdLinePassRateDTO hmeProdLinePassRateDTO = hmeRepairProductPassRateMapper.dateSlotQuery(tenantId);
            dto.setDateFrom(hmeProdLinePassRateDTO.getDateFrom());
        }
        //确定展示的工序
        List<HmeProdLinePassRateVO3> processList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            //如果选择了工序，则根据工序ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByIdQuery(tenantId, dto.getProcessId());
        } else if (StringUtils.isNotBlank(dto.getLineWorkCellId())) {
            //如果选择了工段，则根据工段ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByLineWorkcellQuery(tenantId, dto.getLineWorkCellId());
        } else if (StringUtils.isNotBlank(dto.getProdLineId())) {
            //如果选择了产线，则根据产线ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByProdLineQuery(tenantId, dto.getProdLineId());
        } else if (StringUtils.isNotBlank(dto.getAreaId())) {
            //如果选择了部门，则根据部门ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByDepartmentQuery(tenantId, dto.getAreaId());
        }
        if (CollectionUtils.isEmpty(processList)) {
            return hmeProdLinePassRateVO5;
        }
        List<String> processIdList = processList.stream().map(HmeProdLinePassRateVO3::getOrganizationId).distinct().collect(Collectors.toList());
        //拼接开始时间、结束时间组成时间段
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFrom = simpleDateFormat.format(dto.getDateFrom());
        String dateTo = simpleDateFormat.format(dto.getDateTo());
        String dateSlot = dateFrom + "-" + dateTo;
        //确定动态列中的所有物料，并记录每个工序与在此工序上作业的物料之间的对应关系
        Map<String, List<String>> processMaterialMap = new HashMap<>();
        List<HmeProdLinePassRateVO3> materialList = new ArrayList<>();
        List<HmeProdLinePassRateVO9> hmeProdLinePassRateVO9s = hmeRepairProductPassRateMapper.materialQuery(tenantId, processIdList, dto);
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeProdLinePassRateVO9s)){
            Map<String, List<HmeProdLinePassRateVO9>> proMaterialMap = hmeProdLinePassRateVO9s.stream().collect(Collectors.groupingBy(HmeProdLinePassRateVO9::getProcessId));
            for (Map.Entry<String, List<HmeProdLinePassRateVO9>> entry:proMaterialMap.entrySet()) {
                List<HmeProdLinePassRateVO9> valueList = entry.getValue();
                for (HmeProdLinePassRateVO9 hmeProdLinePassRateVO9:valueList) {
                    HmeProdLinePassRateVO3 material = new HmeProdLinePassRateVO3();
                    material.setOrganizationId(hmeProdLinePassRateVO9.getMaterialId());
                    material.setOrganizationName(hmeProdLinePassRateVO9.getMaterialCode());
                    materialList.add(material);
                }
                List<String> materialIdList = valueList.stream().map(HmeProdLinePassRateVO9::getMaterialId).collect(Collectors.toList());
                processMaterialMap.put(entry.getKey(), materialIdList);
            }
        }
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(materialList)) {
            materialList = materialList.stream().distinct().collect(Collectors.toList());
        }
        //根据工序ID批量查询所属产线
        List<HmeProdLinePassRateVO10> processProdLineList = hmeProdLinePassRateMapper.prodLineQuery(tenantId, processIdList);
        //根据工序与物料分批查询投产数
        List<HmeProdLinePassRateVO11> eoBatchList = new ArrayList<>();
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeProdLinePassRateVO9s)){
            List<List<HmeProdLinePassRateVO9>> splitList = WmsCommonUtils.splitSqlList(hmeProdLinePassRateVO9s, 1000);
            for (List<HmeProdLinePassRateVO9> split : splitList) {
                eoBatchList.addAll(hmeRepairProductPassRateMapper.getEoByProcessAndMaterial(tenantId, split, dto));
            }
        }
        //根据eo与工序与jobId分批查询不良记录
        List<HmeProdLinePassRateVO12> ncRecordBatchList = new ArrayList<>();
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(eoBatchList)){
            Map<String, List<HmeProdLinePassRateVO11>> eoProcessMap = eoBatchList.stream().collect(Collectors.groupingBy(t -> {
                return t.getEoId() + "," + t.getProcessId() + "," + t.getJobId();
            }));
            List<HmeProdLinePassRateVO11> eoProcessList = new ArrayList<>();
            for (String key:eoProcessMap.keySet()) {
                String[] splitKey = key.split(",");
                HmeProdLinePassRateVO11 hmeProdLinePassRateVO11 = new HmeProdLinePassRateVO11();
                hmeProdLinePassRateVO11.setEoId(splitKey[0]);
                hmeProdLinePassRateVO11.setProcessId(splitKey[1]);
                hmeProdLinePassRateVO11.setJobId(splitKey[2]);
                eoProcessList.add(hmeProdLinePassRateVO11);
            }
            List<List<HmeProdLinePassRateVO11>> splitList = WmsCommonUtils.splitSqlList(eoProcessList, 1000);
            for (List<HmeProdLinePassRateVO11> split : splitList) {
                ncRecordBatchList.addAll(hmeRepairProductPassRateMapper.ncRecordByEoQuery(tenantId, split));
            }
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(ncRecordBatchList)){
                ncRecordBatchList = ncRecordBatchList.stream().sorted(Comparator.comparing(HmeProdLinePassRateVO12::getCreationDate)).collect(Collectors.toList());
            }
        }
        for (HmeProdLinePassRateVO3 process : processList) {
            HmeProdLinePassRateVO hmeProdLinePassRateVO = new HmeProdLinePassRateVO();
            hmeProdLinePassRateVO.setDateSlot(dateSlot);
            hmeProdLinePassRateVO.setProcessId(process.getOrganizationId());
            hmeProdLinePassRateVO.setProcessName(process.getOrganizationName());
            List<HmeProdLinePassRateVO10> prodLine = processProdLineList.stream().filter(item -> process.getOrganizationId().equals(item.getProcessId())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(prodLine)) {
                hmeProdLinePassRateVO.setProdLineId(prodLine.get(0).getProdLineId());
                hmeProdLinePassRateVO.setProdLineName(prodLine.get(0).getProdLineName());
            }
            List<HmeProdLinePassRateVO2> materialDataList = new ArrayList<>();
            //根据工序在关系map中找到作业的物料，如果是在该工序上作业的物料则计算合格数、不良数等，否则给默认值"-"
            List<String> processMaterialList = processMaterialMap.get(process.getOrganizationId());
            for (HmeProdLinePassRateVO3 material : materialList) {
                HmeProdLinePassRateVO2 hmeProdLinePassRateVO2 = new HmeProdLinePassRateVO2();
                hmeProdLinePassRateVO2.setMaterialId(material.getOrganizationId());
                hmeProdLinePassRateVO2.setMaterialName(material.getOrganizationName());
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(processMaterialList)
                        && processMaterialList.contains(material.getOrganizationId())) {
                    //投产数查询
                    List<HmeProdLinePassRateVO11> eoList = eoBatchList.stream().filter(item -> process.getOrganizationId().equals(item.getProcessId())
                            && material.getOrganizationId().equals(item.getMaterialId())).collect(Collectors.toList());
                    int productionNum = 0;
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(eoList)) {
                        productionNum = eoList.size();
                    }
                    hmeProdLinePassRateVO2.setProductionNum(productionNum + "");
                    //合格明细
                    Long passNum = 0L;
                    List<String> identificationList = new ArrayList<>();
                    //不良明细
                    Long ncNum = 0L;
                    List<String> ncRecordIdList = new ArrayList<>();
                    List<HmeProdLinePassRateVO4> hmeProdLinePassRateVO4s = new ArrayList<>();
                    if (productionNum == 0) {
                        hmeProdLinePassRateVO2.setPassNum("-");
                        hmeProdLinePassRateVO2.setIdentificationList(identificationList);
                        hmeProdLinePassRateVO2.setNcNum("-");
                        hmeProdLinePassRateVO2.setNcDataList(hmeProdLinePassRateVO4s);
                    } else {
                        for (HmeProdLinePassRateVO11 eo : eoList) {
                            //根据每个eo查询父不良记录
                            List<HmeProdLinePassRateVO12> ncRecordList = ncRecordBatchList.stream().filter(item -> eo.getEoId().equals(item.getEoId())
                                    && process.getOrganizationId().equals(item.getProcessId()) && eo.getJobId().equals(item.getJobId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(ncRecordList)) {
                                //如果找不到父不良记录，则此Eo合格
                                passNum++;
                                identificationList.add(eo.getIdentification());
                            } else {
                                //将找到的父不良记录根据process_method为空或（!=2），且COMPONENT_REQUIRED=N筛选数据
                                List<HmeProdLinePassRateVO12> ncList = ncRecordList.stream().filter(item -> ((StringUtils.isEmpty(item.getProcessMethod())) || ((!"2".equals(item.getProcessMethod())))) && "N".equals(item.getComponentRequired())).collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(ncList)) {
                                    //如果筛选结果为空，则此EO合格
                                    passNum++;
                                    identificationList.add(eo.getIdentification());
                                } else {
                                    //如果筛选结果不为空，则此EO不良,不良明细则展示最早的一笔不良记录
                                    ncNum++;
                                    ncRecordIdList.add(ncList.get(0).getNcRecordId());
                                }
                            }
                        }
                        hmeProdLinePassRateVO2.setPassNum(passNum.toString());
                        hmeProdLinePassRateVO2.setIdentificationList(identificationList);
                        hmeProdLinePassRateVO2.setNcNum(ncNum.toString());
                        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ncRecordIdList)) {
                            List<List<String>> splitList = WmsCommonUtils.splitSqlList(ncRecordIdList, 500);
                            for (List<String> split : splitList) {
                                List<HmeProdLinePassRateVO4> ncRecordDetailList = hmeProdLinePassRateMapper.ncRecordDetailQuery(tenantId, split);
                                hmeProdLinePassRateVO4s.addAll(ncRecordDetailList);
                            }
                        }
                        hmeProdLinePassRateVO2.setNcDataList(hmeProdLinePassRateVO4s);
                    }

                    //良率 合格/投产*100%
                    if (productionNum == 0) {
                        hmeProdLinePassRateVO2.setRate("-");
                    } else {
                        BigDecimal rateBig = new BigDecimal(identificationList.size()).divide(new BigDecimal(productionNum), 4, BigDecimal.ROUND_HALF_UP);
                        hmeProdLinePassRateVO2.setRateBig(rateBig);
                        BigDecimal rate = rateBig.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                        hmeProdLinePassRateVO2.setRate(rate.toString() + "%");
                    }
                } else {
                    hmeProdLinePassRateVO2.setPassNum("-");
                    hmeProdLinePassRateVO2.setNcNum("-");
                    hmeProdLinePassRateVO2.setProductionNum("-");
                    hmeProdLinePassRateVO2.setRate("-");
                }
                materialDataList.add(hmeProdLinePassRateVO2);
            }
            hmeProdLinePassRateVO.setMaterialData(materialDataList);
            resultList.add(hmeProdLinePassRateVO);
        }
        //汇总每个物料的合格数、不良数、投产数、良率等
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(materialList)
                && org.apache.commons.collections4.CollectionUtils.isNotEmpty(resultList)) {
            List<String> passRateData = new ArrayList<>();
            for (HmeProdLinePassRateVO3 material : materialList) {
                BigDecimal totalPassNum = BigDecimal.ZERO;
                BigDecimal totalNcNum = BigDecimal.ZERO;
                BigDecimal totalProductionNum = BigDecimal.ZERO;
                BigDecimal totalRate = null;
                for (HmeProdLinePassRateVO result : resultList) {
                    List<HmeProdLinePassRateVO2> hmeProdLinePassRateVO2s = result.getMaterialData().stream().filter(data -> material.getOrganizationId().equals(data.getMaterialId())).collect(Collectors.toList());
                    if (hmeProdLinePassRateVO2s != null && hmeProdLinePassRateVO2s.size() > 0) {
                        if (!hmeProdLinePassRateVO2s.get(0).getPassNum().equals("-")) {
                            totalPassNum = totalPassNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getPassNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getNcNum().equals("-")) {
                            totalNcNum = totalNcNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getNcNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getProductionNum().equals("-")) {
                            totalProductionNum = totalProductionNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getProductionNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getRate().equals("-")) {
                            if (Objects.isNull(totalRate)) {
                                totalRate = hmeProdLinePassRateVO2s.get(0).getRateBig();
                            } else {
                                totalRate = totalRate.multiply(hmeProdLinePassRateVO2s.get(0).getRateBig()).setScale(4, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                }
                passRateData.add(totalPassNum.toString());
                passRateData.add(totalNcNum.toString());
                passRateData.add(totalProductionNum.toString());
                if(Objects.nonNull(totalRate)){
                    passRateData.add(totalRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                }else{
                    passRateData.add("-");
                }
            }
            hmeProdLinePassRateVO5.setPassRateData(passRateData);
        }
        hmeProdLinePassRateVO5.setResultList(resultList);
        return hmeProdLinePassRateVO5;
    }

    @Override
    public HmeProdLinePassRateVO8 repairProductDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException {
        HmeProdLinePassRateVO8 hmeProdLinePassRateVO8 = new HmeProdLinePassRateVO8();
        List<HmeProdLinePassRateVO6> resultList = new ArrayList<>();
        //确定展示的工序
        List<HmeProdLinePassRateVO3> processList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            //如果选择了工序，则根据工序ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByIdQuery(tenantId, dto.getProcessId());
        } else if (StringUtils.isNotBlank(dto.getLineWorkCellId())) {
            //如果选择了工段，则根据工段ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByLineWorkcellQuery(tenantId, dto.getLineWorkCellId());
        } else if (StringUtils.isNotBlank(dto.getProdLineId())) {
            //如果选择了产线，则根据产线ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByProdLineQuery(tenantId, dto.getProdLineId());
        } else if (StringUtils.isNotBlank(dto.getAreaId())) {
            //如果选择了部门，则根据部门ID确认展示的工序
            processList = hmeProdLinePassRateMapper.processByDepartmentQuery(tenantId, dto.getAreaId());
        }
        if (CollectionUtils.isEmpty(processList)) {
            return hmeProdLinePassRateVO8;
        }
        List<String> processIdList = processList.stream().map(HmeProdLinePassRateVO3::getOrganizationId).distinct().collect(Collectors.toList());
        String dateSlot = dto.getDateFrom().replace("-", "") + "-" + dto.getDateTo().replace("-", "");
        //根据工序ID批量查询所属产线
        List<HmeProdLinePassRateVO10> processProdLineList = hmeProdLinePassRateMapper.prodLineQuery(tenantId, processIdList);
        //根据工序ID批量查询投产数
        List<HmeProdLinePassRateVO13> eoBatchList = new ArrayList<>();
        List<List<String>> splitList = WmsCommonUtils.splitSqlList(processIdList, 1000);
        for (List<String> split : splitList) {
            eoBatchList.addAll(hmeRepairProductPassRateMapper.getEoByProcess(tenantId, split,
                    dto, dto.getDateFrom(), dto.getDateTo()));
        }
        //根据eo与工序与jobId分批查询不良记录
        List<HmeProdLinePassRateVO12> ncRecordBatchList = new ArrayList<>();
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(eoBatchList)){
            Map<String, List<HmeProdLinePassRateVO13>> eoProcessMap = eoBatchList.stream().collect(Collectors.groupingBy(t -> {
                return t.getEoId() + "," + t.getProcessId() + "," + t.getJobId();
            }));
            List<HmeProdLinePassRateVO11> eoProcessList = new ArrayList<>();
            for (String key:eoProcessMap.keySet()) {
                String[] splitKey = key.split(",");
                HmeProdLinePassRateVO11 hmeProdLinePassRateVO11 = new HmeProdLinePassRateVO11();
                hmeProdLinePassRateVO11.setEoId(splitKey[0]);
                hmeProdLinePassRateVO11.setProcessId(splitKey[1]);
                hmeProdLinePassRateVO11.setJobId(splitKey[2]);
                eoProcessList.add(hmeProdLinePassRateVO11);
            }
            List<List<HmeProdLinePassRateVO11>> splitEoProcessList = WmsCommonUtils.splitSqlList(eoProcessList, 1000);
            for (List<HmeProdLinePassRateVO11> split : splitEoProcessList) {
                ncRecordBatchList.addAll(hmeRepairProductPassRateMapper.ncRecordByEoQuery(tenantId, split));
            }
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(ncRecordBatchList)){
                ncRecordBatchList = ncRecordBatchList.stream().sorted(Comparator.comparing(HmeProdLinePassRateVO12::getCreationDate)).collect(Collectors.toList());
            }
        }
        for (HmeProdLinePassRateVO3 process : processList) {
            //根据工序查询所属产线
            HmeProdLinePassRateVO6 hmeProdLinePassRateVO6 = new HmeProdLinePassRateVO6();
            hmeProdLinePassRateVO6.setDate(dateSlot);
            hmeProdLinePassRateVO6.setProcessId(process.getOrganizationId());
            hmeProdLinePassRateVO6.setProcessName(process.getOrganizationName());
            List<HmeProdLinePassRateVO10> prodLine = processProdLineList.stream().filter(item -> process.getOrganizationId().equals(item.getProcessId())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(prodLine)) {
                hmeProdLinePassRateVO6.setProdLineId(prodLine.get(0).getProdLineId());
                hmeProdLinePassRateVO6.setProdLineName(prodLine.get(0).getProdLineName());
            }
            List<HmeProdLinePassRateVO7> shiftDataList = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
            Date start = dateFormat.parse(dto.getDateFrom());
            Date end = dateFormat.parse(dto.getDateTo());
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);
            while (tempStart.before(tempEnd)) {
                String date = dateFormat.format(tempStart.getTime());
                Date dateFrom = DateUtil.string2Date(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                Date dateTo = DateUtil.string2Date(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                HmeProdLinePassRateVO7 hmeProdLinePassRateVO7 = new HmeProdLinePassRateVO7();
                hmeProdLinePassRateVO7.setShiftName(dateFormat2.format(tempStart.getTime()));
                //投产数查询
                List<HmeProdLinePassRateVO13> eoList = eoBatchList.stream().filter(item ->
                        process.getOrganizationId().equals(item.getProcessId())
                                && dateFrom.compareTo(item.getSiteOutDate()) <= 0
                                && dateTo.compareTo(item.getSiteOutDate()) >= 0).collect(Collectors.toList());
                int productionNum = 0;
                if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(eoList)){
                    productionNum = eoList.size();
                }
                hmeProdLinePassRateVO7.setProductionNum(productionNum + "");
                //合格明细
                Long passNum = 0L;
                List<String> identificationList = new ArrayList<>();
                //不良明细
                Long ncNum = 0L;
                List<String> ncRecordIdList = new ArrayList<>();
                List<HmeProdLinePassRateVO4> hmeProdLinePassRateVO4s = new ArrayList<>();
                if (productionNum == 0) {
                    hmeProdLinePassRateVO7.setPassNum("-");
                    hmeProdLinePassRateVO7.setIdentificationList(identificationList);
                    hmeProdLinePassRateVO7.setNcNum("-");
                    hmeProdLinePassRateVO7.setNcDataList(hmeProdLinePassRateVO4s);
                }else{
                    for (HmeProdLinePassRateVO13 eo : eoList) {
                        //根据每个eo查询父不良记录
                        List<HmeProdLinePassRateVO12> ncRecordList = ncRecordBatchList.stream().filter(item -> eo.getEoId().equals(item.getEoId())
                                && process.getOrganizationId().equals(item.getProcessId()) && eo.getJobId().equals(item.getJobId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(ncRecordList)) {
                            //如果找不到父不良记录，则此Eo合格
                            passNum++;
                            identificationList.add(eo.getIdentification());
                        } else {
                            //将找到的父不良记录根据process_method为空或!=2，且COMPONENT_REQUIRED=N筛选数据
                            List<HmeProdLinePassRateVO12> ncList = ncRecordList.stream().filter(item -> ((StringUtils.isEmpty(item.getProcessMethod())) || ((!"2".equals(item.getProcessMethod())))) && "N".equals(item.getComponentRequired())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(ncList)) {
                                //如果筛选结果为空，则此EO合格
                                passNum++;
                                identificationList.add(eo.getIdentification());
                            } else {
                                //如果筛选结果为空，则此EO不良,不良明细则展示最早的一笔不良记录
                                ncNum++;
                                ncRecordIdList.add(ncList.get(0).getNcRecordId());
                            }
                        }
                    }
                    hmeProdLinePassRateVO7.setPassNum(passNum.toString());
                    hmeProdLinePassRateVO7.setIdentificationList(identificationList);
                    hmeProdLinePassRateVO7.setNcNum(ncNum.toString());
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ncRecordIdList)) {
                        List<List<String>> splitNcRecordIdList = WmsCommonUtils.splitSqlList(ncRecordIdList, 500);
                        for (List<String> split : splitNcRecordIdList) {
                            List<HmeProdLinePassRateVO4> ncRecordDetailList = hmeProdLinePassRateMapper.ncRecordDetailQuery(tenantId, split);
                            hmeProdLinePassRateVO4s.addAll(ncRecordDetailList);
                        }
                    }
                    hmeProdLinePassRateVO7.setNcDataList(hmeProdLinePassRateVO4s);
                }

                //良率 合格/投产*100%
                if (productionNum == 0) {
                    hmeProdLinePassRateVO7.setRate("-");
                } else {
                    BigDecimal rateBig = new BigDecimal(identificationList.size()).divide(new BigDecimal(productionNum), 4, BigDecimal.ROUND_HALF_UP);
                    hmeProdLinePassRateVO7.setRateBig(rateBig);
                    BigDecimal rate = rateBig.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    hmeProdLinePassRateVO7.setRate(rate.toString() + "%");
                }
                shiftDataList.add(hmeProdLinePassRateVO7);
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
            hmeProdLinePassRateVO6.setShiftData(shiftDataList);
            resultList.add(hmeProdLinePassRateVO6);
        }
        //汇总每天的合格数、不良数、投产数、良率等
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(resultList)) {
            List<String> passRateData = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
            Date start = dateFormat.parse(dto.getDateFrom());
            Date end = dateFormat.parse(dto.getDateTo());
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);
            while (tempStart.before(tempEnd)) {
                String date = dateFormat2.format(tempStart.getTime());
                BigDecimal totalPassNum = BigDecimal.ZERO;
                BigDecimal totalNcNum = BigDecimal.ZERO;
                BigDecimal totalProductionNum = BigDecimal.ZERO;
                BigDecimal totalRate = null;
                for (HmeProdLinePassRateVO6 result : resultList) {
                    List<HmeProdLinePassRateVO7> hmeProdLinePassRateVO2s = result.getShiftData().stream().filter(data -> date.equals(data.getShiftName())).collect(Collectors.toList());
                    if (hmeProdLinePassRateVO2s != null && hmeProdLinePassRateVO2s.size() > 0) {
                        if (!hmeProdLinePassRateVO2s.get(0).getPassNum().equals("-")) {
                            totalPassNum = totalPassNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getPassNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getNcNum().equals("-")) {
                            totalNcNum = totalNcNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getNcNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getProductionNum().equals("-")) {
                            totalProductionNum = totalProductionNum.add(new BigDecimal(hmeProdLinePassRateVO2s.get(0).getProductionNum()));
                        }
                        if (!hmeProdLinePassRateVO2s.get(0).getRate().equals("-")) {
                            if (Objects.isNull(totalRate)) {
                                totalRate = hmeProdLinePassRateVO2s.get(0).getRateBig();
                            } else {
                                totalRate = totalRate.multiply(hmeProdLinePassRateVO2s.get(0).getRateBig()).setScale(4, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                }
                passRateData.add(totalPassNum.toString());
                passRateData.add(totalNcNum.toString());
                passRateData.add(totalProductionNum.toString());
                if(Objects.nonNull(totalRate)){
                    passRateData.add(totalRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                }else{
                    passRateData.add("-");
                }
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
            hmeProdLinePassRateVO8.setPassRateData(passRateData);
        }
        hmeProdLinePassRateVO8.setResultList(resultList);
        return hmeProdLinePassRateVO8;
    }
}
