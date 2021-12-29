package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;
import com.ruike.hme.app.service.HmePumpSelectionDetailsService;
import com.ruike.hme.domain.repository.HmePumpSelectionDetailsRepository;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeProductionFlowMapper;
import com.ruike.hme.infra.mapper.HmePumpSelectionDetailsMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 泵浦源预筛选报表业务实现
 *
 * @author chaonan.hu@hand-china.com 2021-11-05 09:17:20
 */
@Service
public class HmePumpSelectionDetailsServiceImpl implements HmePumpSelectionDetailsService {

    @Autowired
    private HmePumpSelectionDetailsRepository hmePumpSelectionDetailsRepository;
    @Autowired
    private HmePumpSelectionDetailsMapper hmePumpSelectionDetailsMapper;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private HmeProductionFlowMapper hmeProductionFlowMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Page<HmePumpSelectionDetailsVO> pumpSelectionDetailsPageQuery(Long tenantId, HmePumpSelectionDetailsDTO dto, PageRequest pageRequest) {
        hmePumpSelectionDetailsRepository.pageQueryParamVerify(tenantId, dto);
        if(CollectionUtils.isNotEmpty(dto.getReleaseWorkOrderNumList())){
            //根据投料工单查询组合件SN
            List<String> woCombMaterialLotCodeList = hmePumpSelectionDetailsMapper.combMaterialLotCodeQueryByReleaseWo(tenantId, dto.getReleaseWorkOrderNumList());
            if(CollectionUtils.isEmpty(woCombMaterialLotCodeList)){
                return new Page<>();
            }else {
                //如果投料工单找到的组合件SN与用户自己输入的组合件SN无交集，则直接无数据
                List<String> combMaterialLotCodeList = dto.getCombMaterialLotCodeList();
                if(CollectionUtils.isNotEmpty(combMaterialLotCodeList)){
                    combMaterialLotCodeList.retainAll(woCombMaterialLotCodeList);
                    if(CollectionUtils.isEmpty(combMaterialLotCodeList)){
                        return new Page<>();
                    }else {
                        dto.setCombMaterialLotCodeList(combMaterialLotCodeList);
                    }
                }else {
                    dto.setCombMaterialLotCodeList(woCombMaterialLotCodeList);
                }
            }
        }
        Page<HmePumpSelectionDetailsVO> resultPage =  PageHelper.doPage(pageRequest, () -> hmePumpSelectionDetailsMapper.pumpSelectionDetailsPageQuery(tenantId, dto));
        if(CollectionUtils.isNotEmpty(resultPage.getContent())){
            List<LovValueDTO> pumpSelectStatusLov = lovAdapter.queryLovValue("HME.PUMP_SELECT_STATUS", tenantId);
            List<LovValueDTO> enableFlagLov = lovAdapter.queryLovValue("Z_MTLOT_ENABLE_FLAG", tenantId);
            List<String> materialLotIdList = resultPage.getContent().stream().map(HmePumpSelectionDetailsVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<HmePumpSelectionDetailsVO> labCodeList = new ArrayList<>();
            List<HmePumpSelectionDetailsVO> combMaterialLotCodeList = new ArrayList<>();
            List<String> combMaterialLotIdList = new ArrayList<>();
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                //根据泵浦源SN查询实验代码
                labCodeList.addAll(hmePumpSelectionDetailsMapper.pumpMaterialLotLabCodeQuery(tenantId, splitMaterialLotId));
                //根据泵浦源SN查询组合件SN
                List<HmePumpSelectionDetailsVO> singleCombMaterialLotCodeList = hmePumpSelectionDetailsMapper.combMaterialLotCodeQuery(tenantId, splitMaterialLotId);
                if(CollectionUtils.isNotEmpty(singleCombMaterialLotCodeList)){
                    combMaterialLotCodeList.addAll(singleCombMaterialLotCodeList);
                    combMaterialLotIdList.addAll(singleCombMaterialLotCodeList.stream().map(HmePumpSelectionDetailsVO::getCombMaterialLotId).collect(Collectors.toList()));
                }
            }
            List<HmePumpSelectionDetailsVO> releaseWoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(combMaterialLotIdList)){
                combMaterialLotIdList = combMaterialLotIdList.stream().distinct().collect(Collectors.toList());
                List<List<String>> splitCombMaterialLotIdList = CommonUtils.splitSqlList(combMaterialLotIdList, 1000);
                for (List<String> splitCombMaterialLotId:splitCombMaterialLotIdList) {
                    //根据组合件SN查询投料工单
                    releaseWoList.addAll(hmePumpSelectionDetailsMapper.releaseWoQuery(tenantId, splitCombMaterialLotId));
                }
            }
            //姓名
            List<Long> userIdList = new ArrayList<>();
            userIdList.addAll(resultPage.getContent().stream().map(HmePumpSelectionDetailsVO::getCreatedBy).collect(Collectors.toList()));
            userIdList.addAll(resultPage.getContent().stream().map(HmePumpSelectionDetailsVO::getPackedBy).collect(Collectors.toList()));
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());;
            Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
            //根据工位查询产线
            List<String> workcellIdList = resultPage.getContent().stream().map(HmePumpSelectionDetailsVO::getWorkcellId).distinct().collect(Collectors.toList());
            List<WmsSummaryOfCosBarcodeProcessingVO2> prodLineList = hmeProductionFlowMapper.qeuryProcessLineProdByWorkcell(tenantId, workcellIdList);

            for (HmePumpSelectionDetailsVO hmePumpSelectionDetailsVO:resultPage.getContent()) {
                hmePumpSelectionDetailsVO.setSetsNumber(hmePumpSelectionDetailsVO.getSelectionOrder() + "/" + hmePumpSelectionDetailsVO.getSetsNum());
                if(StringUtils.isBlank(hmePumpSelectionDetailsVO.getFreezeFlag())){
                    hmePumpSelectionDetailsVO.setFreezeFlag("N");
                }
                //实验代码
                List<HmePumpSelectionDetailsVO> singleLabCodeList = labCodeList.stream().filter(item -> hmePumpSelectionDetailsVO.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleLabCodeList)){
                    hmePumpSelectionDetailsVO.setLabCode(singleLabCodeList.get(0).getLabCode());
                }
                //组合件SN
                List<HmePumpSelectionDetailsVO> singleCombMaterialLotCodeList = combMaterialLotCodeList.stream().filter(item -> hmePumpSelectionDetailsVO.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleCombMaterialLotCodeList)){
                    HmePumpSelectionDetailsVO singleCombMaterialLotCode = singleCombMaterialLotCodeList.get(0);
                    hmePumpSelectionDetailsVO.setCombMaterialLotId(singleCombMaterialLotCode.getCombMaterialLotId());
                    hmePumpSelectionDetailsVO.setCombMaterialLotCode(singleCombMaterialLotCode.getCombMaterialLotCode());
                    //投料工单
                    List<HmePumpSelectionDetailsVO> singleReleaseWoList = releaseWoList.stream().filter(item -> hmePumpSelectionDetailsVO.getCombMaterialLotId().equals(item.getCombMaterialLotId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleReleaseWoList)){
                        hmePumpSelectionDetailsVO.setReleaseWorkOrderId(singleReleaseWoList.get(0).getReleaseWorkOrderId());
                        hmePumpSelectionDetailsVO.setReleaseWorkOrderNum(singleReleaseWoList.get(0).getReleaseWorkOrderNum());
                    }
                }
                //预筛选操作人
                hmePumpSelectionDetailsVO.setCreatedByName(userInfoMap.getOrDefault(hmePumpSelectionDetailsVO.getCreatedBy(), new MtUserInfo()).getRealName());
                //装箱操作人
                hmePumpSelectionDetailsVO.setPackedByName(userInfoMap.getOrDefault(hmePumpSelectionDetailsVO.getPackedBy(), new MtUserInfo()).getRealName());
                //筛选产线
                List<WmsSummaryOfCosBarcodeProcessingVO2> singleProdLineList = prodLineList.stream().filter(item -> hmePumpSelectionDetailsVO.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleProdLineList)){
                    WmsSummaryOfCosBarcodeProcessingVO2 prodLine = singleProdLineList.get(0);
                    hmePumpSelectionDetailsVO.setProdLineId(prodLine.getProdLineId());
                    hmePumpSelectionDetailsVO.setProdLineCode(prodLine.getProdLineCode());
                    hmePumpSelectionDetailsVO.setProdLineName(prodLine.getProdLineName());
                }
                if(StringUtils.isNotBlank(hmePumpSelectionDetailsVO.getStatus())){
                    List<LovValueDTO> status = pumpSelectStatusLov.stream().filter(item -> hmePumpSelectionDetailsVO.getStatus().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(status)){
                        hmePumpSelectionDetailsVO.setStatusMeaning(status.get(0).getMeaning());
                    }
                }
                if(StringUtils.isNotBlank(hmePumpSelectionDetailsVO.getFreezeFlag())){
                    List<LovValueDTO> freezzFlag = enableFlagLov.stream().filter(item -> hmePumpSelectionDetailsVO.getFreezeFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(freezzFlag)){
                        hmePumpSelectionDetailsVO.setFreezeFlagMeaning(freezzFlag.get(0).getMeaning());
                    }
                }
            }
        }
        return resultPage;
    }

    @Override
    public List<HmePumpSelectionDetailsVO> pumpSelectionDetailsExport(Long tenantId, HmePumpSelectionDetailsDTO dto) {
        hmePumpSelectionDetailsRepository.pageQueryParamVerify(tenantId, dto);
        if(CollectionUtils.isNotEmpty(dto.getReleaseWorkOrderNumList())){
            //根据投料工单查询组合件SN
            List<String> woCombMaterialLotCodeList = hmePumpSelectionDetailsMapper.combMaterialLotCodeQueryByReleaseWo(tenantId, dto.getReleaseWorkOrderNumList());
            if(CollectionUtils.isEmpty(woCombMaterialLotCodeList)){
                return new ArrayList<>();
            }else {
                //如果投料工单找到的组合件SN与用户自己输入的组合件SN无交集，则直接无数据
                List<String> combMaterialLotCodeList = dto.getCombMaterialLotCodeList();
                if(CollectionUtils.isNotEmpty(combMaterialLotCodeList)){
                    combMaterialLotCodeList.retainAll(woCombMaterialLotCodeList);
                    if(CollectionUtils.isEmpty(combMaterialLotCodeList)){
                        return new ArrayList<>();
                    }else {
                        dto.setCombMaterialLotCodeList(combMaterialLotCodeList);
                    }
                }else {
                    dto.setCombMaterialLotCodeList(woCombMaterialLotCodeList);
                }
            }
        }
        List<HmePumpSelectionDetailsVO> resultList = hmePumpSelectionDetailsMapper.pumpSelectionDetailsPageQuery(tenantId, dto);
        if(CollectionUtils.isNotEmpty(resultList)){
            List<LovValueDTO> pumpSelectStatusLov = lovAdapter.queryLovValue("HME.PUMP_SELECT_STATUS", tenantId);
            List<LovValueDTO> enableFlagLov = lovAdapter.queryLovValue("Z_MTLOT_ENABLE_FLAG", tenantId);
            List<String> materialLotIdList = resultList.stream().map(HmePumpSelectionDetailsVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<HmePumpSelectionDetailsVO> labCodeList = new ArrayList<>();
            List<HmePumpSelectionDetailsVO> combMaterialLotCodeList = new ArrayList<>();
            List<String> combMaterialLotIdList = new ArrayList<>();
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                //根据泵浦源SN查询实验代码
                labCodeList.addAll(hmePumpSelectionDetailsMapper.pumpMaterialLotLabCodeQuery(tenantId, splitMaterialLotId));
                //根据泵浦源SN查询组合件SN
                List<HmePumpSelectionDetailsVO> singleCombMaterialLotCodeList = hmePumpSelectionDetailsMapper.combMaterialLotCodeQuery(tenantId, splitMaterialLotId);
                if(CollectionUtils.isNotEmpty(singleCombMaterialLotCodeList)){
                    combMaterialLotCodeList.addAll(singleCombMaterialLotCodeList);
                    combMaterialLotIdList.addAll(singleCombMaterialLotCodeList.stream().map(HmePumpSelectionDetailsVO::getCombMaterialLotId).collect(Collectors.toList()));
                }
            }
            List<HmePumpSelectionDetailsVO> releaseWoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(combMaterialLotIdList)){
                combMaterialLotIdList = combMaterialLotIdList.stream().distinct().collect(Collectors.toList());
                List<List<String>> splitCombMaterialLotIdList = CommonUtils.splitSqlList(combMaterialLotIdList, 1000);
                for (List<String> splitCombMaterialLotId:splitCombMaterialLotIdList) {
                    //根据组合件SN查询投料工单
                    releaseWoList.addAll(hmePumpSelectionDetailsMapper.releaseWoQuery(tenantId, splitCombMaterialLotId));
                }
            }
            //姓名
            List<Long> userIdList = new ArrayList<>();
            userIdList.addAll(resultList.stream().map(HmePumpSelectionDetailsVO::getCreatedBy).collect(Collectors.toList()));
            userIdList.addAll(resultList.stream().map(HmePumpSelectionDetailsVO::getPackedBy).collect(Collectors.toList()));
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());;
            Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
            //根据工位查询产线
            List<String> workcellIdList = resultList.stream().map(HmePumpSelectionDetailsVO::getWorkcellId).distinct().collect(Collectors.toList());
            List<WmsSummaryOfCosBarcodeProcessingVO2> prodLineList = hmeProductionFlowMapper.qeuryProcessLineProdByWorkcell(tenantId, workcellIdList);

            for (HmePumpSelectionDetailsVO hmePumpSelectionDetailsVO:resultList) {
                hmePumpSelectionDetailsVO.setSetsNumber(hmePumpSelectionDetailsVO.getSelectionOrder() + "/" + hmePumpSelectionDetailsVO.getSetsNum());
                if(StringUtils.isBlank(hmePumpSelectionDetailsVO.getFreezeFlag())){
                    hmePumpSelectionDetailsVO.setFreezeFlag("N");
                }
                //实验代码
                List<HmePumpSelectionDetailsVO> singleLabCodeList = labCodeList.stream().filter(item -> hmePumpSelectionDetailsVO.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleLabCodeList)){
                    hmePumpSelectionDetailsVO.setLabCode(singleLabCodeList.get(0).getLabCode());
                }
                //组合件SN
                List<HmePumpSelectionDetailsVO> singleCombMaterialLotCodeList = combMaterialLotCodeList.stream().filter(item -> hmePumpSelectionDetailsVO.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleCombMaterialLotCodeList)){
                    HmePumpSelectionDetailsVO singleCombMaterialLotCode = singleCombMaterialLotCodeList.get(0);
                    hmePumpSelectionDetailsVO.setCombMaterialLotId(singleCombMaterialLotCode.getCombMaterialLotId());
                    hmePumpSelectionDetailsVO.setCombMaterialLotCode(singleCombMaterialLotCode.getCombMaterialLotCode());
                    //投料工单
                    List<HmePumpSelectionDetailsVO> singleReleaseWoList = releaseWoList.stream().filter(item -> hmePumpSelectionDetailsVO.getCombMaterialLotId().equals(item.getCombMaterialLotId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleReleaseWoList)){
                        hmePumpSelectionDetailsVO.setReleaseWorkOrderId(singleReleaseWoList.get(0).getReleaseWorkOrderId());
                        hmePumpSelectionDetailsVO.setReleaseWorkOrderNum(singleReleaseWoList.get(0).getReleaseWorkOrderNum());
                    }
                }
                //预筛选操作人
                hmePumpSelectionDetailsVO.setCreatedByName(userInfoMap.getOrDefault(hmePumpSelectionDetailsVO.getCreatedBy(), new MtUserInfo()).getRealName());
                //装箱操作人
                hmePumpSelectionDetailsVO.setPackedByName(userInfoMap.getOrDefault(hmePumpSelectionDetailsVO.getPackedBy(), new MtUserInfo()).getRealName());
                //筛选产线
                List<WmsSummaryOfCosBarcodeProcessingVO2> singleProdLineList = prodLineList.stream().filter(item -> hmePumpSelectionDetailsVO.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleProdLineList)){
                    WmsSummaryOfCosBarcodeProcessingVO2 prodLine = singleProdLineList.get(0);
                    hmePumpSelectionDetailsVO.setProdLineId(prodLine.getProdLineId());
                    hmePumpSelectionDetailsVO.setProdLineCode(prodLine.getProdLineCode());
                }
                if(StringUtils.isNotBlank(hmePumpSelectionDetailsVO.getStatus())){
                    List<LovValueDTO> status = pumpSelectStatusLov.stream().filter(item -> hmePumpSelectionDetailsVO.getStatus().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(status)){
                        hmePumpSelectionDetailsVO.setStatusMeaning(status.get(0).getMeaning());
                    }
                }
                if(StringUtils.isNotBlank(hmePumpSelectionDetailsVO.getFreezeFlag())){
                    List<LovValueDTO> freezzFlag = enableFlagLov.stream().filter(item -> hmePumpSelectionDetailsVO.getFreezeFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(freezzFlag)){
                        hmePumpSelectionDetailsVO.setFreezeFlagMeaning(freezzFlag.get(0).getMeaning());
                    }
                }
            }
        }
        return resultList;
    }
}
