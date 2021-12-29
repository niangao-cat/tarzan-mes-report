package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.query.HmeProductionFlowQuery;
import com.ruike.hme.api.dto.representation.HmeProductionFlowRepresentation;
import com.ruike.hme.domain.repository.HmeProductionFlowRepository;
import com.ruike.hme.domain.vo.EoWorkcellStationNcInfoVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.JobEquipmentVO;
import com.ruike.hme.domain.vo.SnStepLabCodeVO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeProductionFlowMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.FileUtils;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO2;
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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import tarzan.common.infra.mapper.SiteMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 生产流转查询报表 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 16:03
 */
@Repository
public class HmeProductionFlowRepositoryImpl implements HmeProductionFlowRepository {
    private static final MtUserInfo BLANK_USER = new MtUserInfo();
    private static final BigDecimal MINUTE = BigDecimal.valueOf(1000 * 60);
    private static final String FILE_TASK_NAME = "生产流转查询报表";

    private static final String[] FIXED_TITLE = {"序号", "产线", "工段", "工序", "工单号", "工单版本", "工单状态", "物料编码", "物料描述",  "SN", "返修SN", "工艺步骤", "实验代码", "作业平台类型",	"工位",	"加工开始时间",	"班次日期", "班次", "进站人员", "加工结束时间", "出站人员", "加工时长(分)", "不良", "是否返修", "设备编码", "设备名称", "备注"};


    private final HmeProductionFlowMapper mapper;
    private final SiteMapper siteMapper;
    private final MtUserClient userClient;
    private final HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    private final HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    private final MtUserClient mtUserClient;

    public HmeProductionFlowRepositoryImpl(HmeProductionFlowMapper mapper, SiteMapper siteMapper, MtUserClient userClient, HmeHzeroFileFeignClient hmeHzeroFileFeignClient, HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient, MtUserClient mtUserClient) {
        this.mapper = mapper;
        this.siteMapper = siteMapper;
        this.userClient = userClient;
        this.hmeHzeroFileFeignClient = hmeHzeroFileFeignClient;
        this.hmeHzeroPlatformFeignClient = hmeHzeroPlatformFeignClient;
        this.mtUserClient = mtUserClient;
    }

    @Override
    @ProcessLovValue
    public Page<HmeProductionFlowRepresentation> pagedList(Long tenantId, PageRequest pageRequest, HmeProductionFlowQuery dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = siteMapper.selectSiteByUser(userId);
        if (StringUtils.isEmpty(siteId)) {
            return new Page<>();
        }
        //将产线、工段、工序等查询条件转换到工位身上
        List<String> workcellStationIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getWorkcellStationIdList())){
            workcellStationIdList = dto.getWorkcellStationIdList();
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellProcessIdList())){
            //根据工序查询工位
            workcellStationIdList = mapper.workcellByProcessBatchQuery(tenantId, dto.getWorkcellProcessIdList());
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellLineIdList())){
            //根据工段查询工位
            workcellStationIdList = mapper.workcellByLineWorkcellBatchQuery(tenantId, dto.getWorkcellLineIdList());
        }else if(StringUtils.isNotBlank(dto.getProdLineId())){
            //根据产线查询工位
            workcellStationIdList = mapper.workcellByProdLineQuery(tenantId, dto.getProdLineId());
        }
        dto.setWorkcellStationIdList(workcellStationIdList);
        // 查询工序流转
        Page<HmeProductionFlowRepresentation> page = PageHelper.doPage(pageRequest, () -> mapper.selectList(tenantId, siteId, dto));
        displayFieldsCompletion(tenantId, page.getContent());
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeProductionFlowRepresentation> export(Long tenantId, HmeProductionFlowQuery dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = siteMapper.selectSiteByUser(userId);
        if (StringUtils.isEmpty(siteId)) {
            return null;
        }
        //将产线、工段、工序等查询条件转换到工位身上
        List<String> workcellStationIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getWorkcellStationIdList())){
            workcellStationIdList = dto.getWorkcellStationIdList();
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellProcessIdList())){
            //根据工序查询工位
            workcellStationIdList = mapper.workcellByProcessBatchQuery(tenantId, dto.getWorkcellProcessIdList());
        }else if(CollectionUtils.isNotEmpty(dto.getWorkcellLineIdList())){
            //根据工段查询工位
            workcellStationIdList = mapper.workcellByLineWorkcellBatchQuery(tenantId, dto.getWorkcellLineIdList());
        }else if(StringUtils.isNotBlank(dto.getProdLineId())){
            //根据产线查询工位
            workcellStationIdList = mapper.workcellByProdLineQuery(tenantId, dto.getProdLineId());
        }
        dto.setWorkcellStationIdList(workcellStationIdList);
        // 查询工序流转
        List<HmeProductionFlowRepresentation> list = mapper.selectList(tenantId, siteId, dto);
        displayFieldsCompletion(tenantId, list);
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeProductionFlowRepresentation> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 查询用户信息
        List<Long> userIdList = Stream.concat(list.stream().map(HmeProductionFlowRepresentation::getCreatedBy), list.stream().map(HmeProductionFlowRepresentation::getCreatedBy)).collect(Collectors.toList());
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, userIdList);

        // 查询不良信息
        List<EoWorkcellStationNcInfoVO> eoWorkcellStationNcInfoVOList = list.stream().map(rec -> new EoWorkcellStationNcInfoVO(rec.getWorkcellId(), rec.getEoId())).distinct().collect(Collectors.toList());
        List<EoWorkcellStationNcInfoVO> ncList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(eoWorkcellStationNcInfoVOList)){
            List<List<EoWorkcellStationNcInfoVO>> splitList = CommonUtils.splitSqlList(eoWorkcellStationNcInfoVOList, 1000);
            for (List<EoWorkcellStationNcInfoVO> split:splitList) {
                ncList.addAll(mapper.ncInfoFlagQuery(tenantId, split));
            }
        }
        Map<String, Long> ncMap = ncList.stream().collect(Collectors.toMap(rec -> rec.getWorkcellId() + "#" + rec.getEoId(), EoWorkcellStationNcInfoVO::getNcRecordCount, (a, b) -> a));

        // 查询实验代码
        List<SnStepLabCodeVO> snStepList = list.stream().filter(rec -> StringUtils.isNotBlank(rec.getMaterialLotId()) && StringUtils.isNotBlank(rec.getRouterStepId())).map(rec -> new SnStepLabCodeVO(rec.getMaterialLotId(), rec.getRouterStepId())).distinct().collect(Collectors.toList());
        List<SnStepLabCodeVO> labCodeList = CollectionUtils.isEmpty(snStepList) ? new ArrayList<>() : mapper.selectLabCodeList(snStepList);
        Map<String, String> labCodeMap = CollectionUtils.isEmpty(labCodeList) ? new HashMap<>(16) : labCodeList.stream().collect(Collectors.toMap(rec -> rec.getMaterialLotId() + "#" + rec.getRouterStepId(), SnStepLabCodeVO::getLabCode, (a, b) -> a));

        // 查询设备
        List<String> jobIdList = list.stream().map(HmeProductionFlowRepresentation::getJobId).distinct().collect(Collectors.toList());
        List<JobEquipmentVO> equipmentList = CollectionUtils.isEmpty(jobIdList) ? new ArrayList<>() : mapper.selectEquipmentList(jobIdList);
        Map<String, List<JobEquipmentVO>> equipmentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(equipmentList)) {
            equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(JobEquipmentVO::getJobId));
        }

        //查询工序、工段、产线信息
        List<String> workcellIdList = list.stream().map(HmeProductionFlowRepresentation::getWorkcellId).distinct().collect(Collectors.toList());
        List<WmsSummaryOfCosBarcodeProcessingVO2> processLineProdInfoList = mapper.qeuryProcessLineProdByWorkcell(tenantId, workcellIdList);
        Map<String, List<WmsSummaryOfCosBarcodeProcessingVO2>> processLineProdInfoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(processLineProdInfoList)){
            processLineProdInfoMap = processLineProdInfoList.stream().collect(Collectors.groupingBy(WmsSummaryOfCosBarcodeProcessingVO2::getWorkcellId));
        }

        AtomicLong lineNum = new AtomicLong(0L);
        Map<String, List<JobEquipmentVO>> finalEquipmentMap = equipmentMap;
        for (HmeProductionFlowRepresentation rec : list) {
            rec.setLineNum(lineNum.incrementAndGet());
            rec.setCreateUserName(userMap.getOrDefault(rec.getCreatedBy(), BLANK_USER).getRealName());
            rec.setOperatorUserName(userMap.getOrDefault(rec.getOperatorId(), BLANK_USER).getRealName());
            List<WmsSummaryOfCosBarcodeProcessingVO2> singleProcessLineProdInfo = processLineProdInfoMap.get(rec.getWorkcellId());
            if(CollectionUtils.isNotEmpty(singleProcessLineProdInfo)){
                rec.setWorkcellProcessName(singleProcessLineProdInfo.get(0).getProcessName());
                rec.setWorkcellLineName(singleProcessLineProdInfo.get(0).getLineWorkcellName());
                rec.setProdLineCode(singleProcessLineProdInfo.get(0).getProdLineName());
            }

            //加工时长
            rec.setProcessTime(rec.getSiteInDate() != null && rec.getSiteOutDate() != null ? BigDecimal.valueOf(rec.getSiteOutDate().getTime() - rec.getSiteInDate().getTime()).divide(MINUTE, 2, BigDecimal.ROUND_HALF_UP) : null);

            // 不良信息点击标识
            String ncKey = rec.getWorkcellId() + "#" + rec.getEoId();
            rec.setNcInfoFlag(ncMap.containsKey(ncKey) && ncMap.get(ncKey) > 0);
            if(rec.getNcInfoFlag()){
                rec.setNcInfoFlagMeaning("是");
            }else {
                rec.setNcInfoFlagMeaning("否");
            }
            // 实验代码
            String labKey = rec.getMaterialLotId() + "#" + rec.getRouterStepId();
            rec.setLabCode(labCodeMap.getOrDefault(labKey, ""));

            // 设备
            List<JobEquipmentVO> jobEquipmentVOS = finalEquipmentMap.get(rec.getJobId());
            if (CollectionUtils.isNotEmpty(jobEquipmentVOS)) {
                rec.setAssetEncoding(jobEquipmentVOS.get(0).getAssetEncoding());
                rec.setAssetName(jobEquipmentVOS.get(0).getAssetName());
            }
        }

    }

    @Override
    @Async
    @ProcessLovValue
    public void asyncExport(Long tenantId, HmeProductionFlowQuery dto) throws IOException {
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
        DetailsHelper.setCustomUserDetails(exportTaskVO.getUserId(), "zh_CN");
        String uuid = exportTaskVO.getTaskCode();
        String prefix = uuid + "@用户-" + currentUserName + "@时间-" + DateUtil.format(CommonUtils.currentTimeGet(), "yyyyMMddHHmmss")+ "@" + FILE_TASK_NAME;
        String suffix = ".xlsx";
        String fileName = prefix + suffix;

        //上传文件
        String state = CommonUtils.ExportTaskStateValue.DONE;
        String errorInfo = Strings.EMPTY;
        ResponseEntity<String> returnStr = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //查询动态列标题及表格数据
            List<HmeProductionFlowRepresentation> resultList = this.export(tenantId, dto);

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
                for (HmeProductionFlowRepresentation rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getLineNum().toString()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProdLineCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellLineName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellProcessName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkOrderNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProductionVersion()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkOrderStatusMeaning()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialLotCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getReworkMaterialLot()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getStepDescription()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getLabCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getJobTypeMeaning()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getWorkcellCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getSiteInDate() != null ? DateUtil.format(rec.getSiteInDate(), "yyyy-MM-dd HH:mm:ss") : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getShiftDate()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getShiftCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getCreateUserName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getSiteOutDate() != null ? DateUtil.format(rec.getSiteOutDate(), "yyyy-MM-dd HH:mm:ss") : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getOperatorUserName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessTime() != null ? rec.getProcessTime().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getNcInfoFlagMeaning()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getIsRework()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getAssetEncoding()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getAssetName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getRemark()).orElse(""));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName) {
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
}
