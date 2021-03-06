package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeTagCheckRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCommonReportMapper;
import com.ruike.hme.infra.mapper.HmeTagCheckMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.FileUtils;
import com.ruike.hme.infra.util.HmeCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.common.domain.sys.MtException;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import tarzan.common.domain.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:44
 */
@Component
@Slf4j
public class HmeTagCheckRepositoryImpl implements HmeTagCheckRepository {

    @Autowired
    private HmeTagCheckMapper hmeTagCheckMapper;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCommonReportMapper hmeCommonReportMapper;
    @Autowired
    private MtUserClient mtUserClient;


    private static final String FILE_NAME = "?????????????????????";

    private static final String[] FIXED_TITLE = {"?????????", "???????????????", "?????????????????????", "??????SN", "??????????????????", "??????????????????"};

    @Override
    public Page<HmeTagCheckVO2> queryList(Long tenantId, HmeTagCheckQueryVO queryVO, PageRequest pageRequest) {
        verifyData(tenantId, queryVO);
        List<HmeTagCheckVO2> hmeTagCheckVO2List = this.queryTagCheckMaterialLotList(tenantId, queryVO);
        Page<HmeTagCheckVO2> pageObj = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeTagCheckVO2List);
        this.handleData(tenantId, pageObj.getContent(), queryVO);
        return pageObj;
    }

    /**
     * ????????????????????????Sn?????????
     *
     * @param tenantId
     * @param queryVO
     * @author sanfeng.zhang@hand-china.com 2021/9/2 15:31
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    private List<HmeTagCheckVO2> queryTagCheckMaterialLotList(Long tenantId, HmeTagCheckQueryVO queryVO) {
        List<HmeTagCheckVO2> hmeTagCheckVO2List = new ArrayList<>();

        Long userId = queryVO.getUserId();
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }

        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(userId);
        Date siteOutDateFrom = null;
        Date siteOutDateTo = null;
        if (StringUtils.isNotBlank(queryVO.getSiteOutDateFrom())) {
            siteOutDateFrom = DateUtil.string2Date(queryVO.getSiteOutDateFrom(), "yyyy-MM-dd HH:mm:ss");
        }
        if (StringUtils.isNotBlank(queryVO.getSiteOutDateTo())) {
            siteOutDateTo = DateUtil.string2Date(queryVO.getSiteOutDateTo(), "yyyy-MM-dd HH:mm:ss");
        }
        Date finalSiteOutDateFrom = siteOutDateFrom;
        Date finalSiteOutDateTo = siteOutDateTo;
        // ???????????? ??????SN?????????????????????
        if (StringUtils.equals(queryVO.getRuleType(), "SN_DATA")) {
            // ????????????????????????
            List<HmeTagCheckVO> tagCheckVOList = hmeTagCheckMapper.querySnMaterialLotCodeJobList(tenantId, queryVO, defaultSiteId);
            // ????????????????????????????????? ?????????????????????
            Map<String, List<HmeTagCheckVO>> tagCheckVOMap = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO::getMaterialLotId));

            // ???????????????????????????
            tagCheckVOMap.forEach((key, value) -> {
                HmeTagCheckVO hmeTagCheckVO = value.get(0);
                HmeTagCheckVO2 checkVO2 = new HmeTagCheckVO2();
                checkVO2.setMaterialLotCode(hmeTagCheckVO.getMaterialLotCode());
                checkVO2.setMaterialCode(hmeTagCheckVO.getMaterialCode());
                checkVO2.setMaterialName(hmeTagCheckVO.getMaterialName());
                checkVO2.setComponentMaterialLotCode("");
                // ????????????????????? ???????????????????????????????????? ????????????????????????????????? ????????????????????????????????????????????????
                List<HmeTagCheckVO> maxJobList = this.queryMaxJobList(finalSiteOutDateFrom, finalSiteOutDateTo, value);
                // ??????????????????????????????????????? ????????????
                if (CollectionUtils.isNotEmpty(maxJobList)) {
                    checkVO2.setJobList(maxJobList);
                    hmeTagCheckVO2List.add(checkVO2);
                }
            });
        } else {
            // ?????????????????????????????????????????????????????????
            List<String> materialIdList = hmeTagCheckMapper.queryItemGroupMaterialByHeaderId(tenantId, queryVO.getRuleHeaderIdList());
            if (CollectionUtils.isNotEmpty(materialIdList)) {
                // ??????????????? ?????????????????? ??????????????????????????????????????????
                List<HmeTagCheckVO> componentMaterialLotList = hmeTagCheckMapper.queryCmbMaterialLotCodeList(tenantId, queryVO, materialIdList);
                // ??????SN??????????????? ?????????????????? ?????????????????????
                Map<String, List<HmeTagCheckVO>> groupSnAndMaterialMap = componentMaterialLotList.stream().collect(Collectors.groupingBy(cm -> cm.getMaterialLotId() + "_" + cm.getComponentMaterialId()));
//                List<HmeTagCheckVO> filterComponentMaterialLotList = new ArrayList<>();
//                groupSnAndMaterialMap.forEach((key, value) -> {
//                    List<HmeTagCheckVO> sortValueList = value.stream().sorted(Comparator.comparing(HmeTagCheckVO::getSiteOutDate).reversed()).collect(Collectors.toList());
//                    filterComponentMaterialLotList.add(sortValueList.get(0));
//                });
                // ???????????????????????????SN????????????????????? ??????????????????????????? ??????????????? ?????????????????????????????????
                List<String> componentMaterialLotIdList = componentMaterialLotList.stream().map(HmeTagCheckVO::getComponentMaterialLotId).collect(Collectors.toList());
                // SN??????????????????????????????
                Map<String, HmeTagCheckVO> componentMaterialLotMap = componentMaterialLotList.stream().collect(Collectors.toMap(HmeTagCheckVO::getComponentMaterialLotId, Function.identity()));
                if (CollectionUtils.isNotEmpty(componentMaterialLotIdList)) {
                    List<HmeTagCheckVO> componentJobList = hmeTagCheckMapper.queryCmbMaterialLotCodeJobList(tenantId, componentMaterialLotIdList, defaultSiteId);
                    // ????????????????????????????????? ?????????????????????
                    Map<String, List<HmeTagCheckVO>> tagCheckVOMap = componentJobList.stream().collect(Collectors.groupingBy(HmeTagCheckVO::getMaterialLotId));
                    // ???????????????????????????
                    tagCheckVOMap.forEach((key, value) -> {
                        HmeTagCheckVO hmeTagCheckVO = componentMaterialLotMap.get(key);
                        if (hmeTagCheckVO != null) {
                            HmeTagCheckVO2 checkVO2 = new HmeTagCheckVO2();
                            checkVO2.setMaterialLotCode(hmeTagCheckVO.getMaterialLotCode());
                            checkVO2.setMaterialCode(hmeTagCheckVO.getMaterialCode());
                            checkVO2.setMaterialName(hmeTagCheckVO.getMaterialName());
                            checkVO2.setComponentMaterialLotCode(hmeTagCheckVO.getComponentMaterialLotCode());
                            checkVO2.setComponentMaterialCode(hmeTagCheckVO.getComponentMaterialCode());
                            checkVO2.setComponentMaterialName(hmeTagCheckVO.getComponentMaterialName());
                            // ????????????????????? ???????????????????????????????????? ????????????????????????????????? ????????????????????????????????????????????????
                            List<HmeTagCheckVO> maxJobList = this.queryMaxJobList(finalSiteOutDateFrom, finalSiteOutDateTo, value);
                            // ??????????????????????????????????????? ????????????
                            if (CollectionUtils.isNotEmpty(maxJobList)) {
                                checkVO2.setJobList(maxJobList);
                                hmeTagCheckVO2List.add(checkVO2);
                            }
                        }
                    });
                }
            }
        }
        // ?????????????????????SN????????????
        List<HmeTagCheckVO2> sortResultList = hmeTagCheckVO2List.stream().sorted(Comparator.comparing(HmeTagCheckVO2::getMaterialLotCode).thenComparing(HmeTagCheckVO2::getComponentMaterialLotCode, Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
        return sortResultList;
    }

    private List<HmeTagCheckVO> queryMaxJobList (Date finalSiteOutDateFrom, Date finalSiteOutDateTo, List<HmeTagCheckVO> tagCheckVOList) {
        Map<String, List<HmeTagCheckVO>> groupProcessList = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO::getProcessId));
        List<HmeTagCheckVO> maxJobList = new ArrayList<>();
        for (Map.Entry<String, List<HmeTagCheckVO>> groupProcessEntry : groupProcessList.entrySet()) {
            List<HmeTagCheckVO> sortList = groupProcessEntry.getValue().stream().sorted(Comparator.comparing(HmeTagCheckVO::getSiteOutDate).reversed()).collect(Collectors.toList());
            Boolean addFlag = false;
            if (finalSiteOutDateFrom != null && finalSiteOutDateFrom.after(sortList.get(0).getSiteOutDate())) {
                addFlag = true;
            }
            if (finalSiteOutDateTo != null && finalSiteOutDateTo.before(sortList.get(0).getSiteOutDate())) {
                addFlag = true;
            }
            if (!addFlag) {
                maxJobList.add(sortList.get(0));
            }
        }
        return maxJobList;
    }

    /**
     * ???????????????
     *
     * @param tenantId
     * @param hmeTagCheckVO2List
     * @author sanfeng.zhang@hand-china.com 2021/9/1 16:23
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    private List<HmeTagCheckVO2> handleData(Long tenantId, List<HmeTagCheckVO2> hmeTagCheckVO2List, HmeTagCheckQueryVO queryVO) {
        // ????????????????????????????????????????????????????????????
        List<HmeTagCheckVO3> hmeTagCheckVO3s = hmeTagCheckMapper.queryTagCheckList(tenantId, queryVO);
        if (CollectionUtils.isEmpty(hmeTagCheckVO2List) || CollectionUtils.isEmpty(hmeTagCheckVO3s)) {
            return hmeTagCheckVO2List;
        }
        // ?????? ?????????????????????????????????????????????????????????????????????????????????
        List<HmeTagCheckVO3> distinctTagCheckList = hmeTagCheckVO3s.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(obj -> obj.getTagId() + "," + obj.getSourceWorkcellId()))), ArrayList::new));
        // ??????Job???tag????????? ????????????????????? ??????????????????????????????job
        List<HmeTagCheckVO> tagCheckVOList = new ArrayList<>();
        List<HmeTagCheckVO> processTagList = null;
        HmeTagCheckVO tagCheckVO = null;
        Map<String, List<HmeTagCheckVO3>> groupProcessTagMap = distinctTagCheckList.stream().collect(Collectors.groupingBy(HmeTagCheckVO3::getSourceWorkcellId));
        for (HmeTagCheckVO2 hmeTagCheckVO2 : hmeTagCheckVO2List) {
            if (CollectionUtils.isNotEmpty(hmeTagCheckVO2.getJobList())) {
                // ?????????????????????????????????
                processTagList = new ArrayList<>();
                for (HmeTagCheckVO hmeTagCheckVO : hmeTagCheckVO2.getJobList()) {
                    // ????????????????????????????????????
                    List<HmeTagCheckVO3> tagCheckList = groupProcessTagMap.getOrDefault(hmeTagCheckVO.getProcessId(), Collections.EMPTY_LIST);
                    for (HmeTagCheckVO3 hmeTagCheckVO3 : tagCheckList) {
                        tagCheckVO = new HmeTagCheckVO();
                        tagCheckVO.setTagId(hmeTagCheckVO3.getTagId());
                        tagCheckVO.setTagCode(hmeTagCheckVO3.getTagCode());
                        tagCheckVO.setTagDescription(hmeTagCheckVO3.getTagDescription());
                        tagCheckVO.setProcessId(hmeTagCheckVO3.getSourceWorkcellId());
                        tagCheckVO.setProcessCode(hmeTagCheckVO3.getProcessCode());
                        tagCheckVO.setProcessName(hmeTagCheckVO3.getProcessName());
                        tagCheckVO.setJobId(hmeTagCheckVO.getJobId());
                        tagCheckVOList.add(tagCheckVO);
                        processTagList.add(tagCheckVO);
                    }
                }
                hmeTagCheckVO2.setJobList(processTagList);
            }
        }
        // ??????jobId?????????????????????????????????
        List<HmeTagCheckVO5> recordResultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagCheckVOList)) {
            recordResultList =  hmeTagCheckMapper.queryRecordResult(tenantId, tagCheckVOList);
        }

        // ???????????????????????????????????? ??????????????????????????????????????????????????????????????? ???????????????
        tagCheckVOList = tagCheckVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(obj -> obj.getTagId() + "," + obj.getProcessId()))), ArrayList::new))
                .stream().sorted(Comparator.comparing(HmeTagCheckVO::getProcessId).thenComparing(HmeTagCheckVO::getTagId)).collect(Collectors.toList());

        Map<String, List<HmeTagCheckVO5>> recordResultMap = recordResultList.stream().collect(Collectors.groupingBy(vo -> vo.getJobId() + "_" + vo.getTagId()));
        //????????????????????? ???????????????????????????????????? ??????????????????
        List<HmeTagCheckVO4> processList = null;
        List<HmeTagCheckVO5> tagList = null;
        Map<String, List<HmeTagCheckVO>> processMap = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO::getProcessId, LinkedHashMap::new, Collectors.toList()));
        for (HmeTagCheckVO2 tagVo : hmeTagCheckVO2List) {
            // ????????????????????????????????? ???????????????????????????????????? ???????????? ???????????????????????????????????????????????????
            Map<String, HmeTagCheckVO> processTagMap = tagVo.getJobList().stream().collect(Collectors.toMap(tag -> tag.getProcessId() + "_" + tag.getTagId(), Function.identity()));
            processList = new ArrayList<>();
            for (Map.Entry<String, List<HmeTagCheckVO>> processEntry : processMap.entrySet()) {
                List<HmeTagCheckVO> tagResultList = processEntry.getValue();
                HmeTagCheckVO4 checkVO4 = new HmeTagCheckVO4();
                checkVO4.setProcessCode(tagResultList.get(0).getProcessCode());
                checkVO4.setProcessName(tagResultList.get(0).getProcessName());
                tagList = new ArrayList<>();
                for (HmeTagCheckVO hmeTagCheckVO : tagResultList) {
                    HmeTagCheckVO5 checkVO5 = new HmeTagCheckVO5();
                    checkVO5.setTagId(hmeTagCheckVO.getTagId());
                    checkVO5.setTagCode(hmeTagCheckVO.getTagCode());
                    checkVO5.setTagDescription(hmeTagCheckVO.getTagDescription());
                    HmeTagCheckVO jobVo = processTagMap.get(tagResultList.get(0).getProcessId() + "_" + hmeTagCheckVO.getTagId());
                    checkVO5.setResult("");
                    if (jobVo != null) {
                        List<HmeTagCheckVO5> resultList = recordResultMap.get(jobVo.getJobId() + "_" + hmeTagCheckVO.getTagId());
                        if (CollectionUtils.isNotEmpty(resultList)) {
                            checkVO5.setResult(resultList.get(0).getResult());
                        }
                    }
                    tagList.add(checkVO5);
                }
                checkVO4.setTagList(tagList);
                processList.add(checkVO4);
            }
            tagVo.setProcessList(processList);
        }
       return hmeTagCheckVO2List;
    }

    private void verifyData(Long tenantId, HmeTagCheckQueryVO queryVO) {
        // ??????????????????????????????????????????
        if (StringUtils.isBlank(queryVO.getBusinessId())) {
            throw new MtException("HME_TAG_CHECK_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_018", "HME", "?????????"));
        }
        if (StringUtils.isBlank(queryVO.getRuleType())) {
            throw new MtException("HME_TAG_CHECK_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_018", "HME", "??????"));
        }
        if (CollectionUtils.isEmpty(queryVO.getRuleHeaderIdList())) {
            throw new MtException("HME_TAG_CHECK_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_018", "HME", "????????????"));
        }
        // ???????????????????????????????????????????????
        if (CollectionUtils.isEmpty(queryVO.getMaterialLotCodeList()) &&
            CollectionUtils.isEmpty(queryVO.getWorkOrderNumList()) &&
            CollectionUtils.isEmpty(queryVO.getMaterialCodeList())) {
            throw new MtException("HME_TAG_CHECK_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_017", "HME"));
        }
    }

    private List<HmeTagCheckVO2> queryExcelExportList(Long tenantId, HmeTagCheckQueryVO queryVO) {
        List<HmeTagCheckVO2> hmeTagCheckVO2List = this.queryTagCheckMaterialLotList(tenantId, queryVO);
        this.handleData(tenantId, hmeTagCheckVO2List, queryVO);
        return hmeTagCheckVO2List;
    }

    @Override
    @Async
    public void asyncExport(Long tenantId, HmeTagCheckQueryVO dto) throws IOException {
        HmeExportTaskVO exportTaskVO = dto.getExportTaskVO();
        if(Objects.isNull(dto.getExportTaskVO())){
            //??????????????????
            exportTaskVO.setState(CommonUtils.ExportTaskStateValue.CANCELED);
            exportTaskVO.setEndDateTime(CommonUtils.currentTimeGet());
            exportTaskVO.setErrorInfo("??????????????????????????????!");
            updateExportTask(exportTaskVO);
            return;
        }
        // ?????????????????????????????? ??????????????????
        dto.setUserId(exportTaskVO.getUserId());

        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        //??????????????????
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();

        //??????UUID
        String uuid = exportTaskVO.getTaskCode();;
        String prefix = uuid + "@??????-" + currentUserName + "@??????-" + cn.hutool.core.date.DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //????????????
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // ????????????
            List<HmeTagCheckVO2> resultList = this.queryExcelExportList(tenantId, dto);
            // ?????????
            List<HmeTagCheckVO4> dynamicTitles = displayFieldsCompletion(tenantId, resultList);

            // ???????????????????????????
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(exportTaskVO.getTaskName());
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // ????????????
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            List<String> processList = dynamicTitles.stream().map(HmeTagCheckVO4::getProcessName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(processList)) {
                titles.addAll(processList);
            }
            XSSFRow headerRow = sheet.createRow(0);
            this.fillHeaderRow(sheet, headerRow, titles, styles.get("center"), dynamicTitles);

            //????????????
            //?????????????????? ?????????????????? ????????????1 ?????????2
            Integer rowIndex = CollectionUtils.isNotEmpty(dynamicTitles) ? 2 : 1;
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (HmeTagCheckVO2 rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getComponentMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getComponentMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getComponentMaterialName()).orElse(""));
                    // ?????????????????????
                    if(CollectionUtils.isNotEmpty(rec.getProcessList())) {
                        fields.addAll(this.dynamicLine(rec.getProcessList()));
                    }
                    this.fillRow(row, fields, styles.get("center"));
                    rowIndex++;
                }
            }
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachMultipartFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API???HmeProdLinePassRateServiceImpl.asyncOnlineReportExport???";
            throw new CommonException(e);
        }
        finally {
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

    private List<String> dynamicLine(List<HmeTagCheckVO4> processList) {
        List<String> resultLineList = new ArrayList<>();
        processList.forEach(md -> {
            for (HmeTagCheckVO5 checkVO5 : md.getTagList()) {
                resultLineList.add(Optional.ofNullable(checkVO5.getResult()).orElse(""));
            }
        });
        return resultLineList;
    }

    private void fillRow(XSSFRow row, List<String> fields, XSSFCellStyle style) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            XSSFCell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(field);
            cell.setCellStyle(style);
        });
    }

    /**
     * ???????????????
     *
     * @param row    ???
     * @param fields ??????
     */
    private void fillHeaderRow(XSSFSheet sheet, XSSFRow row, List<String> fields, XSSFCellStyle style, List<HmeTagCheckVO4> dynamicTitles) {
        Integer headerIndex = 0;
        Map<String, HmeTagCheckVO4> processMap = dynamicTitles.stream().collect(Collectors.toMap(HmeTagCheckVO4::getProcessName, Function.identity()));
        for (String field : fields) {
            XSSFCell cell = row.createCell(headerIndex);
            cell.setCellStyle(style);
            cell.setCellValue(field);
            if (CollectionUtils.isNotEmpty(dynamicTitles)) {
                if (headerIndex.compareTo(5) > 0) {
                    // ??????????????????????????? ?????????????????? ???????????????
                    HmeTagCheckVO4 checkVO4 = processMap.get(field);
                    List<HmeTagCheckVO5> tagList = checkVO4.getTagList();
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, headerIndex, headerIndex + tagList.size()-1));
                    headerIndex += tagList.size();
                } else {
                    headerIndex++;
                }
            } else {
                headerIndex++;
            }
        }
        // ??????????????? ??????????????????????????????
        if (CollectionUtils.isNotEmpty(dynamicTitles)) {
            XSSFRow subHeaderRow = sheet.createRow(1);
            // ?????????
            Integer subIndex = 6;
            for (HmeTagCheckVO4 tagVo : dynamicTitles) {
                for (HmeTagCheckVO5 checkVO5 : tagVo.getTagList()) {
                    XSSFCell hssfCell = subHeaderRow.createCell(subIndex++);
                    hssfCell.setCellValue(checkVO5.getTagDescription());
                    hssfCell.setCellStyle(style);
                }
            }
            // ????????????????????????????????? ?????????
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
        }
    }

    private List<HmeTagCheckVO4> displayFieldsCompletion (Long tenantId, List<HmeTagCheckVO2> resultList) {
        if (CollectionUtils.isNotEmpty(resultList)) {
            return resultList.get(0).getProcessList();
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response) {
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
}
