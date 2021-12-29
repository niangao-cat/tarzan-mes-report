package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.query.CosCompletionDetailQuery;
import com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation;
import com.ruike.hme.domain.repository.CosCompletionDetailRepository;
import com.ruike.hme.domain.vo.HmeCosCompletionVO;
import com.ruike.hme.domain.vo.HmeCosCompletionVO2;
import com.ruike.hme.domain.vo.HmeCosCompletionVO3;
import com.ruike.hme.domain.vo.HmeCosFunctionVO;
import com.ruike.hme.infra.mapper.CosCompletionDetailMapper;
import com.ruike.hme.infra.util.HmeCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Repository;
import tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.common.domain.sys.MtException;
import tarzan.common.infra.mapper.SiteMapper;
import utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

/**
 * <p>
 * COS完工芯片明细报表 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 15:42
 */
@Repository
public class CosCompletionDetailRepositoryImpl implements CosCompletionDetailRepository {
    private final CosCompletionDetailMapper mapper;

    private final SiteMapper siteMapper;

    private final MtErrorMessageRepository mtErrorMessageRepository;

    public CosCompletionDetailRepositoryImpl(CosCompletionDetailMapper mapper,
                                             SiteMapper siteMapper,
                                             MtErrorMessageRepository mtErrorMessageRepository) {
        this.mapper = mapper;
        this.siteMapper = siteMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    private List<String> queryMaterialLotId(CosCompletionDetailQuery query){

        List<HmeCosCompletionVO3> locatorList = mapper.queryLocator(query);
        List<String> locatorIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(locatorList)){
            locatorIdList = locatorList.stream()
                    .map(HmeCosCompletionVO3::getLocatorId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            query.setAllLocatorIdList(locatorIdList);
        }

        List<String> materialLotIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(query.getMaterialLotCodeList())
            || StringUtils.isNotBlank(query.getFreezeFlag())){
            List<HmeCosCompletionVO> hmeCosCompletionVOList = mapper.queryMaterialLot(query);
            if(CollectionUtils.isNotEmpty(hmeCosCompletionVOList)){
                materialLotIdList = hmeCosCompletionVOList.stream()
                        .map(HmeCosCompletionVO::getMaterialLotId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        if(CollectionUtils.isNotEmpty(query.getLabCodeList())){
            List<HmeCosCompletionVO> hmeCosCompletionVOList = mapper.queryMaterialLotOfLabCode(query);
            if(CollectionUtils.isNotEmpty(hmeCosCompletionVOList)){
                List<String> materialLotIdList2 = hmeCosCompletionVOList.stream()
                                    .map(HmeCosCompletionVO::getMaterialLotId)
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(materialLotIdList2)){
                    materialLotIdList.addAll(materialLotIdList2);
                }
            }
        }

        //去重
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
        }
        return materialLotIdList;
    }

    private List<String> queryEoId(CosCompletionDetailQuery query){
        List<String> eoIdList = new ArrayList<>();
        List<HmeCosCompletionVO3> hmeCosCompletionVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(query.getIdentificationList())){
            hmeCosCompletionVOList = mapper.queryEo(query);
            if(CollectionUtils.isNotEmpty(hmeCosCompletionVOList)){
                eoIdList = hmeCosCompletionVOList.stream()
                        .map(HmeCosCompletionVO3::getEoId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        //去重
        if(CollectionUtils.isNotEmpty(eoIdList)){
            eoIdList = eoIdList.stream().distinct().collect(Collectors.toList());
        }
        return eoIdList;
    }

    @Override
    @ProcessLovValue
    public Page<CosCompletionDetailRepresentation> pagedList(CosCompletionDetailQuery query, PageRequest pageRequest) {

        List<String> materialLotIdList = queryMaterialLotId(query);
        if(CollectionUtils.isNotEmpty(materialLotIdList)) {
            query.setMaterialLotIdList(materialLotIdList);
        }

        List<String> eoIdList = queryEoId(query);
        if(CollectionUtils.isNotEmpty(eoIdList)) {
            query.setEoIdList(eoIdList);
        }

        List<CosCompletionDetailRepresentation> cosCompletionDetailRepresentationList = mapper.selectList(query);
        List<CosCompletionDetailRepresentation> cosCompletionDetailRepresentationList2 = mapper.selectList2(query);
        if(CollectionUtils.isNotEmpty(cosCompletionDetailRepresentationList2)){
            cosCompletionDetailRepresentationList.addAll(cosCompletionDetailRepresentationList2);
        }

        if(CollectionUtils.isEmpty(cosCompletionDetailRepresentationList)){
            return new Page<>();
        }

        Page<CosCompletionDetailRepresentation> page = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), cosCompletionDetailRepresentationList);
        displayFieldsCompletion(query.getTenantId(), page.getContent());
        return page;
    }

    @Override
    @ProcessLovValue
    public List<CosCompletionDetailRepresentation> list(CosCompletionDetailQuery query) {

        List<String> materialLotIdList = queryMaterialLotId(query);
        if(CollectionUtils.isNotEmpty(materialLotIdList)) {
            query.setMaterialLotIdList(materialLotIdList);
        }

        List<String> eoIdList = queryEoId(query);
        if(CollectionUtils.isNotEmpty(eoIdList)) {
            query.setEoIdList(eoIdList);
        }

        List<CosCompletionDetailRepresentation> cosCompletionDetailRepresentationList = mapper.selectList(query);
        List<CosCompletionDetailRepresentation> cosCompletionDetailRepresentationList2 = mapper.selectList2(query);
        if(CollectionUtils.isNotEmpty(cosCompletionDetailRepresentationList2)){
            cosCompletionDetailRepresentationList.addAll(cosCompletionDetailRepresentationList2);
        }
        displayFieldsCompletion(query.getTenantId(), cosCompletionDetailRepresentationList);
        return cosCompletionDetailRepresentationList;
    }

    private void displayFieldsCompletion(Long tenantId, List<CosCompletionDetailRepresentation> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 批量获取条码实验代码
        List<String> materialLotCodeIdList = list.stream().map(CosCompletionDetailRepresentation::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosCompletionVO>> labCodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotCodeIdList)) {
            List<HmeCosCompletionVO> labCodeList = mapper.queryLabCodeByMaterialLotIds(tenantId, materialLotCodeIdList);
            if (CollectionUtils.isNotEmpty(labCodeList)) {
                labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(HmeCosCompletionVO::getMaterialLotId));
            }
         }
        // 获取电流点
        List<String> selectionRuleCodeList = list.stream().map(CosCompletionDetailRepresentation::getSelectionRuleCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosCompletionVO2>> currentMap = new HashMap<>();
        List<String> currentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectionRuleCodeList)) {
            List<HmeCosCompletionVO2> currentInfoList = mapper.queryCurrentByRuleCode(tenantId, selectionRuleCodeList);
            if (CollectionUtils.isNotEmpty(currentInfoList)) {
                currentList = currentInfoList.stream().map(HmeCosCompletionVO2::getCurrent).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                currentMap = currentInfoList.stream().collect(Collectors.groupingBy(HmeCosCompletionVO2::getCosRuleCode));
            }
         }
        if(CollectionUtils.isEmpty(currentList)){
            currentList.add("5");
        }else{
            if(!currentList.contains("5")){
                currentList.add("5");
            }
        }
        // 根据电流点和序列获取性能数据
        List<String> loadSequenceList = list.stream().map(CosCompletionDetailRepresentation::getLoadSequence).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosFunctionVO>> hmeCosFunctionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(loadSequenceList) && CollectionUtils.isNotEmpty(currentList)) {

            Long userId = -1L;
            CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
            if(Objects.nonNull(customUserDetails)
                    && Objects.nonNull(customUserDetails.getUserId())){
                userId = customUserDetails.getUserId();
            }

            //查询当前用户默认站点
            String siteId = siteMapper.selectSiteByUser(userId);
            if(StringUtils.isEmpty(siteId)){
                throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            }

            List<HmeCosFunctionVO> hmeCosFunctionList = new ArrayList<>();
            List<List<String>> allLoadSequenceList = Utils.splitSqlList(loadSequenceList,1000);
            for (List<String> subLoadSequenceList : allLoadSequenceList
                 ) {
                List<HmeCosFunctionVO> subHmeCosFunctionList = mapper.queryCosFunctionByCurrentAndLoadSequence(tenantId, siteId , subLoadSequenceList, currentList);
                if(CollectionUtils.isNotEmpty(subHmeCosFunctionList)){
                    hmeCosFunctionList.addAll(subHmeCosFunctionList);
                }
            }

            if (CollectionUtils.isNotEmpty(hmeCosFunctionList)) {
                hmeCosFunctionMap = hmeCosFunctionList.stream().collect(Collectors.groupingBy(dto -> dto.getLoadSequence() + "_" + dto.getCurrent()));
            }
        }
        for (CosCompletionDetailRepresentation detailRepresentation : list) {
            // 位置
            detailRepresentation.setPosition(String.valueOf((char)(detailRepresentation.getLoadRow()+64)) + detailRepresentation.getLoadColumn());
            // 实验代码
            List<HmeCosCompletionVO> labCodes = labCodeMap.get(detailRepresentation.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(labCodes)) {
                detailRepresentation.setLabCode(labCodes.get(0).getLabCode());
            }
            // 挑选来源位置
            if (StringUtils.isNotBlank(detailRepresentation.getOldLoad())) {
                List<String> oldLoad = Arrays.asList(detailRepresentation.getOldLoad().split(","));
                detailRepresentation.setSelectedPosition(String.valueOf((char)(Long.valueOf(oldLoad.get(0))+64))+ oldLoad.get(1));
            }
            List<HmeCosCompletionVO2> hmeCosCompletionVO2List = currentMap.get(detailRepresentation.getSelectionRuleCode());
            List<HmeCosFunctionVO> hmeCosFunctionVOS = hmeCosFunctionMap.get(detailRepresentation.getLoadSequence() + "_" + (CollectionUtils.isNotEmpty(hmeCosCompletionVO2List) ? hmeCosCompletionVO2List.get(0).getCurrent() : ""));
            if (CollectionUtils.isNotEmpty(hmeCosFunctionVOS)) {
                // 功率
                detailRepresentation.setPower(hmeCosFunctionVOS.get(0).getA02());
                // 电压
                detailRepresentation.setVoltage(hmeCosFunctionVOS.get(0).getA06());
            }
            // 装箱操作人 不为已装载则不显示
            // 装箱时间 不为已装载则不显示
            if (!StringUtils.equals(detailRepresentation.getSelectionStatus(), "LOADED")) {
                detailRepresentation.setLoadOperatorName("");
                detailRepresentation.setLoadDate(null);
            }
            List<HmeCosFunctionVO> hmeCosFunctionVOS2 = hmeCosFunctionMap.get(detailRepresentation.getLoadSequence() + "_5");
            if(CollectionUtils.isNotEmpty(hmeCosFunctionVOS2)) {
                detailRepresentation.setA04(hmeCosFunctionVOS2.get(0).getA04());
            }
        }
        // 根据虚拟号分组 过滤虚拟号为空的
        List<CosCompletionDetailRepresentation> filterList = list.stream().filter(dto -> StringUtils.isNotBlank(dto.getVirtualNum())).collect(Collectors.toList());
        Map<String, List<CosCompletionDetailRepresentation>> virtualNumMap = filterList.stream().collect(Collectors.groupingBy(CosCompletionDetailRepresentation::getVirtualNum, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<String, List<CosCompletionDetailRepresentation>> virtualNumEntry : virtualNumMap.entrySet()) {
            List<CosCompletionDetailRepresentation> value = virtualNumEntry.getValue();
            BigDecimal totalWavelength = value.stream().map(CosCompletionDetailRepresentation::getA04).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 总电压
            BigDecimal totalVoltage = value.stream().map(CosCompletionDetailRepresentation::getVoltage).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 总功率
            BigDecimal totalPower = value.stream().map(CosCompletionDetailRepresentation::getPower).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 平均波长
            BigDecimal avgWavelength = totalWavelength.divide(BigDecimal.valueOf(value.size()), 6, BigDecimal.ROUND_HALF_DOWN);
            for (CosCompletionDetailRepresentation detailRepresentation : value) {
                if (avgWavelength.compareTo(BigDecimal.ZERO) != 0) {
                    detailRepresentation.setA04Avg(avgWavelength);
                }
                if (totalVoltage.compareTo(BigDecimal.ZERO) != 0) {
                    detailRepresentation.setVoltageSum(totalVoltage);
                }
                if (totalPower.compareTo(BigDecimal.ZERO) != 0) {
                    detailRepresentation.setPowerSum(totalPower);
                }
            }
        }
    }
}
