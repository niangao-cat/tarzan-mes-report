package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.app.assembler.HmeCosFunctionMergeStrategy;
import com.ruike.hme.domain.repository.HmeCosInProductionRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.Constant;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCosInProductionMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.FileUtils;
import com.ruike.hme.infra.util.HmeCommonUtils;
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
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import utils.ExportUtil;
import utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * COS在制报表 资源库实现
 *
 * @author 35113 2021/01/27 12:50
 */
@Component
@Slf4j
public class HmeCosInProductionRepositoryImpl implements HmeCosInProductionRepository {

    private final HmeCosInProductionMapper hmeCosInProductionMapper;
    private final LovAdapter lovAdapter;
    private final HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    private final HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    private final MtUserClient mtUserClient;

    public HmeCosInProductionRepositoryImpl(HmeCosInProductionMapper hmeCosInProductionMapper, LovAdapter lovAdapter, HmeHzeroFileFeignClient hmeHzeroFileFeignClient, HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient, MtUserClient mtUserClient) {
        this.hmeCosInProductionMapper = hmeCosInProductionMapper;
        this.lovAdapter = lovAdapter;
        this.hmeHzeroFileFeignClient = hmeHzeroFileFeignClient;
        this.hmeHzeroPlatformFeignClient = hmeHzeroPlatformFeignClient;
        this.mtUserClient = mtUserClient;
    }

    private static final int MAP_DEFAULT_CAPACITY = 16;
    private static final String FILE_NAME = "COS在制报表";

    @Override
    @ProcessLovValue
    public Page<HmeCosInProductionVO> pageList(Long tenant, HmeCosInProductionDTO dto, PageRequest pagerequest) {
        // 工段/工序 查询工位 传入了工位就按工位去查 若工段 工序找不到工位 直接返回
        Boolean organizationFlag = false;
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            if (CollectionUtils.isNotEmpty(dto.getProcessIdList())) {
                dto.setWorkcellIdList(hmeCosInProductionMapper.queryWorkcellByProcessIds(tenant, dto.getProcessIdList()));
                organizationFlag = true;
            } else if (CollectionUtils.isNotEmpty(dto.getLineWorkcellIdList())) {
                dto.setWorkcellIdList(hmeCosInProductionMapper.queryWorkcellBylineWorkcellIdList(tenant, dto.getLineWorkcellIdList()));
                organizationFlag = true;
            }
        }
        if (organizationFlag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            return HmeCommonUtils.pagedList(pagerequest.getPage(), pagerequest.getSize(), Collections.EMPTY_LIST);
        }
        List<HmeCosInProductionVO> list = getList(tenant, dto);
        Page<HmeCosInProductionVO> page = HmeCommonUtils.pagedList(pagerequest.getPage(), pagerequest.getSize(), list);
        displayFieldsCompletion(tenant, page.getContent(), dto);
        return page;
    }

    @Override
    @ProcessLovValue
    public void export(Long tenant, HmeCosInProductionDTO dto, HttpServletResponse response) {
        List<HmeCosInProductionVO> list = cosInProductionQuery(tenant, dto);
        ExportUtil.writeExcelOneSheet(response, list, "COS在制报表", "COS在制报表", HmeCosInProductionVO.class);
    }

    @Override
    public Page<HmeCosInNcRecordVO> ncRecordList(Long tenantId, HmeCosInProductionVO dto, PageRequest pageRequest) {
        Page<HmeCosInNcRecordVO> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosInProductionMapper.ncRecordList(tenantId, dto));
        for (HmeCosInNcRecordVO hmeCosInNcRecordVO : pageObj.getContent()) {
            // 位置
            if (hmeCosInNcRecordVO.getNcLoadRow() != null && hmeCosInNcRecordVO.getNcLoadColumn() != null) {
                hmeCosInNcRecordVO.setPosition(String.valueOf((char)(hmeCosInNcRecordVO.getNcLoadRow()+64)) + hmeCosInNcRecordVO.getNcLoadColumn());
            }
        }
        return pageObj;
    }

    @Override
    @ProcessLovValue
    @Async
    public void asyncExport(Long tenantId, HmeCosInProductionDTO dto) throws IOException {
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
            List<HmeCosInProductionVO> resultList = cosInProductionQuery(tenantId, dto);

            //写入表头，表数据
            EasyExcel.write(bos, HmeCosInProductionVO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(FILE_NAME)
                    .registerWriteHandler(HmeCosFunctionMergeStrategy.CellStyleStrategy())
                    .doWrite(resultList);

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
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response) {
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
        exportTaskVO.setTaskName(FILE_NAME);
        exportTaskVO.setServiceName("tarzan-mes-report");

        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        exportTaskVO.setHostName(localAddr + ":" + serverPort);
        exportTaskVO.setUserId(userId);
        exportTaskVO.setState(CommonUtils.ExportTaskStateValue.DOING);
        addExportTask(exportTaskVO);

        return exportTaskVO;
    }

    private List<HmeCosInProductionVO> cosInProductionQuery(Long tenantId, HmeCosInProductionDTO dto) {
        // 工段/工序 查询工位 传入了工位就按工位去查 若工段 工序找不到工位 直接返回
        Boolean organizationFlag = false;
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            if (CollectionUtils.isNotEmpty(dto.getProcessIdList())) {
                dto.setWorkcellIdList(hmeCosInProductionMapper.queryWorkcellByProcessIds(tenantId, dto.getProcessIdList()));
                organizationFlag = true;
            } else if (CollectionUtils.isNotEmpty(dto.getLineWorkcellIdList())) {
                dto.setWorkcellIdList(hmeCosInProductionMapper.queryWorkcellBylineWorkcellIdList(tenantId, dto.getLineWorkcellIdList()));
                organizationFlag = true;
            }
        }
        List<HmeCosInProductionVO> list = null;
        if (organizationFlag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            list = new ArrayList<>();
        } else {
            list = getList(tenantId, dto);
        }
        displayFieldsCompletion(tenantId, list, dto);
        // 值集转化
        List<LovValueDTO> woTypeList = lovAdapter.queryLovValue("MT.WO_TYPE", tenantId);
        List<LovValueDTO> woStatusList = lovAdapter.queryLovValue("MT.WO_STATUS", tenantId);
        List<LovValueDTO> flagList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
        for (HmeCosInProductionVO hmeCosInProductionVO : list) {
            // 工单类型
            if (StringUtils.isNotBlank(hmeCosInProductionVO.getWorkOrderType())) {
                Optional<LovValueDTO> firstOpt = woTypeList.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeCosInProductionVO.getWorkOrderType())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeCosInProductionVO.setWorkOrderTypeMeaning(firstOpt.get().getMeaning());
                }
            }
            // 工单状态
            if (StringUtils.isNotBlank(hmeCosInProductionVO.getStatus())) {
                Optional<LovValueDTO> firstOpt = woStatusList.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeCosInProductionVO.getStatus())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeCosInProductionVO.setStatusMeaning(firstOpt.get().getMeaning());
                }
            }
            // 呆滞标记
            if (StringUtils.isNotBlank(hmeCosInProductionVO.getSluggishFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeCosInProductionVO.getSluggishFlag())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeCosInProductionVO.setSluggishFlagMeaning(firstOpt.get().getMeaning());
                }
            }
            // 是否冻结
            if (StringUtils.isNotBlank(hmeCosInProductionVO.getFreezeFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeCosInProductionVO.getFreezeFlag())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeCosInProductionVO.setFreezeFlagMeaning(firstOpt.get().getMeaning());
                }
            }
            // 是否不良
            if (StringUtils.isNotBlank(hmeCosInProductionVO.getNcFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeCosInProductionVO.getNcFlag())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeCosInProductionVO.setNcFlagMeaning(firstOpt.get().getMeaning());
                }
            }
        }
        return list;
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

    private List<HmeCosInProductionVO> getList(Long tenant, HmeCosInProductionDTO dto) {
        long startDate = System.currentTimeMillis();
        // 先查询出符合条件的job
        startDate =  System.currentTimeMillis();

        //V20210713 modify by penglin.sui for tianyang.xie 根据工单、工单类型、产品编码、生产线查询工单ID，带入查询进站数据
        List<String> allWorkOrderIdList = new ArrayList<>();
        List<String> allMaterialLotIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getWorkOrderNumList())
                || StringUtils.isNotBlank(dto.getWorkOrderType())
                || CollectionUtils.isNotEmpty(dto.getProdLineIdList())){
            List<HmeCosInProductionVO> cosInProductionVOList = hmeCosInProductionMapper.selectWoId(tenant , dto);

            if(CollectionUtils.isNotEmpty(cosInProductionVOList)) {
                List<String> workOrderIdList = cosInProductionVOList.stream()
                        .map(HmeCosInProductionVO::getWorkOrderId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    allWorkOrderIdList.addAll(workOrderIdList);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(dto.getMaterialCodeList())){
            List<HmeCosInProductionVO> cosInProductionVOList = hmeCosInProductionMapper.selectWoIdOfMaterial(tenant,dto);

            if(CollectionUtils.isNotEmpty(cosInProductionVOList)) {
                List<String> workOrderIdList = cosInProductionVOList.stream()
                        .map(HmeCosInProductionVO::getWorkOrderId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    allWorkOrderIdList.addAll(workOrderIdList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(dto.getMaterialLotCodeList())){
            List<HmeCosInProductionVO> cosInProductionVOList = hmeCosInProductionMapper.selectMaterialLotId(tenant , dto);

            if(CollectionUtils.isNotEmpty(cosInProductionVOList)) {
                List<String> materialLotIdList = cosInProductionVOList.stream()
                        .map(HmeCosInProductionVO::getMaterialLotId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    allMaterialLotIdList.addAll(materialLotIdList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(dto.getWaferList())){
            List<HmeCosInProductionVO> cosInProductionVOList = hmeCosInProductionMapper.selectMaterialLotIdOfWafer(tenant , dto);

            if(CollectionUtils.isNotEmpty(cosInProductionVOList)) {
                List<String> materialLotIdList = cosInProductionVOList.stream()
                        .map(HmeCosInProductionVO::getMaterialLotId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    allMaterialLotIdList.addAll(materialLotIdList);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(dto.getLabCodeList())){
            List<HmeCosInProductionVO> cosInProductionVOList = hmeCosInProductionMapper.selectMaterialLotIdOfLabCode(tenant , dto);

            if(CollectionUtils.isNotEmpty(cosInProductionVOList)) {
                List<String> materialLotIdList = cosInProductionVOList.stream()
                        .map(HmeCosInProductionVO::getMaterialLotId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    allMaterialLotIdList.addAll(materialLotIdList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(allWorkOrderIdList)){
            allWorkOrderIdList = allWorkOrderIdList.stream().distinct().collect(Collectors.toList());
            dto.setWorkOrderIdList(allWorkOrderIdList);
        }

        if(CollectionUtils.isNotEmpty(allMaterialLotIdList)){
            allMaterialLotIdList = allMaterialLotIdList.stream().distinct().collect(Collectors.toList());
            dto.setMaterialLotIdList(allMaterialLotIdList);
        }

        List<HmeEoJobSnVO> jobSnList = hmeCosInProductionMapper.queryJobSnList(tenant, dto);
        log.info("<=========通过工位和条码汇总queryJobSnList===========>" + (System.currentTimeMillis() - startDate) + "毫秒");
        if (CollectionUtils.isEmpty(jobSnList)) {
            return new ArrayList<>();
        }

        // 通过工位和条码汇总
        startDate =  System.currentTimeMillis();
        Map<String, List<HmeEoJobSnVO>> jobMap = jobSnList.stream().collect(Collectors.groupingBy(rec -> rec.getMaterialLotId()));
        log.info("<=========通过工位和条码汇总转MAP,jobMap===========>" + (System.currentTimeMillis() - startDate) + "毫秒");
        List<String> jobIdList = new ArrayList<>();
        startDate =  System.currentTimeMillis();
        jobMap.forEach((key, value) -> {
            // 找出日期最大的job
            Optional<HmeEoJobSnVO> jobOptional = value.stream().max(Comparator.comparing(HmeEoJobSnVO::getCreationDate));
            jobOptional.ifPresent(hmeEoJobSn -> jobIdList.add(hmeEoJobSn.getJobId()));
        });
        log.info("<=========通过工位和条码汇总循环MAP,jobMap次数===========>" + jobMap.size());
        log.info("<=========通过工位和条码汇总循环MAP,jobMap===========>" + (System.currentTimeMillis() - startDate) + "毫秒");

        //V20210712 modify by penglin.sui 分批次批量查询
        List<HmeCosInProductionVO> allCosInProductionVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(jobIdList)){
            startDate =  System.currentTimeMillis();
            List<List<String>> allJobIdList = Utils.splitSqlList(jobIdList,3000);
            boolean addFlag = true;
            for (List<String> subJobIdList : allJobIdList
                 ) {
                // 根据job查询数据
                dto.setJobIdList(subJobIdList);

                List<HmeCosInProductionVO> subCosInProductionVOList =
                        hmeCosInProductionMapper.selectListByCondition(tenant, dto);
                if(CollectionUtils.isNotEmpty(subCosInProductionVOList)){
                    Map<String, BigDecimal> ncCountMap = new HashMap<>();
                    if(StringUtils.isNotBlank(dto.getNcFlag())) {
                        //是否不良查询
                        List<String> materialLotIdList = subCosInProductionVOList.stream().map(HmeCosInProductionVO::getMaterialLotId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                            List<HmeCosInNcRecordVO2> hmeCosInNcRecordVO2List = hmeCosInProductionMapper.ncRecordCountList(tenant, materialLotIdList);
                            if (CollectionUtils.isNotEmpty(hmeCosInNcRecordVO2List)) {

                                ncCountMap = hmeCosInNcRecordVO2List.stream().collect(Collectors.toMap(HmeCosInNcRecordVO2::getMaterialLotId,
                                        HmeCosInNcRecordVO2::getNgCount));
                            }
                        }
                    }

                    addFlag = true;
                    for (HmeCosInProductionVO hmeCosInProductionVO : subCosInProductionVOList
                    ) {
                        if(StringUtils.isNotBlank(dto.getNcFlag())) {
                            String ncFlag = ncCountMap.getOrDefault(hmeCosInProductionVO.getMaterialLotId(), BigDecimal.ZERO)
                                    .compareTo(BigDecimal.ZERO) > 0 ? "Y" : "N";
                            addFlag = ncFlag.equals(dto.getNcFlag());
                        }

                        if(addFlag) {
                            allCosInProductionVOList.add(hmeCosInProductionVO);
                        }
                    }
                }
            }
            log.info("<=========通过工位和条码汇总分批次批量查询次数===========>" + allJobIdList.size());
            log.info("<=========通过工位和条码汇总分批次批量查询次数===========>" + (System.currentTimeMillis() - startDate) + "毫秒");
        }

        return allCosInProductionVOList;
    }

    private void displayFieldsCompletion(Long tenant, List<HmeCosInProductionVO> list, HmeCosInProductionDTO dto) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 查询工单实际
        List<String> woIdList = list.stream().map(HmeCosInProductionVO::getWorkOrderId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Double> actualMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        if (CollectionUtils.isNotEmpty(woIdList)) {
            List<HmeWorkOrderActual> actuals = new ArrayList<>();
            List<List<String>> allWoIdList = Utils.splitSqlList(woIdList,3000);
            for (List<String> subWoIdList : allWoIdList
            ) {
                List<HmeWorkOrderActual> subActuals = hmeCosInProductionMapper.selectWoActualList(subWoIdList);
                if(CollectionUtils.isNotEmpty(subActuals)){
                    actuals.addAll(subActuals);
                }
            }

            actualMap = actuals.stream().collect(toMap(HmeWorkOrderActual::getWorkOrderId, HmeWorkOrderActual::getCompletedQty));
        }

        // 查询cos数量
        List<String> waferList = list.stream().map(HmeCosInProductionVO::getWafer).filter(Objects::nonNull).collect(Collectors.toList());
        Map<String, Long> cosNumMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        if (CollectionUtils.isNotEmpty(woIdList) && CollectionUtils.isNotEmpty(waferList)) {
            List<HmeCosOperationRecordVO> cosRecords = hmeCosInProductionMapper.selectCosRecordList(woIdList, waferList, dto.getWorkcellList());
            cosNumMap = cosRecords.stream().collect(toMap(m -> m.getWorkOrderId() + "#" + m.getWafer(), HmeCosOperationRecordVO::getCosNum));
        }
        // 工位对应工序及工段信息
        List<String> workcellIdList = list.stream().map(HmeCosInProductionVO::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosInProductionVO2>> organizationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(workcellIdList)) {
            List<HmeCosInProductionVO2> hmeCosInProductionVO2List = new ArrayList<>();
            List<List<String>> allWorkcellIdList = Utils.splitSqlList(workcellIdList,3000);
            for (List<String> subWorkcellIdList : allWorkcellIdList
                 ) {
                List<HmeCosInProductionVO2> subHmeCosInProductionVO2List = hmeCosInProductionMapper.qeuryProcessAndLineWorkcellByWorkcell(tenant, subWorkcellIdList);
                if(CollectionUtils.isNotEmpty(subHmeCosInProductionVO2List)){
                    hmeCosInProductionVO2List.addAll(subHmeCosInProductionVO2List);
                }
            }

            if (CollectionUtils.isNotEmpty(hmeCosInProductionVO2List)) {
                organizationMap = hmeCosInProductionVO2List.stream().collect(Collectors.groupingBy(HmeCosInProductionVO2::getWorkcellId));
            }
        }
        // 实验代码
        List<String> materialLotIdList = list.stream().map(HmeCosInProductionVO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosInProductionVO3>> labCodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {

            List<HmeCosInProductionVO3> hmeCosInProductionVO3s = new ArrayList<>();
            List<List<String>> allMaterialLotIdList = Utils.splitSqlList(materialLotIdList,3000);
            for (List<String> subMaterialLotIdList : allMaterialLotIdList
            ) {
                List<HmeCosInProductionVO3> subHmeCosInProductionVO3s = hmeCosInProductionMapper.queryLabCodeByMaterialLotIdList(tenant, subMaterialLotIdList);
                if(CollectionUtils.isNotEmpty(subHmeCosInProductionVO3s)){
                    hmeCosInProductionVO3s.addAll(subHmeCosInProductionVO3s);
                }
            }

            if (CollectionUtils.isNotEmpty(hmeCosInProductionVO3s)) {
                labCodeMap = hmeCosInProductionVO3s.stream().collect(Collectors.groupingBy(HmeCosInProductionVO3::getMaterialLotId));
            }
        }
        //是否不良
        Map<String, BigDecimal> ncRecordMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<HmeCosInNcRecordVO2> hmeCosInProductionVO2s = new ArrayList<>();
            List<List<String>> allMaterialLotIdList = Utils.splitSqlList(materialLotIdList,300);
            for (List<String> subMaterialLotIdList : allMaterialLotIdList
                 ) {
                List<HmeCosInNcRecordVO2> subHmeCosInProductionVO2s = hmeCosInProductionMapper.ncRecordCountList(tenant, subMaterialLotIdList);
                if(CollectionUtils.isNotEmpty(subHmeCosInProductionVO2s)){
                    hmeCosInProductionVO2s.addAll(subHmeCosInProductionVO2s);
                }
            }

            if (CollectionUtils.isNotEmpty(hmeCosInProductionVO2s)) {
                ncRecordMap = hmeCosInProductionVO2s.stream().collect(Collectors.toMap(HmeCosInNcRecordVO2::getMaterialLotId,
                        HmeCosInNcRecordVO2::getNgCount));
            }
        }
        // 当前工序 根据job找工艺步骤 会找到多个 再限制工艺步骤根据工单找的一致
        // 20211118 modify by sanfeng.zhang 当前工序 根据条码扩展字段CURRENT_ROUTER_STEP获取
        Map<String, HmeRouterStepVO> hmeRouterStepMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<HmeRouterStepVO> hmeRouterStepList = hmeCosInProductionMapper.queryCurrentProcessByCodes(tenant, materialLotIdList);
            hmeRouterStepMap = hmeRouterStepList.stream().collect(toMap(HmeRouterStepVO::getMaterialLotId, Function.identity()));
        }
        for (HmeCosInProductionVO rec : list) {
            // 工序、工段
            if (StringUtils.isNotBlank(rec.getWorkcellId())) {
                List<HmeCosInProductionVO2> hmeCosInProductionVO2s = organizationMap.get(rec.getWorkcellId());
                if (CollectionUtils.isNotEmpty(hmeCosInProductionVO2s)) {
                    rec.setProcessName(hmeCosInProductionVO2s.get(0).getProcessName());
                    rec.setLineWorkcellName(hmeCosInProductionVO2s.get(0).getLineWorkcellName());
                }
            }
            // 当前工序
            HmeRouterStepVO routerStepVO = hmeRouterStepMap.get(rec.getMaterialLotId());
            String currentProcessName = routerStepVO != null ? routerStepVO.getDescription() : "";
            rec.setCurrentProcessName(currentProcessName);

            // 呆滞标记 呆滞时间大于24 记Y   若已出站 不记录呆滞时间 清空
            if (StringUtils.isNotBlank(rec.getSiteOutDate())) {
                rec.setSluggishTime("");
                rec.setSluggishFlag(Constant.ConstantValue.NO);
            } else {
                if (BigDecimal.valueOf(24).compareTo(BigDecimal.valueOf(Double.valueOf(rec.getSluggishTime()))) < 0) {
                    rec.setSluggishFlag(Constant.ConstantValue.YES);
                } else {
                    rec.setSluggishFlag(Constant.ConstantValue.NO);
                }
            }
            // 实验代码
            if (StringUtils.isNotBlank(rec.getMaterialLotId())) {
                List<HmeCosInProductionVO3> hmeCosInProductionVO3s = labCodeMap.get(rec.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(hmeCosInProductionVO3s)) {
                    rec.setLabCode(hmeCosInProductionVO3s.get(0).getLabCode());
                }
            }
            // 是否不良
            String mapKey = rec.getMaterialLotId();
            BigDecimal ncCount = ncRecordMap.getOrDefault(mapKey , BigDecimal.ZERO);
            if (ncCount.compareTo(BigDecimal.ZERO) > 0) {
                rec.setNcFlag(Constant.ConstantValue.YES);
            } else {
                rec.setNcFlag(Constant.ConstantValue.NO);
            }
            rec.setCompletedQty(BigDecimal.valueOf(actualMap.getOrDefault(rec.getWorkOrderId(), 0D)));
            rec.setCosNum(cosNumMap.getOrDefault(rec.getWorkOrderId() + "#" + rec.getWafer(), 0L));
        }
    }
}
