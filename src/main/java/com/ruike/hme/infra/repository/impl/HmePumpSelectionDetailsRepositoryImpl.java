package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;
import com.ruike.hme.domain.repository.HmePumpSelectionDetailsRepository;
import com.ruike.hme.infra.mapper.HmeProductionFlowMapper;
import com.ruike.hme.infra.mapper.HmePumpSelectionDetailsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 泵浦源预筛选报表资源库实现
 *
 * @author: chaonan.hu@hand-china.com 2021-11-05 09:17:20
 **/
@Component
public class HmePumpSelectionDetailsRepositoryImpl implements HmePumpSelectionDetailsRepository {

    @Autowired
    private HmeProductionFlowMapper hmeProductionFlowMapper;
    @Autowired
    private HmePumpSelectionDetailsMapper hmePumpSelectionDetailsMapper;

    @Override
    public void pageQueryParamVerify(Long tenantId, HmePumpSelectionDetailsDTO dto) {
        if(StringUtils.isNotBlank(dto.getMaterialLotCode())){
            String[] materialLotCodeArray = dto.getMaterialLotCode().split(",");
            dto.setMaterialLotCodeList(Arrays.asList(materialLotCodeArray));
        }
        if(StringUtils.isNotBlank(dto.getWarehouseId())){
            String[] warehouseIdArray = dto.getWarehouseId().split(",");
            dto.setWarehouseIdList(Arrays.asList(warehouseIdArray));
        }
        if(StringUtils.isNotBlank(dto.getLocatorId())){
            String[] locatorIdArray = dto.getLocatorId().split(",");
            dto.setLocatorIdList(Arrays.asList(locatorIdArray));
        }
        if(StringUtils.isNotBlank(dto.getWorkOrderNum())){
            String[] workOrderNumArray = dto.getWorkOrderNum().split(",");
            dto.setWorkOrderNumList(Arrays.asList(workOrderNumArray));
        }
        if(StringUtils.isNotBlank(dto.getMaterialId())){
            String[] materialIdArray = dto.getMaterialId().split(",");
            dto.setMaterialIdList(Arrays.asList(materialIdArray));
        }
        if(StringUtils.isNotBlank(dto.getOldContainerCode())){
            String[] OldContainerCodeArray = dto.getOldContainerCode().split(",");
            dto.setOldContainerCodeList(Arrays.asList(OldContainerCodeArray));
        }
        if(StringUtils.isNotBlank(dto.getNewContainerCode())){
            String[] newContainerCodeArray = dto.getNewContainerCode().split(",");
            dto.setNewContainerCodeList(Arrays.asList(newContainerCodeArray));
        }
        if(StringUtils.isNotBlank(dto.getRuleCode())){
            String[] ruleCodeArray = dto.getRuleCode().split(",");
            dto.setRuleCodeList(Arrays.asList(ruleCodeArray));
        }
        if(StringUtils.isNotBlank(dto.getSelectionLot())){
            String[] selectionLotArray = dto.getSelectionLot().split(",");
            dto.setSelectionLotList(Arrays.asList(selectionLotArray));
        }
        if(StringUtils.isNotBlank(dto.getCombMaterialId())){
            String[] combMaterialIdArray = dto.getCombMaterialId().split(",");
            dto.setCombMaterialIdList(Arrays.asList(combMaterialIdArray));
        }
        if(StringUtils.isNotBlank(dto.getRevision())){
            String[] revisionArray = dto.getRevision().split(",");
            dto.setRevisionList(Arrays.asList(revisionArray));
        }
        if(StringUtils.isNotBlank(dto.getCombMaterialLotCode())){
            String[] combMaterialLotCodeArray = dto.getCombMaterialLotCode().split(",");
            dto.setCombMaterialLotCodeList(Arrays.asList(combMaterialLotCodeArray));
        }
        if(StringUtils.isNotBlank(dto.getReleaseWorkOrderNum())){
            String[] releaseWorkOrderNumArray = dto.getReleaseWorkOrderNum().split(",");
            dto.setReleaseWorkOrderNumList(Arrays.asList(releaseWorkOrderNumArray));
        }
        //将筛选产线查询条件转换到工位上
        if(StringUtils.isBlank(dto.getWorkcellId()) && StringUtils.isNotBlank(dto.getProdLineId())){
            List<String> workcellIdList = hmeProductionFlowMapper.workcellByProdLineQuery(tenantId, dto.getProdLineId());
            dto.setWorkcellIdList(workcellIdList);
        }

    }
}
