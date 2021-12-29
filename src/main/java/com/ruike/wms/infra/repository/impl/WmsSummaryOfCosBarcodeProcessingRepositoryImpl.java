package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.domain.repository.WmsSummaryOfCosBarcodeProcessingRepository;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import com.ruike.wms.infra.mapper.WmsSummaryOfCosBarcodeProcessingMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ywj
 * @version 0.0.1
 * @description COS条码加工汇总表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
@Component
public class WmsSummaryOfCosBarcodeProcessingRepositoryImpl implements WmsSummaryOfCosBarcodeProcessingRepository {

    @Autowired
    WmsSummaryOfCosBarcodeProcessingMapper mapper;

    @Autowired
    private LovAdapter lovAdapter;

    /**
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @description COS条码加工汇总表
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     */
    @Override
    @ProcessLovValue
    public Page<WmsSummaryOfCosBarcodeProcessingVO> list(Long tenantId, PageRequest pageRequest, WmsSummaryOfCosBarcodeProcessingDTO dto) {
        // 获取当前用户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long currentUserId = curUser == null ? -1L : curUser.getUserId();
        dto.initParam(tenantId, lovAdapter);
        if(StringUtils.isNotEmpty(dto.getWorkcellId())){
            dto.setWorkcellIdList(Arrays.asList(dto.getWorkcellId().split(",")));
        }else if(StringUtils.isNotEmpty(dto.getProcessId())){
            dto.setWorkcellIdList(mapper.workcellByProcessQuery(tenantId, dto.getProcessId()));
        }else if (StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            dto.setWorkcellIdList(mapper.workcellByLineWorkcellQuery(tenantId, dto.getLineWorkcellId()));
        }
        // 查询方法
        Page<WmsSummaryOfCosBarcodeProcessingVO> result = PageHelper.doPage(pageRequest, () -> mapper.list(tenantId, currentUserId, dto));
        // 批量查询不良数
        displayFieldsCompletion(tenantId, result.getContent());
        return result;
    }

    @Override
    @ProcessLovValue
    public List<WmsSummaryOfCosBarcodeProcessingVO> listForExport(Long tenantId, WmsSummaryOfCosBarcodeProcessingDTO dto) {
        // 获取当前用户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long currentUserId = curUser == null ? -1L : curUser.getUserId();
        dto.initParam(tenantId, lovAdapter);
        if(StringUtils.isNotEmpty(dto.getWorkcellId())){
            dto.setWorkcellIdList(Arrays.asList(dto.getWorkcellId().split(",")));
        }else if(StringUtils.isNotEmpty(dto.getProcessId())){
            dto.setWorkcellIdList(mapper.workcellByProcessQuery(tenantId, dto.getProcessId()));
        }else if (StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            dto.setWorkcellIdList(mapper.workcellByLineWorkcellQuery(tenantId, dto.getLineWorkcellId()));
        }
        // 查询方法
        List<WmsSummaryOfCosBarcodeProcessingVO> result = mapper.list(tenantId, currentUserId, dto);
        // 批量查询不良数
        displayFieldsCompletion(tenantId, result);
        return result;
    }

    private void displayFieldsCompletion(Long tenantId, List<WmsSummaryOfCosBarcodeProcessingVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
//        List<String> workOrderIdList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<String> waferNumList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWaferNum).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialLotIdList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<String> createByList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getCreatedBy).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> workcellIdList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> jobIdList = list.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        //查询工序工段信息 因取不良数量时需用到工序ID,故这里先查出来工序工段信息赋值进去
        List<WmsSummaryOfCosBarcodeProcessingVO2> workcellInfoList = mapper.qeuryProcessAndLineWorkcellByWorkcell(tenantId, workcellIdList);
        Map<String, List<WmsSummaryOfCosBarcodeProcessingVO2>> workcellInfoMap = workcellInfoList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO2::getWorkcellId));
        for (WmsSummaryOfCosBarcodeProcessingVO wmsSummaryOfCosBarcodeProcessingVO : list) {
            List<WmsSummaryOfCosBarcodeProcessingVO2> barcodeWorkcellInfoList = workcellInfoMap.get(wmsSummaryOfCosBarcodeProcessingVO.getWorkcellId());
            if(CollectionUtils.isNotEmpty(barcodeWorkcellInfoList)){
                WmsSummaryOfCosBarcodeProcessingVO2 barCodeWorkcellInfo = barcodeWorkcellInfoList.get(0);
                wmsSummaryOfCosBarcodeProcessingVO.setProcessId(barCodeWorkcellInfo.getProcessId());
                wmsSummaryOfCosBarcodeProcessingVO.setProcessName(barCodeWorkcellInfo.getProcessName());
                wmsSummaryOfCosBarcodeProcessingVO.setLineWorkcellId(barCodeWorkcellInfo.getLineWorkcellId());
                wmsSummaryOfCosBarcodeProcessingVO.setLineWorkcellName(barCodeWorkcellInfo.getLineWorkcellName());
            }
        }

        // 20210720 modify by sanfeng.zhang for peng.zhao 按根据工单+wafer+盒子号+工序取不良数
        // 重新取不良数量
        //List<WmsSummaryOfCosBarcodeProcessingVO> processingVOList = mapper.batchListQueryNg(tenantId, workOrderIdList, waferNumList, materialLotIdList, createByList, workcellIdList);
        List<WmsSummaryOfCosBarcodeProcessingVO> ngQueryList = list.stream().filter(item -> StringUtils.isNotBlank(item.getWorkOrderId()) && StringUtils.isNotBlank(item.getWaferNum())
                && StringUtils.isNotBlank(item.getMaterialLotId()) && StringUtils.isNotBlank(item.getProcessId())).collect(Collectors.toList());
        List<WmsSummaryOfCosBarcodeProcessingVO> processingVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ngQueryList)){
            Map<String, List<WmsSummaryOfCosBarcodeProcessingVO>> ngQueryMap = ngQueryList.stream().collect(Collectors.groupingBy((t -> {
                return t.getWorkOrderId() + "#" + t.getWaferNum() + "#" + t.getMaterialLotId() + "#" + t.getProcessId();
            })));
            ngQueryList = new ArrayList<>();
            for (Map.Entry<String, List<WmsSummaryOfCosBarcodeProcessingVO>> entry:ngQueryMap.entrySet()) {
                String[] split = entry.getKey().split("#");
                if(split.length == 4){
                    WmsSummaryOfCosBarcodeProcessingVO cosBarcodeProcessingVO = new WmsSummaryOfCosBarcodeProcessingVO();
                    cosBarcodeProcessingVO.setWorkOrderId(split[0]);
                    cosBarcodeProcessingVO.setWaferNum(split[1]);
                    cosBarcodeProcessingVO.setMaterialLotId(split[2]);
                    cosBarcodeProcessingVO.setProcessId(split[3]);
                    ngQueryList.add(cosBarcodeProcessingVO);
                }
            }
            if(CollectionUtils.isNotEmpty(ngQueryList)){
                List<List<WmsSummaryOfCosBarcodeProcessingVO>> splitList = CommonUtils.splitSqlList(ngQueryList, 1000);
                for (List<WmsSummaryOfCosBarcodeProcessingVO> split:splitList) {
                    processingVOList.addAll(mapper.batchListQueryNgNew(tenantId, split));
                }
            }
        }
        // 重新查询设备
        List<WmsSummaryOfCosBarcodeProcessingVO> jobVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(jobIdList)){
            List<List<String>> splitList = CommonUtils.splitSqlList(jobIdList, 3000);
            for (List<String> split:splitList) {
                jobVOList.addAll(mapper.batchListQueryAssetEncoding(tenantId, split));
            }
        }
        //查询物料批装载信息、实验代码信息
        List<WmsSummaryOfCosBarcodeProcessingVO> materialLotLoadList = new ArrayList<>();
        List<WmsSummaryOfCosBarcodeProcessingVO> labCodeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            List<List<String>> splitList = CommonUtils.splitSqlList(materialLotIdList, 1000);
            for (List<String> split:splitList) {
                materialLotLoadList.addAll(mapper.batchMaterialLotLoadQuery(tenantId, split));
                labCodeList.addAll(mapper.batchLabCodeQuery(tenantId, split));
            }
        }

        Map<Object, List<WmsSummaryOfCosBarcodeProcessingVO>> processingVOMap = processingVOList.stream().collect(Collectors.groupingBy(this::spliceKey));
        Map<Object, List<WmsSummaryOfCosBarcodeProcessingVO>> jobVOMap = jobVOList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO::getJobId));
        Map<String, List<WmsSummaryOfCosBarcodeProcessingVO>> materialLotLoadMap = materialLotLoadList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO::getMaterialLotId));
        Map<String, List<WmsSummaryOfCosBarcodeProcessingVO>> labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO::getMaterialLotId));

        for (WmsSummaryOfCosBarcodeProcessingVO wmsSummaryOfCosBarcodeProcessingVO : list) {
            if(Objects.isNull(wmsSummaryOfCosBarcodeProcessingVO.getSnQty())){
                wmsSummaryOfCosBarcodeProcessingVO.setSnQty(BigDecimal.ZERO);
            }
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeJobVOList = jobVOMap.get(wmsSummaryOfCosBarcodeProcessingVO.getJobId());
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeLoadList = materialLotLoadMap.get(wmsSummaryOfCosBarcodeProcessingVO.getMaterialLotId());
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeLabCodeList = labCodeMap.get(wmsSummaryOfCosBarcodeProcessingVO.getMaterialLotId());

            if (CollectionUtils.isNotEmpty(barcodeJobVOList)) {
                wmsSummaryOfCosBarcodeProcessingVO.setAssetEncoding(barcodeJobVOList.get(0).getAssetEncoding());
            } else {
                wmsSummaryOfCosBarcodeProcessingVO.setAssetEncoding("");
            }

            if (CollectionUtils.isNotEmpty(barcodeLoadList)) {
                List<String> sinkCodeList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getSinkCode).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(sinkCodeList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setSinkCode(StringUtils.join(sinkCodeList, "/"));
                }
                List<String> sinkMaterialCodeList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getSinkMaterialCode).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(sinkMaterialCodeList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setSinkMaterialCode(StringUtils.join(sinkMaterialCodeList, "/"));
                }
                List<String> sinkSupplierLotList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getSinkSupplierLot).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(sinkSupplierLotList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setSinkSupplierLot(StringUtils.join(sinkSupplierLotList, "/"));
                }
                List<String> ausnRatioList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getAusnRatio).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(ausnRatioList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setAusnRatio(StringUtils.join(ausnRatioList, "/"));
                }
                List<String> goldCodeList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getGoldCode).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(goldCodeList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setGoldCode(StringUtils.join(goldCodeList, "/"));
                }
                List<String> goldMaterialCodeList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getGoldMaterialCode).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(goldMaterialCodeList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setGoldMaterialCode(StringUtils.join(goldMaterialCodeList, "/"));
                }
                List<String> goldSupplierLotList = barcodeLoadList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getGoldSupplierLot).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(goldSupplierLotList)){
                    wmsSummaryOfCosBarcodeProcessingVO.setGoldSupplierLot(StringUtils.join(goldSupplierLotList, "/"));
                }
            }

            String resultKey = this.spliceKey(wmsSummaryOfCosBarcodeProcessingVO);
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeProcessingVOList = processingVOMap.get(resultKey);
            if (CollectionUtils.isNotEmpty(barcodeProcessingVOList)) {
                wmsSummaryOfCosBarcodeProcessingVO.setNgQty(barcodeProcessingVOList.get(0).getNgQty());
            } else {
                wmsSummaryOfCosBarcodeProcessingVO.setNgQty(BigDecimal.ZERO);
            }
            wmsSummaryOfCosBarcodeProcessingVO.setOkQty(wmsSummaryOfCosBarcodeProcessingVO.getSnQty().subtract(wmsSummaryOfCosBarcodeProcessingVO.getNgQty()));

            if(CollectionUtils.isNotEmpty(barcodeLabCodeList)){
                List<String> labCode = barcodeLabCodeList.stream().map(WmsSummaryOfCosBarcodeProcessingVO::getLabCode).collect(Collectors.toList());
                wmsSummaryOfCosBarcodeProcessingVO.setLabCode(StringUtils.join(labCode, "/"));
            }
        }
    }

    private String spliceKey(WmsSummaryOfCosBarcodeProcessingVO processingVO) {
        return processingVO.getWorkOrderId() + "#" +
                processingVO.getWaferNum() + "#" +
                processingVO.getMaterialLotId() + "#" +
                processingVO.getProcessId();
    }

}
