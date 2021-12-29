package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmePreparationSurplusChipDTO;
import com.ruike.hme.app.service.HmePreparationSurplusChipService;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO2;
import com.ruike.hme.infra.mapper.HmePreparationSurplusChipMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * COS筛选剩余芯片统计报表应用服务默认实现
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:51:21
 **/
@Service
public class HmePreparationSurplusChipServiceImpl implements HmePreparationSurplusChipService {

    @Autowired
    private HmePreparationSurplusChipMapper hmePreparationSurplusChipMapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public Page<HmePreparationSurplusChipVO> listQuery(Long tenantId, HmePreparationSurplusChipDTO dto, PageRequest pageRequest) {
        Page<HmePreparationSurplusChipVO> resultPage = PageHelper.doPage(pageRequest, () -> hmePreparationSurplusChipMapper.listQueryNew(tenantId, dto));
        if(CollectionUtils.isNotEmpty(resultPage.getContent())){
            List<String> oldMaterialLotIdList = resultPage.getContent().stream().map(HmePreparationSurplusChipVO::getOldMaterialLotId).distinct().collect(Collectors.toList());
            //批量查询操作人
            List<Long> createdByList = resultPage.getContent().stream().map(HmePreparationSurplusChipVO::getCreatedBy).distinct().collect(Collectors.toList());
            Map<Long, MtUserInfo> userMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(createdByList)){
                userMap = userClient.userInfoBatchGet(tenantId, createdByList);
            }
            //批量查询筛选批次总数
            List<String> preSelectionIdList = resultPage.getContent().stream().map(HmePreparationSurplusChipVO::getPreSelectionId).distinct().collect(Collectors.toList());
            Map<String, Long> preSelectionCountMap = new HashMap<>();
            //筛选时间Map edit by chaonan.hu for peng.zhao 根据preSelectionId在表hme_selection_details中随意取一条数据的创建时间
            Map<String, List<String>> preparationDateMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(preSelectionIdList)){
                List<HmePreparationSurplusChipVO> preSelectionCountList = new ArrayList<>();
                List<List<String>> splitList = CommonUtils.splitSqlList(preSelectionIdList, 1000);
                for (List<String> split:splitList) {
                    preSelectionCountList.addAll(hmePreparationSurplusChipMapper.preSelectionCountQuery(tenantId, split));
                }
                if(CollectionUtils.isNotEmpty(preSelectionCountList)){
                    preSelectionCountMap = preSelectionCountList.stream().collect(Collectors.groupingBy(HmePreparationSurplusChipVO::getPreSelectionId, Collectors.counting()));
                    preparationDateMap = preSelectionCountList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO::getPreSelectionId, Collectors.mapping(
                                    HmePreparationSurplusChipVO::getPreparationDate, Collectors.toList())));
                }
            }

            if(CollectionUtils.isNotEmpty(oldMaterialLotIdList)){
                List<List<String>> splitOldMaterialLotIdList = splitSqlList(oldMaterialLotIdList, 900);
                List<HmePreparationSurplusChipVO2> selectionDetailsLoadSequenceList = new ArrayList<>();
                List<HmePreparationSurplusChipVO2> materialLotLoadSequenceList = new ArrayList<>();
                for (List<String> splitOldMaterialLotId:splitOldMaterialLotIdList) {
                    selectionDetailsLoadSequenceList.addAll(hmePreparationSurplusChipMapper.selectionDetailsLoadSequenceQuery(tenantId, splitOldMaterialLotId));
                    materialLotLoadSequenceList.addAll(hmePreparationSurplusChipMapper.materialLotLoadSequenceQuery(tenantId, splitOldMaterialLotId));
                }
                Map<String, List<String>> selectionDetailsLoadSequenceMap = new HashMap<>();
                if(CollectionUtils.isNotEmpty(selectionDetailsLoadSequenceList)){
                    selectionDetailsLoadSequenceMap = selectionDetailsLoadSequenceList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO2::getOldMaterialLotId, Collectors.mapping(
                                    HmePreparationSurplusChipVO2::getLoadSequence, Collectors.toList())));
                }
                Map<String, List<String>> materialLotLoadSequenceMap = new HashMap<>();
                if(CollectionUtils.isNotEmpty(materialLotLoadSequenceList)){
                    materialLotLoadSequenceMap = materialLotLoadSequenceList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO2::getOldMaterialLotId, Collectors.mapping(
                                    HmePreparationSurplusChipVO2::getLoadSequence, Collectors.toList())));
                }
                Map<String, Integer> cosCountMap = new HashMap<>();
                Map<String, Integer> noPreCountMap = new HashMap<>();
                for (String oldMaterialLotId:oldMaterialLotIdList) {
                    //该盒COS总数
                    List<String> loadSequenceList = new ArrayList<>();
                    List<String> selectionDetailsList = selectionDetailsLoadSequenceMap.get(oldMaterialLotId);
                    List<String> materialLotLoadList = materialLotLoadSequenceMap.get(oldMaterialLotId);
                    if(CollectionUtils.isNotEmpty(selectionDetailsList)){
                        loadSequenceList.addAll(selectionDetailsList);
                    }
                    if(CollectionUtils.isNotEmpty(materialLotLoadList)){
                        loadSequenceList.addAll(materialLotLoadList);
                    }
                    if(CollectionUtils.isNotEmpty(loadSequenceList)){
                        loadSequenceList = loadSequenceList.stream().distinct().collect(Collectors.toList());
                    }
                    cosCountMap.put(oldMaterialLotId, loadSequenceList.size());
                    //该盒未挑选数
                    if(CollectionUtils.isNotEmpty(materialLotLoadList)){
                        materialLotLoadList.removeAll(selectionDetailsList);
                        noPreCountMap.put(oldMaterialLotId, materialLotLoadList.size());
                    }else{
                        noPreCountMap.put(oldMaterialLotId, 0);
                    }
                }
                for (HmePreparationSurplusChipVO hmePreparationSurplusChipVO:resultPage.getContent()) {
                    Integer cosCount = cosCountMap.get(hmePreparationSurplusChipVO.getOldMaterialLotId());
                    Integer noPreCount = noPreCountMap.get(hmePreparationSurplusChipVO.getOldMaterialLotId());
                    hmePreparationSurplusChipVO.setCosCount(cosCount);
                    hmePreparationSurplusChipVO.setNoPreCount(noPreCount);
                    hmePreparationSurplusChipVO.setUserName(userMap.getOrDefault(hmePreparationSurplusChipVO.getCreatedBy(), new MtUserInfo()).getRealName());
                    hmePreparationSurplusChipVO.setPreSelectionCount(preSelectionCountMap.getOrDefault(hmePreparationSurplusChipVO.getPreSelectionId(), 0L).intValue());
                    List<String> preparationDateList = preparationDateMap.get(hmePreparationSurplusChipVO.getPreSelectionId());
                    if(CollectionUtils.isNotEmpty(preparationDateList)){
                        hmePreparationSurplusChipVO.setPreparationDate(preparationDateList.get(0));
                    }
                }
            }
        }
        return resultPage;
    }

    @Override
    public List<HmePreparationSurplusChipVO> export(Long tenantId, HmePreparationSurplusChipDTO dto) {
        List<HmePreparationSurplusChipVO> resultList = hmePreparationSurplusChipMapper.listQueryNew(tenantId, dto);
        if(CollectionUtils.isNotEmpty(resultList)){
            List<String> oldMaterialLotIdList = resultList.stream().map(HmePreparationSurplusChipVO::getOldMaterialLotId).distinct().collect(Collectors.toList());
            //批量查询操作人
            List<Long> createdByList = resultList.stream().map(HmePreparationSurplusChipVO::getCreatedBy).distinct().collect(Collectors.toList());
            Map<Long, MtUserInfo> userMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(createdByList)){
                userMap = userClient.userInfoBatchGet(tenantId, createdByList);
            }
            //批量查询筛选批次总数
            List<String> preSelectionIdList = resultList.stream().map(HmePreparationSurplusChipVO::getPreSelectionId).distinct().collect(Collectors.toList());
            Map<String, Long> preSelectionCountMap = new HashMap<>();
            //筛选时间Map edit by chaonan.hu for peng.zhao 根据preSelectionId在表hme_selection_details中随意取一条数据的创建时间
            Map<String, List<String>> preparationDateMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(preSelectionIdList)){
                List<HmePreparationSurplusChipVO> preSelectionCountList = new ArrayList<>();
                List<List<String>> splitList = CommonUtils.splitSqlList(preSelectionIdList, 1000);
                for (List<String> split:splitList) {
                    preSelectionCountList.addAll(hmePreparationSurplusChipMapper.preSelectionCountQuery(tenantId, split));
                }
                if(CollectionUtils.isNotEmpty(preSelectionCountList)){
                    preSelectionCountMap = preSelectionCountList.stream().collect(Collectors.groupingBy(HmePreparationSurplusChipVO::getPreSelectionId, Collectors.counting()));
                    preparationDateMap = preSelectionCountList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO::getPreSelectionId, Collectors.mapping(
                                    HmePreparationSurplusChipVO::getPreparationDate, Collectors.toList())));
                }
            }

            if(CollectionUtils.isNotEmpty(oldMaterialLotIdList)){
                List<List<String>> splitOldMaterialLotIdList = splitSqlList(oldMaterialLotIdList, 900);
                List<HmePreparationSurplusChipVO2> selectionDetailsLoadSequenceList = new ArrayList<>();
                List<HmePreparationSurplusChipVO2> materialLotLoadSequenceList = new ArrayList<>();
                for (List<String> splitOldMaterialLotId:splitOldMaterialLotIdList) {
                    selectionDetailsLoadSequenceList.addAll(hmePreparationSurplusChipMapper.selectionDetailsLoadSequenceQuery(tenantId, splitOldMaterialLotId));
                    materialLotLoadSequenceList.addAll(hmePreparationSurplusChipMapper.materialLotLoadSequenceQuery(tenantId, splitOldMaterialLotId));
                }
                Map<String, List<String>> selectionDetailsLoadSequenceMap = new HashMap<>();
                if(CollectionUtils.isNotEmpty(selectionDetailsLoadSequenceList)){
                    selectionDetailsLoadSequenceMap = selectionDetailsLoadSequenceList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO2::getOldMaterialLotId, Collectors.mapping(
                                    HmePreparationSurplusChipVO2::getLoadSequence, Collectors.toList())));
                }
                Map<String, List<String>> materialLotLoadSequenceMap = new HashMap<>();
                if(CollectionUtils.isNotEmpty(materialLotLoadSequenceList)){
                    materialLotLoadSequenceMap = materialLotLoadSequenceList.stream().collect(Collectors.groupingBy(
                            HmePreparationSurplusChipVO2::getOldMaterialLotId, Collectors.mapping(
                                    HmePreparationSurplusChipVO2::getLoadSequence, Collectors.toList())));
                }
                Map<String, Integer> cosCountMap = new HashMap<>();
                Map<String, Integer> noPreCountMap = new HashMap<>();
                for (String oldMaterialLotId:oldMaterialLotIdList) {
                    //该盒COS总数
                    List<String> loadSequenceList = new ArrayList<>();
                    List<String> selectionDetailsList = selectionDetailsLoadSequenceMap.get(oldMaterialLotId);
                    List<String> materialLotLoadList = materialLotLoadSequenceMap.get(oldMaterialLotId);
                    if(CollectionUtils.isNotEmpty(selectionDetailsList)){
                        loadSequenceList.addAll(selectionDetailsList);
                    }
                    if(CollectionUtils.isNotEmpty(materialLotLoadList)){
                        loadSequenceList.addAll(materialLotLoadList);
                    }
                    if(CollectionUtils.isNotEmpty(loadSequenceList)){
                        loadSequenceList = loadSequenceList.stream().distinct().collect(Collectors.toList());
                    }
                    cosCountMap.put(oldMaterialLotId, loadSequenceList.size());
                    //该盒未挑选数
                    if(CollectionUtils.isNotEmpty(materialLotLoadList)){
                        materialLotLoadList.removeAll(selectionDetailsList);
                        noPreCountMap.put(oldMaterialLotId, materialLotLoadList.size());
                    }else{
                        noPreCountMap.put(oldMaterialLotId, 0);
                    }
                }
                for (HmePreparationSurplusChipVO hmePreparationSurplusChipVO:resultList) {
                    Integer cosCount = cosCountMap.get(hmePreparationSurplusChipVO.getOldMaterialLotId());
                    Integer noPreCount = noPreCountMap.get(hmePreparationSurplusChipVO.getOldMaterialLotId());
                    hmePreparationSurplusChipVO.setCosCount(cosCount);
                    hmePreparationSurplusChipVO.setNoPreCount(noPreCount);
                    hmePreparationSurplusChipVO.setUserName(userMap.getOrDefault(hmePreparationSurplusChipVO.getCreatedBy(), new MtUserInfo()).getRealName());
                    hmePreparationSurplusChipVO.setPreSelectionCount(preSelectionCountMap.getOrDefault(hmePreparationSurplusChipVO.getPreSelectionId(), 0L).intValue());
                    List<String> preparationDateList = preparationDateMap.get(hmePreparationSurplusChipVO.getPreSelectionId());
                    if(CollectionUtils.isNotEmpty(preparationDateList)){
                        hmePreparationSurplusChipVO.setPreparationDate(preparationDateList.get(0));
                    }
                }
            }
        }
        return resultList;
    }

    public static <T> List<List<T>> splitSqlList(List<T> sqlList, Integer splitNum) {
        int defaultNum = 500;
        splitNum = Optional.ofNullable(splitNum).orElse(defaultNum);
        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }
}
