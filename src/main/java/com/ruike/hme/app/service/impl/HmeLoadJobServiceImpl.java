package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.query.HmeLoadJobQuery;
import com.ruike.hme.api.dto.representation.HmeLoadJobRept;
import com.ruike.hme.app.service.HmeLoadJobService;
import com.ruike.hme.domain.vo.HmeTagCheckVO3;
import com.ruike.hme.domain.vo.LoadJobEquipmentVO;
import com.ruike.hme.domain.vo.LoadJobNcVO;
import com.ruike.hme.domain.vo.MaterialLotLabCodeVO;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import com.ruike.hme.infra.mapper.HmeProductionFlowMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.StringCommonUtils;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 芯片装载作业服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 18:58
 */
@Service
public class HmeLoadJobServiceImpl implements HmeLoadJobService {

    private final HmeLoadJobMapper hmeLoadJobMapper;
    private final HmeProductionFlowMapper hmeProductionFlowMapper;

    public HmeLoadJobServiceImpl(HmeLoadJobMapper hmeLoadJobMapper, HmeProductionFlowMapper hmeProductionFlowMapper) {
        this.hmeLoadJobMapper = hmeLoadJobMapper;
        this.hmeProductionFlowMapper = hmeProductionFlowMapper;
    }

    @Override
    @ProcessLovValue
    public Page<HmeLoadJobRept> pageList(Long tenantId, HmeLoadJobQuery dto, PageRequest pageRequest) {
        //将工段、工序等查询条件转换到工位上
        List<String> workcellStationIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getWorkcellStationIdList())){
            workcellStationIdList = dto.getWorkcellStationIdList();
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellProcessIdList())){
            //根据工序查询工位
            workcellStationIdList = hmeProductionFlowMapper.workcellByProcessBatchQuery(tenantId, dto.getWorkcellProcessIdList());
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellLineIdList())){
            //根据工段查询工位
            workcellStationIdList = hmeProductionFlowMapper.workcellByLineWorkcellBatchQuery(tenantId, dto.getWorkcellLineIdList());
        }
        dto.setWorkcellStationIdList(workcellStationIdList);
        Page<HmeLoadJobRept> result = PageHelper.doPage(pageRequest, () -> hmeLoadJobMapper.selectList(tenantId, dto));
        displayFieldsCompletion(tenantId, result.getContent());
        return result;
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeLoadJobRept> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 查询需要拼接的数据
        List<String> loadJobIds = list.stream().map(HmeLoadJobRept::getLoadJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialLotIds = list.stream().map(HmeLoadJobRept::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<LoadJobNcVO> ncList = new ArrayList<>();
        List<LoadJobEquipmentVO> equipmentList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(loadJobIds)){
            List<List<String>> splitList = CommonUtils.splitSqlList(loadJobIds, 5000);
            for (List<String> split:splitList) {
                ncList.addAll(hmeLoadJobMapper.selectNcList(tenantId, split));
                equipmentList.addAll(hmeLoadJobMapper.selectEquipmentList(tenantId, split));
            }
        }
        List<MaterialLotLabCodeVO> labCodeList = new ArrayList<>();
        // 根据条码加loadSequence 去重
        List<HmeLoadJobRept> distinctList = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(obj -> obj.getMaterialLotCode() + "," + obj.getLoadSequence()))), ArrayList::new));
        if(CollectionUtils.isNotEmpty(distinctList)){
            List<List<HmeLoadJobRept>> splitList = CommonUtils.splitSqlList(distinctList, 3000);
            for (List<HmeLoadJobRept> split:splitList) {
                labCodeList.addAll(hmeLoadJobMapper.selectLabCodeListByMaterialLotIdAndLoadSequence(tenantId, split));
            }
        }
        //查询工序、工段信息
        List<String> workcellIdList = list.stream().map(HmeLoadJobRept::getWorkcellId).distinct().collect(Collectors.toList());
        List<WmsSummaryOfCosBarcodeProcessingVO2> processLineProdInfoList = hmeLoadJobMapper.qeuryProcessLineByWorkcell(tenantId, workcellIdList);
        Map<String, List<WmsSummaryOfCosBarcodeProcessingVO2>> processLineInfoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(processLineProdInfoList)){
            processLineInfoMap = processLineProdInfoList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO2::getWorkcellId));
        }
        Map<String, List<LoadJobNcVO>> ncMap = ncList.stream().collect(Collectors.groupingBy(LoadJobNcVO::getLoadJobId));
        Map<String, List<LoadJobEquipmentVO>> equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(LoadJobEquipmentVO::getLoadJobId));
        Map<String, List<MaterialLotLabCodeVO>> labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(lab -> lab.getMaterialLotId() + "_" + lab.getLoadSequence()));
        for (HmeLoadJobRept record : list) {
            if (Objects.nonNull(record.getLoadRow()) && Objects.nonNull(record.getLoadColumn())) {
                record.setPosition(StringCommonUtils.numberToUpperLetter(record.getLoadRow()) + record.getLoadColumn());
            }
            if (Objects.nonNull(record.getSourceLoadRow()) && Objects.nonNull(record.getSourceLoadColumn())) {
                record.setSourcePosition(StringCommonUtils.numberToUpperLetter(record.getSourceLoadRow()) + record.getSourceLoadColumn());
            }
            record.setNcCode(ncMap.getOrDefault(record.getLoadJobId(), new ArrayList<>()).stream().map(LoadJobNcVO::getNcCode).collect(Collectors.joining("/")));
            record.setNcCodeDescription(ncMap.getOrDefault(record.getLoadJobId(), new ArrayList<>()).stream().map(LoadJobNcVO::getNcCodeDescription).collect(Collectors.joining("/")));
            record.setEquipment(equipmentMap.getOrDefault(record.getLoadJobId(), new ArrayList<>()).stream().map(LoadJobEquipmentVO::getAssetEncoding).collect(Collectors.joining("/")));
            record.setLabCode(labCodeMap.getOrDefault(record.getMaterialLotId() + "_" + record.getLoadSequence() , new ArrayList<>()).stream().map(MaterialLotLabCodeVO::getLabCode).collect(Collectors.joining("/")));
            List<WmsSummaryOfCosBarcodeProcessingVO2> singleProcessLineProdInfo = processLineInfoMap.get(record.getWorkcellId());
            if(CollectionUtils.isNotEmpty(singleProcessLineProdInfo)){
                record.setWorkcellProcessName(singleProcessLineProdInfo.get(0).getProcessName());
                record.setWorkcellLineName(singleProcessLineProdInfo.get(0).getLineWorkcellName());
            }
        }
    }

    @Override
    @ProcessLovValue
    public List<HmeLoadJobRept> export(Long tenantId, HmeLoadJobQuery dto) {
        //将工段、工序等查询条件转换到工位上
        List<String> workcellStationIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getWorkcellStationIdList())){
            workcellStationIdList = dto.getWorkcellStationIdList();
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellProcessIdList())){
            //根据工序查询工位
            workcellStationIdList = hmeProductionFlowMapper.workcellByProcessBatchQuery(tenantId, dto.getWorkcellProcessIdList());
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellLineIdList())){
            //根据工段查询工位
            workcellStationIdList = hmeProductionFlowMapper.workcellByLineWorkcellBatchQuery(tenantId, dto.getWorkcellLineIdList());
        }
        dto.setWorkcellStationIdList(workcellStationIdList);
        List<HmeLoadJobRept> list = hmeLoadJobMapper.selectList(tenantId, dto);
        displayFieldsCompletion(tenantId, list);
        return list;
    }
}
