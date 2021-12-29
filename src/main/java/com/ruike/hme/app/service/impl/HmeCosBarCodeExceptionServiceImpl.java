package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import com.ruike.hme.app.service.HmeCosBarCodeExceptionService;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO2;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO3;
import com.ruike.hme.domain.vo.MaterialLotLabCodeVO;
import com.ruike.hme.infra.mapper.HmeCosBarCodeExceptionMapper;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import com.ruike.hme.infra.util.HmeCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:29
 */
@Service
public class HmeCosBarCodeExceptionServiceImpl implements HmeCosBarCodeExceptionService {

    @Autowired
    private HmeCosBarCodeExceptionMapper hmeCosBarCodeExceptionMapper;

    private final HmeLoadJobMapper hmeLoadJobMapper;

    public HmeCosBarCodeExceptionServiceImpl(HmeLoadJobMapper hmeLoadJobMapper) {
        this.hmeLoadJobMapper = hmeLoadJobMapper;
    }

    /**
     *
     * 字符转集合
     * @param dto
     * @author penglin.sui@HAND-CHINA.COM 2021/08/03 15:23
     * @return
     */
    private void strToList(HmeCosBarCodeExceptionDTO dto){
        if(StringUtils.isNotBlank(dto.getWorkOrderNum())){
            dto.setWorkOrderNumList(Arrays.asList(StringUtils.split(dto.getWorkOrderNum(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getMaterialLotCode())){
            dto.setMaterialLotCodeList(Arrays.asList(StringUtils.split(dto.getMaterialLotCode(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getWaferNum())){
            dto.setWaferNumList(Arrays.asList(StringUtils.split(dto.getWaferNum(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getHeatSinkMaterialId())){
            dto.setHeatSinkMaterialIdList(Arrays.asList(StringUtils.split(dto.getHeatSinkMaterialId(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getGoldWireMaterialId())){
            dto.setGoldWireMaterialIdList(Arrays.asList(StringUtils.split(dto.getGoldWireMaterialId(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getWorkcellId())){
            dto.setWorkcellIdList(Arrays.asList(StringUtils.split(dto.getWorkcellId(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getProcessId())){
            dto.setProcessIdList(Arrays.asList(StringUtils.split(dto.getProcessId(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getLineWorkcellId())){
            dto.setLineWorkcellIdList(Arrays.asList(StringUtils.split(dto.getLineWorkcellId(), ",")));
        }

        if(StringUtils.isNotBlank(dto.getProdLineId())){
            dto.setProdLineIdList(Arrays.asList(StringUtils.split(dto.getProdLineId(), ",")));
        }
    }

    /**
     *
     * 获取工位集合
     * @param tenantId
     * @param dto
     * @author penglin.sui@HAND-CHINA.COM 2021/08/03 15:24
     * @return
     */
    private void getWorkcellIdList(Long tenantId, HmeCosBarCodeExceptionDTO dto){
        List<String> workcellIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getProcessIdList())){
            //如果选择工序，则取工序下所有工位
            List<HmeCosBarCodeExceptionVO2> processWorkcellIdList = hmeCosBarCodeExceptionMapper.getWorkcellByProcess(tenantId, dto.getProcessIdList(),new ArrayList<>());
            if(CollectionUtils.isNotEmpty(processWorkcellIdList)){
                workcellIdList = processWorkcellIdList.stream()
                        .map(HmeCosBarCodeExceptionVO2::getWorkcellId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());

            }
        }
        if(CollectionUtils.isNotEmpty(dto.getLineWorkcellIdList())){
            //如果未选工序，则取工段下所有工序下工位
            List<HmeCosBarCodeExceptionVO2> lineWorkcellIdList = hmeCosBarCodeExceptionMapper.getWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellIdList(), workcellIdList);
            if(CollectionUtils.isNotEmpty(lineWorkcellIdList)){
                workcellIdList = lineWorkcellIdList.stream()
                        .map(HmeCosBarCodeExceptionVO2::getWorkcellId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());

            }
        }
        if(CollectionUtils.isNotEmpty(dto.getProdLineIdList())){
            //如果未选工段，取产线下所有工段下所有工序下工位
            List<HmeCosBarCodeExceptionVO2> prodLineWorkcellIdList = hmeCosBarCodeExceptionMapper.getWorkcellByProdLine(tenantId, dto.getProdLineIdList(), workcellIdList);
            if(CollectionUtils.isNotEmpty(prodLineWorkcellIdList)){
                workcellIdList = prodLineWorkcellIdList.stream()
                        .map(HmeCosBarCodeExceptionVO2::getWorkcellId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        if(CollectionUtils.isNotEmpty(workcellIdList)){
            if(CollectionUtils.isNotEmpty(dto.getWorkcellIdList())){
                List<String> workcellIdList2 = new ArrayList<>();
                for (String workcellId : workcellIdList
                     ) {
                    if(dto.getWorkcellIdList().contains(workcellId)){
                        workcellIdList2.add(workcellId);
                    }
                }
                dto.setWorkcellIdList(workcellIdList2);
            }else{
                dto.setWorkcellIdList(workcellIdList);
            }
        }
    }

    @Override
    public Page<HmeCosBarCodeExceptionVO> queryList(Long tenantId, HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest) {

        strToList(dto);

        getWorkcellIdList(tenantId , dto);

//        Page<HmeCosBarCodeExceptionVO> result = PageHelper.doPage(pageRequest, () -> hmeCosBarCodeExceptionMapper.queryList(tenantId, dto));
        List<HmeCosBarCodeExceptionVO> resultList = hmeCosBarCodeExceptionMapper.queryList2(tenantId , dto);
        if(CollectionUtils.isEmpty(resultList)){
            return new Page<>();
        }
        resultList = resultList.stream().distinct().collect(Collectors.toList());
        Page<HmeCosBarCodeExceptionVO> result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        if(CollectionUtils.isNotEmpty(result.getContent())) {
            List<String> jobIdList = result.getContent().stream().map(HmeCosBarCodeExceptionVO::getJobId).distinct().collect(Collectors.toList());

            List<HmeCosBarCodeExceptionVO> equipmentList = hmeCosBarCodeExceptionMapper.queryEquipmentList(tenantId,jobIdList);
            Map<String,String> equipmentMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(equipmentList)){
                equipmentMap = equipmentList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getJobId, HmeCosBarCodeExceptionVO::getAssetEncoding));
            }
            List<String> materialLotIds = result.stream().map(HmeCosBarCodeExceptionVO::getMaterialLotId).distinct().collect(Collectors.toList());
            Map<String, List<MaterialLotLabCodeVO>> labCodeMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                List<MaterialLotLabCodeVO> labCodeList = hmeLoadJobMapper.selectLabCodeList(tenantId, materialLotIds);
                if (CollectionUtils.isNotEmpty(labCodeList)){
                    labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(MaterialLotLabCodeVO::getMaterialLotId));
                }
            }
            List<String> loadSequenceList = result.getContent().stream().map(HmeCosBarCodeExceptionVO::getLoadSequence).distinct().collect(Collectors.toList());
            List<HmeCosBarCodeExceptionVO> ncList = hmeCosBarCodeExceptionMapper.queryNc(tenantId , loadSequenceList);
            Map<String , HmeCosBarCodeExceptionVO> ncMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(ncList)){
                ncMap = ncList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getLoadSequence, t -> t));
            }

            List<String> workcellIdList = result.getContent().stream().map(HmeCosBarCodeExceptionVO::getWorkcellId).distinct().collect(Collectors.toList());
            List<HmeCosBarCodeExceptionVO2> wkcRelList = hmeCosBarCodeExceptionMapper.queryWkcRel(tenantId , workcellIdList);
            Map<String , HmeCosBarCodeExceptionVO2> wkcRelMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(wkcRelList)){
                wkcRelMap = wkcRelList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO2::getWorkcellId, t -> t));
            }

            for (HmeCosBarCodeExceptionVO vo : result.getContent()) {
                if (StringUtils.isNotBlank(vo.getMaterialLotCode())) {
                    vo.setHeatSinkType(vo.getMaterialLotCode().substring(7, 8));
                }
                vo.setAssetEncoding(equipmentMap.getOrDefault(vo.getJobId() , ""));
                //位置行号替换为字母
                if (StringUtils.isNotBlank(vo.getLocation())) {
                    String[] split = vo.getLocation().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    vo.setLocation((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }

                HmeCosBarCodeExceptionVO cosBarCodeExceptionVO = ncMap.getOrDefault(vo.getLoadSequence() , null);
                if(Objects.nonNull(cosBarCodeExceptionVO)){
                    vo.setDefectCountSum(cosBarCodeExceptionVO.getDefectCountSum());
                    vo.setDescription(cosBarCodeExceptionVO.getDescription());
                    vo.setCreationDate(cosBarCodeExceptionVO.getCreationDate());
                }

                HmeCosBarCodeExceptionVO2 cosBarCodeExceptionVO2 = wkcRelMap.getOrDefault(vo.getWorkcellId() , null);
                if(Objects.nonNull(cosBarCodeExceptionVO2)){
                    vo.setProcessName(cosBarCodeExceptionVO2.getProcessName());
                    vo.setLineWorkcellName(cosBarCodeExceptionVO2.getLineName());
                    vo.setProdLineName(cosBarCodeExceptionVO2.getProdLineName());
                }

                //实验代码
                vo.setLabCode(labCodeMap.getOrDefault(vo.getMaterialLotId(), new ArrayList<>()).stream().map(MaterialLotLabCodeVO::getLabCode).collect(Collectors.joining("/")));
            }
        }
        return result;
    }

    @Override
    public List<HmeCosBarCodeExceptionVO> listForExport(Long tenantId, HmeCosBarCodeExceptionDTO dto) {

        strToList(dto);

        getWorkcellIdList(tenantId , dto);

//        List<HmeCosBarCodeExceptionVO> hmeCosBarCodeExceptionVOList = hmeCosBarCodeExceptionMapper.queryList(tenantId, dto);
        List<HmeCosBarCodeExceptionVO> hmeCosBarCodeExceptionVOList = hmeCosBarCodeExceptionMapper.queryList2(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosBarCodeExceptionVOList)){
            return new Page<>();
        }
        hmeCosBarCodeExceptionVOList = hmeCosBarCodeExceptionVOList.stream().distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(hmeCosBarCodeExceptionVOList)) {
            List<String> jobIdList = hmeCosBarCodeExceptionVOList.stream().map(HmeCosBarCodeExceptionVO::getJobId).distinct().collect(Collectors.toList());

            List<List<String>> splitJobIdList = Utils.splitSqlList(jobIdList,3000);
            List<HmeCosBarCodeExceptionVO> equipmentList = new ArrayList<>();
            for (List<String> subJobIdList : splitJobIdList
                 ) {
                List<HmeCosBarCodeExceptionVO> subEquipmentList = hmeCosBarCodeExceptionMapper.queryEquipmentList(tenantId,subJobIdList);
                if(CollectionUtils.isNotEmpty(subEquipmentList)){
                    equipmentList.addAll(subEquipmentList);
                }
            }
            List<String> materialLotIds = hmeCosBarCodeExceptionVOList.stream().map(HmeCosBarCodeExceptionVO::getMaterialLotId).distinct().collect(Collectors.toList());
            Map<String, List<MaterialLotLabCodeVO>> labCodeMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                List<MaterialLotLabCodeVO> labCodeList = hmeLoadJobMapper.selectLabCodeList(tenantId, materialLotIds);
                if (CollectionUtils.isNotEmpty(labCodeList)){
                    labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(MaterialLotLabCodeVO::getMaterialLotId));
                }
            }
            Map<String,String> equipmentMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(equipmentList)){
                equipmentMap = equipmentList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getJobId, HmeCosBarCodeExceptionVO::getAssetEncoding));
            }

            List<String> loadSequenceList = hmeCosBarCodeExceptionVOList.stream().map(HmeCosBarCodeExceptionVO::getLoadSequence).distinct().collect(Collectors.toList());
            List<List<String>> splitLoadSequenceList = Utils.splitSqlList(loadSequenceList,3000);
            List<HmeCosBarCodeExceptionVO> ncList = new ArrayList<>();
            for (List<String> subSplitLoadSequenceList : splitLoadSequenceList
            ) {
                List<HmeCosBarCodeExceptionVO> subNcList = hmeCosBarCodeExceptionMapper.queryNc(tenantId , subSplitLoadSequenceList);
                if(CollectionUtils.isNotEmpty(subNcList)){
                    ncList.addAll(subNcList);
                }
            }

            Map<String , HmeCosBarCodeExceptionVO> ncMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(ncList)){
                ncMap = ncList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getLoadSequence, t -> t));
            }

            List<String> workcellIdList = hmeCosBarCodeExceptionVOList.stream().map(HmeCosBarCodeExceptionVO::getWorkcellId).distinct().collect(Collectors.toList());
            List<List<String>> splitWorkcellIdList = Utils.splitSqlList(workcellIdList,3000);
            List<HmeCosBarCodeExceptionVO2> wkcRelList = new ArrayList<>();
            for (List<String> subWorkcellIdList : splitWorkcellIdList
            ) {
                List<HmeCosBarCodeExceptionVO2> subWkcRelList = hmeCosBarCodeExceptionMapper.queryWkcRel(tenantId , subWorkcellIdList);
                if(CollectionUtils.isNotEmpty(subWkcRelList)){
                    wkcRelList.addAll(subWkcRelList);
                }
            }
            Map<String , HmeCosBarCodeExceptionVO2> wkcRelMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(wkcRelList)){
                wkcRelMap = wkcRelList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO2::getWorkcellId, t -> t));
            }

            for (HmeCosBarCodeExceptionVO vo : hmeCosBarCodeExceptionVOList) {
                if (StringUtils.isNotBlank(vo.getMaterialLotCode())) {
                    vo.setHeatSinkType(vo.getMaterialLotCode().substring(7, 8));
                }
                vo.setAssetEncoding(equipmentMap.getOrDefault(vo.getJobId() , ""));
                //位置行号替换为字母
                if (StringUtils.isNotBlank(vo.getLocation())) {
                    String[] split = vo.getLocation().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    vo.setLocation((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }

                HmeCosBarCodeExceptionVO cosBarCodeExceptionVO = ncMap.getOrDefault(vo.getLoadSequence() , null);
                if(Objects.nonNull(cosBarCodeExceptionVO)){
                    vo.setDefectCountSum(cosBarCodeExceptionVO.getDefectCountSum());
                    vo.setDescription(cosBarCodeExceptionVO.getDescription());
                    vo.setCreationDate(cosBarCodeExceptionVO.getCreationDate());
                }

                HmeCosBarCodeExceptionVO2 cosBarCodeExceptionVO2 = wkcRelMap.getOrDefault(vo.getWorkcellId() , null);
                if(Objects.nonNull(cosBarCodeExceptionVO2)){
                    vo.setProcessName(cosBarCodeExceptionVO2.getProcessName());
                    vo.setLineWorkcellName(cosBarCodeExceptionVO2.getLineName());
                    vo.setProdLineName(cosBarCodeExceptionVO2.getProdLineName());
                }

                //实验代码
                vo.setLabCode(labCodeMap.getOrDefault(vo.getMaterialLotId(), new ArrayList<>()).stream().map(MaterialLotLabCodeVO::getLabCode).collect(Collectors.joining("/")));
            }
        }
        return hmeCosBarCodeExceptionVOList;
    }
}
