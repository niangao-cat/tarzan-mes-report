package com.ruike.hme.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.beust.jcommander.internal.Lists;
import com.ruike.common.app.service.GetMesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFilePara;
import com.ruike.common.infra.constant.Constant;
import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.app.assembler.HmeCosFunctionMergeStrategy;
import com.ruike.hme.app.service.HmeCosFunctionService;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCosFunctionMapper;
import com.ruike.hme.infra.util.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import utils.BeanCopierUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 芯片性能表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
@Service
@Slf4j
public class HmeCosFunctionServiceImpl implements HmeCosFunctionService {

    @Autowired
    private HmeCosFunctionMapper hmeCosFunctionMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtUserClient mtUserClient;

    @Autowired
    private GetMesReportFileInfo getMesReportFileInfo;

    private static final String FILE_NAME = "COS测试明细报表";

    private static final String PRE_TABLE_NAME = "hme_cos_function";

    private static final String PRE_TABLE_NAME_2 = "hme_cos_function_current";

    private void stringToList(Long tenantId,HmeCosFunctionDTO2 dto){
        //将工段、工序等查询条件转换到工位身上
        List<String> workcellIdQueryList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWorkcellId())){
            workcellIdQueryList = Arrays.asList(dto.getWorkcellId().split(","));
        }else if(StringUtils.isNotBlank(dto.getProcessId())){
            workcellIdQueryList = hmeCosFunctionMapper.getWorkcellByProcess(tenantId, dto.getProcessId());
//            workcellIdQueryList = hmeCosFunctionMapper.getWorkcellByProcess2(tenantId, Arrays.asList(dto.getProcessId().split(",")));
        }else if(StringUtils.isNotBlank(dto.getLineWorkcellId())){
            workcellIdQueryList = hmeCosFunctionMapper.getWorkcellByLineWorkcell(tenantId, dto.getLineWorkcellId());
//            workcellIdQueryList = hmeCosFunctionMapper.getWorkcellByLineWorkcell2(tenantId, Arrays.asList(dto.getLineWorkcellId().split(",")));
        }
        dto.setWorkcellIdList(workcellIdQueryList);

        if(StringUtils.isNotBlank(dto.getWorkOrderNum())){
            dto.setWorkOrderNumList(Arrays.asList(dto.getWorkOrderNum().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getMaterialId())){
            dto.setMaterialIdList(Arrays.asList(dto.getMaterialId().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getPreStatus())){
            dto.setPreStatusList(Arrays.asList(dto.getPreStatus().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getWafer())){
            dto.setWaferList(Arrays.asList(dto.getWafer().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getCosType())){
            dto.setCosTypeList(Arrays.asList(dto.getCosType().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getMaterialLotCode())){
            dto.setMaterialLotCodeList(Arrays.asList(dto.getMaterialLotCode().split(",")));

            //查询条码ID
            List<String> materialLotIdLits = hmeCosFunctionMapper.selectMaterialLot(tenantId , dto.getMaterialLotCodeList());
            dto.setMaterialLotIdList(materialLotIdLits);
        }

        if(StringUtils.isNotBlank(dto.getHotSinkCode())){
            dto.setHotSinkCodeList(Arrays.asList(dto.getHotSinkCode().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getHotSinkCode())){
            dto.setHotSinkCodeList(Arrays.asList(dto.getHotSinkCode().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getLabCode())){
            dto.setLabCodeList(Arrays.asList(dto.getLabCode().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getHeatSinkMaterialLot())){
            dto.setHeatSinkMaterialLotList(Arrays.asList(dto.getHeatSinkMaterialLot().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getHeatSinkMaterialId())){
            dto.setHeatSinkMaterialIdList(Arrays.asList(dto.getHeatSinkMaterialId().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getHeatSinkSupplierLot())){
            dto.setHeatSinkSupplierLotList(Arrays.asList(dto.getHeatSinkSupplierLot().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getGoldWireMaterialLot())){
            dto.setGoldWireMaterialLotList(Arrays.asList(dto.getGoldWireMaterialLot().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getGoldWireMaterialId())){
            dto.setGoldWireMaterialIdList(Arrays.asList(dto.getGoldWireMaterialId().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getGoldWireSupplierLot())){
            dto.setGoldWireSupplierLotList(Arrays.asList(dto.getGoldWireSupplierLot().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getWarehouseId())){
            dto.setWarehouseIdList(Arrays.asList(dto.getWarehouseId().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getLocatorId())){
            dto.setLocatorIdList(Arrays.asList(dto.getLocatorId().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getNcCode())){
            dto.setNcCodeList(Arrays.asList(dto.getNcCode().split(",")));
        }

        if(StringUtils.isNotBlank(dto.getCurrent())){
            dto.setCurrentList(Arrays.asList(dto.getCurrent().split(",")));
        }
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosFunctionVO2> cosFunctionReport(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest) {
        stringToList(tenantId , dto);

//        Page<HmeCosFunctionVO2> resultPage = PageHelper.doPage(pageRequest, ()->hmeCosFunctionMapper.cosFunctionReport(tenantId, dto));
//        Page<HmeCosFunctionVO2> resultPage = PageHelper.doPage(pageRequest, ()->hmeCosFunctionMapper.cosFunctionReport2(tenantId, dto));
        Page<HmeCosFunctionVO2> resultPage = new Page<>();
        List<HmeCosFunctionVO2> resultList = new ArrayList<>();
        List<HmeCosFunctionVO2> hmeCosFunctionVO2List = hmeCosFunctionMapper.cosFunctionReport(tenantId , dto);
        if(CollectionUtils.isNotEmpty(hmeCosFunctionVO2List)){

            List<String> loadSequenceList = hmeCosFunctionVO2List.stream()
                    .map(HmeCosFunctionVO2::getLoadSequence)
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(loadSequenceList)) {
                List<List<String>> splitLoadSequenceList = WmsCommonUtils.splitSqlList(loadSequenceList, 3000);
                for (List<String> subLoadSequenceList : splitLoadSequenceList
                     ) {
                    dto.setLoadSequenceList(subLoadSequenceList);
                    List<HmeCosFunctionVO9> hmeCosFunctionVO2List2 = hmeCosFunctionMapper.cosFunctionReport3(tenantId, dto);
                    if(CollectionUtils.isNotEmpty(hmeCosFunctionVO2List2)){
                        Map<String , List<HmeCosFunctionVO9>> hmeCosFunctionMap =
                                hmeCosFunctionVO2List2.stream().collect(Collectors.groupingBy(HmeCosFunctionVO9::getLoadSequence));
                        for (HmeCosFunctionVO2 hmeCosFunctionVO2 : hmeCosFunctionVO2List
                             ) {
                            if(hmeCosFunctionMap.containsKey(hmeCosFunctionVO2.getLoadSequence())){
                                List<HmeCosFunctionVO9> hmeCosFunctionVO2List3 = hmeCosFunctionMap.get(hmeCosFunctionVO2.getLoadSequence());
                                for (HmeCosFunctionVO9 hmeCosFunctionVO3 : hmeCosFunctionVO2List3
                                     ) {
                                    HmeCosFunctionVO2 resultVO = new HmeCosFunctionVO2();
                                    BeanCopierUtil.copy(hmeCosFunctionVO2 , resultVO);
                                    BeanCopierUtil.copy(hmeCosFunctionVO3 , resultVO);
                                    resultList.add(resultVO);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(resultList)){
            resultPage = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        }
        if(CollectionUtils.isNotEmpty(resultPage)){
            List<String> materialLotIdList = resultPage.getContent().stream().map(HmeCosFunctionVO2::getMaterialLotId).distinct().collect(Collectors.toList());
            List<HmeCosFunctionVO3> labCodeList = new ArrayList<>();
            Map<String, String> materialLotWorkcellIdMap = new HashMap<>();
            List<HmeCosFunctionVO4> workcellInfoList = new ArrayList<>();
            List<String> workcellIdList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(materialLotIdList)){
                List<List<String>> splitMaterialLotIdList = WmsCommonUtils.splitSqlList(materialLotIdList, 900);
                for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                    //根据条码批量查询实验代码
                    List<HmeCosFunctionVO3> labCode = hmeCosFunctionMapper.getLabCodeByMaterialLot(tenantId, splitMaterialLotId);
                    labCodeList.addAll(labCode);
                    //根据条码批量查询在制工位,只有当没有工位查询条件时这样做
                    if(CollectionUtil.isEmpty(dto.getWorkcellIdList())){
                        List<HmeCosFunctionVO5> workcell = hmeCosFunctionMapper.getWorkcellByMaterialLot(tenantId, splitMaterialLotId);
                        Map<String, List<HmeCosFunctionVO5>> materialLotWorkcellMap = workcell.stream().collect(Collectors.groupingBy(HmeCosFunctionVO5::getMaterialLotId));
                        for (Map.Entry<String, List<HmeCosFunctionVO5>> entry:materialLotWorkcellMap.entrySet()) {
                            List<HmeCosFunctionVO5> hmeCosFunctionVO5List = entry.getValue().stream().sorted(Comparator.comparing(HmeCosFunctionVO5::getCreationDate).reversed()).collect(Collectors.toList());
                            workcellIdList.add(hmeCosFunctionVO5List.get(0).getWorkcellId());
                            materialLotWorkcellIdMap.put(entry.getKey(), hmeCosFunctionVO5List.get(0).getWorkcellId());
                        }
                    }else{
                        //否则，当有工位查询条件时，条码及其在制工位对应关系已查出，只需再查询工位的工序、工段信息即可
                        workcellIdList.addAll(resultPage.getContent().stream().map(HmeCosFunctionVO2::getWorkcellId).distinct().collect(Collectors.toList()));
                    }
                }
            }
            if(CollectionUtil.isNotEmpty(workcellIdList)){
                workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
                List<List<String>> splitWorkcellIdList = WmsCommonUtils.splitSqlList(workcellIdList, 900);
                //根据在制工位批量查询工位、工序、工段等信息
                for (List<String> splitWorkcellId:splitWorkcellIdList) {
                    List<HmeCosFunctionVO4> hmeCosFunctionVO4s = hmeCosFunctionMapper.workcellInfoQuery(tenantId, splitWorkcellId);
                    workcellInfoList.addAll(hmeCosFunctionVO4s);
                }
            }
            for (HmeCosFunctionVO2 hmeCosFunctionVO2:resultPage.getContent()) {
                //取出条码对应的实验代码
                List<HmeCosFunctionVO3> hmeCosFunctionVO3List = labCodeList.stream().filter(item -> hmeCosFunctionVO2.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(hmeCosFunctionVO3List)){
                    hmeCosFunctionVO2.setLabCode(hmeCosFunctionVO3List.get(0).getLabCode());
                }
                if(CollectionUtil.isEmpty(dto.getWorkcellIdList())){
                    //当工位查询条件为空时，条码的在制工位需从map中取
                    String workcellId = materialLotWorkcellIdMap.get(hmeCosFunctionVO2.getMaterialLotId());
                    if(StringUtils.isNotBlank(workcellId)){
                        hmeCosFunctionVO2.setWorkcellId(workcellId);
                    }
                }
                if(StringUtils.isNotBlank(hmeCosFunctionVO2.getWorkcellId())){
                    List<HmeCosFunctionVO4> hmeCosFunctionVO4List = workcellInfoList.stream().filter(item -> hmeCosFunctionVO2.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(hmeCosFunctionVO4List)){
                        hmeCosFunctionVO2.setWorkcellCode(hmeCosFunctionVO4List.get(0).getWorkcellCode());
                        hmeCosFunctionVO2.setWorkcellName(hmeCosFunctionVO4List.get(0).getWorkcellName());
                        hmeCosFunctionVO2.setProcessId(hmeCosFunctionVO4List.get(0).getProcessId());
                        hmeCosFunctionVO2.setProcessCode(hmeCosFunctionVO4List.get(0).getProcessCode());
                        hmeCosFunctionVO2.setProcessName(hmeCosFunctionVO4List.get(0).getProcessName());
                        hmeCosFunctionVO2.setLineWorkcellId(hmeCosFunctionVO4List.get(0).getLineWorkcellId());
                        hmeCosFunctionVO2.setLineWorkcellCode(hmeCosFunctionVO4List.get(0).getLineWorkcellCode());
                        hmeCosFunctionVO2.setLineWorkcellName(hmeCosFunctionVO4List.get(0).getLineWorkcellName());
                    }
                }
                //旧位置行号替换为字母
                if (StringUtils.isNotBlank(hmeCosFunctionVO2.getRowCloumn())) {
                    String[] split = hmeCosFunctionVO2.getRowCloumn().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    hmeCosFunctionVO2.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }
            }
        }
        return resultPage;
    }

    @Override
    public HmeCosFunctionVO7 cosFunctionListQuery(Long tenantId, HmeCosFunctionDTO2 dto) {
        HmeCosFunctionVO7 hmeCosFunctionVO7 = new HmeCosFunctionVO7();
        stringToList(tenantId , dto);
        List<HmeCosFunctionVO2> resultList = new ArrayList<>();

        List<HmeCosFunctionVO2> hmeCosFunctionVO2List = hmeCosFunctionMapper.cosFunctionReport(tenantId , dto);
        if(CollectionUtils.isNotEmpty(hmeCosFunctionVO2List)){

            List<String> loadSequenceList = hmeCosFunctionVO2List.stream()
                    .map(HmeCosFunctionVO2::getLoadSequence)
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(loadSequenceList)) {
                List<List<String>> splitLoadSequenceList = WmsCommonUtils.splitSqlList(loadSequenceList, 3000);
                for (List<String> subLoadSequenceList : splitLoadSequenceList
                ) {
                    dto.setLoadSequenceList(subLoadSequenceList);
                    List<HmeCosFunctionVO9> hmeCosFunctionVO2List2 = hmeCosFunctionMapper.cosFunctionReport3(tenantId, dto);
                    if(CollectionUtils.isNotEmpty(hmeCosFunctionVO2List2)){
                        Map<String , List<HmeCosFunctionVO9>> hmeCosFunctionMap =
                                hmeCosFunctionVO2List2.stream().collect(Collectors.groupingBy(HmeCosFunctionVO9::getLoadSequence));
                        for (HmeCosFunctionVO2 hmeCosFunctionVO2 : hmeCosFunctionVO2List
                        ) {
                            if(hmeCosFunctionMap.containsKey(hmeCosFunctionVO2.getLoadSequence())){
                                List<HmeCosFunctionVO9> hmeCosFunctionVO2List3 = hmeCosFunctionMap.get(hmeCosFunctionVO2.getLoadSequence());
                                for (HmeCosFunctionVO9 hmeCosFunctionVO3 : hmeCosFunctionVO2List3
                                ) {
                                    HmeCosFunctionVO2 resultVO = new HmeCosFunctionVO2();
                                    BeanCopierUtil.copy(hmeCosFunctionVO2 , resultVO);
                                    BeanCopierUtil.copy(hmeCosFunctionVO3 , resultVO);
                                    resultList.add(resultVO);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(CollectionUtils.isNotEmpty(resultList)){
            //查询值集
            List<LovValueDTO> ynLov = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> preStatusLov = lovAdapter.queryLovValue("HME.SELECT_STATUS", tenantId);

            List<String> materialLotIdList = resultList.stream().map(HmeCosFunctionVO2::getMaterialLotId).distinct().collect(Collectors.toList());
            List<HmeCosFunctionVO3> labCodeList = new ArrayList<>();
            Map<String, String> materialLotWorkcellIdMap = new HashMap<>();
            List<HmeCosFunctionVO4> workcellInfoList = new ArrayList<>();
            List<String> workcellIdList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(materialLotIdList)){
                List<List<String>> splitMaterialLotIdList = WmsCommonUtils.splitSqlList(materialLotIdList, 900);
                for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                    //根据条码批量查询实验代码
                    List<HmeCosFunctionVO3> labCode = hmeCosFunctionMapper.getLabCodeByMaterialLot(tenantId, splitMaterialLotId);
                    labCodeList.addAll(labCode);
                    //根据条码批量查询在制工位,只有当没有工位查询条件时这样做
                    if(CollectionUtil.isEmpty(dto.getWorkcellIdList())){
                        List<HmeCosFunctionVO5> workcell = hmeCosFunctionMapper.getWorkcellByMaterialLot(tenantId, splitMaterialLotId);
                        Map<String, List<HmeCosFunctionVO5>> materialLotWorkcellMap = workcell.stream().collect(Collectors.groupingBy(HmeCosFunctionVO5::getMaterialLotId));
                        for (Map.Entry<String, List<HmeCosFunctionVO5>> entry:materialLotWorkcellMap.entrySet()) {
                            List<HmeCosFunctionVO5> hmeCosFunctionVO5List = entry.getValue().stream().sorted(Comparator.comparing(HmeCosFunctionVO5::getCreationDate).reversed()).collect(Collectors.toList());
                            workcellIdList.add(hmeCosFunctionVO5List.get(0).getWorkcellId());
                            materialLotWorkcellIdMap.put(entry.getKey(), hmeCosFunctionVO5List.get(0).getWorkcellId());
                        }
                    }else{
                        //否则，当有工位查询条件时，条码及其在制工位对应关系已查出，只需再查询工位的工序、工段信息即可
                        workcellIdList.addAll(resultList.stream().map(HmeCosFunctionVO2::getWorkcellId).distinct().collect(Collectors.toList()));
                    }
                }
            }
            if(CollectionUtil.isNotEmpty(workcellIdList)){
                workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
                List<List<String>> splitWorkcellIdList = WmsCommonUtils.splitSqlList(workcellIdList, 900);
                //根据在制工位批量查询工位、工序、工段等信息
                for (List<String> splitWorkcellId:splitWorkcellIdList) {
                    List<HmeCosFunctionVO4> hmeCosFunctionVO4s = hmeCosFunctionMapper.workcellInfoQuery(tenantId, splitWorkcellId);
                    workcellInfoList.addAll(hmeCosFunctionVO4s);
                }
            }

            //确定动态列标题，结果中有多少个不同的电流值，就有多少组动态列
            List<Integer> currentList = resultList.stream().map(HmeCosFunctionVO2::currentToNumber).distinct().collect(Collectors.toList());
            currentList = currentList.stream().sorted().collect(Collectors.toList());
            List<String> title = new ArrayList<>();
            for (Integer current:currentList) {
                title.add(current + "_电压");
                title.add(current + "_功率");
                title.add(current + "_中心波长");
                title.add(current + "_SE");
                title.add(current + "_线宽");
                title.add(current + "_WPE");
            }
            hmeCosFunctionVO7.setTitle(title);
            //根据条码+位置分组，同一分组下的数据合并为一条数据
            Map<String, List<HmeCosFunctionVO2>> map = resultList.stream().collect(Collectors.groupingBy(t -> {
                return t.getMaterialLotId() + "," + t.getRowCloumn();
            }));
            List<HmeCosFunctionVO6> hmeCosFunctionVO6List = new ArrayList<>();
            for (Map.Entry<String, List<HmeCosFunctionVO2>> entry:map.entrySet()) {
                HmeCosFunctionVO6 hmeCosFunctionVO6 = new HmeCosFunctionVO6();
                BeanCopierUtil.copy(entry.getValue().get(0), hmeCosFunctionVO6);
                //是否在制
                if(StringUtils.isNotBlank(hmeCosFunctionVO6.getMfFlag())){
                    List<LovValueDTO> mfFlagMeaning = ynLov.stream().filter(item -> hmeCosFunctionVO6.getMfFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(mfFlagMeaning)){
                        hmeCosFunctionVO6.setMfFlagMeaning(mfFlagMeaning.get(0).getMeaning());
                    }
                }
                //筛选状态
                if(StringUtils.isNotBlank(hmeCosFunctionVO6.getPreStatus())){
                    List<LovValueDTO> preStatusMeaning = preStatusLov.stream().filter(item -> hmeCosFunctionVO6.getPreStatus().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(preStatusMeaning)){
                        hmeCosFunctionVO6.setPreStatusMeaning(preStatusMeaning.get(0).getMeaning());
                    }
                }
                //是否不良
                if(StringUtils.isNotBlank(hmeCosFunctionVO6.getNcFlag())){
                    List<LovValueDTO> ncFlagMeaning = ynLov.stream().filter(item -> hmeCosFunctionVO6.getNcFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(ncFlagMeaning)){
                        hmeCosFunctionVO6.setNcFlagMeaning(ncFlagMeaning.get(0).getMeaning());
                    }
                }
                //是否冻结
                if(StringUtils.isNotBlank(hmeCosFunctionVO6.getFreezeFlag())){
                    List<LovValueDTO> freezeFlagMeaning = ynLov.stream().filter(item -> hmeCosFunctionVO6.getFreezeFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(freezeFlagMeaning)){
                        hmeCosFunctionVO6.setFreezeFlagMeaning(freezeFlagMeaning.get(0).getMeaning());
                    }
                }
                //取出条码对应的实验代码
                List<HmeCosFunctionVO3> hmeCosFunctionVO3List = labCodeList.stream().filter(item -> hmeCosFunctionVO6.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(hmeCosFunctionVO3List)){
                    hmeCosFunctionVO6.setLabCode(hmeCosFunctionVO3List.get(0).getLabCode());
                }
                if(CollectionUtil.isEmpty(dto.getWorkcellIdList())){
                    //当工位查询条件为空时，条码的在制工位需从map中取
                    String workcellId = materialLotWorkcellIdMap.get(hmeCosFunctionVO6.getMaterialLotId());
                    if(StringUtils.isNotBlank(workcellId)){
                        hmeCosFunctionVO6.setWorkcellId(workcellId);
                    }
                }
                if(StringUtils.isNotBlank(hmeCosFunctionVO6.getWorkcellId())){
                    List<HmeCosFunctionVO4> hmeCosFunctionVO4List = workcellInfoList.stream().filter(item -> hmeCosFunctionVO6.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(hmeCosFunctionVO4List)){
                        hmeCosFunctionVO6.setWorkcellCode(hmeCosFunctionVO4List.get(0).getWorkcellCode());
                        hmeCosFunctionVO6.setWorkcellName(hmeCosFunctionVO4List.get(0).getWorkcellName());
                        hmeCosFunctionVO6.setProcessId(hmeCosFunctionVO4List.get(0).getProcessId());
                        hmeCosFunctionVO6.setProcessCode(hmeCosFunctionVO4List.get(0).getProcessCode());
                        hmeCosFunctionVO6.setProcessName(hmeCosFunctionVO4List.get(0).getProcessName());
                        hmeCosFunctionVO6.setLineWorkcellId(hmeCosFunctionVO4List.get(0).getLineWorkcellId());
                        hmeCosFunctionVO6.setLineWorkcellCode(hmeCosFunctionVO4List.get(0).getLineWorkcellCode());
                        hmeCosFunctionVO6.setLineWorkcellName(hmeCosFunctionVO4List.get(0).getLineWorkcellName());
                    }
                }
                //确定每一行动态列的值
                List<BigDecimal> dynamicColumn = new ArrayList<>();
                for (Integer current:currentList) {
                    List<HmeCosFunctionVO2> hmeCosFunctionVO2List2 = entry.getValue().stream().filter(item ->
                            hmeCosFunctionVO6.getMaterialLotId().equals(item.getMaterialLotId()) && hmeCosFunctionVO6.getRowCloumn().equals(item.getRowCloumn()) && current.toString().equals(item.getCurrent())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(hmeCosFunctionVO2List2)){
                        //电压
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA06());
                        //功率
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA02());
                        //中心波长
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA04());
                        //SE
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA012());
                        //线宽
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA013());
                        //WPE
                        dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA014());
                    }else{
                        //即使没有值，这里也要占空
                        dynamicColumn.add(null);
                        dynamicColumn.add(null);
                        dynamicColumn.add(null);
                        dynamicColumn.add(null);
                        dynamicColumn.add(null);
                        dynamicColumn.add(null);
                    }
                }
                //旧位置行号替换为字母
                if (StringUtils.isNotBlank(hmeCosFunctionVO6.getRowCloumn())) {
                    String[] split = hmeCosFunctionVO6.getRowCloumn().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    hmeCosFunctionVO6.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }
                hmeCosFunctionVO6.setDynamicColumn(dynamicColumn);
                hmeCosFunctionVO6List.add(hmeCosFunctionVO6);
            }
            hmeCosFunctionVO7.setHmeCosFunctionVO6List(hmeCosFunctionVO6List);
        }
        return hmeCosFunctionVO7;
    }

    @Override
    public void cosFunctionReportExport(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException {
        //查询动态列标题及表格数据
        HmeCosFunctionVO7 hmeCosFunctionVO7 = cosFunctionListQuery(tenantId, dto);
        //确定表的所有标题
        List<List<String>> headTitles = queryTitle(hmeCosFunctionVO7.getTitle());
        //确定表格内容数据
        List<List<Object>> data = queryData(hmeCosFunctionVO7.getHmeCosFunctionVO6List());
        try{
            String filename = URLEncoder.encode("COS测试明细报表", "UTF-8").replaceAll("\\+", "%20");

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet("COS测试明细报表")
                    .doWrite(data);
        }catch (Exception ex){
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().println("COS测试明细报表导出失败" + ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public HmeExportTaskVO addExportTask(HmeExportTaskVO hmeExportTaskVO){
        //新建导出任务
        hmeHzeroPlatformFeignClient.addExportTask(hmeExportTaskVO);
        return hmeExportTaskVO;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public HmeExportTaskVO updateExportTask(HmeExportTaskVO hmeExportTaskVO){
        //更新导出任务
        hmeHzeroPlatformFeignClient.updateExportTask(hmeExportTaskVO);
        return hmeExportTaskVO;
    }

    @Override
    @Async
    public void cosFunctionReportAsyncExport(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException {
        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        if(Objects.isNull(dto.getExportTaskVO())){
            //更新导出任务
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("未传入正在进行的任务!");
            updateExportTask(exportTaskVO);
            return;
        }
        //获取当前用户
        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();

        //获取UUID
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //查询动态列标题及表格数据
            HmeCosFunctionVO7 hmeCosFunctionVO7 = cosFunctionListQuery(tenantId, dto);
            //确定表的所有标题
            List<List<String>> headTitles = queryTitle(hmeCosFunctionVO7.getTitle());
            //确定表格内容数据
            List<List<Object>> data = queryData(hmeCosFunctionVO7.getHmeCosFunctionVO6List());

            //写入表头，表数据
            EasyExcel.write(bos)
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet(exportTaskVO.getTaskName())
                    .doWrite(data);

            byte[] bytes = bos.toByteArray();

            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachByteFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API【HmeCosFunctionServiceImpl.cosFunctionReportAsyncExport】";
            log.info(errorInfo);
            throw new CommonException(e);
        }finally {
            bos.close();

            //更新导出任务
            exportTaskVO.setState(state);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            if(Objects.nonNull(returnStr)) {
                exportTaskVO.setDownloadUrl(returnStr.getBody());
            }
            exportTaskVO.setErrorInfo(errorInfo);
            updateExportTask(exportTaskVO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String taskName) {
        //获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //获取UUID
        ResponseEntity<Map<String,String>> uuidMap= hmeHzeroFileFeignClient.getAttachUUID(tenantId);
        String uuid = uuidMap.getBody().get(BaseConstants.FIELD_CONTENT);

        //新建导出任务
        HmeExportTaskVO exportTaskVO = new HmeExportTaskVO();
        exportTaskVO.setTenantId(tenantId);
        exportTaskVO.setTaskCode(uuid);
        exportTaskVO.setTaskName(taskName);
        exportTaskVO.setServiceName("tarzan-mes-report");

        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        exportTaskVO.setHostName(localAddr + ":" + serverPort);
        exportTaskVO.setUserId(userId);
        exportTaskVO.setState(CommonUtils.ExportTaskStateValue.DOING);
        addExportTask(exportTaskVO);

        return exportTaskVO;
    }

    public HmeCosFunctionVO10 gpCosFunctionQuery(Long tenantId, HmeCosFunctionDTO2 dto){
        HmeCosFunctionVO10 hmeCosFunctionVO10 = new HmeCosFunctionVO10();
        hmeCosFunctionVO10.setCosFunctionVO2List(new ArrayList<>());
        hmeCosFunctionVO10.setCosFunctionVO4Map(new HashMap<>());

        List<HmeCosFunctionVO2> resultList = new ArrayList<>();

        //字符串转集合
        stringToList(tenantId,dto);

        if(CollectionUtils.isEmpty(dto.getCurrentList())){
            return hmeCosFunctionVO10;
        }
        List<Future<List<HmeCosFunctionVO2>>> futureList = new ArrayList<>();
        for (String current : dto.getCurrentList()
        ) {
            Future<List<HmeCosFunctionVO2>> cosFunctionOne = poolExecutor.submit(() -> {
                SecurityTokenHelper.close();
                List<HmeCosFunctionVO2> cosFunctionList = hmeCosFunctionMapper.gpCosFunctionReport(tenantId , dto , PRE_TABLE_NAME + "_" + current);
                return cosFunctionList;
            });
            futureList.add(cosFunctionOne);
        }

        for (Future<List<HmeCosFunctionVO2>> subFutureList : futureList
        ) {
            try {
                while (!subFutureList.isDone())
                {
                    Thread.sleep(100);
                }
                resultList.addAll(subFutureList.get());
            } catch (InterruptedException | ExecutionException e) {
                log.info("<==============HmeCosFunctionServiceImpl.gpCosFunctionReport InterruptedException,ExecutionException =============>");
                subFutureList.cancel(true);
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        if(CollectionUtils.isEmpty(resultList)){
            return hmeCosFunctionVO10;
        }

        List<String> materialLotIdList = resultList.stream()
                .filter(Objects::nonNull)
                .map(HmeCosFunctionVO2::getMaterialLotId)
                .distinct()
                .collect(Collectors.toList());
        List<String> materialLotIdList2 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            //查询条码当前工位
            List<HmeCosFunctionVO4> cosFunctionVO4List = new ArrayList<>();
            List<HmeCosFunctionVO3> labCodeList = new ArrayList<>();
            List<List<String>> splitMaterialLotIdList = WmsCommonUtils.splitSqlList(materialLotIdList, 5000);
            for (List<String> subMaterialLotIdList : splitMaterialLotIdList
            ) {
                SecurityTokenHelper.close();
                List<HmeCosFunctionVO4> subCosFunctionVO4List = hmeCosFunctionMapper.gpWorkcellInfoQuery(tenantId, dto.getWorkcellIdList(),subMaterialLotIdList);
                if(CollectionUtils.isNotEmpty(subCosFunctionVO4List)){
                    cosFunctionVO4List.addAll(subCosFunctionVO4List);
                }
            }

            if (CollectionUtils.isNotEmpty(cosFunctionVO4List)) {
                Map<String , HmeCosFunctionVO4> cosFunctionVO4Map = cosFunctionVO4List.stream()
                        .collect(Collectors.toMap(HmeCosFunctionVO4::getMaterialLotId, t -> t));
                hmeCosFunctionVO10.setCosFunctionVO4Map(cosFunctionVO4Map);
                materialLotIdList2 = cosFunctionVO4List.stream()
                        .map(HmeCosFunctionVO4::getMaterialLotId)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        if(CollectionUtils.isNotEmpty(dto.getWorkcellIdList())) {
            List<String> finalMaterialLotIdList = materialLotIdList2;
            resultList = resultList.stream()
                    .filter(item -> finalMaterialLotIdList.contains(item.getMaterialLotId()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(resultList)){
                hmeCosFunctionVO10.setCosFunctionVO2List(resultList);
                return hmeCosFunctionVO10;
            }
        }

        hmeCosFunctionVO10.setCosFunctionVO2List(resultList);

        return hmeCosFunctionVO10;
    }

    public HmeCosFunctionVO7 gpCosFunctionExport(Long tenantId, HmeCosFunctionVO10 hmeCosFunctionVO10){
        HmeCosFunctionVO7 hmeCosFunctionVO7 = new HmeCosFunctionVO7();

        List<HmeCosFunctionVO2> resultList = hmeCosFunctionVO10.getCosFunctionVO2List();
        Map<String , HmeCosFunctionVO4> cosFunctionVO4Map = hmeCosFunctionVO10.getCosFunctionVO4Map();

        //确定动态列标题，结果中有多少个不同的电流值，就有多少组动态列
        List<Integer> currentList = resultList.stream().map(HmeCosFunctionVO2::currentToNumber).distinct().collect(Collectors.toList());
        currentList = currentList.stream().sorted().collect(Collectors.toList());
        List<String> title = new ArrayList<>();
        for (Integer current:currentList) {
            title.add(current + "_电压");
            title.add(current + "_功率");
            title.add(current + "_中心波长");
            title.add(current + "_SE");
            title.add(current + "_线宽");
            title.add(current + "_WPE");
        }

        hmeCosFunctionVO7.setTitle(title);

        //根据条码+位置分组，同一分组下的数据合并为一条数据
        Map<String, List<HmeCosFunctionVO2>> map = resultList.stream().collect(Collectors.groupingBy(t -> {
            return t.getMaterialLotId() + "," + t.getRowCloumn();
        }));
        List<HmeCosFunctionVO6> hmeCosFunctionVO6List = new ArrayList<>();
        for (Map.Entry<String, List<HmeCosFunctionVO2>> entry:map.entrySet()) {

            HmeCosFunctionVO6 hmeCosFunctionVO6 = new HmeCosFunctionVO6();
            BeanCopierUtil.copy(entry.getValue().get(0), hmeCosFunctionVO6);

            if(StringUtils.isNotBlank(hmeCosFunctionVO6.getWorkcellId())){
                HmeCosFunctionVO4 hmeCosFunctionVO4 = cosFunctionVO4Map.getOrDefault(hmeCosFunctionVO6.getMaterialLotId() , null);
                if(Objects.nonNull(hmeCosFunctionVO4)){
                    hmeCosFunctionVO6.setWorkcellCode(hmeCosFunctionVO4.getWorkcellCode());
                    hmeCosFunctionVO6.setWorkcellName(hmeCosFunctionVO4.getWorkcellName());
                    hmeCosFunctionVO6.setProcessId(hmeCosFunctionVO4.getProcessId());
                    hmeCosFunctionVO6.setProcessCode(hmeCosFunctionVO4.getProcessCode());
                    hmeCosFunctionVO6.setProcessName(hmeCosFunctionVO4.getProcessName());
                    hmeCosFunctionVO6.setLineWorkcellId(hmeCosFunctionVO4.getLineWorkcellId());
                    hmeCosFunctionVO6.setLineWorkcellCode(hmeCosFunctionVO4.getLineWorkcellCode());
                    hmeCosFunctionVO6.setLineWorkcellName(hmeCosFunctionVO4.getLineWorkcellName());
                }
            }
            //确定每一行动态列的值
            List<BigDecimal> dynamicColumn = new ArrayList<>();
            for (Integer current:currentList) {
                List<HmeCosFunctionVO2> hmeCosFunctionVO2List2 = entry.getValue().stream().filter(item ->
                        hmeCosFunctionVO6.getMaterialLotId().equals(item.getMaterialLotId()) && hmeCosFunctionVO6.getRowCloumn().equals(item.getRowCloumn()) && current.toString().equals(item.getCurrent())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(hmeCosFunctionVO2List2)){
                    //电压
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA06());
                    //功率
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA02());
                    //中心波长
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA04());
                    //SE
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA012());
                    //线宽
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA013());
                    //WPE
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA014());
                }else{
                    //即使没有值，这里也要占空
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                }
            }
            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(hmeCosFunctionVO6.getRowCloumn())) {
                String[] split = hmeCosFunctionVO6.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                hmeCosFunctionVO6.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
            hmeCosFunctionVO6.setDynamicColumn(dynamicColumn);
            hmeCosFunctionVO6List.add(hmeCosFunctionVO6);
        }
        hmeCosFunctionVO7.setHmeCosFunctionVO6List(hmeCosFunctionVO6List);

        return hmeCosFunctionVO7;
    }

    @Override
    public Page<HmeCosFunctionVO2> gpCosFunctionReport(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest) {
        Page<HmeCosFunctionVO2> resultPage = new Page<>();

        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosFunctionVO10.getCosFunctionVO2List())){
            return resultPage;
        }

        List<HmeCosFunctionVO2> resultList = hmeCosFunctionVO10.getCosFunctionVO2List();
        Map<String , HmeCosFunctionVO4> cosFunctionVO4Map = hmeCosFunctionVO10.getCosFunctionVO4Map();

        resultPage = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);

        for (HmeCosFunctionVO2 hmeCosFunctionVO2:resultPage.getContent()) {
            if(StringUtils.isNotBlank(hmeCosFunctionVO2.getWorkcellId())){
                HmeCosFunctionVO4 hmeCosFunctionVO4 = cosFunctionVO4Map.getOrDefault(hmeCosFunctionVO2.getMaterialLotId() , null);
                if(Objects.nonNull(hmeCosFunctionVO4)){
                    hmeCosFunctionVO2.setWorkcellCode(hmeCosFunctionVO4.getWorkcellCode());
                    hmeCosFunctionVO2.setWorkcellName(hmeCosFunctionVO4.getWorkcellName());
                    hmeCosFunctionVO2.setProcessId(hmeCosFunctionVO4.getProcessId());
                    hmeCosFunctionVO2.setProcessCode(hmeCosFunctionVO4.getProcessCode());
                    hmeCosFunctionVO2.setProcessName(hmeCosFunctionVO4.getProcessName());
                    hmeCosFunctionVO2.setLineWorkcellId(hmeCosFunctionVO4.getLineWorkcellId());
                    hmeCosFunctionVO2.setLineWorkcellCode(hmeCosFunctionVO4.getLineWorkcellCode());
                    hmeCosFunctionVO2.setLineWorkcellName(hmeCosFunctionVO4.getLineWorkcellName());
                }
            }

            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(hmeCosFunctionVO2.getRowCloumn())) {
                String[] split = hmeCosFunctionVO2.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                hmeCosFunctionVO2.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
        }

        return resultPage;
    }

    @Override
    public void gpCosFunctionReportExport(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException {
        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosFunctionVO10.getCosFunctionVO2List())){
            return;
        }

        HmeCosFunctionVO7 hmeCosFunctionVO7 = gpCosFunctionExport(tenantId , hmeCosFunctionVO10);

        //确定表的所有标题
        List<List<String>> headTitles = queryTitle(hmeCosFunctionVO7.getTitle());
        //确定表格内容数据
        List<List<Object>> data = queryData(hmeCosFunctionVO7.getHmeCosFunctionVO6List());
        try{
            String filename = URLEncoder.encode("COS测试明细报表", "UTF-8").replaceAll("\\+", "%20");

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet("COS测试明细报表")
                    .doWrite(data);
        }catch (Exception ex){
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().println("COS测试明细报表(GP)导出失败" + ex.getMessage());
        }

    }

    @Override
    @Async
    public void gpCosFunctionReportAsyncExport(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException {

        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        if(Objects.isNull(dto.getExportTaskVO())){
            //更新导出任务
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("未传入正在进行的任务!");
            updateExportTask(exportTaskVO);
            return;
        }

        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosFunctionVO10.getCosFunctionVO2List())){
            return;
        }

        //获取当前用户
        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();

        //获取UUID
        ResponseEntity<Map<String,String>> uuidMap= hmeHzeroFileFeignClient.getAttachUUID(tenantId);
        String uuid = uuidMap.getBody().get(BaseConstants.FIELD_CONTENT);
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //查询动态列标题及表格数据
            HmeCosFunctionVO7 hmeCosFunctionVO7 = gpCosFunctionExport2(tenantId , hmeCosFunctionVO10);
            //确定表的所有标题
            List<List<String>> headTitles = queryTitle2(hmeCosFunctionVO7.getTitle());
            //确定表格内容数据
            List<List<Object>> data = queryData2(hmeCosFunctionVO7.getHmeCosFunctionVO6List());

            //写入表头，表数据
            EasyExcel.write(bos)
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet(FILE_NAME)
                    .doWrite(data);

            byte[] bytes = bos.toByteArray();

            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachByteFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API【HmeCosFunctionServiceImpl.cosFunctionReportAsyncExport】";
            log.info(errorInfo);
            throw new CommonException(e);
        }finally {
            bos.close();

            //更新导出任务
            exportTaskVO.setState(state);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            if(Objects.nonNull(returnStr)) {
                exportTaskVO.setDownloadUrl(returnStr.getBody());
            }
            exportTaskVO.setErrorInfo(errorInfo);
            updateExportTask(exportTaskVO);
        }
    }

    public HmeCosFunctionVO10 gpCosFunctionQuery2(Long tenantId, HmeCosFunctionDTO2 dto){
        HmeCosFunctionVO10 hmeCosFunctionVO10 = new HmeCosFunctionVO10();
        hmeCosFunctionVO10.setCosFunctionVO2List(new ArrayList<>());
        hmeCosFunctionVO10.setCosFunctionVO4Map(new HashMap<>());

        List<HmeCosFunctionVO2> resultList = new ArrayList<>();

        //字符串转集合
        stringToList(tenantId,dto);

        if(CollectionUtils.isEmpty(dto.getCurrentList())){
            return hmeCosFunctionVO10;
        }
        List<Future<List<HmeCosFunctionVO2>>> futureList = new ArrayList<>();
        for (String current : dto.getCurrentList()
        ) {
            Future<List<HmeCosFunctionVO2>> cosFunctionOne = poolExecutor.submit(() -> {
                SecurityTokenHelper.close();
                List<HmeCosFunctionVO2> cosFunctionList = hmeCosFunctionMapper.gpCosFunctionReport2(tenantId , dto , PRE_TABLE_NAME_2 + "_" + current);
                return cosFunctionList;
            });
            futureList.add(cosFunctionOne);
        }

        for (Future<List<HmeCosFunctionVO2>> subFutureList : futureList
        ) {
            try {
                while (!subFutureList.isDone())
                {
                    Thread.sleep(100);
                }
                resultList.addAll(subFutureList.get());
            } catch (InterruptedException | ExecutionException e) {
                log.info("<==============HmeCosFunctionServiceImpl.gpCosFunctionReport InterruptedException,ExecutionException =============>");
                subFutureList.cancel(true);
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        if(CollectionUtils.isEmpty(resultList)){
            return hmeCosFunctionVO10;
        }

        hmeCosFunctionVO10.setCosFunctionVO2List(resultList);

        return hmeCosFunctionVO10;
    }

    @Override
    public Page<HmeCosFunctionVO2> gpCosFunctionReport2(Long tenantId, HmeCosFunctionDTO2 dto, PageRequest pageRequest) {
        Page<HmeCosFunctionVO2> resultPage = new Page<>();

        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery2(tenantId , dto);

        List<HmeCosFunctionVO2> resultList = hmeCosFunctionVO10.getCosFunctionVO2List();

        if(CollectionUtils.isEmpty(resultList)){
            return resultPage;
        }

        resultPage = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);

        return resultPage;
    }

    public HmeCosFunctionVO7 gpCosFunctionExport2(Long tenantId, HmeCosFunctionVO10 hmeCosFunctionVO10){
        HmeCosFunctionVO7 hmeCosFunctionVO7 = new HmeCosFunctionVO7();

        List<HmeCosFunctionVO2> resultList = hmeCosFunctionVO10.getCosFunctionVO2List();

        //确定动态列标题，结果中有多少个不同的电流值，就有多少组动态列
        List<Integer> currentList = resultList.stream().map(HmeCosFunctionVO2::currentToNumber).distinct().collect(Collectors.toList());
        currentList = currentList.stream().sorted().collect(Collectors.toList());
        List<String> title = new ArrayList<>();
        for (Integer current:currentList) {
            title.add(current + "_电压");
            title.add(current + "_功率");
            title.add(current + "_中心波长");
            title.add(current + "_SE");
            title.add(current + "_线宽");
            title.add(current + "_WPE");
        }

        hmeCosFunctionVO7.setTitle(title);

        //根据条码+位置分组，同一分组下的数据合并为一条数据
        Map<String, List<HmeCosFunctionVO2>> map = resultList.stream().collect(Collectors.groupingBy(t -> {
            return t.getMaterialLotId() + "," + t.getRowCloumn();
        }));
        List<HmeCosFunctionVO6> hmeCosFunctionVO6List = new ArrayList<>();
        for (Map.Entry<String, List<HmeCosFunctionVO2>> entry:map.entrySet()) {

            HmeCosFunctionVO6 hmeCosFunctionVO6 = new HmeCosFunctionVO6();
            BeanCopierUtil.copy(entry.getValue().get(0), hmeCosFunctionVO6);
            //确定每一行动态列的值
            List<BigDecimal> dynamicColumn = new ArrayList<>();
            for (Integer current:currentList) {
                List<HmeCosFunctionVO2> hmeCosFunctionVO2List2 = entry.getValue().stream().filter(item ->
                        hmeCosFunctionVO6.getMaterialLotId().equals(item.getMaterialLotId()) && hmeCosFunctionVO6.getRowCloumn().equals(item.getRowCloumn()) && current.toString().equals(item.getCurrent())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(hmeCosFunctionVO2List2)){
                    //电压
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA06());
                    //功率
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA02());
                    //中心波长
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA04());
                    //SE
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA012());
                    //线宽
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA013());
                    //WPE
                    dynamicColumn.add(hmeCosFunctionVO2List2.get(0).getA014());
                }else{
                    //即使没有值，这里也要占空
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                    dynamicColumn.add(null);
                }
            }
            hmeCosFunctionVO6.setDynamicColumn(dynamicColumn);
            hmeCosFunctionVO6List.add(hmeCosFunctionVO6);
        }
        hmeCosFunctionVO7.setHmeCosFunctionVO6List(hmeCosFunctionVO6List);

        return hmeCosFunctionVO7;
    }

    @Override
    public void gpCosFunctionReportExport2(Long tenantId, HmeCosFunctionDTO2 dto, HttpServletResponse response) throws IOException {
        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery2(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosFunctionVO10.getCosFunctionVO2List())){
            return;
        }

        HmeCosFunctionVO7 hmeCosFunctionVO7 = gpCosFunctionExport2(tenantId , hmeCosFunctionVO10);

        //确定表的所有标题
        List<List<String>> headTitles = queryTitle2(hmeCosFunctionVO7.getTitle());
        //确定表格内容数据
        List<List<Object>> data = queryData2(hmeCosFunctionVO7.getHmeCosFunctionVO6List());
        try{
            String filename = URLEncoder.encode("COS测试明细报表", "UTF-8").replaceAll("\\+", "%20");

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
            //写入表头，表数据
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet("COS测试明细报表")
                    .doWrite(data);
        }catch (Exception ex){
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().println("COS测试明细报表(GP)导出失败" + ex.getMessage());
        }
    }

    @Override
    @Async
    public void gpCosFunctionReportAsyncExport2(Long tenantId, HmeCosFunctionDTO2 dto) throws IOException {
        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        if(Objects.isNull(dto.getExportTaskVO())){
            //更新导出任务
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("未传入正在进行的任务!");
            updateExportTask(exportTaskVO);
            return;
        }

        HmeCosFunctionVO10 hmeCosFunctionVO10 = gpCosFunctionQuery2(tenantId , dto);
        if(CollectionUtils.isEmpty(hmeCosFunctionVO10.getCosFunctionVO2List())){
            return;
        }

        //获取当前用户
        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();

        //获取UUID
        ResponseEntity<Map<String,String>> uuidMap= hmeHzeroFileFeignClient.getAttachUUID(tenantId);
        String uuid = uuidMap.getBody().get(BaseConstants.FIELD_CONTENT);
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //查询动态列标题及表格数据
            HmeCosFunctionVO7 hmeCosFunctionVO7 = gpCosFunctionExport2(tenantId , hmeCosFunctionVO10);
            //确定表的所有标题
            List<List<String>> headTitles = queryTitle2(hmeCosFunctionVO7.getTitle());
            //确定表格内容数据
            List<List<Object>> data = queryData2(hmeCosFunctionVO7.getHmeCosFunctionVO6List());

            //写入表头，表数据
            EasyExcel.write(bos)
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headTitles)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy()) // 设置样式
                    .sheet(FILE_NAME)
                    .doWrite(data);

            byte[] bytes = bos.toByteArray();

            if(bytes.length > 0) {

                MesReportFilePara mesReportFilePara = new MesReportFilePara();
                mesReportFilePara.setBucketNameValue(FileUtils.UploadValue.BUCKET_NAME);
                mesReportFilePara.setDirectoryValue(FileUtils.UploadValue.DIRECTORY);
                mesReportFilePara.setOpType(Constant.OpType.ASYNC_EXPORT);
                List<MesReportFileInfo> fileInfoList = getMesReportFileInfo.getFileInfo(tenantId , mesReportFilePara);
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, fileInfoList.get(0).getBucketName(), fileInfoList.get(0).getDirectory(), uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachByteFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API【HmeCosFunctionServiceImpl.cosFunctionReportAsyncExport】";
            log.info(errorInfo);
            throw new CommonException(e);
        }finally {
            bos.close();

            //更新导出任务
            exportTaskVO.setState(state);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            if(Objects.nonNull(returnStr)) {
                exportTaskVO.setDownloadUrl(returnStr.getBody());
            }
            exportTaskVO.setErrorInfo(errorInfo);
            updateExportTask(exportTaskVO);
        }
    }

    @Override
    public Page<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest) {
        Page<HmeCosFunctionHeadDTO> result = PageHelper.doPage(pageRequest, () -> hmeCosFunctionMapper.cosFunctionHeadQuery(tenantId, dto));
        for (HmeCosFunctionHeadDTO dto4 : result) {
            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                String[] split = dto4.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
        }
        return result;
    }

    @Override
    public Page<HmeCosFunction> cosFunctionQuery(Long tenantId, String loadSequence, PageRequest pageRequest) {
        HmeCosFunction hmeCosFunction = new HmeCosFunction();
        hmeCosFunction.setLoadSequence(loadSequence);
        return PageHelper.doPage(pageRequest, () -> hmeCosFunctionMapper.select(hmeCosFunction));
    }

    List<List<String>> queryTitle(List<String> dynamicTitle){
        List<List<String>> headTitles = Lists.newArrayList();
        headTitles.add(Lists.newArrayList("工单号"));
        headTitles.add(Lists.newArrayList("产品编码"));
        headTitles.add(Lists.newArrayList("产品描述"));
        headTitles.add(Lists.newArrayList("是否在制"));
        headTitles.add(Lists.newArrayList("测试设备"));
        headTitles.add(Lists.newArrayList("贴片设备"));
        headTitles.add(Lists.newArrayList("WAFER"));
        headTitles.add(Lists.newArrayList("COS类型"));
        headTitles.add(Lists.newArrayList("条码"));
        headTitles.add(Lists.newArrayList("位置"));
        headTitles.add(Lists.newArrayList("筛选状态"));
        headTitles.add(Lists.newArrayList("热沉编码"));
        headTitles.add(Lists.newArrayList("实验代码"));
        headTitles.add(Lists.newArrayList("测试模式"));
        headTitles.add(Lists.newArrayList("阈值电流"));
        headTitles.add(Lists.newArrayList("阈值电压"));
        if(CollectionUtil.isNotEmpty(dynamicTitle)){
            for (String title:dynamicTitle) {
                headTitles.add(Lists.newArrayList(title));
            }
        }
        headTitles.add(Lists.newArrayList("波长差"));
        headTitles.add(Lists.newArrayList("透镜功率"));
        headTitles.add(Lists.newArrayList("PBS功率"));
        headTitles.add(Lists.newArrayList("偏振度数"));
        headTitles.add(Lists.newArrayList("X半宽高"));
        headTitles.add(Lists.newArrayList("X86能量宽"));
        headTitles.add(Lists.newArrayList("X95能量宽"));
        headTitles.add(Lists.newArrayList("Y半宽高"));
        headTitles.add(Lists.newArrayList("Y86能量宽"));
        headTitles.add(Lists.newArrayList("Y95能量宽"));
        headTitles.add(Lists.newArrayList("功率等级"));
        headTitles.add(Lists.newArrayList("波长等级"));
        headTitles.add(Lists.newArrayList("电压等级"));
        headTitles.add(Lists.newArrayList("发散角等级"));
        headTitles.add(Lists.newArrayList("偏振度等级"));
        headTitles.add(Lists.newArrayList("不良描述"));
        headTitles.add(Lists.newArrayList("备注"));
        headTitles.add(Lists.newArrayList("不良代码"));
        headTitles.add(Lists.newArrayList("是否不良"));
        headTitles.add(Lists.newArrayList("热沉条码"));
        headTitles.add(Lists.newArrayList("热沉物料编码"));
        headTitles.add(Lists.newArrayList("热沉供应商批次"));
        headTitles.add(Lists.newArrayList("热沉焊料金锡比"));
        headTitles.add(Lists.newArrayList("金线条码"));
        headTitles.add(Lists.newArrayList("金线物料编码"));
        headTitles.add(Lists.newArrayList("金线供应商批次"));
        headTitles.add(Lists.newArrayList("测试操作人"));
        headTitles.add(Lists.newArrayList("测试时间"));
        headTitles.add(Lists.newArrayList("工位编码"));
        headTitles.add(Lists.newArrayList("工位描述"));
        headTitles.add(Lists.newArrayList("工序描述"));
        headTitles.add(Lists.newArrayList("工段描述"));
        headTitles.add(Lists.newArrayList("生产线描述"));
        headTitles.add(Lists.newArrayList("仓库"));
        headTitles.add(Lists.newArrayList("货位"));
        headTitles.add(Lists.newArrayList("是否冻结"));
        return headTitles;
    }

    List<List<String>> queryTitle2(List<String> dynamicTitle){
        List<List<String>> headTitles = Lists.newArrayList();
        headTitles.add(Lists.newArrayList("工单号"));
        headTitles.add(Lists.newArrayList("产品编码"));
        headTitles.add(Lists.newArrayList("产品描述"));
        headTitles.add(Lists.newArrayList("测试设备"));
        headTitles.add(Lists.newArrayList("贴片设备"));
        headTitles.add(Lists.newArrayList("WAFER"));
        headTitles.add(Lists.newArrayList("COS类型"));
        headTitles.add(Lists.newArrayList("条码"));
        headTitles.add(Lists.newArrayList("位置"));
        headTitles.add(Lists.newArrayList("筛选状态"));
        headTitles.add(Lists.newArrayList("热沉编码"));
        headTitles.add(Lists.newArrayList("实验代码"));
        headTitles.add(Lists.newArrayList("测试模式"));
        headTitles.add(Lists.newArrayList("阈值电流"));
        headTitles.add(Lists.newArrayList("阈值电压"));
        if(CollectionUtil.isNotEmpty(dynamicTitle)){
            for (String title:dynamicTitle) {
                headTitles.add(Lists.newArrayList(title));
            }
        }
        headTitles.add(Lists.newArrayList("波长差"));
        headTitles.add(Lists.newArrayList("透镜功率"));
        headTitles.add(Lists.newArrayList("PBS功率"));
        headTitles.add(Lists.newArrayList("偏振度数"));
        headTitles.add(Lists.newArrayList("X半宽高"));
        headTitles.add(Lists.newArrayList("X86能量宽"));
        headTitles.add(Lists.newArrayList("X95能量宽"));
        headTitles.add(Lists.newArrayList("Y半宽高"));
        headTitles.add(Lists.newArrayList("Y86能量宽"));
        headTitles.add(Lists.newArrayList("Y95能量宽"));
        headTitles.add(Lists.newArrayList("功率等级"));
        headTitles.add(Lists.newArrayList("波长等级"));
        headTitles.add(Lists.newArrayList("电压等级"));
        headTitles.add(Lists.newArrayList("发散角等级"));
        headTitles.add(Lists.newArrayList("偏振度等级"));
        headTitles.add(Lists.newArrayList("不良描述"));
        headTitles.add(Lists.newArrayList("备注"));
        headTitles.add(Lists.newArrayList("不良代码"));
        headTitles.add(Lists.newArrayList("是否不良"));
        headTitles.add(Lists.newArrayList("热沉条码"));
        headTitles.add(Lists.newArrayList("热沉物料编码"));
        headTitles.add(Lists.newArrayList("热沉供应商批次"));
        headTitles.add(Lists.newArrayList("热沉焊料金锡比"));
        headTitles.add(Lists.newArrayList("金线条码"));
        headTitles.add(Lists.newArrayList("金线物料编码"));
        headTitles.add(Lists.newArrayList("金线供应商批次"));
        headTitles.add(Lists.newArrayList("测试操作人"));
        headTitles.add(Lists.newArrayList("测试时间"));
        return headTitles;
    }

    List<List<Object>> queryData(List<HmeCosFunctionVO6> hmeCosFunctionVO6List){
        List<List<Object>> data = Lists.newArrayList();
        for (HmeCosFunctionVO6 hmeCosFunctionVO6:hmeCosFunctionVO6List) {
            List<Object> singleData = Lists.newArrayList();
            singleData.add(hmeCosFunctionVO6.getWorkOrderNum());
            singleData.add(hmeCosFunctionVO6.getMaterialCode());
            singleData.add(hmeCosFunctionVO6.getMaterialName());
            singleData.add(hmeCosFunctionVO6.getMfFlagMeaning());
            singleData.add(hmeCosFunctionVO6.getTestEquipment());
            singleData.add(hmeCosFunctionVO6.getPatchEquipment());
            singleData.add(hmeCosFunctionVO6.getWafer());
            singleData.add(hmeCosFunctionVO6.getCosType());
            singleData.add(hmeCosFunctionVO6.getMaterialLotCode());
            singleData.add(hmeCosFunctionVO6.getRowCloumn());
            singleData.add(hmeCosFunctionVO6.getPreStatusMeaning());
            singleData.add(hmeCosFunctionVO6.getHotSinkCode());
            singleData.add(hmeCosFunctionVO6.getLabCode());
            singleData.add(hmeCosFunctionVO6.getA09());
            singleData.add(hmeCosFunctionVO6.getA010());
            singleData.add(hmeCosFunctionVO6.getA011());
            for (BigDecimal value:hmeCosFunctionVO6.getDynamicColumn()) {
                singleData.add(value);
            }
            singleData.add(hmeCosFunctionVO6.getA05());
            singleData.add(hmeCosFunctionVO6.getA22());
            singleData.add(hmeCosFunctionVO6.getA23());
            singleData.add(hmeCosFunctionVO6.getA15());
            singleData.add(hmeCosFunctionVO6.getA16());
            singleData.add(hmeCosFunctionVO6.getA18());
            singleData.add(hmeCosFunctionVO6.getA20());
            singleData.add(hmeCosFunctionVO6.getA17());
            singleData.add(hmeCosFunctionVO6.getA19());
            singleData.add(hmeCosFunctionVO6.getA21());
            singleData.add(hmeCosFunctionVO6.getA01());
            singleData.add(hmeCosFunctionVO6.getA03());
            singleData.add(hmeCosFunctionVO6.getA27());
            singleData.add(hmeCosFunctionVO6.getDivergenceGrade());
            singleData.add(hmeCosFunctionVO6.getPolarizationGrade());
            singleData.add(hmeCosFunctionVO6.getDescription());
            singleData.add(hmeCosFunctionVO6.getA26());
            singleData.add(hmeCosFunctionVO6.getNcCode());
            singleData.add(hmeCosFunctionVO6.getNcFlagMeaning());
            singleData.add(hmeCosFunctionVO6.getHeatSinkMaterialLot());
            singleData.add(hmeCosFunctionVO6.getHeatSinkMaterialCode());
            singleData.add(hmeCosFunctionVO6.getHeatSinkSupplierLot());
            singleData.add(hmeCosFunctionVO6.getSolderAusnRatio());
            singleData.add(hmeCosFunctionVO6.getGoldWireMaterialLot());
            singleData.add(hmeCosFunctionVO6.getGoldWireMaterialCode());
            singleData.add(hmeCosFunctionVO6.getGoldWireSupplierLot());
            singleData.add(hmeCosFunctionVO6.getRealName());
            singleData.add(hmeCosFunctionVO6.getCreationDate());
            singleData.add(hmeCosFunctionVO6.getWorkcellCode());
            singleData.add(hmeCosFunctionVO6.getWorkcellName());
            singleData.add(hmeCosFunctionVO6.getProcessName());
            singleData.add(hmeCosFunctionVO6.getLineWorkcellName());
            singleData.add(hmeCosFunctionVO6.getProdLineName());
            singleData.add(hmeCosFunctionVO6.getWarehouseCode());
            singleData.add(hmeCosFunctionVO6.getLocatorCode());
            singleData.add(hmeCosFunctionVO6.getFreezeFlagMeaning());
            data.add(singleData);
        }
        return data;
    }

    List<List<Object>> queryData2(List<HmeCosFunctionVO6> hmeCosFunctionVO6List){
        List<List<Object>> data = Lists.newArrayList();
        for (HmeCosFunctionVO6 hmeCosFunctionVO6:hmeCosFunctionVO6List) {
            List<Object> singleData = Lists.newArrayList();
            singleData.add(hmeCosFunctionVO6.getWorkOrderNum());
            singleData.add(hmeCosFunctionVO6.getMaterialCode());
            singleData.add(hmeCosFunctionVO6.getMaterialName());
            singleData.add(hmeCosFunctionVO6.getTestEquipment());
            singleData.add(hmeCosFunctionVO6.getPatchEquipment());
            singleData.add(hmeCosFunctionVO6.getWafer());
            singleData.add(hmeCosFunctionVO6.getCosType());
            singleData.add(hmeCosFunctionVO6.getMaterialLotCode());
            singleData.add(hmeCosFunctionVO6.getRowCloumn());
            singleData.add(hmeCosFunctionVO6.getPreStatusMeaning());
            singleData.add(hmeCosFunctionVO6.getHotSinkCode());
            singleData.add(hmeCosFunctionVO6.getLabCode());
            singleData.add(hmeCosFunctionVO6.getA09());
            singleData.add(hmeCosFunctionVO6.getA010());
            singleData.add(hmeCosFunctionVO6.getA011());
            for (BigDecimal value:hmeCosFunctionVO6.getDynamicColumn()) {
                singleData.add(value);
            }
            singleData.add(hmeCosFunctionVO6.getA05());
            singleData.add(hmeCosFunctionVO6.getA22());
            singleData.add(hmeCosFunctionVO6.getA23());
            singleData.add(hmeCosFunctionVO6.getA15());
            singleData.add(hmeCosFunctionVO6.getA16());
            singleData.add(hmeCosFunctionVO6.getA18());
            singleData.add(hmeCosFunctionVO6.getA20());
            singleData.add(hmeCosFunctionVO6.getA17());
            singleData.add(hmeCosFunctionVO6.getA19());
            singleData.add(hmeCosFunctionVO6.getA21());
            singleData.add(hmeCosFunctionVO6.getA01());
            singleData.add(hmeCosFunctionVO6.getA03());
            singleData.add(hmeCosFunctionVO6.getA27());
            singleData.add(hmeCosFunctionVO6.getDivergenceGrade());
            singleData.add(hmeCosFunctionVO6.getPolarizationGrade());
            singleData.add(hmeCosFunctionVO6.getDescription());
            singleData.add(hmeCosFunctionVO6.getA26());
            singleData.add(hmeCosFunctionVO6.getNcCode());
            singleData.add(hmeCosFunctionVO6.getNcFlagMeaning());
            singleData.add(hmeCosFunctionVO6.getHeatSinkMaterialLot());
            singleData.add(hmeCosFunctionVO6.getHeatSinkMaterialCode());
            singleData.add(hmeCosFunctionVO6.getHeatSinkSupplierLot());
            singleData.add(hmeCosFunctionVO6.getSolderAusnRatio());
            singleData.add(hmeCosFunctionVO6.getGoldWireMaterialLot());
            singleData.add(hmeCosFunctionVO6.getGoldWireMaterialCode());
            singleData.add(hmeCosFunctionVO6.getGoldWireSupplierLot());
            singleData.add(hmeCosFunctionVO6.getRealName());
            singleData.add(hmeCosFunctionVO6.getCreationDate());
            data.add(singleData);
        }
        return data;
    }
}
