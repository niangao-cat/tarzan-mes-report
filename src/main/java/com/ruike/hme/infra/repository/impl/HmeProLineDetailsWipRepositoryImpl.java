package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.repository.HmeProLineDetailsWipRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeProLineDetailsWipMapper;
import com.ruike.hme.infra.util.ProLineDetailsExcellUtils;
import com.ruike.qms.infra.util.CollectorsUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HmeProLineDetailsRepositoryImpl
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:40
 */

@Component
@Slf4j
public class HmeProLineDetailsWipRepositoryImpl implements HmeProLineDetailsWipRepository {

    @Autowired
    private HmeProLineDetailsWipMapper hmeProLineDetailsMapper;

    @Override
    public List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(Long tenantId, String siteId, String prodLineId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.batchQueryWorkingQTYAndCompletedQTY(tenantId, siteId, prodLineId, materialIdList);
    }

    @Override
    public List<HmeEoVO> selectQueueNumByMaterialList(Long tenantId, String prodLineId, String siteId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.selectQueueNumByMaterialList(tenantId, prodLineId, siteId, materialIdList);
    }

    @Override
    public List<HmeEoVO> selectUnCountByMaterialList(Long tenantId, String prodLineId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.selectUnCountByMaterialList(tenantId, prodLineId, materialIdList);
    }

    @Override
    public List<HmeProductionQueryDTO> queryProductDetails(HmeProductionQueryVO params) {
        return hmeProLineDetailsMapper.queryProductDetails(params.getTenantId(), params.getSiteId(), params.getProdLineId(),
                params.getProductType(), params.getProductClassification(), params.getProductCode(), params.getProductModel(), params.getMaterialId());
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        //??????????????????
        String defaultSiteId = hmeProLineDetailsMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        Page<HmeProductEoInfoVO> pageObj = null;

        switch (params.getFlag()) {
            // ??????
            case "Y":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByRun(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                HmeWorkcellScheduleVO mtModWorkcellSchedule =
                        hmeProLineDetailsMapper.getWorkcellScheduleVO(tenantId, params.getWorkcellId());
                BigDecimal standardTimer = BigDecimal.ZERO;
                if (mtModWorkcellSchedule != null) {
                    if (StringUtils.isNotBlank(mtModWorkcellSchedule.getRateType())) {
                        if (StringUtils.equals("PERHOUR", mtModWorkcellSchedule.getRateType())) {
                            standardTimer = BigDecimal.valueOf(60).divide(BigDecimal.valueOf(mtModWorkcellSchedule.getRate()), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(mtModWorkcellSchedule.getActivity())).divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        } else if (StringUtils.equals("SECOND", mtModWorkcellSchedule.getRateType())) {
                            standardTimer = BigDecimal.valueOf(mtModWorkcellSchedule.getRate()).divide(BigDecimal.valueOf(60), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(mtModWorkcellSchedule.getActivity())).divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

                        }
                    }
                }
                for (HmeProductEoInfoVO infoVO : pageObj) {
                    infoVO.setStandardTimer(standardTimer);
                }
                break;
            // ??????
            case "N":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByFinish(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                break;
            // ?????????
            case "Q":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByQueueQty(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                break;
            // ?????????
            case "M":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByNoCount(tenantId, params.getWorkcellId(), params.getMaterialId()));
                break;
            default:
                pageObj = new Page<HmeProductEoInfoVO>();
        }
        return pageObj;
    }

    @Override
    public void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException {

        params.setTenantId(tenantId);
        List<HmeProductionQueryDTO> queryDTOList = this.queryProductDetails(params);
        List<HmeProductionLineDetailsVO3> hmeProductionLineDetailsVO3s = new ArrayList<>();
        BigDecimal qty = BigDecimal.ZERO;
        // ????????????
        List<HmeProcessInfoVO> processInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(queryDTOList)) {
            // ????????????????????????????????????
            List<String> materialIdList = queryDTOList.stream().map(HmeProductionQueryDTO::getMaterialId).filter(Objects::nonNull).collect(Collectors.toList());
            List<HmeProductDetailsVO> hmeProductDetailsVOList = this.batchQueryWorkingQTYAndCompletedQTY(tenantId, params.getSiteId(), params.getProdLineId(), materialIdList);
            Map<String, String> workcellMap = hmeProductDetailsVOList.stream().collect(Collectors.toMap(HmeProductDetailsVO::getWorkcellId, HmeProductDetailsVO::getDescription, (k1, k2) -> k1, LinkedMap::new));

            // ??????????????????????????????
            Map<String, List<HmeProductDetailsVO>> qtyMap = hmeProductDetailsVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getWorkcellId()));

            Map<String, BigDecimal> queueNumMap = this.selectQueueNumByMaterialList(tenantId, params.getProdLineId(), params.getSiteId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
            Map<String, BigDecimal> unCountMap = this.selectUnCountByMaterialList(tenantId, params.getProdLineId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));

            for (HmeProductionQueryDTO dto : queryDTOList) {
                List<HmeProcessInfoVO> resultMap = new ArrayList<>();
                if (!workcellMap.isEmpty()) {
                    workcellMap.forEach((workcellId, description) -> {
                        HmeProcessInfoVO process = new HmeProcessInfoVO();
                        // ???map?????????????????????????????????0
                        String key = dto.getMaterialId() + "_" + workcellId;
                        BigDecimal runNum = BigDecimal.ZERO, finishNum = BigDecimal.ZERO;
                        if (qtyMap.containsKey(key)) {
                            List<HmeProductDetailsVO> qtyList = qtyMap.get(key);
                            runNum = qtyList.stream().map(HmeProductDetailsVO::getRunNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                            finishNum = qtyList.stream().map(HmeProductDetailsVO::getFinishNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                        }
                        // ????????????
                        process.setMaterialId(dto.getMaterialId());
                        process.setWorkcellId(workcellId);
                        process.setDescription(workcellMap.get(workcellId));
                        process.setRunNum(runNum);
                        process.setFinishNum(finishNum);
                        resultMap.add(process);
                        // ??????????????????????????????
                        Optional<HmeProcessInfoVO> processOpt = processInfoList.stream().filter(pro -> StringUtils.equals(pro.getWorkcellId(), workcellId)).findFirst();
                        if (!processOpt.isPresent()) {
                            processInfoList.add(process);
                        }
                    });
                }
                dto.setWorkcells(resultMap);
                dto.setQueueNum(queueNumMap.containsKey(dto.getMaterialId()) ? queueNumMap.get(dto.getMaterialId()).longValue() : 0);
                dto.setUnCount(unCountMap.containsKey(dto.getMaterialId()) ? unCountMap.get(dto.getMaterialId()).longValue() : 0);
            }
            //????????????????????????
            hmeProductionLineDetailsVO3s = hmeProLineDetailsMapper.processQtyQuery(tenantId, params.getSiteId(), params.getProdLineId(), params.getMaterialId());
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(hmeProductionLineDetailsVO3s)){
                qty = hmeProductionLineDetailsVO3s.stream().collect(CollectorsUtil
                        .summingBigDecimal(item -> item.getTotalQty()));
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>>??????????????????????????????");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("????????????");
        String fileName = "????????????" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //??????excel????????????
            fOut = response.getOutputStream();
            //?????????????????????????????????????????????
            //headers??????excel????????????????????????
            List<String> headerList = new ArrayList<>();
            String[] headers = {"?????????", "????????????", "????????????", "?????????"};
            headerList.addAll(Arrays.asList(headers));
            List<String> processNameList = processInfoList.stream().map(HmeProcessInfoVO::getDescription).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(processNameList)) {
                headerList.addAll(processNameList);
            }
            headerList.add("???????????????");

            // ??????????????????HSSFWorkbook??????
            HSSFRow row = sheet.createRow(0);
            //??????
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ProLineDetailsExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("??? ??? ??? ???    ??????:" + qty);
            //???????????? ??????????????????????????? ??????????????????
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, processNameList.size() * 2 + 5 - 1));
            //??????
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
                if (CollectionUtils.isNotEmpty(processNameList)) {
                    if (headerIndex.compareTo(3) > 0 && headerIndex.compareTo(3 + processNameList.size() * 2) <= 0) {
                        // ???????????????
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, headerIndex, headerIndex + 1));
                        headerIndex += 2;
                    } else {
                        headerIndex++;
                    }
                } else {
                    headerIndex++;
                }
            }
            // ???????????? ??????????????????
            if (CollectionUtils.isNotEmpty(processInfoList)) {
                HSSFRow subHeaderRow = sheet.createRow(2);
                // ?????????
                Integer subIndex = 3;
                // ???????????????????????????
                for (HmeProcessInfoVO infoVO : processInfoList) {
                    HSSFCell runCell = subHeaderRow.createCell(subIndex + 1);
                    runCell.setCellValue("??????");
                    runCell.setCellStyle(styles.get("subTitle"));
                    HSSFCell finCell = subHeaderRow.createCell(subIndex + 2);
                    finCell.setCellValue("??????");
                    finCell.setCellStyle(styles.get("subTitle"));
                    subIndex += 2;
                }
                // ?????????????????????????????? ?????????
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, subIndex + 1, subIndex + 1));
            }
            //?????????????????? ??????????????? ????????????2 ?????????3
            Integer rowIndex = CollectionUtils.isNotEmpty(processInfoList) ? 3 : 2;
            //?????????????????????
            HSSFRow hssfTotalRow = sheet.createRow(rowIndex);
            HSSFCell cellTotal0 = hssfTotalRow.createCell(0);
            cellTotal0.setCellStyle(styles.get("center"));
            cellTotal0.setCellValue("????????????");
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
            if(CollectionUtils.isNotEmpty(hmeProductionLineDetailsVO3s)){
                Integer index = 4;
                for (HmeProductionLineDetailsVO3 hmeProductionLineDetailsVO3:hmeProductionLineDetailsVO3s) {
                    HSSFCell cellTotal1 = hssfTotalRow.createCell(index);
                    cellTotal1.setCellStyle(styles.get("center"));
                    cellTotal1.setCellValue(hmeProductionLineDetailsVO3.getTotalQty().toString());
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, index, index+1));
                    index += 2;
                }
            }
            rowIndex++;
            for (HmeProductionQueryDTO queryDTO : queryDTOList) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                HSSFCell cell0 = hssfRow.createCell(0);
                cell0.setCellStyle(styles.get("center"));
                cell0.setCellValue(queryDTO.getProdLineName());
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(queryDTO.getMaterialCode());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(queryDTO.getMaterialName());
                HSSFCell cell3 = hssfRow.createCell(3);
                cell3.setCellStyle(styles.get("center"));
                cell3.setCellValue(queryDTO.getQueueNum());
                Integer columnIndex = 4;
                if (CollectionUtils.isNotEmpty(queryDTO.getWorkcells())) {
                    for (HmeProcessInfoVO workcell : queryDTO.getWorkcells()) {
                        HSSFCell runCell = hssfRow.createCell(columnIndex);
                        runCell.setCellStyle(styles.get("runCell"));
                        runCell.setCellValue(workcell.getRunNum().longValue());
                        HSSFCell finishCell = hssfRow.createCell(columnIndex + 1);
                        finishCell.setCellStyle(styles.get("finishCell"));
                        finishCell.setCellValue(workcell.getFinishNum().longValue());
                        columnIndex += 2;
                    }
                }
                HSSFCell cell4 = hssfRow.createCell(columnIndex);
                cell4.setCellStyle(styles.get("center"));
                cell4.setCellValue(queryDTO.getUnCount());
            }
            ProLineDetailsExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        } catch (IOException e) {
            throw new CommonException("????????????????????????");
        } finally {
            //???????????????????????????
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>????????????????????????");
    }
}
