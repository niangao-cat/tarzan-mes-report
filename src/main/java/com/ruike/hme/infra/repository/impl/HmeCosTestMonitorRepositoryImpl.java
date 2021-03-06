package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeCosTestMonitorDTO;
import com.ruike.hme.domain.repository.HmeCosTestMonitorRepository;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO2;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.FileUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xssf.usermodel.*;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:41
 */
@Component
public class HmeCosTestMonitorRepositoryImpl implements HmeCosTestMonitorRepository {

    @Autowired
    private HmeCosTestMonitorMapper hmeCosTestMonitorMapper;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private MtUserClient mtUserClient;

    private static final String FILE_COS_RETURN = "COS??????????????????";

    private static final String[] FIXED_TITLE = {"???????????????", "COS??????", "??????", "Wafer", "??????", "??????????????????",	"??????????????????", "?????????????????????",	"??????????????????", "??????????????????", "??????????????????", "????????????", "??????????????????", "??????????????????", "????????????", "??????????????????", "??????????????????", "????????????", "??????????????????", "??????????????????", "????????????", "????????????"};

    @Override
    public Page<HmeCosTestMonitorVO> queryRecordList(Long tenantId, HmeCosTestMonitorDTO dto, PageRequest pageRequest) {
        Page<HmeCosTestMonitorVO> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosTestMonitorMapper.queryRecordList(tenantId, dto));
        this.handleRecordList(tenantId, pageObj.getContent());
        return pageObj;
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param list
     * @author sanfeng.zhang@hand-china.com 2021/11/8 17:05
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosTestMonitorVO>
     */
    private List<HmeCosTestMonitorVO> handleRecordList(Long tenantId, List<HmeCosTestMonitorVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            // ??????wafer?????????????????????
            List<HmeCosTestMonitorVO2> materialLotList = hmeCosTestMonitorMapper.queryMaterialLotCodeByWaferAndWorkOrder(tenantId, list);
            // ????????????????????????????????? ?????????????????? ??????????????? ???????????????
            List<String> materialLotIds = materialLotList.stream().map(HmeCosTestMonitorVO2::getMaterialLotId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                Map<String, HmeCosTestMonitorVO2> materialLotMap = materialLotList.stream().collect(Collectors.toMap(HmeCosTestMonitorVO2::getMaterialLotId, Function.identity()));
                List<HmeCosTestMonitorVO2> eoJobSnList = hmeCosTestMonitorMapper.queryEoJobSnByMaterialLotIds(tenantId, materialLotIds);
                eoJobSnList.stream().forEach(ejs -> {
                    HmeCosTestMonitorVO2 monitorVO2 = materialLotMap.get(ejs.getMaterialLotId());
                    if (monitorVO2 != null) {
                        ejs.setWaferNum(monitorVO2.getWaferNum());
                        ejs.setWorkOrderId(monitorVO2.getWorkOrderId());
                        ejs.setMaterialCode(monitorVO2.getMaterialCode());
                        ejs.setMaterialLotCode(monitorVO2.getMaterialLotCode());
                    }
                });
                Map<String, List<HmeCosTestMonitorVO2>> eoJobSnMap = eoJobSnList.stream().collect(Collectors.groupingBy(dto -> dto.getWaferNum() + "_" + dto.getWorkOrderId()));
                String testDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                for (HmeCosTestMonitorVO hmeCosTestMonitorVO : list) {
                    List<HmeCosTestMonitorVO2> eoJobSns = eoJobSnMap.getOrDefault(hmeCosTestMonitorVO.getWafer() + "_" + hmeCosTestMonitorVO.getWorkOrderId(), Collections.emptyList());
                    // ??????????????????
                    Double incomeInputTotalSum = eoJobSns.stream().filter(ejs -> ejs.getMaterialCode().startsWith("3") && ejs.getSiteOutDate() != null && "IO".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    BigDecimal incomeInputTotal = incomeInputTotalSum != null ? BigDecimal.valueOf(incomeInputTotalSum) : BigDecimal.ZERO;
                    hmeCosTestMonitorVO.setIncomeInputTotal(BigDecimal.valueOf(incomeInputTotalSum));
                    // ?????????????????????
                    BigDecimal multiply = incomeInputTotal.multiply(hmeCosTestMonitorVO.getInputPassRate() != null ? hmeCosTestMonitorVO.getInputPassRate() : BigDecimal.ZERO);
                    hmeCosTestMonitorVO.setIncomeInputBaseNum(multiply.divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_DOWN));
                    // ??????????????????
                    Double qpSiteOutSum = eoJobSns.stream().filter(ejs -> ejs.getSiteOutDate() != null && "COS_FETCH_OUT".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setQpSiteOutTotal(BigDecimal.valueOf(qpSiteOutSum));
                    // ??????????????????
                    Double tpSiteInSum = eoJobSns.stream().filter(ejs -> "COS_PASTER_IN".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setTpSiteInTotal(BigDecimal.valueOf(tpSiteInSum));
                    // ??????????????????
                    Double tpSiteOutSum = eoJobSns.stream().filter(ejs -> ejs.getSiteOutDate() != null && "COS_PASTER_OUT".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setTpSiteOutTotal(BigDecimal.valueOf(tpSiteOutSum));
                    // ???????????? = ??????????????????-??????????????????
                    hmeCosTestMonitorVO.setTpStayNum(hmeCosTestMonitorVO.getTpSiteInTotal().subtract(hmeCosTestMonitorVO.getTpSiteOutTotal()));
                    // ??????????????????
                    Double dxSiteInSum = eoJobSns.stream().filter(ejs -> "COS_WIRE_BOND".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setDxSiteInTotal(BigDecimal.valueOf(dxSiteInSum));
                    // ??????????????????
                    Double dxSiteOutSum = eoJobSns.stream().filter(ejs -> ejs.getSiteOutDate() != null && "COS_WIRE_BOND".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setDxSiteOutTotal(BigDecimal.valueOf(dxSiteOutSum));
                    // ???????????? = ??????????????????-??????????????????
                    hmeCosTestMonitorVO.setDxStayNum(hmeCosTestMonitorVO.getDxSiteInTotal().subtract(hmeCosTestMonitorVO.getDxSiteOutTotal()));
                    // ??????????????????(??????????????? ???????????????)
                    List<HmeCosTestMonitorVO2> cosTestList = eoJobSns.stream().filter(ejs -> "COS_TEST".equals(ejs.getJobType())).sorted(Comparator.comparing(HmeCosTestMonitorVO2::getSiteInDate).reversed()).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getMaterialLotId()))), ArrayList::new));
                    Double csSiteInSum = cosTestList.stream().map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setCsSiteInTotal(BigDecimal.valueOf(csSiteInSum));
                    // ??????????????????(??????????????? ???????????????)
                    List<HmeCosTestMonitorVO2> cosTestOutList = eoJobSns.stream().filter(ejs -> ejs.getSiteOutDate() != null && "COS_TEST".equals(ejs.getJobType())).sorted(Comparator.comparing(HmeCosTestMonitorVO2::getSiteInDate).reversed()).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getMaterialLotId()))), ArrayList::new));
                    Double csSiteOutSum = cosTestOutList.stream().map(HmeCosTestMonitorVO2::getSiteOutQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setCsSiteOutTotal(BigDecimal.valueOf(csSiteOutSum));
                    // ???????????? = ??????????????????-??????????????????
                    hmeCosTestMonitorVO.setCsStayNum(hmeCosTestMonitorVO.getCsSiteInTotal().subtract(hmeCosTestMonitorVO.getCsSiteOutTotal()));
                    // ??????????????????
                    Double mjSiteInSum = eoJobSns.stream().filter(ejs -> "COS_MJ_COMPLETED".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setMjSiteInTotal(BigDecimal.valueOf(mjSiteInSum));
                    // ??????????????????
                    Double mjSiteOutSum = eoJobSns.stream().filter(ejs -> ejs.getSiteOutDate() != null && "COS_MJ_COMPLETED".equals(ejs.getJobType())).map(HmeCosTestMonitorVO2::getSiteOutQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    hmeCosTestMonitorVO.setMjSiteOutTotal(BigDecimal.valueOf(mjSiteOutSum));
                    // ???????????? = ??????????????????-??????????????????
                    hmeCosTestMonitorVO.setMjStayNum(hmeCosTestMonitorVO.getMjSiteInTotal().subtract(hmeCosTestMonitorVO.getMjSiteOutTotal()));
                    hmeCosTestMonitorVO.setOperationDate(testDate);
                }
            }
        }
        return list;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosTestMonitorVO> export(Long tenantId, HmeCosTestMonitorDTO dto) {
        List<HmeCosTestMonitorVO> resultList = hmeCosTestMonitorMapper.queryRecordList(tenantId, dto);
        this.handleRecordList(tenantId, resultList);
        return resultList;
    }

    @Override
    @ProcessLovValue
    @Async
    public void asyncExport(Long tenantId, HmeCosTestMonitorDTO dto) throws IOException {
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
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@??????-" + currentUserName + "@??????-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_COS_RETURN;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //????????????
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //????????????????????????????????????
            List<HmeCosTestMonitorVO> resultList = hmeCosTestMonitorMapper.queryRecordList(tenantId, dto);
            this.handleRecordList(tenantId, resultList);

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
                for (HmeCosTestMonitorVO rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getMonitorDocNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getCosType()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProdLineCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWafer()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getTestPassRate()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getIncomeInputTotal() != null ? rec.getIncomeInputTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getIncomeInputBaseNum() != null ? rec.getIncomeInputBaseNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getQpSiteOutTotal() != null ? rec.getQpSiteOutTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getTpSiteInTotal() != null ? rec.getTpSiteInTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getTpSiteOutTotal() != null ? rec.getTpSiteOutTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getTpStayNum() != null ? rec.getTpStayNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getDxSiteInTotal() != null ? rec.getDxSiteInTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getDxSiteOutTotal() != null ? rec.getDxSiteOutTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getDxStayNum() != null ? rec.getDxStayNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getCsSiteInTotal() != null ? rec.getCsSiteInTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getCsSiteOutTotal() != null ? rec.getCsSiteOutTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getCsStayNum() != null ? rec.getCsStayNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getMjSiteInTotal() != null ? rec.getMjSiteInTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getMjSiteOutTotal() != null ? rec.getMjSiteOutTotal().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getMjStayNum() != null ? rec.getMjStayNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getOperationDate()).orElse(""));
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
        exportTaskVO.setTaskName(FILE_COS_RETURN);
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
