package com.ruike.wms.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcellUtils;
import com.ruike.wms.api.dto.WmsFinishWarehouseDTO;
import com.ruike.wms.domain.repository.WmsFinishWarehouseRepository;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO2;
import com.ruike.wms.infra.mapper.WmsFinishWarehouseMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static afu.org.checkerframework.checker.units.UnitsTools.mm;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:31
 */
@Component
@Slf4j
public class WmsFinishWarehouseRepositoryImpl implements WmsFinishWarehouseRepository {

    @Autowired
    private WmsFinishWarehouseMapper wmsFinishWarehouseMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public WmsFinishWarehouseVO2 querySummary(Long tenantId, WmsFinishWarehouseDTO dto, PageRequest pageRequest) {
        List<WmsFinishWarehouseVO> resultList = handleGroupData(tenantId, dto);
        List<WmsFinishWarehouseVO> finishWarehouseVOList = handleData(tenantId, resultList, dto);
        WmsFinishWarehouseVO2 resultPage = new WmsFinishWarehouseVO2();
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), finishWarehouseVOList.size());
        resultPage.setTotalPages(finishWarehouseVOList.size()/pageRequest.getSize()+1);
        resultPage.setTotalElements(finishWarehouseVOList.size());
        resultPage.setNumberOfElements(toIndex-fromIndex);
        resultPage.setSize(pageRequest.getSize());
        resultPage.setNumber(pageRequest.getPage());
        resultPage.setContent(CommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), finishWarehouseVOList));
        Double finishSumQty = finishWarehouseVOList.stream().map(WmsFinishWarehouseVO::getFinishQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
        Double warehousingSumQty = finishWarehouseVOList.stream().map(WmsFinishWarehouseVO::getWarehousingQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
        resultPage.setFinishSumQty(finishSumQty != null ?BigDecimal.valueOf(finishSumQty) : BigDecimal.ZERO);
        resultPage.setWarehousingSumQty(warehousingSumQty != null ?BigDecimal.valueOf(warehousingSumQty) : BigDecimal.ZERO);
        return resultPage;
    }

    @Override
    public void export(Long tenantId, WmsFinishWarehouseDTO dto, HttpServletResponse response) throws IOException {
        List<WmsFinishWarehouseVO> resultList = handleGroupData(tenantId, dto);
        List<WmsFinishWarehouseVO> finishWarehouseVOList = handleData(tenantId, resultList, dto);
        double finishSumQty = finishWarehouseVOList.stream().map(WmsFinishWarehouseVO::getFinishQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
        double warehousingSumQty = finishWarehouseVOList.stream().map(WmsFinishWarehouseVO::getWarehousingQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();

        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出完工及入库数量汇总报表数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("完工及入库数量汇总报表");
        String fileName = "完工及入库数量汇总报表" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"站点", "制造部", "制造部名称", "生产线", "产品编码", "产品描述", "版本号", "完工数量", "入库数量", "库存地点", "物料组", "物料组描述", "物料分类"};
            headerList.addAll(Arrays.asList(headers));
            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("完 工 及 入 库 数 量 汇 总 报 表");
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 11));
            // 完工汇总及入库汇总 值占两个列
            HSSFRow sumRow = sheet.createRow(1);
            HSSFCell finishCellName = sumRow.createCell(0);
            finishCellName.setCellValue("完工汇总");
            // 值
            HSSFCell finishCellValue = sumRow.createCell(1);
            finishCellValue.setCellStyle(styles.get("center"));
            finishCellValue.setCellValue(finishSumQty);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
            // 入库汇总
            HSSFCell warehousingCellName = sumRow.createCell(4);
            warehousingCellName.setCellValue("入库汇总");
            // 值
            HSSFCell warehousingCellValue = sumRow.createCell(5);
            warehousingCellValue.setCellStyle(styles.get("center"));
            warehousingCellValue.setCellValue(warehousingSumQty);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
            //表头
            HSSFRow headerRow = sheet.createRow(2);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex++);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
            }
            //内容的起始行为3
            Integer rowIndex = 3;
            for (WmsFinishWarehouseVO vo : finishWarehouseVOList) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                HSSFCell cell0 = hssfRow.createCell(0);
                cell0.setCellStyle(styles.get("center"));
                cell0.setCellValue(vo.getSiteCode());
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(vo.getAreaCode());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(vo.getAreaName());
                HSSFCell cell3 = hssfRow.createCell(3);
                cell3.setCellStyle(styles.get("center"));
                cell3.setCellValue(vo.getProdLineName());
                HSSFCell cell4 = hssfRow.createCell(4);
                cell4.setCellStyle(styles.get("center"));
                cell4.setCellValue(vo.getMaterialCode());
                HSSFCell cell5 = hssfRow.createCell(5);
                cell5.setCellStyle(styles.get("center"));
                cell5.setCellValue(vo.getMaterialName());
                HSSFCell cell6 = hssfRow.createCell(6);
                cell6.setCellStyle(styles.get("center"));
                cell6.setCellValue(vo.getProductionVersion());
                HSSFCell cell7 = hssfRow.createCell(7);
                cell7.setCellStyle(styles.get("center"));
                cell7.setCellValue(vo.getFinishQty() != null ? vo.getFinishQty().stripTrailingZeros().toPlainString() : "");
                HSSFCell cell8 = hssfRow.createCell(8);
                cell8.setCellStyle(styles.get("center"));
                cell8.setCellValue(vo.getWarehousingQty() != null ? vo.getWarehousingQty().stripTrailingZeros().toPlainString() : "");
                HSSFCell cell9 = hssfRow.createCell(9);
                cell9.setCellStyle(styles.get("center"));
                cell9.setCellValue(vo.getLocatorCode());
                HSSFCell cell10 = hssfRow.createCell(10);
                cell10.setCellStyle(styles.get("center"));
                cell10.setCellValue(vo.getItemGroupCode());
                HSSFCell cell11 = hssfRow.createCell(11);
                cell11.setCellStyle(styles.get("center"));
                cell11.setCellValue(vo.getItemGroupDescription());
                HSSFCell cell12 = hssfRow.createCell(12);
                cell12.setCellStyle(styles.get("center"));
                cell12.setCellValue(vo.getMaterialCategoryMeaning());
            }
            ExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        } catch (IOException e) {
            throw new CommonException("完工汇总及入库汇总导出报错");
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>完工汇总及入库汇总导出完成");
    }

    private List<WmsFinishWarehouseVO> handleData (Long tenantId, List<WmsFinishWarehouseVO> dtoList, WmsFinishWarehouseDTO warehouseDTO) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Collections.EMPTY_LIST;
        }
        List<WmsFinishWarehouseVO> resultList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.MATERIAL_SITE_ATTRIBUTE13", tenantId);
        // 根据站点、制造部、制造部名称、生产线、产品编码、产品描述、版本号、物料组、物料组描述及物料分类分组
        Map<String, List<WmsFinishWarehouseVO>> dtoMap = dtoList.stream().collect(Collectors.groupingBy(dto -> spliceStr(dto)));
        for (Map.Entry<String, List<WmsFinishWarehouseVO>> dtoEntry : dtoMap.entrySet()) {
            // 完工及入库取并集
            List<WmsFinishWarehouseVO> valueList = dtoEntry.getValue();
            List<WmsFinishWarehouseVO> finishList = valueList.stream().filter(vo -> StringUtils.equals(vo.getSummaryType(), "F")).collect(Collectors.toList());
            List<WmsFinishWarehouseVO> warehousingList = valueList.stream().filter(vo -> StringUtils.equals(vo.getSummaryType(), "W")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(warehousingList)) {
                // 入库有几条数据 返回几条码数据  完工最多只会有一条数据
                for (WmsFinishWarehouseVO wmsFinishWarehouseVO : warehousingList) {
                    WmsFinishWarehouseVO warehouseVO = new WmsFinishWarehouseVO();
                    BeanUtils.copyProperties(wmsFinishWarehouseVO, warehouseVO);
                    if (CollectionUtils.isNotEmpty(finishList)) {
                        warehouseVO.setFinishQty(finishList.get(0).getFinishQty());
                    } else {
                        warehouseVO.setFinishQty(BigDecimal.ZERO);
                    }
                    if (StringUtils.isNotBlank(warehouseVO.getMaterialCategory())) {
                        Optional<LovValueDTO> firstOpt = lovValueDTOS.stream().filter(lov -> StringUtils.equals(warehouseVO.getMaterialCategory(), lov.getValue())).findFirst();
                        if (firstOpt.isPresent()) {
                            warehouseVO.setMaterialCategoryMeaning(firstOpt.get().getMeaning());
                        }
                    }
                    resultList.add(warehouseVO);
                }
            } else {
                // 若筛选了库存地点 则不插入数据
                if (CollectionUtils.isEmpty(warehouseDTO.getWarehouseIdList())) {
                    // 入库没有数据 直接返回 入库数量及库存地点记空
                    WmsFinishWarehouseVO wmsFinishWarehouseVO = finishList.get(0);
                    wmsFinishWarehouseVO.setWarehousingQty(BigDecimal.ZERO);
                    wmsFinishWarehouseVO.setLocatorCode("");
                    if (StringUtils.isNotBlank(wmsFinishWarehouseVO.getMaterialCategory())) {
                        Optional<LovValueDTO> firstOpt = lovValueDTOS.stream().filter(lov -> StringUtils.equals(wmsFinishWarehouseVO.getMaterialCategory(), lov.getValue())).findFirst();
                        if (firstOpt.isPresent()) {
                            wmsFinishWarehouseVO.setMaterialCategoryMeaning(firstOpt.get().getMeaning());
                        }
                    }
                    resultList.add(wmsFinishWarehouseVO);
                }
            }
        }
        return resultList;
    }

    private List<WmsFinishWarehouseVO> handleGroupData(Long tenantId, WmsFinishWarehouseDTO dto) {
        List<WmsFinishWarehouseVO> resultFinishList = wmsFinishWarehouseMapper.queryFinishSummary(tenantId, dto);
        List<WmsFinishWarehouseVO> resultWarehousingList = wmsFinishWarehouseMapper.queryWarehousingSummary(tenantId, dto);
        List<WmsFinishWarehouseVO> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resultFinishList)) {
            // 手动对数据进行分组
            Map<String, List<WmsFinishWarehouseVO>> resultMap = resultFinishList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
            List<WmsFinishWarehouseVO> finishList = new ArrayList<>();
            for (Map.Entry<String, List<WmsFinishWarehouseVO>> resultEntry : resultMap.entrySet()) {
                List<WmsFinishWarehouseVO> valueList = resultEntry.getValue();
                WmsFinishWarehouseVO warehouseVO = valueList.get(0);
                Double sumQty = valueList.stream().map(WmsFinishWarehouseVO::getFinishQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
                warehouseVO.setFinishQty(sumQty != null ? BigDecimal.valueOf(sumQty) : BigDecimal.ZERO);
                finishList.add(warehouseVO);
            }
            resultList.addAll(finishList);
        }
        if (CollectionUtils.isNotEmpty(resultWarehousingList)) {
            // 手动分组
            Map<String, List<WmsFinishWarehouseVO>> resultMap = resultWarehousingList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
            List<WmsFinishWarehouseVO> warehousingList = new ArrayList<>();
            for (Map.Entry<String, List<WmsFinishWarehouseVO>> resultEntry : resultMap.entrySet()) {
                List<WmsFinishWarehouseVO> valueList = resultEntry.getValue();
                WmsFinishWarehouseVO warehouseVO = valueList.get(0);
                Double sumQty = valueList.stream().map(WmsFinishWarehouseVO::getWarehousingQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
                warehouseVO.setWarehousingQty(sumQty != null ? BigDecimal.valueOf(sumQty) : BigDecimal.ZERO);
                warehousingList.add(warehouseVO);
            }
            resultList.addAll(warehousingList);
        }
        return resultList;
    }

    private String spliceStr(WmsFinishWarehouseVO vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getSiteCode());
        sb.append(vo.getAreaCode());
        sb.append(vo.getAreaName());
        sb.append(vo.getProdLineName());
        sb.append(vo.getMaterialCode());
        sb.append(vo.getMaterialName());
        sb.append(vo.getProductionVersion());
        sb.append(vo.getItemGroupCode());
        sb.append(vo.getItemGroupDescription());
        sb.append(vo.getMaterialCategory());
        return sb.toString();
    }
}
