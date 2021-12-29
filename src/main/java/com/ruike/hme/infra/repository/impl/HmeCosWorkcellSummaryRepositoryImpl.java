package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.query.HmeCosWorkcellSummaryQuery;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import com.ruike.hme.domain.repository.HmeCosWorkcellSummaryRepository;
import com.ruike.hme.domain.valueobject.HmeCosNcRecord;
import com.ruike.hme.domain.vo.JobEquipmentVO;
import com.ruike.hme.infra.mapper.HmeCosWorkcellSummaryMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.qms.infra.util.CollectorsUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * COS工位加工汇总 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:23
 */
@Repository
public class HmeCosWorkcellSummaryRepositoryImpl implements HmeCosWorkcellSummaryRepository {
    private static final MtUserInfo BLANK_USER = new MtUserInfo();
    private final HmeCosWorkcellSummaryMapper mapper;
    private final MtUserClient userClient;

    public HmeCosWorkcellSummaryRepositoryImpl(HmeCosWorkcellSummaryMapper mapper, MtUserClient userClient) {
        this.mapper = mapper;
        this.userClient = userClient;
    }

    @Override
    public Page<HmeCosWorkcellSummaryRepresentation> pageList(Long tenantId, HmeCosWorkcellSummaryQuery dto, PageRequest pageRequest) {
        List<HmeCosWorkcellSummaryRepresentation> summaryList = getSummaryList(tenantId, dto);
        Page<HmeCosWorkcellSummaryRepresentation> page = Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), summaryList);
        displayFieldsCompletion(tenantId, dto.getWorkcells(), page.getContent());
        return page;
    }

    @Override
    public List<HmeCosWorkcellSummaryRepresentation> export(Long tenantId, HmeCosWorkcellSummaryQuery dto) {
        List<HmeCosWorkcellSummaryRepresentation> summaryList = getSummaryList(tenantId, dto);
        displayFieldsCompletion(tenantId, dto.getWorkcells(), summaryList);
        return summaryList;
    }

    private List<HmeCosWorkcellSummaryRepresentation> getSummaryList(Long tenantId, HmeCosWorkcellSummaryQuery dto) {
        List<HmeCosWorkcellSummaryRepresentation> list = mapper.selectListByConditionNew(tenantId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        Map<HmeCosWorkcellSummaryRepresentation, List<HmeCosWorkcellSummaryRepresentation>> listMap = list.stream().collect(Collectors.groupingBy(HmeCosWorkcellSummaryRepresentation::summary));
        List<HmeCosWorkcellSummaryRepresentation> resultList = new ArrayList<>();
        for (Map.Entry<HmeCosWorkcellSummaryRepresentation, List<HmeCosWorkcellSummaryRepresentation>> entry:listMap.entrySet()) {
            HmeCosWorkcellSummaryRepresentation cosWorkcellSummaryRepresentation = entry.getKey();
            BigDecimal snQty = BigDecimal.ZERO;
            List<HmeCosWorkcellSummaryRepresentation> valueList = entry.getValue();
            //记录下每行数据下的jobId，方便查询工位设备
            List<String> jobIdList = new ArrayList<>();
            for (HmeCosWorkcellSummaryRepresentation singleValue:valueList) {
                if(Objects.nonNull(singleValue.getSnQty())){
                    snQty = snQty.add(singleValue.getSnQty());
                }
                jobIdList.add(singleValue.getJobId());
            }
            cosWorkcellSummaryRepresentation.setSnQty(snQty);
            cosWorkcellSummaryRepresentation.setJobIdList(jobIdList);
            resultList.add(cosWorkcellSummaryRepresentation);
        }
//        Map<HmeCosWorkcellSummaryRepresentation , BigDecimal> listMap = list.stream().collect(Collectors.groupingBy(HmeCosWorkcellSummaryRepresentation::summary , Collectors.reducing(BigDecimal.ZERO , HmeCosWorkcellSummaryRepresentation::getSnQty , BigDecimal::add)));
//
//        for(Map.Entry<HmeCosWorkcellSummaryRepresentation, BigDecimal> entry : listMap.entrySet()){
//            entry.getKey().setSnQty(entry.getValue());
//        }
//
//        List<HmeCosWorkcellSummaryRepresentation> resultList = new ArrayList<>(listMap.keySet().size());
//        resultList.addAll(listMap.keySet());

//        //2021-07-27 edit by chaonan.hu 改为分批查询设备
//        if(CollectionUtils.isNotEmpty(resultList)){
//            List<String> sourceJobIdList = resultList.stream().map(HmeCosWorkcellSummaryRepresentation::getSourceJobId).distinct().collect(Collectors.toList());
//            List<List<String>> splitList = CommonUtils.splitSqlList(sourceJobIdList, 1000);
//            List<JobEquipmentVO> equipmentList = new ArrayList<>();
//            for (List<String> split:splitList) {
//                equipmentList.addAll(mapper.selectEquipmentList2(tenantId, split));
//            }
//            if(CollectionUtils.isNotEmpty(equipmentList)){
//                Map<String, String> equipmentMap = equipmentList.stream().collect(Collectors.toMap(JobEquipmentVO::getSourceJobId, JobEquipmentVO::getAssetEncoding));
//                resultList.forEach(rec -> rec.setEquipment(equipmentMap.getOrDefault(rec.getSourceJobId() , "")));
//            }
//        }
        return resultList;
    }

    private void displayFieldsCompletion(Long tenantId, Set<String> workcells, List<HmeCosWorkcellSummaryRepresentation> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        //2021-07-27 edit by chaonan.hu 改为分批查询设备
        List<String> totalJobIdList = new ArrayList<>();
        for (HmeCosWorkcellSummaryRepresentation cosWorkcellSummaryRepresentation:list) {
            if(CollectionUtils.isNotEmpty(cosWorkcellSummaryRepresentation.getJobIdList())){
                totalJobIdList.addAll(cosWorkcellSummaryRepresentation.getJobIdList());
            }
        }
        if(CollectionUtils.isNotEmpty(totalJobIdList)){
            totalJobIdList = totalJobIdList.stream().distinct().collect(Collectors.toList());
            List<List<String>> splitList = CommonUtils.splitSqlList(totalJobIdList, 1000);
            List<JobEquipmentVO> equipmentList = new ArrayList<>();
            for (List<String> split:splitList) {
                equipmentList.addAll(mapper.selectEquipmentList2(tenantId, split));
            }
            if(CollectionUtils.isNotEmpty(equipmentList)){
                Map<String, List<String>> equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(JobEquipmentVO::getJobId, Collectors.mapping(JobEquipmentVO::getAssetEncoding, Collectors.toList())));
                for (HmeCosWorkcellSummaryRepresentation result:list) {
                    List<String> equipment = new ArrayList<>();
                    for (String jobId:result.getJobIdList()) {
                        equipment.addAll(equipmentMap.getOrDefault(jobId, new ArrayList<>()));
                    }
                    if(CollectionUtils.isNotEmpty(equipment)){
                        equipment = equipment.stream().distinct().collect(Collectors.toList());
                        result.setEquipment(String.join("/", equipment));
                    }
                }
            }
        }

        // wafer来料数量
        Map<String, Long> waferMap = list.stream().filter(rec -> workcells.contains(rec.getWorkcellCode())).collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkcellId(), rec.getWafer(), rec.getWorkOrderId()), Collectors.summingLong(HmeCosWorkcellSummaryRepresentation::getCosNum)));

        // 加工数量
        Map<String, BigDecimal> snQtyMap = list.stream().collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), CollectorsUtil.summingBigDecimal(HmeCosWorkcellSummaryRepresentation::getSnQty)));

        // 不良总数
//        List<String> operationIdList = list.stream().map(HmeCosWorkcellSummaryRepresentation::getOperationId).distinct().collect(Collectors.toList());
//        List<String> workcellIdList = list.stream().map(HmeCosWorkcellSummaryRepresentation::getWorkcellId).distinct().collect(Collectors.toList());
//        List<Long> createdByList = list.stream().map(HmeCosWorkcellSummaryRepresentation::getOperatorId).distinct().collect(Collectors.toList());
//        List<String> waferList = list.stream().map(HmeCosWorkcellSummaryRepresentation::getWafer).distinct().collect(Collectors.toList());
//        List<String> workOrderList = list.stream().map(HmeCosWorkcellSummaryRepresentation::getWorkOrderId).distinct().collect(Collectors.toList());
//        List<HmeCosNcRecord> ngQtyList = mapper.selectNgQtyList(tenantId, waferList, workOrderList, operationIdList, workcellIdList, createdByList);
        //不良总数改为多个字段组合in的方式去分批查询
        List<HmeCosNcRecord> ngQtyList = new ArrayList<>();
        Map<String, List<HmeCosWorkcellSummaryRepresentation>>  ngQtyQueryMap = list.stream().collect(Collectors.groupingBy(item -> {
            return item.getOperationId() + "," + item.getWorkcellId() + "," + item.getOperatorId() + ","
                    + item.getWafer() + "," + item.getWorkOrderId();
        }));
        List<HmeCosNcRecord> ngQtyQueryList = new ArrayList<>();
        for (Map.Entry<String, List<HmeCosWorkcellSummaryRepresentation>> entry:ngQtyQueryMap.entrySet()) {
            String[] split = entry.getKey().split(",");
            if(split.length != 5){
                continue;
            }
            HmeCosNcRecord hmeCosNcRecord = new HmeCosNcRecord();
            hmeCosNcRecord.setOperationId(split[0]);
            hmeCosNcRecord.setWorkcellId(split[1]);
            hmeCosNcRecord.setCreatedBy(Long.valueOf(split[2]));
            hmeCosNcRecord.setWaferNum(split[3]);
            hmeCosNcRecord.setWorkOrderId(split[4]);
            ngQtyQueryList.add(hmeCosNcRecord);
        }
        if(CollectionUtils.isNotEmpty(ngQtyQueryList)){
            List<List<HmeCosNcRecord>> splitNgQtyQueryList = CommonUtils.splitSqlList(ngQtyQueryList, 1000);
            for (List<HmeCosNcRecord> split:splitNgQtyQueryList) {
                ngQtyList.addAll(mapper.selectNgQtyListNew(tenantId, split));
            }
        }
        Map<String, BigDecimal> ngQtyMap = ngQtyList.stream().collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkOrderId(), rec.getWaferNum(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getCreatedBy()), DateUtil.formatDate(rec.getCreationDate())), CollectorsUtil.summingBigDecimal(HmeCosNcRecord::getDefectCount)));

        // 人员
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, list.stream().map(HmeCosWorkcellSummaryRepresentation::getOperatorId).collect(Collectors.toList()));
        list.forEach(rec -> {
            rec.setWaferNum(waferMap.getOrDefault(mapKeyGenerator(rec.getWorkcellId(), rec.getWafer(), rec.getWorkOrderId()), 0L));
            rec.setSnQty(snQtyMap.getOrDefault(mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), BigDecimal.ZERO));
            rec.setNgQty(ngQtyMap.getOrDefault(mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), BigDecimal.ZERO));
            rec.setOkQty(rec.getSnQty().subtract(rec.getNgQty()));
            rec.setOperatorName(userMap.getOrDefault(rec.getOperatorId(), BLANK_USER).getRealName());
        });
    }

    private String mapKeyGenerator(String... element) {
        return StringUtils.join(element, '#');
    }
}
