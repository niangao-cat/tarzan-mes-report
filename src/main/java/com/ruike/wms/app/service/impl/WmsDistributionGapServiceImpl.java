package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.util.WmsCommonUtils;
import com.ruike.wms.api.dto.WmsDistributionGapDTO;
import com.ruike.wms.api.dto.WmsDistributionGapDTO2;
import com.ruike.wms.api.dto.WmsDistributionGapDTO3;
import com.ruike.wms.api.dto.WmsDistributionGapDTO4;
import com.ruike.wms.app.service.WmsDistributionGapService;
import com.ruike.wms.infra.mapper.WmsDistributionGapMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGapServiceImpl
 * @description:
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-19 10:58
 **/
@Service
@Slf4j
public class WmsDistributionGapServiceImpl implements WmsDistributionGapService {

    @Autowired
    private WmsDistributionGapMapper wmsDistributionGapMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Page<WmsDistributionGapDTO> selectDelivery(Long tenantId, WmsDistributionGapDTO3 dto3, PageRequest pageRequest) {
        List<String> locatorCodeList = lovAdapter.queryLovValue("HME.DISTRIBUTION_LOCATOR", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<String> hours = lovAdapter.queryLovValue("HME.DISTRIBUTION_LEAD_TIME", tenantId).stream().filter(item -> "1000".equals(item.getValue())).map(LovValueDTO::getMeaning).collect(Collectors.toList());
        BigDecimal diffHours = wmsDistributionGapMapper.selectShiftTime();
        if (CollectionUtils.isNotEmpty(hours)) {
            diffHours = diffHours.subtract(BigDecimal.valueOf(Double.valueOf(hours.get(0)))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        String deliveryHours = diffHours + "h";
        // 配送库存计算涉及仓库值集有值才查询
        List<WmsDistributionGapDTO2> gapList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(locatorCodeList)) {
            gapList = wmsDistributionGapMapper.selectLotQty(locatorCodeList);
        }
        List<WmsDistributionGapDTO> resultList = wmsDistributionGapMapper.selectDelivery(tenantId, dto3.getProdLineIdList());
        List<WmsDistributionGapDTO> filterList = this.handleDistributionDataList(tenantId, resultList, deliveryHours, gapList);
        Page<WmsDistributionGapDTO> page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), filterList);
        return page;
    }

    private List<WmsDistributionGapDTO> handleDistributionDataList(Long tenantId, List<WmsDistributionGapDTO> resultList, String deliveryHours, List<WmsDistributionGapDTO2> gapList) {
        List<WmsDistributionGapDTO> filterList = new ArrayList<>();
        // 查询当日生产需求/当日生产已配送/次日生产需求/已配送
        if (CollectionUtils.isNotEmpty(resultList)) {
            // 当日生产需求
            List<WmsDistributionGapDTO4> currentDemandQtyList = wmsDistributionGapMapper.queryCurrentDemandQty(tenantId, resultList);
            Map<Object, List<WmsDistributionGapDTO4>> currentDemandQtyMap = currentDemandQtyList.stream().collect(Collectors.groupingBy(vo -> this.splitStr(vo)));
            // 当日生产已配送
            List<WmsDistributionGapDTO4> currentDeliveryQtyList = wmsDistributionGapMapper.queryCurrentDeliveryQty(tenantId, resultList);
            Map<Object, List<WmsDistributionGapDTO4>> currentDeliveryQtyMap = currentDeliveryQtyList.stream().collect(Collectors.groupingBy(vo -> this.splitStr(vo)));
            // 次日生产需求
            List<WmsDistributionGapDTO4> nextDemandQtyList = wmsDistributionGapMapper.queryNextDemandQty(tenantId, resultList);
            Map<Object, List<WmsDistributionGapDTO4>> nextDemandQtyMap = nextDemandQtyList.stream().collect(Collectors.groupingBy(vo -> this.splitStr(vo)));
            // 已配送
            List<WmsDistributionGapDTO4> nextDeliveryQtyList = wmsDistributionGapMapper.queryNextDeliveryQty(tenantId, resultList);
            Map<Object, List<WmsDistributionGapDTO4>> nextDeliveryQtyMap = nextDeliveryQtyList.stream().collect(Collectors.groupingBy(vo -> this.splitStr(vo)));
            // 线边非限制库存
            List<String> workcellIdList = resultList.stream().map(WmsDistributionGapDTO::getWorkcellId).distinct().collect(Collectors.toList());
            List<WmsDistributionGapDTO4> lineNotLimitQtyList = wmsDistributionGapMapper.queryLineNotLimitQty(tenantId, resultList, workcellIdList);
            Map<String, List<WmsDistributionGapDTO4>> lineNotLimitQtyMap = lineNotLimitQtyList.stream().collect(Collectors.groupingBy(vo -> vo.getMaterialId() + vo.getWorkcellId() + (StringUtils.isNotBlank(vo.getMaterialVersion()) ? vo.getMaterialVersion() : "")));
            resultList.stream().forEach(vo -> {
                vo.setDemandQty1(BigDecimal.ZERO);
                vo.setDeliveryQty1(BigDecimal.ZERO);
                vo.setDemandQty2(BigDecimal.ZERO);
                vo.setDeliveryQty2(BigDecimal.ZERO);
                vo.setLineQty(BigDecimal.ZERO);
                List<WmsDistributionGapDTO4> currentDemandQtys = currentDemandQtyMap.getOrDefault(this.assemblyStr(vo), new ArrayList<>());
                if (CollectionUtils.isNotEmpty(currentDemandQtys)) {
                    vo.setDemandQty1(currentDemandQtys.get(0).getQty());
                }
                List<WmsDistributionGapDTO4> currentDeliveryQtys = currentDeliveryQtyMap.getOrDefault(this.assemblyStr(vo), new ArrayList<>());
                if (CollectionUtils.isNotEmpty(currentDeliveryQtys)) {
                    vo.setDeliveryQty1(currentDemandQtys.get(0).getQty());
                }
                List<WmsDistributionGapDTO4> nextDemandQtys = nextDemandQtyMap.getOrDefault(this.assemblyStr(vo), new ArrayList<>());
                if (CollectionUtils.isNotEmpty(nextDemandQtys)) {
                    vo.setDemandQty2(nextDemandQtys.get(0).getQty());
                }
                List<WmsDistributionGapDTO4> nextDeliveryQtys = nextDeliveryQtyMap.getOrDefault(this.assemblyStr(vo), new ArrayList<>());
                if (CollectionUtils.isNotEmpty(nextDeliveryQtys)) {
                    vo.setDeliveryQty2(nextDeliveryQtys.get(0).getQty());
                }
                List<WmsDistributionGapDTO4> lineNotLimitQtys = lineNotLimitQtyMap.getOrDefault(vo.getMaterialId() + vo.getWorkcellId() + vo.getMaterialVersion(), new ArrayList<>());
                if (CollectionUtils.isNotEmpty(lineNotLimitQtys)) {
                    vo.setLineQty(lineNotLimitQtys.get(0).getQty());
                }
            });
        }
        // 当日生产缺口 与 次日生产缺口 均为0 ，则不显示
        filterList = resultList.stream().filter(vo -> vo.getDeliveryQty1().compareTo(vo.getDemandQty1()) != 0 && vo.getDeliveryQty2().compareTo(vo.getDemandQty2()) != 0).collect(Collectors.toList());
        for (WmsDistributionGapDTO wmsDistributionGapDTO : filterList) {
            wmsDistributionGapDTO.setDeliveryHours(deliveryHours);
            List<WmsDistributionGapDTO2> totalList = gapList.stream().filter(
                    e -> e.getMaterialCode().equals(wmsDistributionGapDTO.getMaterialCode())
                            && (e.getMaterialVersion() == null ? wmsDistributionGapDTO.getMaterialVersion() : e.getMaterialVersion()).equals(wmsDistributionGapDTO.getMaterialVersion())).collect(Collectors.toList());
            BigDecimal totalQty = totalList.stream().map(WmsDistributionGapDTO2::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal freezeQty = totalList.stream().filter(e -> "Y".equals(e.getFreezeFlag())).map(WmsDistributionGapDTO2::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal receiveQty = totalList.stream().filter(e -> "RECEIVE_PENDING".equals(e.getLocatorType())).map(WmsDistributionGapDTO2::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            wmsDistributionGapDTO.setReceiveQty(receiveQty);
            wmsDistributionGapDTO.setStockQty(totalQty.subtract(freezeQty).subtract(receiveQty).setScale(6, BigDecimal.ROUND_HALF_UP));

            //V20210603 modify by penglin.sui for kang.wang 当日生产缺口&次日生产缺口字段为负数时显示为0

            wmsDistributionGapDTO.setDiffQty1(wmsDistributionGapDTO.getDemandQty1().subtract(wmsDistributionGapDTO.getDeliveryQty1()).setScale(2, BigDecimal.ROUND_HALF_UP));
            if(wmsDistributionGapDTO.getDiffQty1().compareTo(BigDecimal.ZERO) < 0){
                wmsDistributionGapDTO.setDiffQty1(BigDecimal.ZERO);
            }
            wmsDistributionGapDTO.setDiffQty2(wmsDistributionGapDTO.getDemandQty2().subtract(wmsDistributionGapDTO.getDeliveryQty2()).setScale(2, BigDecimal.ROUND_HALF_UP));
            if(wmsDistributionGapDTO.getDiffQty2().compareTo(BigDecimal.ZERO) < 0){
                wmsDistributionGapDTO.setDiffQty2(BigDecimal.ZERO);
            }
        }
        return filterList.stream().sorted(Comparator.comparing(WmsDistributionGapDTO::getProdLineName).thenComparing(WmsDistributionGapDTO::getWorkcellCode).thenComparing(WmsDistributionGapDTO::getDiffQty1).reversed().thenComparing(WmsDistributionGapDTO::getDiffQty2).thenComparing(WmsDistributionGapDTO::getShiftCode)).collect(Collectors.toList());
    }

    private String splitStr(WmsDistributionGapDTO4 dto4) {
        StringBuffer sb = new StringBuffer();
        sb.append(dto4.getShiftCode());
        sb.append(dto4.getWorkcellId());
        sb.append(dto4.getMaterialId());
        sb.append(dto4.getMaterialVersion());
        return sb.toString();
    }

    private String assemblyStr(WmsDistributionGapDTO dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(dto.getShiftCode());
        sb.append(dto.getWorkcellId());
        sb.append(dto.getMaterialId());
        sb.append(dto.getMaterialVersion());
        return sb.toString();
    }
}
