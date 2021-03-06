package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeInputRecordDTO;
import com.ruike.hme.api.dto.HmePlanRateReportResponseDTO;
import com.ruike.hme.app.service.HmeInputRecordService;
import com.ruike.hme.domain.vo.HmeCosReturnVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeInputRecordVO;
import com.ruike.hme.domain.vo.WorkcellVO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeInputRecordMapper;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import com.ruike.hme.infra.util.*;
import com.ruike.qms.infra.mapper.QmsIqcInspectionKanbanMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xssf.usermodel.*;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * ?????????????????????????????????????????????
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:22:12
 **/
@Service
public class HmeInputRecordServiceImpl implements HmeInputRecordService {

    @Autowired
    private HmeInputRecordMapper hmeInputRecordMapper;

    @Autowired
    private QmsIqcInspectionKanbanMapper qmsIqcInspectionKanbanMapper;

    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private MtUserClient mtUserClient;

    private static final String FILE_TASK_NAME = "??????????????????";

    private static final String[] FIXED_TITLE = {"?????????", "????????????", "??????????????????", "??????????????????", "????????????", "??????????????????", "??????", "????????????",	"??????", "??????", "????????????", "SN??????", "????????????",	"????????????",	"????????????",	"BOM??????", "???????????????", "??????", "???????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "????????????????????????"};


    public String defaultSiteUi(Long tenantId) {
        // ????????????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<String> siteIdList = qmsIqcInspectionKanbanMapper.queryDefaultSiteByUserId(tenantId, userId);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(siteIdList) || siteIdList.size() > 1) {
            throw new CommonException("??????????????????????????????????????????,?????????!");
        }
        return siteIdList.get(0);
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param parentWorkcellIdList ??????ID
     * @author penglin.sui@hand-china.com 2021/5/10 15:17
     * @return java.util.Map<java.lang.String, com.ruike.hme.domain.vo.WorkcellVO>
     */
    private Map<String,WorkcellVO> selectProcess(Long tenantId, List<String> parentWorkcellIdList){
        Map<String,WorkcellVO> processMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(parentWorkcellIdList)){
            return processMap;
        }
        List<WorkcellVO> allProcessList = new ArrayList<>();
        List<List<String>> splitParentWorkcellIdList = WmsCommonUtils.splitSqlList(parentWorkcellIdList , 1000);
        for (List<String> parentWorkcellIdList2 : splitParentWorkcellIdList
             ) {
            List<WorkcellVO> processList = hmeInputRecordMapper.selectProcess(tenantId, parentWorkcellIdList2);
            if(CollectionUtils.isNotEmpty(processList)){
                allProcessList.addAll(processList);
            }
        }
        if (CollectionUtils.isNotEmpty(allProcessList)) {
            processMap = allProcessList.stream().collect(Collectors.toMap(WorkcellVO::getParentWorkcellId, t -> t));
        }
        return processMap;
    }

    private void queryEo(Long tenantId , HmeInputRecordDTO dto){
        if(CollectionUtils.isNotEmpty(dto.getWorkOrderNumIdList())){
            List<String> eoIdList = hmeInputRecordMapper.getEoIdByWorkOrderId(tenantId , dto.getWorkOrderNumIdList());
            if(CollectionUtils.isNotEmpty(eoIdList)){
                if(CollectionUtils.isNotEmpty(dto.getEoIdList())){
                    List<String> eoIdList2 = new ArrayList<>();
                    for (String eoId : dto.getEoIdList()
                         ) {
                        if(eoIdList.contains(eoId)){
                            eoIdList2.add(eoId);
                        }
                    }
                    dto.setEoIdList(eoIdList2);
                }else{
                    dto.setEoIdList(eoIdList);
                }
            }
        }
    }

    @Override
    @ProcessLovValue
    public Page<HmeInputRecordVO> inputRecord(Long tenantId, HmeInputRecordDTO dto, PageRequest pageRequest) {

        //????????????????????????
        dto.setSiteId(defaultSiteUi(tenantId));

        //V20210802 modify by penglin.sui for peng.zhao
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //????????????????????????????????????????????????
            List<String> processList = Arrays.asList(dto.getProcessId().split(","));
            workcellIdList = hmeInputRecordMapper.getWorkcellByProcess(tenantId, processList);
        }
        List<String> selectWorkcellIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWorkcellId())){
            selectWorkcellIdList = Arrays.asList(dto.getWorkcellId().split(","));
        }

        if(CollectionUtils.isNotEmpty(workcellIdList)){
            if(CollectionUtils.isNotEmpty(selectWorkcellIdList)){
                List<String> intersectionWkcIdList = new ArrayList<>();
                for (String workcellId : workcellIdList
                     ) {
                    if(selectWorkcellIdList.contains(workcellId)){
                        intersectionWkcIdList.add(workcellId);
                    }
                }

                if(CollectionUtils.isNotEmpty(intersectionWkcIdList)){
                    dto.setWorkcellIdList(intersectionWkcIdList);
                }

            }else{
                dto.setWorkcellIdList(workcellIdList);
            }
        }else if(CollectionUtils.isNotEmpty(selectWorkcellIdList)){
            dto.setWorkcellIdList(selectWorkcellIdList);
        }

        queryEo(tenantId , dto);

//        Page<HmeInputRecordVO> result = PageHelper.doPage(pageRequest, () -> hmeInputRecordMapper.cosFunctionReportQuery(tenantId,dto));

        List<HmeInputRecordVO> resultList = new ArrayList<>();
        List<HmeInputRecordVO> inputRecordVOOfSnList = hmeInputRecordMapper.cosFunctionReportQueryOfSn(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfSnList)){
            resultList.addAll(inputRecordVOOfSnList);
        }

        List<HmeInputRecordVO> inputRecordVOOfLotTimeList = hmeInputRecordMapper.cosFunctionReportQueryOfLotTime(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfLotTimeList)){
            resultList.addAll(inputRecordVOOfLotTimeList);
        }

        List<HmeInputRecordVO> inputRecordVOOfWoList = hmeInputRecordMapper.cosFunctionReportQueryOfWo(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfWoList)){
            resultList.addAll(inputRecordVOOfWoList);
        }

        Page<HmeInputRecordVO> result = new Page<>();
        if(CollectionUtils.isNotEmpty(resultList)){

            //??????
            resultList = resultList.stream().sorted(Comparator.comparing(HmeInputRecordVO::getCreationDate).reversed()).collect(Collectors.toList());

            //??????
            result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        }

        if(CollectionUtils.isNotEmpty(result)) {

            Map<String,WorkcellVO> processMap = new HashMap<>();
            if(StringUtils.isBlank(dto.getProcessId())) {
                List<String> parentWorkcellIdList = result.stream().map(HmeInputRecordVO::getWorkcellId).distinct().filter(Objects::nonNull)
                        .collect(Collectors.toList());
                processMap = selectProcess(tenantId,parentWorkcellIdList);
            }

            for (HmeInputRecordVO temp : result) {
                if (Strings.isNotBlank(temp.getAttribute14())) {
                    temp.setAttrValue(temp.getAttribute14());
                } else {
                    if (Strings.isNotBlank(temp.getAttribute1()) && "2".equals(temp.getAttribute1())) {
                        temp.setAttrValue("??????");
                    }
                }

                if(StringUtils.isBlank(dto.getProcessId())) {
                    WorkcellVO process = processMap.getOrDefault(temp.getWorkcellId(), null);
                    if (Objects.nonNull(process)) {
                        temp.setProcessName(process.getWorkcellName());
                    }
                }
            }
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeInputRecordVO> inputRecordExport(Long tenantId, HmeInputRecordDTO dto) {

        //????????????????????????
        dto.setSiteId(defaultSiteUi(tenantId));

        //V20210802 modify by penglin.sui for peng.zhao
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //????????????????????????????????????????????????
            List<String> processList = Arrays.asList(dto.getProcessId().split(","));
            workcellIdList = hmeInputRecordMapper.getWorkcellByProcess(tenantId, processList);
        }
        List<String> selectWorkcellIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getWorkcellId())){
            selectWorkcellIdList = Arrays.asList(dto.getWorkcellId().split(","));
        }

        if(CollectionUtils.isNotEmpty(workcellIdList)){
            if(CollectionUtils.isNotEmpty(selectWorkcellIdList)){
                List<String> intersectionWkcIdList = new ArrayList<>();
                for (String workcellId : workcellIdList
                ) {
                    if(selectWorkcellIdList.contains(workcellId)){
                        intersectionWkcIdList.add(workcellId);
                    }
                }

                if(CollectionUtils.isNotEmpty(intersectionWkcIdList)){
                    dto.setWorkcellIdList(intersectionWkcIdList);
                }

            }else{
                dto.setWorkcellIdList(workcellIdList);
            }
        }else if(CollectionUtils.isNotEmpty(selectWorkcellIdList)){
            dto.setWorkcellIdList(selectWorkcellIdList);
        }

        queryEo(tenantId , dto);

//        List<HmeInputRecordVO> resultList = hmeInputRecordMapper.cosFunctionReportQuery(tenantId,dto);

        List<HmeInputRecordVO> resultList = new ArrayList<>();
        List<HmeInputRecordVO> inputRecordVOOfSnList = hmeInputRecordMapper.cosFunctionReportQueryOfSn(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfSnList)){
            resultList.addAll(inputRecordVOOfSnList);
        }

        List<HmeInputRecordVO> inputRecordVOOfLotTimeList = hmeInputRecordMapper.cosFunctionReportQueryOfLotTime(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfLotTimeList)){
            resultList.addAll(inputRecordVOOfLotTimeList);
        }

        List<HmeInputRecordVO> inputRecordVOOfWoList = hmeInputRecordMapper.cosFunctionReportQueryOfWo(tenantId , dto);
        if(CollectionUtils.isNotEmpty(inputRecordVOOfWoList)){
            resultList.addAll(inputRecordVOOfWoList);
        }

        if(CollectionUtils.isNotEmpty(resultList)) {

            Map<String,WorkcellVO> processMap = new HashMap<>();
            if(StringUtils.isBlank(dto.getProcessId())) {
                List<String> parentWorkcellIdList = resultList.stream().map(HmeInputRecordVO::getWorkcellId).distinct().filter(Objects::nonNull)
                        .collect(Collectors.toList());
                processMap = selectProcess(tenantId,parentWorkcellIdList);
            }

            for (HmeInputRecordVO temp : resultList) {
                if (Strings.isNotBlank(temp.getAttribute14())) {
                    temp.setAttrValue(temp.getAttribute14());
                } else {
                    if (Strings.isNotBlank(temp.getAttribute1()) && "2".equals(temp.getAttribute1())) {
                        temp.setAttrValue("??????");
                    }
                }

                if(StringUtils.isBlank(dto.getProcessId())) {
                    WorkcellVO process = processMap.getOrDefault(temp.getWorkcellId(), null);
                    if (Objects.nonNull(process)) {
                        temp.setProcessName(process.getWorkcellName());
                    }
                }
            }

            //??????
            resultList = resultList.stream().sorted(Comparator.comparing(HmeInputRecordVO::getCreationDate).reversed()).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    @Async
    public void asyncExport(Long tenantId, HmeInputRecordDTO dto) throws IOException {
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
        MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, exportTaskVO.getUserId());
        String currentUserName = mtUserInfo == null ? "-1" : mtUserInfo.getRealName();
        DetailsHelper.setCustomUserDetails(exportTaskVO.getUserId(), "zh_CN");
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_TASK_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //????????????
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //????????????????????????????????????
            List<HmeInputRecordVO> resultList = this.inputRecordExport(tenantId, dto);

            // ???????????????????????????
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(exportTaskVO.getTaskName());
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // ????????????
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            XSSFRow headerRow = sheet.createRow(0);
            this.fillHeaderRow(sheet, headerRow, titles, styles.get("center"));

            //????????????
            Integer rowIndex = 1;
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (HmeInputRecordVO rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProductionVersion()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWoMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWoMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getQty() != null ? rec.getQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getEoNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getLoginName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getRealName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getJobTypeMeaning()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getIdentification()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialVersion()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getBomQty() != null ? rec.getBomQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getAttritionChance()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getUomCode()).orElse(""));

                    fields.add(Optional.ofNullable(rec.getLot()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getReleaseQty() != null ? rec.getReleaseQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getCreationDateStr()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getAttrValue()).orElse(""));

                    fields.add(Optional.ofNullable(rec.getYN()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getVirtualFlag()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getPrimaryUomQty() != null ? rec.getPrimaryUomQty().toPlainString() : "").orElse(""));
                    this.fillRow(row, fields, styles.get("center"));
                    rowIndex++;
                }
            }
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API???HmeCosFunctionServiceImpl.cosFunctionReportAsyncExport???";
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
            fileName = FILE_TASK_NAME;
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
    private void fillHeaderRow(XSSFSheet sheet, XSSFRow row, List<String> fields, XSSFCellStyle style) {
        Integer headerIndex = 0;
        for (String field : fields) {
            XSSFCell cell = row.createCell(headerIndex);
            cell.setCellStyle(style);
            cell.setCellValue(field);
            headerIndex++;
        }
    }
}
