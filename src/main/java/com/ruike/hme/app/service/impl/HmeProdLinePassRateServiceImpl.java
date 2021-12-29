package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.app.service.HmeProdLinePassRateService;
import com.ruike.hme.domain.repository.HmeProdLinePassRateRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.ExcellUtils;
import com.ruike.hme.infra.util.FileUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
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
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 产品直通率报表应用服务默认实现
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:22:12
 **/
@Service
@Slf4j
public class HmeProdLinePassRateServiceImpl implements HmeProdLinePassRateService {

    @Autowired
    private HmeProdLinePassRateRepository hmeProdLinePassRateRepository;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private MtUserClient mtUserClient;

    private static final String FILE_NAME = "产品直通率报表";
    private static final String[] FIXED_TITLE = {"时间段", "产线", "工序"};
    private static final String DAY_FILE_NAME = "日直通率报表";
    private static final String[] DAY_FIXED_TITLE = {"时间", "产线", "工序"};


    @Override
    public HmeProdLinePassRateVO5 prodLinePassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto) {
        if(StringUtils.isEmpty(dto.getAreaId()) && StringUtils.isEmpty(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getLineWorkCellId()) && StringUtils.isEmpty(dto.getProcessId())){
            throw new CommonException("部门、产线、工段、工序必输其一");
        }
        if(StringUtils.isNotBlank(dto.getMaterialId())){
            List<String> materialIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(materialIdList);
        }
        if(StringUtils.isNotBlank(dto.getWorkOrderId())){
            List<String> woIdList = Arrays.asList(dto.getWorkOrderId().split(","));
            dto.setWorkOrderIdList(woIdList);
        }
        if(StringUtils.isNotBlank(dto.getLineWorkCellId())){
            List<String> lineWorkCellIdList = Arrays.asList(dto.getLineWorkCellId().split(","));
            dto.setLineWorkCellIdList(lineWorkCellIdList);
        }
        if(StringUtils.isNotBlank(dto.getProcessId())){
            List<String> processIdList = Arrays.asList(dto.getProcessId().split(","));
            dto.setProcessIdList(processIdList);
        }
        return hmeProdLinePassRateRepository.prodLinePassRateQuery(tenantId, dto);
    }

    @Override
    public void prodLinePassRateExport(Long tenantId, HmeProdLinePassRateDTO params, HttpServletResponse response) throws IOException {
        HmeProdLinePassRateVO5 hmeProdLinePassRateVO5 = this.prodLinePassRateQuery(tenantId, params);

        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出产品直通率报表数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("产品直通率报表");
        String fileName = "产品直通率报表" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"时间段", "产线", "工序"};
            headerList.addAll(Arrays.asList(headers));
            List<String> materialNameList = new ArrayList<>();
            List<HmeProdLinePassRateVO> resultList = new ArrayList<>();
            if(Objects.nonNull(hmeProdLinePassRateVO5)){
                resultList = hmeProdLinePassRateVO5.getResultList();
                if(CollectionUtils.isNotEmpty(resultList)){
                    materialNameList = resultList.get(0).getMaterialData().stream().map(HmeProdLinePassRateVO2::getMaterialName).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(materialNameList)){
                        headerList.addAll(materialNameList);
                    }
                }
            }
            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("产 品 直 通 率 报 表");
            //存在合并 物料下有合格数和不良数和投产数和良率 占四个单元格
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, materialNameList.size() * 4 + 3 - 1));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
                if (CollectionUtils.isNotEmpty(materialNameList)) {
                    if (headerIndex.compareTo(2) > 0 && headerIndex.compareTo(2 + materialNameList.size() * 4) <= 0) {
                        // 物料占4列
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, headerIndex, headerIndex + 3));
                        headerIndex += 4;
                    } else {
                        headerIndex++;
                    }
                } else {
                    headerIndex++;
                }
            }
            // 物料存在 需上下合并行
            if (CollectionUtils.isNotEmpty(materialNameList)) {
                HSSFRow subHeaderRow = sheet.createRow(2);
                // 初始值
                Integer subIndex = 2;
                // 物料下分合格数和不良数和投产数和良率
                for (String materialName : materialNameList) {
                    HSSFCell hssfCell = subHeaderRow.createCell(subIndex + 1);
                    hssfCell.setCellValue("合格数");
                    hssfCell.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell2 = subHeaderRow.createCell(subIndex + 2);
                    hssfCell2.setCellValue("不良数");
                    hssfCell2.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell3 = subHeaderRow.createCell(subIndex + 3);
                    hssfCell3.setCellValue("投产数");
                    hssfCell3.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell4 = subHeaderRow.createCell(subIndex + 4);
                    hssfCell4.setCellValue("良率");
                    hssfCell4.setCellStyle(styles.get("subTitle"));
                    subIndex += 4;
                }
                // 合并除物料外其他标题 占两行
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
            }
            //内容的起始行 物料不存在 起始行为2 否则为3
            Integer rowIndex = CollectionUtils.isNotEmpty(materialNameList) ? 3 : 2;
            for (int i = 0; i < resultList.size(); i++) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                if(i == 0 && resultList.size() > 1){
                    HSSFCell cell0 = hssfRow.createCell(0);
                    cell0.setCellStyle(styles.get("center"));
                    cell0.setCellValue(resultList.get(i).getDateSlot());
                    sheet.setColumnWidth(0, 5000);
                    sheet.addMergedRegion(new CellRangeAddress(3, resultList.size()+3-1, 0, 0));
                }else{
                    HSSFCell cell0 = hssfRow.createCell(0);
                    cell0.setCellStyle(styles.get("center"));
                    cell0.setCellValue(resultList.get(i).getDateSlot());
                }
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(resultList.get(i).getProdLineName());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(resultList.get(i).getProcessName());
                Integer columnIndex = 3;
                if (CollectionUtils.isNotEmpty(resultList.get(i).getMaterialData())) {
                    for (HmeProdLinePassRateVO2 workcell : resultList.get(i).getMaterialData()) {
                        HSSFCell runCell = hssfRow.createCell(columnIndex);
                        runCell.setCellStyle(styles.get("runCell"));
                        runCell.setCellValue(workcell.getPassNum());
                        HSSFCell finishCell = hssfRow.createCell(columnIndex + 1);
                        finishCell.setCellStyle(styles.get("finishCell"));
                        finishCell.setCellValue(workcell.getNcNum());
                        HSSFCell hssfCell3 = hssfRow.createCell(columnIndex + 2);
                        hssfCell3.setCellStyle(styles.get("finishCell"));
                        hssfCell3.setCellValue(workcell.getProductionNum());
                        HSSFCell hssfCell4 = hssfRow.createCell(columnIndex + 3);
                        hssfCell4.setCellStyle(styles.get("finishCell"));
                        hssfCell4.setCellValue(workcell.getRate());
                        columnIndex += 4;
                    }
                }
            }

            HSSFRow hssfRow = sheet.createRow(rowIndex++);
            HSSFCell cell0 = hssfRow.createCell(0);
            cell0.setCellStyle(styles.get("center"));
            cell0.setCellValue("");
            HSSFCell cell1 = hssfRow.createCell(1);
            cell1.setCellStyle(styles.get("center"));
            cell1.setCellValue("");
            HSSFCell cell2 = hssfRow.createCell(2);
            cell2.setCellStyle(styles.get("center"));
            cell2.setCellValue("直通率");
            Integer columnIndex = 3;
            if(CollectionUtils.isNotEmpty(hmeProdLinePassRateVO5.getPassRateData())){
                for (String passRate:hmeProdLinePassRateVO5.getPassRateData()) {
                    HSSFCell cell3 = hssfRow.createCell(columnIndex);
                    cell3.setCellStyle(styles.get("center"));
                    cell3.setCellValue(passRate);
                    columnIndex++;
                }
            }

            ExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        }catch (IOException e) {
            throw new CommonException("产品直通率报表导出报错" + e.getMessage());
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>产品直通率报表导出完成");
    }

    @Override
    @Async
    public void asyncOnlineReportExport(Long tenantId, HmeProdLinePassRateDTO dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        String uuid = exportTaskVO.getTaskCode();;
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 取出数据
            HmeProdLinePassRateVO5 hmeProdLinePassRateVO5 = this.prodLinePassRateQuery(tenantId, dto);
            // 动态列
            List<String> dynamicTitles = displayFieldsCompletion(tenantId, hmeProdLinePassRateVO5.getResultList());

            // 生成文档及准备工作
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(exportTaskVO.getTaskName());
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // 写入表头
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            titles.addAll(dynamicTitles);
            XSSFRow headerRow = sheet.createRow(0);
            this.fillHeaderRow(sheet, headerRow, titles, styles.get("center"), dynamicTitles);

            //写入数据
            //内容的起始行 物料不存在 起始行为1 否则为2
            Integer rowIndex = CollectionUtils.isNotEmpty(dynamicTitles) ? 2 : 1;
            if (CollectionUtils.isNotEmpty(hmeProdLinePassRateVO5.getResultList())) {
                Integer indexNum = 0;
                Integer finalRowIndex = CollectionUtils.isNotEmpty(dynamicTitles) ? 2 : 1;
                for (HmeProdLinePassRateVO rec : hmeProdLinePassRateVO5.getResultList()) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getDateSlot()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProdLineName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                    // 动态采集项数据
                    if(CollectionUtils.isNotEmpty(rec.getMaterialData())) {
                        fields.addAll(this.dynamicLine(rec.getMaterialData()));
                    }
                    this.fillRow(row, fields, styles.get("center"));
                    if(indexNum.compareTo(0) == 0 && hmeProdLinePassRateVO5.getResultList().size() > 1){
                        sheet.setColumnWidth(0, 5000);
                        sheet.addMergedRegion(new CellRangeAddress(finalRowIndex, hmeProdLinePassRateVO5.getResultList().size()+finalRowIndex-1, 0, 0));
                    }
                    indexNum++;
                    rowIndex++;
                }
            }
            XSSFRow bottomRow = sheet.createRow(rowIndex);
            List<String> fieldList = new ArrayList<>();
            fieldList.add("");
            fieldList.add("");
            fieldList.add("直通率");
            if(CollectionUtils.isNotEmpty(hmeProdLinePassRateVO5.getPassRateData())){
                for (String passRate:hmeProdLinePassRateVO5.getPassRateData()) {
                    fieldList.add(Optional.ofNullable(passRate).orElse(""));
                }
            }
            this.fillRow(bottomRow, fieldList, styles.get("center"));
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachMultipartFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API【HmeProdLinePassRateServiceImpl.asyncOnlineReportExport】";
            log.info(errorInfo);
            throw new CommonException(e);
        }
        finally {
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

    private List<String> displayFieldsCompletion (Long tenantId, List<HmeProdLinePassRateVO> resultList) {
        List<String> displayFieldList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            List<String> materialNameList = resultList.get(0).getMaterialData().stream().map(HmeProdLinePassRateVO2::getMaterialName).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialNameList)){
                displayFieldList.addAll(materialNameList);
            }
        }
        return displayFieldList;
    }

    private List<String> displayDayFieldsCompletion (Long tenantId, List<HmeProdLinePassRateVO6> resultList) {
        List<String> displayFieldList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            List<String> shiftNameList = resultList.get(0).getShiftData().stream().map(HmeProdLinePassRateVO7::getShiftName).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(shiftNameList)){
                displayFieldList.addAll(shiftNameList);
            }
        }
        return displayFieldList;
    }

    private void fillRow(XSSFRow row, List<String> fields, XSSFCellStyle style) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            XSSFCell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(field);
            cell.setCellStyle(style);
        });
    }

    private List<String> dynamicLine(List<HmeProdLinePassRateVO2> materialData) {
        List<String> resultLineList = new ArrayList<>();
        materialData.forEach(md -> {
            resultLineList.add(Optional.ofNullable(md.getPassNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getNcNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getProductionNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getRate()).orElse(""));
        });
        return resultLineList;
    }

    private List<String> dynamicDayLine(List<HmeProdLinePassRateVO7> shiftData) {
        List<String> resultLineList = new ArrayList<>();
        shiftData.forEach(md -> {
            resultLineList.add(Optional.ofNullable(md.getPassNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getNcNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getProductionNum()).orElse(""));
            resultLineList.add(Optional.ofNullable(md.getRate()).orElse(""));
        });
        return resultLineList;
    }

    /**
     * 输入行内容
     *
     * @param row    行
     * @param fields 字段
     */
    private void fillHeaderRow(XSSFSheet sheet, XSSFRow row, List<String> fields, XSSFCellStyle style, List<String> dynamicTitles) {
        Integer headerIndex = 0;
        for (String field : fields) {
            XSSFCell cell = row.createCell(headerIndex);
            cell.setCellStyle(style);
            cell.setCellValue(field);
            if (CollectionUtils.isNotEmpty(dynamicTitles)) {
                if (headerIndex.compareTo(2) > 0 && headerIndex.compareTo(2 + dynamicTitles.size() * 4) <= 0) {
                    // 物料占4列
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, headerIndex, headerIndex + 3));
                    headerIndex += 4;
                } else {
                    headerIndex++;
                }
            } else {
                headerIndex++;
            }
        }
        // 物料存在 需上下合并行
        if (CollectionUtils.isNotEmpty(dynamicTitles)) {
            XSSFRow subHeaderRow = sheet.createRow(1);
            // 初始值
            Integer subIndex = 2;
            // 物料下分合格数和不良数和投产数和良率
            for (String materialName : dynamicTitles) {
                XSSFCell hssfCell = subHeaderRow.createCell(subIndex + 1);
                hssfCell.setCellValue("合格数");
                hssfCell.setCellStyle(style);
                XSSFCell hssfCell2 = subHeaderRow.createCell(subIndex + 2);
                hssfCell2.setCellValue("不良数");
                hssfCell2.setCellStyle(style);
                XSSFCell hssfCell3 = subHeaderRow.createCell(subIndex + 3);
                hssfCell3.setCellValue("投产数");
                hssfCell3.setCellStyle(style);
                XSSFCell hssfCell4 = subHeaderRow.createCell(subIndex + 4);
                hssfCell4.setCellValue("良率");
                hssfCell4.setCellStyle(style);
                subIndex += 4;
            }
            // 合并除物料外其他标题 占两行
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        }
    }


    @Override
    public HmeProdLinePassRateVO8 prodLineDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException {
        if(StringUtils.isEmpty(dto.getAreaId()) && StringUtils.isEmpty(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getLineWorkCellId()) && StringUtils.isEmpty(dto.getProcessId())){
            throw new CommonException("部门、产线、工段、工序必输其一");
        }
        if(StringUtils.isNotBlank(dto.getMaterialId())){
            List<String> materialIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(materialIdList);
        }
        if(StringUtils.isNotBlank(dto.getWorkOrderId())){
            List<String> woIdList = Arrays.asList(dto.getWorkOrderId().split(","));
            dto.setWorkOrderIdList(woIdList);
        }
        if(StringUtils.isNotBlank(dto.getLineWorkCellId())){
            List<String> lineWorkCellIdList = Arrays.asList(dto.getLineWorkCellId().split(","));
            dto.setLineWorkCellIdList(lineWorkCellIdList);
        }
        if(StringUtils.isNotBlank(dto.getProcessId())){
            List<String> processIdList = Arrays.asList(dto.getProcessId().split(","));
            dto.setProcessIdList(processIdList);
        }
        if(StringUtils.isNotBlank(dto.getWorkcellId())){
            List<String> workcellIdList = Arrays.asList(dto.getWorkcellId().split(","));
            dto.setWorkcellIdList(workcellIdList);
        }
        return hmeProdLinePassRateRepository.prodLineDayPassRateQuery(tenantId, dto);
    }

    @Override
    public void prodLineDayPassRateExport(Long tenantId, HmeProdLinePassRateDTO2 params, HttpServletResponse response) throws IOException, ParseException {
        HmeProdLinePassRateVO8 hmeProdLinePassRateVO8 = this.prodLineDayPassRateQuery(tenantId, params);

        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出日直通率报表数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("日直通率报表");
        String fileName = "日直通率报表" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"时间", "产线", "工序"};
            headerList.addAll(Arrays.asList(headers));
            List<String> dateList = new ArrayList<>();
            List<HmeProdLinePassRateVO6> resultList = new ArrayList<>();
            if(Objects.nonNull(hmeProdLinePassRateVO8)){
                resultList = hmeProdLinePassRateVO8.getResultList();
                if(CollectionUtils.isNotEmpty(resultList)){
                    dateList = resultList.get(0).getShiftData().stream().map(HmeProdLinePassRateVO7::getShiftName).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(dateList)){
                        headerList.addAll(dateList);
                    }
                }
            }
            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("日 直 通 率 报 表");
            //存在合并 日期下有合格数和不良数和投产数和良率 占四个单元格
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, dateList.size() * 4 + 3 - 1));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
                if (CollectionUtils.isNotEmpty(dateList)) {
                    if (headerIndex.compareTo(2) > 0 && headerIndex.compareTo(2 + dateList.size() * 4) <= 0) {
                        // 日期占4列
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, headerIndex, headerIndex + 3));
                        headerIndex += 4;
                    } else {
                        headerIndex++;
                    }
                } else {
                    headerIndex++;
                }
            }
            // 日期存在 需上下合并行
            if (CollectionUtils.isNotEmpty(dateList)) {
                HSSFRow subHeaderRow = sheet.createRow(2);
                // 初始值
                Integer subIndex = 2;
                // 物料下分合格数和不良数和投产数和良率
                for (String date : dateList) {
                    HSSFCell hssfCell = subHeaderRow.createCell(subIndex + 1);
                    hssfCell.setCellValue("合格数");
                    hssfCell.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell2 = subHeaderRow.createCell(subIndex + 2);
                    hssfCell2.setCellValue("不良数");
                    hssfCell2.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell3 = subHeaderRow.createCell(subIndex + 3);
                    hssfCell3.setCellValue("投产数");
                    hssfCell3.setCellStyle(styles.get("subTitle"));
                    HSSFCell hssfCell4 = subHeaderRow.createCell(subIndex + 4);
                    hssfCell4.setCellValue("良率");
                    hssfCell4.setCellStyle(styles.get("subTitle"));
                    subIndex += 4;
                }
                // 合并除日期外其他标题 占两行
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
            }
            //内容的起始行 物料不存在 起始行为2 否则为3
            Integer rowIndex = CollectionUtils.isNotEmpty(dateList) ? 3 : 2;
            for (int i = 0; i < resultList.size(); i++) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                //时间段单元格合并
                if(i == 0 && resultList.size() > 1){
                    HSSFCell cell0 = hssfRow.createCell(0);
                    cell0.setCellStyle(styles.get("center"));
                    cell0.setCellValue(resultList.get(i).getDate());
                    sheet.setColumnWidth(0, 5000);
                    sheet.addMergedRegion(new CellRangeAddress(3, resultList.size()+3-1, 0, 0));
                }else{
                    HSSFCell cell0 = hssfRow.createCell(0);
                    cell0.setCellStyle(styles.get("center"));
                    cell0.setCellValue(resultList.get(i).getDate());
                }
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(resultList.get(i).getProdLineName());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(resultList.get(i).getProcessName());
                Integer columnIndex = 3;
                if (CollectionUtils.isNotEmpty(resultList.get(i).getShiftData())) {
                    for (HmeProdLinePassRateVO7 workcell : resultList.get(i).getShiftData()) {
                        HSSFCell runCell = hssfRow.createCell(columnIndex);
                        runCell.setCellStyle(styles.get("runCell"));
                        runCell.setCellValue(workcell.getPassNum());
                        HSSFCell finishCell = hssfRow.createCell(columnIndex + 1);
                        finishCell.setCellStyle(styles.get("finishCell"));
                        finishCell.setCellValue(workcell.getNcNum());
                        HSSFCell hssfCell3 = hssfRow.createCell(columnIndex + 2);
                        hssfCell3.setCellStyle(styles.get("finishCell"));
                        hssfCell3.setCellValue(workcell.getProductionNum());
                        HSSFCell hssfCell4 = hssfRow.createCell(columnIndex + 3);
                        hssfCell4.setCellStyle(styles.get("finishCell"));
                        hssfCell4.setCellValue(workcell.getRate());
                        columnIndex += 4;
                    }
                }
            }

            HSSFRow hssfRow = sheet.createRow(rowIndex++);
            HSSFCell cell0 = hssfRow.createCell(0);
            cell0.setCellStyle(styles.get("center"));
            cell0.setCellValue("");
            HSSFCell cell1 = hssfRow.createCell(1);
            cell1.setCellStyle(styles.get("center"));
            cell1.setCellValue("");
            HSSFCell cell2 = hssfRow.createCell(2);
            cell2.setCellStyle(styles.get("center"));
            cell2.setCellValue("直通率");
            Integer columnIndex = 3;
            if(CollectionUtils.isNotEmpty(hmeProdLinePassRateVO8.getPassRateData())){
                for (String passRate:hmeProdLinePassRateVO8.getPassRateData()) {
                    HSSFCell cell3 = hssfRow.createCell(columnIndex);
                    cell3.setCellStyle(styles.get("center"));
                    cell3.setCellValue(passRate);
                    columnIndex++;
                }
            }

            ExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        }catch (IOException e) {
            throw new CommonException("日直通率报表导出报错" + e.getMessage());
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>日直通率报表导出完成");
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
    public void asyncProdLineDayPassRateExport(Long tenantId, HmeProdLinePassRateDTO2 dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + DAY_FILE_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 取出数据
            HmeProdLinePassRateVO8 hmeProdLinePassRateVO8 = this.prodLineDayPassRateQuery(tenantId, dto);
            // 动态列
            List<String> dynamicTitles = displayDayFieldsCompletion(tenantId, hmeProdLinePassRateVO8.getResultList());

            // 生成文档及准备工作
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(exportTaskVO.getTaskName());
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // 写入表头
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            titles.addAll(dynamicTitles);
            XSSFRow headerRow = sheet.createRow(0);
            this.fillHeaderRow(sheet, headerRow, titles, styles.get("center"), dynamicTitles);

            //写入数据
            //内容的起始行 物料不存在 起始行为1 否则为2
            Integer rowIndex = CollectionUtils.isNotEmpty(dynamicTitles) ? 2 : 1;
            if (CollectionUtils.isNotEmpty(hmeProdLinePassRateVO8.getResultList())) {
                Integer indexNum = 0;
                Integer finalRowIndex = CollectionUtils.isNotEmpty(dynamicTitles) ? 2 : 1;
                for (HmeProdLinePassRateVO6 rec : hmeProdLinePassRateVO8.getResultList()) {
                    XSSFRow row = sheet.createRow(rowIndex++);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getDate()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProdLineName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                    // 动态采集项数据
                    if(CollectionUtils.isNotEmpty(rec.getShiftData())) {
                        fields.addAll(this.dynamicDayLine(rec.getShiftData()));
                    }
                    this.fillRow(row, fields, styles.get("center"));
                    if(indexNum.compareTo(0) == 0 && hmeProdLinePassRateVO8.getResultList().size() > 1){
                        sheet.setColumnWidth(0, 5000);
                        sheet.addMergedRegion(new CellRangeAddress(finalRowIndex, hmeProdLinePassRateVO8.getResultList().size()+finalRowIndex-1, 0, 0));
                    }
                    indexNum++;
                }
            }
            XSSFRow bottomRow = sheet.createRow(rowIndex);
            List<String> fieldList = new ArrayList<>();
            fieldList.add("");
            fieldList.add("");
            fieldList.add("直通率");
            if(CollectionUtils.isNotEmpty(hmeProdLinePassRateVO8.getPassRateData())){
                for (String passRate : hmeProdLinePassRateVO8.getPassRateData()) {
                    fieldList.add(Optional.ofNullable(passRate).orElse(""));
                }
            }
            this.fillRow(bottomRow, fieldList, styles.get("center"));
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            if(bytes.length > 0) {
                returnStr = hmeHzeroFileFeignClient.uploadAttachByteFile(tenantId, FileUtils.UploadValue.BUCKET_NAME, FileUtils.UploadValue.DIRECTORY, uuid, fileName, FileUtils.UploadValue.FILE_TYPE, "", bytes);
                log.info("<==========uploadAttachMultipartFile=============>" + returnStr.getStatusCode() + "-" + returnStr.getBody());
            }
        }catch (Exception e){
            state = CommonUtils.ExportTaskStateValue.CANCELED;
            errorInfo = e.getMessage() + ",API【HmeProdLinePassRateServiceImpl.asyncProdLineDayPassRateExport】";
            log.info(errorInfo);
            throw new CommonException(e);
        }
        finally {
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
}
