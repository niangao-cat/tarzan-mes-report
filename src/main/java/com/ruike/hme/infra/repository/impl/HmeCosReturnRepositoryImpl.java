package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.app.assembler.HmeCosFunctionMergeStrategy;
import com.ruike.hme.domain.repository.HmeCosReturnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeCosReturnMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.FileUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
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
 * @author sanfeng.zhang@hand-china.com 2021/11/3 16:36
 */
@Component
public class HmeCosReturnRepositoryImpl implements HmeCosReturnRepository {

    @Autowired
    private HmeCosReturnMapper hmeCosReturnMapper;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private MtUserClient mtUserClient;

    private static final String FILE_COS_RETURN = "COS退料报表";

    private static final String[] FIXED_TITLE = {"退料产线",	"工单号", "产品编码", "产品描述", "WAFER", "COS类型",	"退料条码",	"条码数量",	"组件物料编码", "组件物料描述", "处理方式", "单位", "单位用量",	"目标条码",	"数量",	"供应商", "供应商批次", "库存批次", "不良代码描述", "操作人", "操作时间", "操作工位编码",	"退料工位描述"};

    @Override
    @ProcessLovValue
    public Page<HmeCosReturnVO> queryRecordList(Long tenantId, HmeCosReturnDTO dto, PageRequest pageRequest) {
        Page<HmeCosReturnVO> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosReturnMapper.queryRecordList(tenantId, dto));
        this.handleCosReturn(tenantId, pageObj.getContent());
        return pageObj;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosReturnVO> export(Long tenantId, HmeCosReturnDTO dto) {
        return this.handleCosReturn(tenantId, hmeCosReturnMapper.queryRecordList(tenantId, dto));
    }

    public List<HmeCosReturnVO> handleCosReturn(Long tenantId, List<HmeCosReturnVO> resultList) {
        // 退料数量 根据退料条码汇总 报废的默认0
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<String> targetMaterialLotIdList = resultList.stream().map(HmeCosReturnVO::getTargetMaterialLotId).filter(targetMaterialLotId -> !StringUtils.equals("-1", targetMaterialLotId)).collect(Collectors.toList());
            Map<String, BigDecimal> cosReturnVO2Map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(targetMaterialLotIdList)) {
                List<HmeCosReturnVO2> cosReturnVO2List = hmeCosReturnMapper.queryCosReturnList(tenantId, targetMaterialLotIdList);
                cosReturnVO2Map = cosReturnVO2List.stream().collect(Collectors.toMap(HmeCosReturnVO2::getTargetMaterialLotId, HmeCosReturnVO2::getReturnQty, (n1, n2) -> n1.add(n2)));
            }
            for (HmeCosReturnVO hmeCosReturnVO : resultList) {
                BigDecimal returnQty = cosReturnVO2Map.getOrDefault(hmeCosReturnVO.getTargetMaterialLotId(), null);
                if (returnQty != null) {
                    hmeCosReturnVO.setReturnQty(returnQty);
                }
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    @Async
    public void asyncExport(Long tenantId, HmeCosReturnDTO dto) throws IOException {
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
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_COS_RETURN;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try{
            //查询动态列标题及表格数据
            List<HmeCosReturnVO> resultList = hmeCosReturnMapper.queryRecordList(tenantId, dto);
            resultList = handleCosReturn(tenantId, resultList);
            // 生成文档及准备工作
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet(exportTaskVO.getTaskName());
            Map<String, XSSFCellStyle> styles = ExcelUtils.createStyles(workbook);

            // 写入表头
            List<String> titles = new ArrayList<>();
            titles.addAll(Arrays.asList(FIXED_TITLE));
            XSSFRow headerRow = sheet.createRow(0);
            this.fillHeaderRow(sheet, headerRow, titles, styles.get("center"));

            //写入数据
            Integer rowIndex = 1;
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (HmeCosReturnVO rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getProdLineCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWaferNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getCosType()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getReturnMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getPrimaryUomQty() != null ? rec.getPrimaryUomQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getComponentMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getComponentMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getReturnTypeMeaning()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getUomCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getUsageQty() != null ? rec.getUsageQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getTargetMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getReturnQty() != null ? rec.getReturnQty().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getSupplierName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getSupplierLot()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getLot()).orElse(""));

                    fields.add(Optional.ofNullable(rec.getNcDescription()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getRealName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getCreationDate()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellName()).orElse(""));
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
            errorInfo = e.getMessage() + ",API【HmeCosFunctionServiceImpl.cosFunctionReportAsyncExport】";
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

    private void fillRow(XSSFRow row, List<String> fields, XSSFCellStyle style) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            XSSFCell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(field);
            cell.setCellStyle(style);
        });
    }

    /**
     * 输入行内容
     *
     * @param row    行
     * @param fields 字段
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

}
