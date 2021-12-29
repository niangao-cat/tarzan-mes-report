package com.ruike.hme.infra.repository.impl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.domain.repository.HmeServiceSplitRk05ReportRepository;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
import com.ruike.hme.infra.mapper.HmeServiceSplitRk05ReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 售后在制品盘点-半成品报表资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-03-31 16:18:00
 */
@Component
public class HmeServiceSplitRk05ReportRepositoryImpl implements HmeServiceSplitRk05ReportRepository {

    @Autowired
    private HmeServiceSplitRk05ReportMapper hmeServiceSplitRk05ReportMapper;

    private void setParas(HmeServiceSplitRk05ReportDTO dto){

        List<String> snNumList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getSnNum())) {
            if(!dto.getSnNum().contains(",")){
                snNumList.add(dto.getSnNum());
            }else{
                snNumList = Arrays.asList(dto.getSnNum().split(","));
            }
        }
        dto.setSnNumList(snNumList);

        List<String> materialLotCodeList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            if(!dto.getMaterialLotCode().contains(",")){
                materialLotCodeList.add(dto.getMaterialLotCode());
            }else{
                materialLotCodeList = Arrays.asList(dto.getMaterialLotCode().split(","));
            }
        }
        dto.setMaterialLotCodeList(materialLotCodeList);

        List<String> materialCodeList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getMaterialCode())) {
            if(!dto.getMaterialCode().contains(",")){
                materialCodeList.add(dto.getMaterialCode());
            }else{
                materialCodeList = Arrays.asList(dto.getMaterialCode().split(","));
            }
        }
        dto.setMaterialCodeList(materialCodeList);

        List<String> splitStatusList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getSplitStatus())) {
            if(!dto.getSplitStatus().contains(",")){
                splitStatusList.add(dto.getSplitStatus());
            }else{
                splitStatusList = Arrays.asList(dto.getSplitStatus().split(","));
            }
        }
        dto.setSplitStatusList(splitStatusList);

        List<String> warehouseCodeList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWarehouseCode())) {
            if(!dto.getWarehouseCode().contains(",")){
                warehouseCodeList.add(dto.getWarehouseCode());
            }else{
                warehouseCodeList = Arrays.asList(dto.getWarehouseCode().split(","));
            }
        }
        dto.setWarehouseCodeList(warehouseCodeList);

        List<String> workcellCodeList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWorkcellCode())) {
            if(!dto.getWorkcellCode().contains(",")){
                workcellCodeList.add(dto.getWorkcellCode());
            }else{
                workcellCodeList = Arrays.asList(dto.getWorkcellCode().split(","));
            }
        }
        dto.setWorkcellCodeList(workcellCodeList);

        List<String> workOrderStatusList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWorkOrderStatus())) {
            if(!dto.getWorkOrderStatus().contains(",")){
                workOrderStatusList.add(dto.getWorkOrderStatus());
            }else{
                workOrderStatusList = Arrays.asList(dto.getWorkOrderStatus().split(","));
            }
        }
        dto.setWorkOrderStatusList(workOrderStatusList);

        List<String> materialtLotStatusList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getMaterialtLotStatus())) {
            if(!dto.getMaterialtLotStatus().contains(",")){
                materialtLotStatusList.add(dto.getMaterialtLotStatus());
            }else{
                materialtLotStatusList = Arrays.asList(dto.getMaterialtLotStatus().split(","));
            }
        }
        dto.setMaterialtLotStatusList(materialtLotStatusList);
    }

    @Override
    @ProcessLovValue
    public Page<HmeServiceSplitRk05ReportVO> selectSplitRecordList(Long tenantId, HmeServiceSplitRk05ReportDTO dto, PageRequest pageRequest) {

        setParas(dto);

        Page<HmeServiceSplitRk05ReportVO> resultList = PageHelper.doPage(pageRequest, () -> hmeServiceSplitRk05ReportMapper.selectSplitRecord(tenantId, dto));

        if(CollectionUtils.isNotEmpty(resultList.getContent())){
            List<String> splitRecordIdList = resultList.getContent().stream().map(HmeServiceSplitRk05ReportVO::getSplitRecordId).distinct()
                    .collect(Collectors.toList());
            List<HmeServiceSplitRk05ReportVO> workcellList = hmeServiceSplitRk05ReportMapper.selectSplitWorkcell(tenantId,splitRecordIdList,dto);
            if(CollectionUtils.isNotEmpty(workcellList)) {
                Map<String, List<HmeServiceSplitRk05ReportVO>> workcellMap = workcellList.stream().collect(Collectors.groupingBy(e -> e.getSplitRecordId()));
                for (HmeServiceSplitRk05ReportVO result:resultList.getContent()
                     ) {
                    List<HmeServiceSplitRk05ReportVO> hmeServiceSplitRk05ReportVOList = workcellMap.getOrDefault(result.getSplitRecordId() , new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(hmeServiceSplitRk05ReportVOList)){
                        result.setWorkcellCode(hmeServiceSplitRk05ReportVOList.get(0).getWorkcellCode());
                        result.setWorkcellName(hmeServiceSplitRk05ReportVOList.get(0).getWorkcellName());
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<HmeServiceSplitRk05ReportVO> serviceSplitRk05Export(Long tenantId, HmeServiceSplitRk05ReportDTO dto) {

        setParas(dto);

        return hmeServiceSplitRk05ReportMapper.selectExportSplitRecord(tenantId, dto);
    }
}
