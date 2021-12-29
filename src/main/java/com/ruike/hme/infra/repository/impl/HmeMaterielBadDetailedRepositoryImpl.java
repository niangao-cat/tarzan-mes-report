package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.hme.domain.repository.HmeMaterielBadDetailedRepository;
import com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO;
import com.ruike.hme.infra.mapper.HmeMaterielBadDetailedMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.qms.infra.util.CollectorsUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:04
 */
@Component
public class HmeMaterielBadDetailedRepositoryImpl implements HmeMaterielBadDetailedRepository {

    @Autowired
    private HmeMaterielBadDetailedMapper hmeMaterielBadDetailedMapper;
    /**
     * 材料不良明细报表 资源库查询
     *
     * @param tenantId
     * @param hmeMaterielBadDetailedDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.ItfMaterielBadDetailedVO>
     * @auther wenqiang.yin@hand-china.com 2021/2/2 12:56
     */
    @Override
    @ProcessLovValue
    public Page<HmeMaterielBadDetailedVO> pageList(Long tenantId, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO, PageRequest pagerequest) {
        Page<HmeMaterielBadDetailedVO> pageResult = PageHelper.doPageAndSort(pagerequest, () -> hmeMaterielBadDetailedMapper.selectListByCondition(tenantId, hmeMaterielBadDetailedDTO));
        Map<String, List<HmeMaterielBadDetailedVO>> detailedVOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(pageResult.getContent())){
            Map<String, List<HmeMaterielBadDetailedVO>> releaseQtyQueryMap = pageResult.getContent().stream().collect(Collectors.groupingBy((t -> {
                return t.getMaterialLotId() + "#" + t.getWorkOrderId();
            })));
            List<HmeMaterielBadDetailedVO> releaseQtyQueryList = new ArrayList<>();
            for (Map.Entry<String, List<HmeMaterielBadDetailedVO>> entry:releaseQtyQueryMap.entrySet()) {
                String[] split = entry.getKey().split("#");
                if(split.length == 2){
                    HmeMaterielBadDetailedVO hmeMaterielBadDetailedVO = new HmeMaterielBadDetailedVO();
                    hmeMaterielBadDetailedVO.setMaterialLotId(split[0]);
                    hmeMaterielBadDetailedVO.setWorkOrderId(split[1]);
                    releaseQtyQueryList.add(hmeMaterielBadDetailedVO);
                }
            }
            if(CollectionUtils.isNotEmpty(releaseQtyQueryList)){
                List<List<HmeMaterielBadDetailedVO>> splitList = CommonUtils.splitSqlList(releaseQtyQueryList, 1000);
                List<HmeMaterielBadDetailedVO> releaseQtyList = new ArrayList<>();
                for (List<HmeMaterielBadDetailedVO> split:splitList) {
                    releaseQtyList.addAll(hmeMaterielBadDetailedMapper.queryReleaseQtyInEoJobMaterial(tenantId, split));
                    releaseQtyList.addAll(hmeMaterielBadDetailedMapper.queryReleaseQtyInEoJobSnLotMaterial(tenantId, split));
                }
                if(CollectionUtils.isNotEmpty(releaseQtyList)){
                    detailedVOMap = releaseQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId() + "_" + dto.getWorkOrderId()));
                }
            }
        }
//
//        List<String> materialLotIdList = pageResult.getContent().stream().map(HmeMaterielBadDetailedVO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<String> workOrderIdList = pageResult.getContent().stream().map(HmeMaterielBadDetailedVO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<String, List<HmeMaterielBadDetailedVO>> detailedVOMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(materialLotIdList) && CollectionUtils.isNotEmpty(workOrderIdList)) {
//            List<HmeMaterielBadDetailedVO> detailedVOList = hmeMaterielBadDetailedMapper.queryReleaseQty(tenantId, materialLotIdList, workOrderIdList);
//            if (CollectionUtils.isNotEmpty(detailedVOList)) {
//                detailedVOMap = detailedVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId() + "_" + dto.getWorkOrderId()));
//            }
//        }
        for (HmeMaterielBadDetailedVO hmeMaterielBadDetailedVO : pageResult.getContent()) {
            if (StringUtils.isNotBlank(hmeMaterielBadDetailedVO.getMaterialLotId()) && StringUtils.isNotBlank(hmeMaterielBadDetailedVO.getWorkOrderId())) {
                String mapKey = hmeMaterielBadDetailedVO.getMaterialLotId() + "_" + hmeMaterielBadDetailedVO.getWorkOrderId();
                List<HmeMaterielBadDetailedVO> detailedVOList = detailedVOMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(detailedVOList)) {
                    BigDecimal releaseQty = detailedVOList.stream().collect(CollectorsUtil.summingBigDecimal(item -> Objects.isNull(item.getReleaseQty()) ? BigDecimal.ZERO : item.getReleaseQty()));
                    hmeMaterielBadDetailedVO.setReleaseQty(releaseQty);
                }
            }
        }
        return pageResult;
    }

    @Override
    @ProcessLovValue
    public List<HmeMaterielBadDetailedVO> materialNcExport(Long tenantId, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO) {
        List<HmeMaterielBadDetailedVO> resultList = hmeMaterielBadDetailedMapper.selectListByCondition(tenantId, hmeMaterielBadDetailedDTO);
        Map<String, List<HmeMaterielBadDetailedVO>> detailedVOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            Map<String, List<HmeMaterielBadDetailedVO>> releaseQtyQueryMap = resultList.stream().collect(Collectors.groupingBy((t -> {
                return t.getMaterialLotId() + "#" + t.getWorkOrderId();
            })));
            List<HmeMaterielBadDetailedVO> releaseQtyQueryList = new ArrayList<>();
            for (Map.Entry<String, List<HmeMaterielBadDetailedVO>> entry:releaseQtyQueryMap.entrySet()) {
                String[] split = entry.getKey().split("#");
                if(split.length == 2){
                    HmeMaterielBadDetailedVO hmeMaterielBadDetailedVO = new HmeMaterielBadDetailedVO();
                    hmeMaterielBadDetailedVO.setMaterialLotId(split[0]);
                    hmeMaterielBadDetailedVO.setWorkOrderId(split[1]);
                    releaseQtyQueryList.add(hmeMaterielBadDetailedVO);
                }
            }
            if(CollectionUtils.isNotEmpty(releaseQtyQueryList)){
                List<List<HmeMaterielBadDetailedVO>> splitList = CommonUtils.splitSqlList(releaseQtyQueryList, 1000);
                List<HmeMaterielBadDetailedVO> releaseQtyList = new ArrayList<>();
                for (List<HmeMaterielBadDetailedVO> split:splitList) {
                    releaseQtyList.addAll(hmeMaterielBadDetailedMapper.queryReleaseQtyInEoJobMaterial(tenantId, split));
                    releaseQtyList.addAll(hmeMaterielBadDetailedMapper.queryReleaseQtyInEoJobSnLotMaterial(tenantId, split));
                }
                if(CollectionUtils.isNotEmpty(releaseQtyList)){
                    detailedVOMap = releaseQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId() + "_" + dto.getWorkOrderId()));
                }
            }
        }

//        List<String> materialLotIdList = resultList.stream().map(HmeMaterielBadDetailedVO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<String> workOrderIdList = resultList.stream().map(HmeMaterielBadDetailedVO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<String, List<HmeMaterielBadDetailedVO>> detailedVOMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(materialLotIdList) && CollectionUtils.isNotEmpty(workOrderIdList)) {
//            List<HmeMaterielBadDetailedVO> detailedVOList = hmeMaterielBadDetailedMapper.queryReleaseQty(tenantId, materialLotIdList, workOrderIdList);
//            if (CollectionUtils.isNotEmpty(detailedVOList)) {
//                detailedVOMap = detailedVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId() + "_" + dto.getWorkOrderId()));
//            }
//        }
        for (HmeMaterielBadDetailedVO hmeMaterielBadDetailedVO : resultList) {
            if (StringUtils.isNotBlank(hmeMaterielBadDetailedVO.getMaterialLotId()) && StringUtils.isNotBlank(hmeMaterielBadDetailedVO.getWorkOrderId())) {
                String mapKey = hmeMaterielBadDetailedVO.getMaterialLotId() + "_" + hmeMaterielBadDetailedVO.getWorkOrderId();
                List<HmeMaterielBadDetailedVO> detailedVOList = detailedVOMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(detailedVOList)) {
                    BigDecimal releaseQty = detailedVOList.stream().collect(CollectorsUtil.summingBigDecimal(item -> Objects.isNull(item.getReleaseQty()) ? BigDecimal.ZERO : item.getReleaseQty()));
                    hmeMaterielBadDetailedVO.setReleaseQty(releaseQty);
                }
            }
            if(StringUtils.isBlank(hmeMaterielBadDetailedVO.getFreezeFlag())){
                hmeMaterielBadDetailedVO.setFreezeFlag("N");
            }
        }
        return resultList;
    }
}
