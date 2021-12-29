package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.util.HmeCommonUtils;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO2;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO3;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO4;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO5;
import com.ruike.wms.app.service.WmsDeliveryMonitoringService;
import com.ruike.wms.infra.constant.WmsConstants;
import com.ruike.wms.infra.mapper.WmsDeliveryMonitoringMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringServiceImpl
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-13 18:56
 **/
@Service
public class WmsDeliveryMonitoringServiceImpl implements WmsDeliveryMonitoringService {

    @Autowired
    private WmsDeliveryMonitoringMapper wmsDeliveryMonitoringMapper;

    @Override
    @ProcessLovValue
    public List<WmsDeliveryMonitoringDTO> selectDailyDelivery(Long tenantId) {
        List<WmsDeliveryMonitoringDTO> monitoringDTOList = wmsDeliveryMonitoringMapper.selectDailyDelivery(tenantId);
        List<String> docStatusList = monitoringDTOList.stream().map(WmsDeliveryMonitoringDTO::getInstructionDocStatus).collect(Collectors.toList());
        List<String> status = new ArrayList<>();
        status.add(WmsConstants.InstructionDocStatus.NEW);
        status.add(WmsConstants.InstructionDocStatus.RELEASED);
        status.add(WmsConstants.InstructionDocStatus.PREPARE_EXECUTE);
        status.add(WmsConstants.InstructionDocStatus.PREPARE_COMPLETE);
        status.add(WmsConstants.InstructionDocStatus.SIGN_EXECUTE);
        status.add(WmsConstants.InstructionDocStatus.SIGN_COMPLETE);
        status.add(WmsConstants.InstructionDocStatus.CLOSED);
        status.forEach(item -> {
            if (!docStatusList.contains(item)) {
                WmsDeliveryMonitoringDTO dto = new WmsDeliveryMonitoringDTO();
                dto.setInstructionDocStatus(item);
                dto.setQuantity(BigDecimal.ZERO);
                monitoringDTOList.add(dto);
            }
        });
        BigDecimal sum = monitoringDTOList.stream().map(WmsDeliveryMonitoringDTO::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        monitoringDTOList.forEach(item -> {
            if (item.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal proportion = item.getQuantity().divide(sum, 3, BigDecimal.ROUND_HALF_UP);
                item.setProportion(proportion);
            }
            if (WmsConstants.InstructionDocStatus.NEW.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_01);
            } else if (WmsConstants.InstructionDocStatus.RELEASED.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_02);
            } else if (WmsConstants.InstructionDocStatus.PREPARE_EXECUTE.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_03);
            } else if (WmsConstants.InstructionDocStatus.PREPARE_COMPLETE.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_04);
            } else if (WmsConstants.InstructionDocStatus.SIGN_EXECUTE.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_05);
            } else if (WmsConstants.InstructionDocStatus.SIGN_COMPLETE.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_06);
            } else if (WmsConstants.InstructionDocStatus.CLOSED.equals(item.getInstructionDocStatus())) {
                item.setColor(WmsConstants.Color.COLOR_07);
            }
        });
        BigDecimal proportionSum = monitoringDTOList.stream().filter(item -> item.getProportion() != null).map(WmsDeliveryMonitoringDTO::getProportion).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (proportionSum.compareTo(BigDecimal.ZERO) > 0 && proportionSum.compareTo(BigDecimal.ONE) != 0) {
            BigDecimal diff = BigDecimal.ONE.subtract(proportionSum);
            BigDecimal proportion = monitoringDTOList.get(0).getProportion();
            monitoringDTOList.get(0).setProportion(proportion.add(diff));
        }
        return monitoringDTOList;
    }

    @Override
    public Page<WmsDeliveryMonitoringDTO2> selectProLineDelivery(Long tenantId, PageRequest pageRequest) {
        List<WmsDeliveryMonitoringDTO2> result = new ArrayList<>();
        List<WmsDeliveryMonitoringDTO3> data = wmsDeliveryMonitoringMapper.selectProLineDelivery(tenantId);
        if (CollectionUtils.isEmpty(data)) {
            return HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), Collections.EMPTY_LIST);
        }
        Map<String, List<WmsDeliveryMonitoringDTO3>> proLineMap = data.stream().collect(Collectors.groupingBy(score -> score.getProdLineName()));
        proLineMap.entrySet().stream().forEach(c -> {
            WmsDeliveryMonitoringDTO2 dto2 = setProLineQty(c.getValue());
            dto2.setProLineName(c.getKey());
            result.add(dto2);
        });
        Page<WmsDeliveryMonitoringDTO2> page = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), result);
        WmsDeliveryMonitoringDTO2 monitoringSum = setStatusQty(page.getContent());
        page.getContent().add(monitoringSum);
        return page;
    }

    @Override
    public WmsDeliveryMonitoringDTO4 selectMonthDelivery(Long tenantId) {
        WmsDeliveryMonitoringDTO4 result = new WmsDeliveryMonitoringDTO4();
        List<WmsDeliveryMonitoringDTO5> data = wmsDeliveryMonitoringMapper.selectMonthDelivery(tenantId);
        data.forEach(item -> {
            if (WmsConstants.Month.MONTH_01.equals(item.getMonths())) {
                result.setQty1(item.getQty());
            } else if (WmsConstants.Month.MONTH_02.equals(item.getMonths())) {
                result.setQty2(item.getQty());
            } else if (WmsConstants.Month.MONTH_03.equals(item.getMonths())) {
                result.setQty3(item.getQty());
            } else if (WmsConstants.Month.MONTH_04.equals(item.getMonths())) {
                result.setQty4(item.getQty());
            } else if (WmsConstants.Month.MONTH_05.equals(item.getMonths())) {
                result.setQty5(item.getQty());
            } else if (WmsConstants.Month.MONTH_06.equals(item.getMonths())) {
                result.setQty6(item.getQty());
            } else if (WmsConstants.Month.MONTH_07.equals(item.getMonths())) {
                result.setQty7(item.getQty());
            } else if (WmsConstants.Month.MONTH_08.equals(item.getMonths())) {
                result.setQty8(item.getQty());
            } else if (WmsConstants.Month.MONTH_09.equals(item.getMonths())) {
                result.setQty9(item.getQty());
            } else if (WmsConstants.Month.MONTH_10.equals(item.getMonths())) {
                result.setQty10(item.getQty());
            } else if (WmsConstants.Month.MONTH_11.equals(item.getMonths())) {
                result.setQty11(item.getQty());
            } else if (WmsConstants.Month.MONTH_12.equals(item.getMonths())) {
                result.setQty12(item.getQty());
            }
        });
        return result;
    }


    /**
     * 每个产线计算
     *
     * @param monitoringDTO3List
     * @return
     */
    private WmsDeliveryMonitoringDTO2 setProLineQty(List<WmsDeliveryMonitoringDTO3> monitoringDTO3List) {
        WmsDeliveryMonitoringDTO2 dto2 = new WmsDeliveryMonitoringDTO2();
        monitoringDTO3List.forEach(item -> {
            if (WmsConstants.InstructionDocStatus.NEW.equals(item.getInstructionDocStatus())) {
                dto2.setNewQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.RELEASED.equals(item.getInstructionDocStatus())) {
                dto2.setReleasedQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.PREPARE_EXECUTE.equals(item.getInstructionDocStatus())) {
                dto2.setPrepareExecuteQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.PREPARE_COMPLETE.equals(item.getInstructionDocStatus())) {
                dto2.setPrepareCompleteQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.SIGN_EXECUTE.equals(item.getInstructionDocStatus())) {
                dto2.setSignExecuteQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.SIGN_COMPLETE.equals(item.getInstructionDocStatus())) {
                dto2.setSignCompleteQty(item.getQuantity());
            } else if (WmsConstants.InstructionDocStatus.CLOSED.equals(item.getInstructionDocStatus())) {
                dto2.setCloseQty(item.getQuantity());
            }
        });
        BigDecimal sum = monitoringDTO3List.stream().map(WmsDeliveryMonitoringDTO3::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setProLineSum(sum);
        return dto2;
    }

    /**
     * 汇总计算
     *
     * @param monitoringDTO2List
     * @return
     */
    private WmsDeliveryMonitoringDTO2 setStatusQty(List<WmsDeliveryMonitoringDTO2> monitoringDTO2List) {
        WmsDeliveryMonitoringDTO2 dto2 = new WmsDeliveryMonitoringDTO2();
        BigDecimal newQty = monitoringDTO2List.stream().filter(item -> item.getNewQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getNewQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setNewQty(newQty);
        BigDecimal releasedQty = monitoringDTO2List.stream().filter(item -> item.getReleasedQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getReleasedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setReleasedQty(releasedQty);
        BigDecimal prepareExecuteQty = monitoringDTO2List.stream().filter(item -> item.getPrepareExecuteQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getPrepareExecuteQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setPrepareExecuteQty(prepareExecuteQty);
        BigDecimal prepareCompleteQty = monitoringDTO2List.stream().filter(item -> item.getPrepareCompleteQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getPrepareCompleteQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setPrepareCompleteQty(prepareCompleteQty);
        BigDecimal signExecuteQty = monitoringDTO2List.stream().filter(item -> item.getSignExecuteQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getSignExecuteQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setSignExecuteQty(signExecuteQty);
        BigDecimal signCompleteQty = monitoringDTO2List.stream().filter(item -> item.getSignCompleteQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getSignCompleteQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setSignCompleteQty(signCompleteQty);
        BigDecimal closeQty = monitoringDTO2List.stream().filter(item -> item.getCloseQty() != null)
                .map(WmsDeliveryMonitoringDTO2::getCloseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setCloseQty(closeQty);
        BigDecimal sum = monitoringDTO2List.stream().filter(item -> item.getProLineSum() != null)
                .map(WmsDeliveryMonitoringDTO2::getProLineSum).reduce(BigDecimal.ZERO, BigDecimal::add);
        dto2.setProLineSum(sum);
        dto2.setProLineName("合计");
        return dto2;
    }

}
