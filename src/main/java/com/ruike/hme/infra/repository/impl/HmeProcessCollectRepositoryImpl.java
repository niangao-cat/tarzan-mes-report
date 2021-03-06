package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.domain.repository.HmeProcessCollectRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCommonReportMapper;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import com.ruike.hme.infra.mapper.HmeProcessCollectMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
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
import org.apache.poi.xssf.usermodel.*;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.AopProxy;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * ????????????????????? ???????????????
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:09
 */
@Repository
@Slf4j
public class HmeProcessCollectRepositoryImpl implements HmeProcessCollectRepository, AopProxy<HmeProcessCollectRepositoryImpl> {
    private static final String[] FIXED_TITLE = {"?????????", "??????????????????", "??????????????????", "?????????", "????????????", "SN????????????", "SN????????????", "????????????", "??????", "??????", "????????????", "EO??????", "????????????", "????????????", "????????????????????????", "??????????????????", "?????????", "????????????", "????????????"};
    private static final String[] GP_FIXED_TITLE = {"?????????", "??????????????????", "??????????????????", "?????????", "SN????????????", "SN????????????", "????????????", "??????", "??????","?????????", "????????????", "????????????"};
    private static final String FILE_NAME = "?????????????????????";

    private final HmeProcessCollectMapper mapper;
    private final HmeLoadJobMapper loadJobMapper;
    private final HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    private final HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    private final HmeCommonReportMapper hmeCommonReportMapper;
    private final MtUserClient mtUserClient;

    public HmeProcessCollectRepositoryImpl(HmeProcessCollectMapper mapper, HmeLoadJobMapper loadJobMapper, HmeHzeroFileFeignClient hmeHzeroFileFeignClient, HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient, HmeCommonReportMapper hmeCommonReportMapper, MtUserClient mtUserClient) {
        this.mapper = mapper;
        this.loadJobMapper = loadJobMapper;
        this.hmeHzeroFileFeignClient = hmeHzeroFileFeignClient;
        this.hmeHzeroPlatformFeignClient = hmeHzeroPlatformFeignClient;
        this.hmeCommonReportMapper = hmeCommonReportMapper;
        this.mtUserClient = mtUserClient;
    }

    @Override
    public HmeProcessCollectTitleVO pagedList(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest) {
        Page<HmeProcessCollectVO> page = self().getPage(tenantId, dto, pageRequest);
        HmeProcessCollectTitleVO result = new HmeProcessCollectTitleVO(page , null);
        List<String> dynamicTitles = displayFieldsCompletion(tenantId, page.getContent());
        result.setDynamicTitles(dynamicTitles);
        return result;
    }

    @Override
    public Page<HmeProcessJobDetailVO> pagedJobList(Long tenantId, String jobId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mapper.selectProcessJobDetailList(tenantId, jobId));
    }

    @ProcessLovValue
    public Page<HmeProcessCollectVO> getPage(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest) {

        //V20210712 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
        List<String> workcellIdList = mapper.selectWorkcellIdList(tenantId , dto);
        dto.setWorkcellIdList(workcellIdList);
        if(CollectionUtils.isEmpty(workcellIdList)){
            return new Page<>();
        }
        return PageHelper.doPage(pageRequest, () -> mapper.selectList(tenantId, dto));
    }

    @ProcessLovValue
    public List<HmeProcessCollectVO> list(Long tenantId, HmeProcessCollectQuery dto) {
        //V20210712 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
        List<String> workcellIdList = mapper.selectWorkcellIdList(tenantId , dto);
        dto.setWorkcellIdList(workcellIdList);
        if(CollectionUtils.isEmpty(workcellIdList)){
            return new ArrayList<>();
        }
        return mapper.selectList(tenantId, dto);
    }

    @Override
    public void export(Long tenantId, HmeProcessCollectQuery dto, HttpServletRequest request, HttpServletResponse response) {
        // ????????????
        List<HmeProcessCollectVO> list = self().list(tenantId, dto);
        List<String> dynamicTitles = displayFieldsCompletion(tenantId, list);

        // ???????????????????????????
        XSSFWorkbook workbook = new XSSFWorkbook();

        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String currentUserName = curUser == null ? "-1" : curUser.getUsername();
        //??????UUID
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME + ".xlsx";

        XSSFSheet sheet = workbook.createSheet(FILE_NAME);
        Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

        // ????????????
        List<String> titles = new ArrayList<>();
        titles.addAll(Arrays.asList(FIXED_TITLE));
        titles.addAll(dynamicTitles);
        XSSFRow headerRow = sheet.createRow(0);
        this.fillRow(headerRow, titles, styles.get("normal"));

        //????????????
        AtomicInteger rowNumGen = new AtomicInteger(1);
        list.forEach(rec -> {
            int rowNum = rowNumGen.getAndIncrement();
            XSSFRow row = sheet.createRow(rowNum);
            List<String> fields = new ArrayList<>();
            fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWoMaterialCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWoMaterialName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getIdentification()).orElse(""));
            fields.add(Optional.ofNullable(rec.getLabCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
            fields.add(rec.getWorkTime() != null ? DateUtil.format(rec.getWorkTime(), BaseConstants.Pattern.DATETIME) : "");
            fields.add(Optional.ofNullable(rec.getProcessWorkcellName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWorkcellName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getQualityStatusMeaning()).orElse(""));
            fields.add(Optional.ofNullable(rec.getEoStatusMeaning()).orElse(""));
            fields.add(Optional.ofNullable(rec.getFreezeFlagMeaning()).orElse(""));
            fields.add(Optional.ofNullable(rec.getTransformFlagMeaning()).orElse(""));
            fields.add(Optional.ofNullable(rec.getLatestNcTag()).orElse(""));
            fields.add(rec.getNcDate() != null ? DateUtil.format(rec.getNcDate(), BaseConstants.Pattern.DATETIME) : "");
            fields.add(Optional.ofNullable(rec.getWorker()).orElse(""));
            fields.add(rec.getShiftDate() != null ? DateUtil.format(rec.getShiftDate(), BaseConstants.Pattern.DATETIME) : "");
            fields.add(Optional.ofNullable(rec.getShiftCode()).orElse(""));
            // ?????????????????????
            fields.addAll(rec.getProList());
            this.fillRow(row, fields, styles.get("normal"));
        });

        // ????????????
        FileUtils.downloadWorkbook(workbook, request, response, fileName);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public HmeExportTaskVO addExportTask(HmeExportTaskVO hmeExportTaskVO){
        //??????????????????
        hmeHzeroPlatformFeignClient.addExportTask(hmeExportTaskVO);
        return hmeExportTaskVO;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public HmeExportTaskVO updateExportTask(HmeExportTaskVO hmeExportTaskVO){
        //??????????????????
        hmeHzeroPlatformFeignClient.updateExportTask(hmeExportTaskVO);
        return hmeExportTaskVO;
    }

    @Override
    @Async
    public void asyncExport(Long tenantId, HmeProcessCollectQuery dto, List<LovValueDTO> qualityStatusLov, List<LovValueDTO> flagYnLov) throws IOException {
        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        if(Objects.isNull(dto.getExportTaskVO())){
            //??????????????????
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("??????????????????????????????!");
            updateExportTask(exportTaskVO);
            return;
        }
        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String currentUserName = curUser == null ? "-1" : curUser.getUsername();
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //????????????
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {

            // ????????????
            List<HmeProcessCollectVO> list = list(tenantId, dto);

            if(CollectionUtils.isNotEmpty(list)){
                //2021-09-23 11:43 edit by chaonan.hu for hui.gu ?????????????????????????????????????????????
                Map<String, String> qualityStatusMap = new HashMap<>();
                for (LovValueDTO qualityStatus:qualityStatusLov) {
                    qualityStatusMap.put(qualityStatus.getValue(), qualityStatus.getMeaning());
                }
                Map<String, String> flagYnMap = new HashMap<>();
                for (LovValueDTO flagYn:flagYnLov) {
                    flagYnMap.put(flagYn.getValue(), flagYn.getMeaning());
                }
                list.forEach(rec -> {
                    rec.setQualityStatusMeaning(qualityStatusMap.getOrDefault(rec.getQualityStatus(), ""));
                    rec.setFreezeFlagMeaning(flagYnMap.getOrDefault(rec.getFreezeFlag(), ""));
                    rec.setTransformFlagMeaning(flagYnMap.getOrDefault(rec.getTransformFlag(), ""));
                });
            }

            List<String> dynamicTitles = displayFieldsCompletion(tenantId, list);

            // ???????????????????????????
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(FILE_NAME);
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // ????????????
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            titles.addAll(dynamicTitles);
            XSSFRow headerRow = sheet.createRow(0);
            this.fillRow(headerRow, titles, styles.get("normal"));

            //????????????
            AtomicInteger rowNumGen = new AtomicInteger(1);
            list.forEach(rec -> {
                int rowNum = rowNumGen.getAndIncrement();
                XSSFRow row = sheet.createRow(rowNum);
                List<String> fields = new ArrayList<>();
                fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWoMaterialCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWoMaterialName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getIdentification()).orElse(""));
                fields.add(Optional.ofNullable(rec.getLabCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                fields.add(rec.getWorkTime() != null ? DateUtil.format(rec.getWorkTime(), BaseConstants.Pattern.DATETIME) : "");
                fields.add(Optional.ofNullable(rec.getProcessWorkcellName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWorkcellName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getQualityStatusMeaning()).orElse(""));
                fields.add(Optional.ofNullable(rec.getEoStatusMeaning()).orElse(""));
                fields.add(Optional.ofNullable(rec.getFreezeFlagMeaning()).orElse(""));
                fields.add(Optional.ofNullable(rec.getTransformFlagMeaning()).orElse(""));
                fields.add(Optional.ofNullable(rec.getLatestNcTag()).orElse(""));
                fields.add(rec.getNcDate() != null ? DateUtil.format(rec.getNcDate(), BaseConstants.Pattern.DATETIME) : "");
                fields.add(Optional.ofNullable(rec.getWorker()).orElse(""));
                fields.add(rec.getShiftDate() != null ? DateUtil.format(rec.getShiftDate(), BaseConstants.Pattern.DATETIME) : "");
                fields.add(Optional.ofNullable(rec.getShiftCode()).orElse(""));
                // ?????????????????????
                fields.addAll(rec.getProList());
                this.fillRow(row, fields, styles.get("normal"));
            });

            workbook.write(bos);

            byte[] bytes = bos.toByteArray();

            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachMultipartFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API???HmeProcessCollectRepositoryImpl.asyncExport???";
            log.info(errorInfo);
            throw new CommonException(e);
        }finally {
            bos.close();
            //??????????????????
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
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName) {
        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //??????UUID
        ResponseEntity<Map<String,String>> uuidMap= hmeHzeroFileFeignClient.getAttachUUID(tenantId);
        String uuid = uuidMap.getBody().get(BaseConstants.FIELD_CONTENT);

        //??????????????????
        HmeExportTaskVO exportTaskVO = new HmeExportTaskVO();
        exportTaskVO.setTenantId(tenantId);
        exportTaskVO.setTaskCode(uuid);
        if (StringUtils.isBlank(fileName)) {
            fileName = FILE_NAME;
        }
        exportTaskVO.setTaskName(fileName);
        exportTaskVO.setServiceName("tarzan-mes-report");

        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        exportTaskVO.setHostName(localAddr + ":" + serverPort);
        exportTaskVO.setUserId(userId);
        exportTaskVO.setState(CommonUtils.ExportTaskStateValue.DOING);
        addExportTask(exportTaskVO);

        return exportTaskVO;
    }

    private List<HmeProcessCollectVO2> gpSelectList(Long tenantId, HmeProcessCollectQuery dto){
        //V20210712 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
        List<String> workcellIdList = mapper.selectWorkcellIdList(tenantId , dto);
        dto.setWorkcellIdList(workcellIdList);
        if(CollectionUtils.isEmpty(workcellIdList)){
            return new ArrayList<>();
        }

        //??????????????????????????????
        HmeProcessCollectVO3 processCollectVO3 = mapper.gpSelectTableName(tenantId , dto.getProcessCode());
        if(Objects.isNull(processCollectVO3)){
            return new ArrayList<>();
        }
        dto.setOperationId(processCollectVO3.getOperationId());

        Integer seqNum = mapper.gpSelectMaxSeqNum(tenantId , processCollectVO3.getOperationId());
        if(Objects.isNull(seqNum)){
            return new ArrayList<>();
        }
        dto.setSeqNum(seqNum);
        List<String> resultFieldList = new ArrayList<>();
        for(int i = 1 ; i <= seqNum ; i++){
            resultFieldList.add("result" + i);
        }
        dto.setResultFieldList(resultFieldList);
        dto.setTableName(processCollectVO3.getTableName());

        SecurityTokenHelper.close();
        return mapper.gpSelectList(tenantId, dto);
    }

    public Page<HmeProcessCollectVO2> gpGetPage(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest) {

        List<HmeProcessCollectVO2> resultList = gpSelectList(tenantId , dto);
        if(CollectionUtils.isEmpty(resultList)){
            return new Page<>();
        }

        return HmeCommonUtils.pagedList(pageRequest.getPage() , pageRequest.getSize() , resultList);
    }

    @Override
    public HmeProcessCollectTitleVO gpPagedList(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest) {
        //??????????????????
        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        dto.setSiteId(defaultSiteId);
        Page<HmeProcessCollectVO2> gpPage = self().gpGetPage(tenantId, dto, pageRequest);
        HmeProcessCollectTitleVO result = new HmeProcessCollectTitleVO(null , gpPage);
        List<String> dynamicTitles = mapper.gpSelectDynamicColDesc(tenantId , dto.getOperationId());
        result.setDynamicTitles(dynamicTitles);
        return result;
    }

    @Override
    public void gpExport(Long tenantId, HmeProcessCollectQuery dto, HttpServletRequest request, HttpServletResponse response) {
        //??????????????????
        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        dto.setSiteId(defaultSiteId);
        // ????????????
        List<HmeProcessCollectVO2> resultList = gpSelectList(tenantId , dto);
        List<String> dynamicTitles = mapper.gpSelectDynamicColDesc(tenantId , dto.getOperationId());

        // ???????????????????????????
        XSSFWorkbook workbook = new XSSFWorkbook();

        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String currentUserName = curUser == null ? "-1" : curUser.getUsername();
        //??????UUID
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME + ".xlsx";

        XSSFSheet sheet = workbook.createSheet(FILE_NAME);
        Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

        // ????????????
        List<String> titles = new ArrayList<>();
        titles.addAll(Arrays.asList(GP_FIXED_TITLE));
        titles.addAll(dynamicTitles);
        XSSFRow headerRow = sheet.createRow(0);
        this.fillRow(headerRow, titles, styles.get("normal"));

        //????????????
        AtomicInteger rowNumGen = new AtomicInteger(1);
        resultList.forEach(rec -> {
            int rowNum = rowNumGen.getAndIncrement();
            XSSFRow row = sheet.createRow(rowNum);
            List<String> fields = new ArrayList<>();
            fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWoMaterialCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWoMaterialName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getMaterialLotCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getSnMaterialCode()).orElse(""));
            fields.add(Optional.ofNullable(rec.getSnMaterialName()).orElse(""));
            fields.add(DateUtil.format(rec.getSiteInDate(), BaseConstants.Pattern.DATETIME));
            fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getWorkcellName()).orElse(""));
            fields.add(Optional.ofNullable(rec.getSiteInRealName()).orElse(""));
            fields.add(rec.getShiftDate() != null ? DateUtil.format(rec.getShiftDate(), BaseConstants.Pattern.DATETIME) : "");
            fields.add(Optional.ofNullable(rec.getShiftCode()).orElse(""));
            // ?????????????????????
            fields.addAll(HmeProcessCollectVO2.getResult(rec, dto.getSeqNum()));
            this.fillRow(row, fields, styles.get("normal"));
        });

        // ????????????
        FileUtils.downloadWorkbook(workbook, request, response, fileName);
    }

    @Override
    @Async
    public void gpAsyncExport(Long tenantId, HmeProcessCollectQuery dto) throws IOException {
        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        //??????????????????
        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(exportTaskVO.getUserId());
        dto.setSiteId(defaultSiteId);
        if(Objects.isNull(dto.getExportTaskVO())){
            //??????????????????
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("??????????????????????????????!");
            updateExportTask(exportTaskVO);
            return;
        }
        //??????????????????
        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //????????????
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {

            // ????????????
            List<HmeProcessCollectVO2> resultList = gpSelectList(tenantId , dto);
            List<String> dynamicTitles = mapper.gpSelectDynamicColDesc(tenantId , dto.getOperationId());

            // ???????????????????????????
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(FILE_NAME);
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // ????????????
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            titles.addAll(dynamicTitles);
            XSSFRow headerRow = sheet.createRow(0);
            this.fillRow(headerRow, titles, styles.get("normal"));

            //????????????
            AtomicInteger rowNumGen = new AtomicInteger(1);
            resultList.forEach(rec -> {
                int rowNum = rowNumGen.getAndIncrement();
                XSSFRow row = sheet.createRow(rowNum);
                List<String> fields = new ArrayList<>();
                fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWoMaterialCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWoMaterialName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getMaterialLotCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getSnMaterialCode()).orElse(""));
                fields.add(Optional.ofNullable(rec.getSnMaterialName()).orElse(""));
                fields.add(DateUtil.format(rec.getSiteInDate(), BaseConstants.Pattern.DATETIME));
                fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getWorkcellName()).orElse(""));
                fields.add(Optional.ofNullable(rec.getSiteInRealName()).orElse(""));
                fields.add(rec.getShiftDate() != null ? DateUtil.format(rec.getShiftDate(), BaseConstants.Pattern.DATETIME) : "");
                fields.add(Optional.ofNullable(rec.getShiftCode()).orElse(""));
                // ?????????????????????
                fields.addAll(HmeProcessCollectVO2.getResult(rec, dto.getSeqNum()));
                this.fillRow(row, fields, styles.get("normal"));
            });

            workbook.write(bos);

            byte[] bytes = bos.toByteArray();

            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachMultipartFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API???HmeProcessCollectRepositoryImpl.asyncExport???";
            log.info(errorInfo);
            throw new CommonException(e);
        }finally {
            bos.close();
            //??????????????????
            exportTaskVO.setState(state);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            if(Objects.nonNull(returnStr)) {
                exportTaskVO.setDownloadUrl(returnStr.getBody());
            }
            exportTaskVO.setErrorInfo(errorInfo);
            updateExportTask(exportTaskVO);
        }
    }

    /**
     * ???????????????
     *
     * @param row    ???
     * @param fields ??????
     */
    private void fillRow(XSSFRow row, List<String> fields, XSSFCellStyle style) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            XSSFCell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(field);
            cell.setCellStyle(style);
        });
    }

    private List<String> displayFieldsCompletion(Long tenantId, List<HmeProcessCollectVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        // ??????????????????
        List<String> materialLotIds = list.stream().map(HmeProcessCollectVO::getMaterialLotId).collect(Collectors.toList());
        List<MaterialLotLabCodeVO> labCodeList = loadJobMapper.selectLabCodeList(tenantId, materialLotIds);
        Map<String, String> labCodeMap = labCodeList.stream().collect(Collectors.groupingBy(MaterialLotLabCodeVO::getMaterialLotId, Collectors.mapping(MaterialLotLabCodeVO::getLabCode, Collectors.joining("/"))));
        // ?????????????????????
        List<String> jobIdList = list.stream().map(HmeProcessCollectVO::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeProcessCollectProVO> detailList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jobIdList)) {

            List<List<String>> allJobIdList = Utils.splitSqlList(jobIdList,3000);
            for (List<String> subJobIdList : allJobIdList
                 ) {
                List<HmeProcessCollectProVO> subDetailList = mapper.selectCollectProBatchList(tenantId, subJobIdList);
                if(CollectionUtils.isNotEmpty(subDetailList)){
                    detailList.addAll(subDetailList);
                }
            }
        }
        // ??????????????????
        List<String> dynamicTitles = detailList.stream().map(HmeProcessCollectProVO::getProName).distinct().collect(Collectors.toList());

        // ??????????????????
        List<String> eoIdList = list.stream().map(HmeProcessCollectVO::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<EoNcRecordVO> ncRecordList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            ncRecordList = mapper.selectEoNcBatchList(tenantId, eoIdList);
        }
        Map<String, List<EoNcRecordVO>> ncMap = ncRecordList.stream().collect(Collectors.groupingBy(e -> e.getEoId()));

        // ????????????MAP
        Map<String, Map<String, String>> tagMap = detailList.stream().collect(Collectors.groupingBy(HmeProcessCollectProVO::getJobId, Collectors.toMap(HmeProcessCollectProVO::getProName, HmeProcessCollectProVO::getProResult, (a, b) -> a)));
        list.forEach(c -> {
            Map<String, String> jobTagMap = tagMap.get(c.getJobId());
            List<EoNcRecordVO> ncRecordVOList = ncMap.getOrDefault(c.getEoId() , new ArrayList<>());
            if (!(jobTagMap == null || jobTagMap.size() == 0)) {
                List<String> proList = new ArrayList<>();
                dynamicTitles.forEach(proName -> proList.add(jobTagMap.getOrDefault(proName, "")));
                c.setProList(proList);
            } else {
                List<String> proList = new ArrayList<>();
                dynamicTitles.forEach(proName -> proList.add(""));
                c.setProList(proList);
            }
            if (CollectionUtils.isNotEmpty(ncRecordVOList)) {
                EoNcRecordVO eoNcRecordVO = ncRecordVOList.get(0);
                c.setNcDate(eoNcRecordVO.getNcDate());
                c.setLatestNcTag(eoNcRecordVO.getLatestNcTag());
            }
            c.setLabCode(labCodeMap.getOrDefault(c.getMaterialLotId(), null));
        });
        return dynamicTitles;
    }
}
