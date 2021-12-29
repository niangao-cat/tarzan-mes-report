package com.ruike.hme.app.assembler;

import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import com.ruike.hme.domain.vo.BomComponentWorkcellVO;
import com.ruike.hme.domain.vo.PendingNcQueryVO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/16 09:24
 */
@Component
public class WorkOrderAttritionSumAssembler {

    public BomComponentWorkcellVO representationToBomComponent(WorkOrderAttritionSumRepresentationDTO representation) {
        BomComponentWorkcellVO bomComponent = new BomComponentWorkcellVO();
        bomComponent.setWorkOrderId(representation.getWorkOrderId());
        bomComponent.setBomComponentId(representation.getBomComponentId());
        bomComponent.setWorkcellId(representation.getWorkcellId());
        return bomComponent;
    }

    public WorkOrderAttritionSumRepresentationDTO substituteCompletion(WorkOrderAttritionSumRepresentationDTO substitute, WorkOrderAttritionSumRepresentationDTO component) {
        if (Objects.isNull(component)) {
            return substitute;
        }
        // 工单相关字段
        substitute.setWorkOrderNum(component.getWorkOrderNum());
        substitute.setWorkOrderStatus(component.getWorkOrderStatus());
        substitute.setProductionVersion(component.getProductionVersion());
        substitute.setProductionVersionDescription(component.getProductionVersionDescription());
        substitute.setWorkOrderType(component.getWorkOrderType());
        substitute.setAssemblyMaterialId(component.getAssemblyMaterialId());
        substitute.setAssemblyMaterialCode(component.getAssemblyMaterialCode());
        substitute.setAssemblyMaterialName(component.getAssemblyMaterialName());
        substitute.setWoQuantity(component.getWoQuantity());
        substitute.setCompletedQuantity(component.getCompletedQuantity());
        substitute.setProdLineId(component.getProdLineId());
        substitute.setProdLineName(component.getProdLineName());
        substitute.setWorkcellId(component.getWorkcellId());
        substitute.setWorkcellName(component.getWorkcellName());
        substitute.setProcessId(component.getProcessId());
        substitute.setCoproductScrappedQuantity(BigDecimal.ZERO);
        // 合计字段
        substitute.setPlannedScrappedSumQuantity(component.getPlannedScrappedSumQuantity());
        substitute.setUnplannedScrappedSumQuantity(component.getUnplannedScrappedSumQuantity());
        substitute.setScrappedSumQuantity(component.getScrappedSumQuantity());
        substitute.setActualAttritionChance(component.getActualAttritionChance());
        substitute.setAttritionChanceDifference(component.getAttritionChanceDifference());
        substitute.setAttritionOverFlag(component.getAttritionOverFlag());
        substitute.setMaterialAttritionChance(component.getMaterialAttritionChance());
        return substitute;
    }

    public PendingNcQueryVO representationToNc(WorkOrderAttritionSumRepresentationDTO representation) {
        PendingNcQueryVO nc = new PendingNcQueryVO();
        nc.setWorkOrderId(representation.getWorkOrderId());
        nc.setMaterialId(representation.getComponentMaterialId());
        nc.setProcessId(representation.getProcessId());
        return nc;
    }
}
