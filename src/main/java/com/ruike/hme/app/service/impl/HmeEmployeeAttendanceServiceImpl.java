package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.api.dto.representation.HmeProductionFlowRepresentation;
import com.ruike.hme.app.service.HmeEmployeeAttendanceService;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcelUtils;
import com.ruike.hme.infra.util.FileUtils;
import com.ruike.hme.infra.util.HmeCommonUtils;
import com.ruike.qms.infra.mapper.QmsIqcInspectionKanbanMapper;
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
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.common.domain.sys.MtException;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;
import tarzan.common.infra.mapper.MtExtendMapper;
import tarzan.common.infra.mapper.SiteMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class HmeEmployeeAttendanceServiceImpl implements HmeEmployeeAttendanceService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtUserClient mtUserClient;

    @Autowired
    private HmeSignInOutRecordMapper hmeSignInOutRecordMapper;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private MtExtendMapper mtExtendMapper;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private QmsIqcInspectionKanbanMapper qmsIqcInspectionKanbanMapper;

    public static final Long HOUR = 3600000L;
    public static final Long RECOUND = 60000L;
    public static final Long MISS = 1000L;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static DateTimeFormatter dteTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final String patter = "yyyy-MM-dd HH";

    private static final String FILE_TASK_NAME = "????????????????????????";

    private static final String[] FIXED_TITLE = {"??????", "??????", "??????", "??????", "??????", "????????????", "????????????", "????????????", "????????????",  "??????", "?????????", "?????????", "?????????", "???????????????", "???????????????"};

    private String spliceStr (HmeEmployeeAttendanceExportVO6 vo6) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo6.getSiteInBy());
        sb.append(vo6.getProcessId());
        sb.append(vo6.getSnMaterialId());
        sb.append(vo6.getProductionVersion());
        return sb.toString();
    }

    private String spliceResultStr (HmeEmployeeAttendanceExportVO5 vo5) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo5.getUserId());
        sb.append(vo5.getProcessId());
        sb.append(vo5.getMaterialId());
        sb.append(vo5.getMaterialVersion());
        return sb.toString();
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO5> sumQuery(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest) {

        //V20210730 modify by penglin.sui for tianyang.xie ?????????????????? 00:00
        dto.setDateFrom(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateFrom() , "yyyy-MM-dd HH") + ":00:00","yyyy-MM-dd HH:mm:ss"));
        dto.setDateTo(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateTo() , "yyyy-MM-dd HH") + ":59:59","yyyy-MM-dd HH:mm:ss"));

        Page<HmeEmployeeAttendanceExportVO5> resultPage = new Page<>();
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProcess(tenantId, dto.getProcessId());
            dto.setProcessIdList(Arrays.asList(dto.getProcessId().split(",")));
        }
//        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isNotEmpty(dto.getLineWorkcellId())){
//            //?????????????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellId());
//        }
        if(StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            //?????????????????????????????????????????????????????????
            dto.setLineWorkcellIdList(Arrays.asList(dto.getLineWorkcellId().split(",")));
        }
//        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getLineWorkcellId()) && StringUtils.isNotEmpty(dto.getProdLineId())){
//            //?????????????????????????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProdLine(tenantId, dto.getProdLineId());
//        }
        if(StringUtils.isNotEmpty(dto.getProdLineId())){
            //?????????????????????????????????????????????????????????????????????
            dto.setProdLineIdList(Arrays.asList(dto.getProdLineId().split(",")));
        }
        List<HmeEmployeeAttendanceExportVO5> allHmeEmployeeAttendanceExportVO5List =
                hmeSignInOutRecordMapper.sumQuery2(tenantId , dto);
//        if(CollectionUtils.isNotEmpty(workcellIdList)){
//            workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
//            List<List<String>> allWorkcellIdList = Utils.splitSqlList(workcellIdList,1000);
//            for (List<String> subWorkcellIdList : allWorkcellIdList
//                 ) {
//                dto.setWorkcellIdList(subWorkcellIdList);
//                List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = hmeSignInOutRecordMapper.sumQuery(tenantId, dto);
//                if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
//                    allHmeEmployeeAttendanceExportVO5List.addAll(hmeEmployeeAttendanceExportVO5List);
//                }
//            }
//        }
//        else {
//            dto.setWorkcellIdList(workcellIdList);
//            allHmeEmployeeAttendanceExportVO5List = hmeSignInOutRecordMapper.sumQuery(tenantId, dto);
//        }

        if(CollectionUtils.isNotEmpty(allHmeEmployeeAttendanceExportVO5List)){
            resultPage = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), allHmeEmployeeAttendanceExportVO5List);
        }else{
            return new Page<>();
        }

        // ??????????????????
        List<Long> userIdList = resultPage.getContent().stream().map(vo -> {return Long.parseLong(vo.getUserId());}).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(userIdList)) {
//            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
//        }
//        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
//        Long userId = Objects.isNull(customUserDetails) ? -1L : DetailsHelper.getUserDetails().getUserId();
//        //??????????????????????????????
//        String siteId = siteMapper.selectSiteByUser(userId);
//        if(StringUtils.isEmpty(siteId)){
//            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
//        }
//        // ?????????????????????????????????
        List<String> processIdList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
//        // ??????????????????
//        Map<String, List<MtExtendVO>> attrMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(processIdList)) {
//            List<HmeOrganizationVO> hmeOrganizationVOS = hmeSignInOutRecordMapper.queryOrganizationByProcessIds(tenantId,siteId, processIdList);
//            if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
//                hmeOrganizationMap = hmeOrganizationVOS.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getProcessId));
//            }
//            List<MtExtendVO> mtExtendVOList = mtExtendMapper.selectExtend(tenantId , processIdList , Collections.singletonList("OUTPUT_FLAG"));
//            if(CollectionUtils.isNotEmpty(mtExtendVOList)){
//                attrMap = mtExtendVOList.stream().collect(Collectors.groupingBy(MtExtendVO::getKeyId));
//            }
//        }
//        // ??????
        List<String> materialIdList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialVersionList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getMaterialVersion).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<HmeEmployeeAttendanceExportVO6> countNumberList = hmeSignInOutRecordMapper.queryBatchSumCountNumber(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> countNumberMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(countNumberList)) {
//            countNumberMap = countNumberList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ?????????
        List<HmeEmployeeAttendanceExportVO6> makeList = hmeSignInOutRecordMapper.queryBatchSumInMakeNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> makeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(makeList)) {
            makeMap = makeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ?????????
//        List<HmeEmployeeAttendanceExportVO6> repairNumList = hmeSignInOutRecordMapper.queryBatchSumRepairNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> repairNumMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(repairNumList)) {
//            repairNumMap = repairNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ???????????????
        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNumList = hmeSignInOutRecordMapper.queryBatchSumEoWorkcellGroup(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoWorkcellNumList)) {
            eoWorkcellNumMap = eoWorkcellNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ??????
//        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNcList = hmeSignInOutRecordMapper.queryBatchEoWorkcellList(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNcMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(eoWorkcellNcList)) {
//            eoWorkcellNcMap = eoWorkcellNcList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ???????????????
//        List<HmeEmployeeAttendanceExportVO6> totalProductionTimeList = hmeSignInOutRecordMapper.queryBatchTotalProductionTime(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> totalProductionTimeMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(totalProductionTimeList)) {
//            totalProductionTimeMap = totalProductionTimeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportNMap = new HashMap<>();
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportYMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultPage)) {
            List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportNVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagNGroupQuery(tenantId, dto,resultPage);
            if(CollectionUtils.isNotEmpty(employeeAttendanceExportNVO10s)){
                employeeAttendanceExportNMap = employeeAttendanceExportNVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
            }
            List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportYVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagYGroupQuery(tenantId, dto,resultPage);
            if(CollectionUtils.isNotEmpty(employeeAttendanceExportYVO10s)){
                employeeAttendanceExportYMap = employeeAttendanceExportYVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
            }
        }

        // ????????????
        for (HmeEmployeeAttendanceExportVO5 result : resultPage) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
//            if (StringUtils.isNotBlank(result.getUserId())) {
//                MtUserInfo mtUserInfo = userInfoMap.get(Long.parseLong(result.getUserId()));
//                if (mtUserInfo != null) {
//                    // ????????????
//                    result.setUserName(mtUserInfo.getRealName());
//                    //??????
//                    result.setUserNum(mtUserInfo.getLoginName());
//                }
//            }
//            if (StringUtils.isNotBlank(result.getProcessId())) {
//                List<HmeOrganizationVO> hmeOrganizationVOS = hmeOrganizationMap.get(result.getProcessId());
//                if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
//                    // ??????
//                    result.setLineWorkcellId(hmeOrganizationVOS.get(0).getLineWorkcellId());
//                    result.setLineWorkcerllName(hmeOrganizationVOS.get(0).getLineWorkcellName());
//                    //??????
//                    result.setProdLineId(hmeOrganizationVOS.get(0).getProdLineId());
//                    result.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());
//                }
//            }
            //????????????
//            BigDecimal actualOutputNumber = BigDecimal.ZERO;
//            List<MtExtendVO> mtExtendAttrVOS = attrMap.get(result.getProcessId());
//            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
//                actualOutputNumber = hmeSignInOutRecordMapper.getSumActualOutputNumber(tenantId, result);
//            }
//            result.setActualOutputNumber(actualOutputNumber);
            //??????
//            List<HmeEmployeeAttendanceExportVO6>  exportVO6s = countNumberMap.get(spliceResultStr(result));
//            result.setCountNumber(CollectionUtils.isNotEmpty(exportVO6s) ? exportVO6s.get(0).getQty() : BigDecimal.ZERO);
            //?????????
            List<HmeEmployeeAttendanceExportVO6> marks = makeMap.get(spliceResultStr(result));
            result.setInMakeNum(CollectionUtils.isNotEmpty(marks) ? marks.get(0).getQty() : BigDecimal.ZERO);
            //?????????
//            List<HmeEmployeeAttendanceExportVO6> repairNums = repairNumMap.get(spliceResultStr(result));
//            result.setRepairNum(CollectionUtils.isNotEmpty(repairNums) ? repairNums.get(0).getQty() : BigDecimal.ZERO);
            //???????????????
            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNums = eoWorkcellNumMap.get(spliceResultStr(result));
            log.info("<=============???????????????===============>:" + result.getUserId() + "#" + result.getProcessId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion());
            BigDecimal eoWorkcellNum = CollectionUtils.isNotEmpty(eoWorkcellNums) ? BigDecimal.valueOf(eoWorkcellNums.size()) : BigDecimal.ZERO;
            //?????????
//            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNcs = eoWorkcellNcMap.get(spliceResultStr(result));
//            if (CollectionUtils.isNotEmpty(eoWorkcellNcs)) {
//                List<String> eoIdList = eoWorkcellNcs.stream().map(HmeEmployeeAttendanceExportVO6::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//                List<String> workcellIds = eoWorkcellNcs.stream().map(HmeEmployeeAttendanceExportVO6::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//                BigDecimal defectsNumber = hmeSignInOutRecordMapper.getSumDefectsNumbTwo(tenantId, eoIdList, workcellIds, dto.getDateFrom(), dto.getDateTo());
//                result.setDefectsNumber(defectsNumber);
//            }
            //???????????????
            BigDecimal totalProductionTime = result.getTotalProductionTimeDecimal().divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP);
            result.setTotalProductionTime( totalProductionTime + "h");
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagNEoList = employeeAttendanceExportNMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagNEoList = employeeAttendanceExportReworkFlagNEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagYEoList = employeeAttendanceExportYMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagYEoList = employeeAttendanceExportReworkFlagYEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());
                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
                //???????????????
//                List<HmeEmployeeAttendanceExportVO6> totalProductionTimes = totalProductionTimeMap.get(spliceResultStr(result));
//                BigDecimal totalProductionTime = CollectionUtils.isNotEmpty(totalProductionTimes) ? totalProductionTimes.get(0).getTotalProductionTime() : BigDecimal.ZERO;
//                result.setTotalProductionTime(BigDecimal.valueOf(Double.valueOf(result.getTotalProductionTime())).divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP) + "h");
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO5> sumQueryNew(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest) {
        Date dateFrom = dto.getDateFrom();
        Date dateTo = dto.getDateTo();
        Date nowDate = new Date();

        //V20210730 modify by penglin.sui for tianyang.xie ?????????????????? 00:00
        dto.setDateFrom(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateFrom() , "yyyy-MM-dd HH") + ":00:00","yyyy-MM-dd HH:mm:ss"));
        dto.setDateTo(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateTo() , "yyyy-MM-dd HH") + ":59:59","yyyy-MM-dd HH:mm:ss"));

        Page<HmeEmployeeAttendanceExportVO5> resultPage = new Page<>();
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            dto.setProcessIdList(Arrays.asList(dto.getProcessId().split(",")));
        }
        if(StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            dto.setLineWorkcellIdList(Arrays.asList(dto.getLineWorkcellId().split(",")));
        }
        if(StringUtils.isNotEmpty(dto.getProdLineId())){
            dto.setProdLineIdList(Arrays.asList(dto.getProdLineId().split(",")));
        }
        //???????????????????????????????????????1??????
        Date preMaxJobTime = hmeSignInOutRecordMapper.selectMaxJobTime(tenantId);
        Date preMaxJobTimeAdd = CommonUtils.calculateDate(preMaxJobTime , 1 , Calendar.HOUR_OF_DAY , patter);

        List<HmeEmployeeAttendanceExportVO5> allHmeEmployeeAttendanceExportVO5List = new ArrayList<>();
        allHmeEmployeeAttendanceExportVO5List.addAll(hmeSignInOutRecordMapper.sumQuery2(tenantId , dto));
        if(dateTo.getTime() >= nowDate.getTime()){
            //????????????????????????????????????????????????,???????????????????????????????????????????????????????????????????????????
            if(dateFrom.getTime() > dto.getDateFrom().getTime()){
                if(CollectionUtils.isNotEmpty(allHmeEmployeeAttendanceExportVO5List)){
                    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dto.getDateFrom(), dateFrom, dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:allHmeEmployeeAttendanceExportVO5List) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
            }
            //????????????jobTime+1??????????????????????????????????????????????????????????????????????????????????????????
            if(dateTo.getTime() > preMaxJobTimeAdd.getTime()){
                List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, preMaxJobTimeAdd, dateTo, dto);
                if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                    //???????????????????????????????????????
                    for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:allHmeEmployeeAttendanceExportVO5List) {
                        List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                            HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                            hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().add(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                            hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().add(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                            hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().add(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                            hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().add(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                            hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().add(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                        }
                    }
                    //???????????????????????????
                    for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:hmeEmployeeAttendanceExportVO5List) {
                        List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = allHmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                        if(CollectionUtils.isEmpty(singleEmployeeAttendanceExportVO5List)){
                            allHmeEmployeeAttendanceExportVO5List.add(hmeEmployeeAttendanceExportVO5);
                        }
                    }
                }
            }
        }else {
            //????????????????????????????????????????????????,????????????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(allHmeEmployeeAttendanceExportVO5List)){
                if(dateFrom.getTime() > dto.getDateFrom().getTime()){
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dto.getDateFrom(), dateFrom, dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:allHmeEmployeeAttendanceExportVO5List) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
                if(dto.getDateTo().getTime() > dateTo.getTime()){
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dateTo, dto.getDateTo(), dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:allHmeEmployeeAttendanceExportVO5List) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
            }
        }

        if(CollectionUtils.isNotEmpty(allHmeEmployeeAttendanceExportVO5List)){
            resultPage = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), allHmeEmployeeAttendanceExportVO5List);
        }else{
            return new Page<>();
        }

        // ??????????????????
        List<Long> userIdList = resultPage.getContent().stream().map(vo -> {return Long.parseLong(vo.getUserId());}).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> processIdList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialIdList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialVersionList = resultPage.getContent().stream().map(HmeEmployeeAttendanceExportVO5::getMaterialVersion).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ?????????
        List<HmeEmployeeAttendanceExportVO6> makeList = hmeSignInOutRecordMapper.queryBatchSumInMakeNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dateFrom, dateTo);
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> makeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(makeList)) {
            makeMap = makeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ???????????????
        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNumList = hmeSignInOutRecordMapper.queryBatchSumEoWorkcellGroup(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dateFrom, dateTo);
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoWorkcellNumList)) {
            eoWorkcellNumMap = eoWorkcellNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportNMap = new HashMap<>();
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportYMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultPage)) {
            dto.setDateFrom(dateFrom);
            dto.setDateTo(dateTo);
            List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportNVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagNGroupQuery(tenantId, dto,resultPage);
            if(CollectionUtils.isNotEmpty(employeeAttendanceExportNVO10s)){
                employeeAttendanceExportNMap = employeeAttendanceExportNVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
            }
            List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportYVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagYGroupQuery(tenantId, dto,resultPage);
            if(CollectionUtils.isNotEmpty(employeeAttendanceExportYVO10s)){
                employeeAttendanceExportYMap = employeeAttendanceExportYVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
            }
        }

        // ????????????
        for (HmeEmployeeAttendanceExportVO5 result : resultPage) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
            //?????????
            List<HmeEmployeeAttendanceExportVO6> marks = makeMap.get(spliceResultStr(result));
            result.setInMakeNum(CollectionUtils.isNotEmpty(marks) ? marks.get(0).getQty() : BigDecimal.ZERO);
            //???????????????
            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNums = eoWorkcellNumMap.get(spliceResultStr(result));
            log.info("<=============???????????????===============>:" + result.getUserId() + "#" + result.getProcessId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion());
            BigDecimal eoWorkcellNum = CollectionUtils.isNotEmpty(eoWorkcellNums) ? BigDecimal.valueOf(eoWorkcellNums.size()) : BigDecimal.ZERO;
            //???????????????
            BigDecimal totalProductionTime = result.getTotalProductionTimeDecimal().divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP);
            result.setTotalProductionTime( totalProductionTime + "h");
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagNEoList = employeeAttendanceExportNMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagNEoList = employeeAttendanceExportReworkFlagNEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagYEoList = employeeAttendanceExportYMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagYEoList = employeeAttendanceExportReworkFlagYEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());
                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
            }
        }
        return resultPage;
    }

    @Override
    public List<HmeEmployeeAttendanceExportVO5> employeeOutPutSummary(Long tenantId, Date startTime, Date endTime, HmeEmployeeAttendanceDTO13 queryDto) {
        List<HmeEmployeeAttendanceExportVO5> result = new ArrayList<>();
        // ????????????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<String> siteIdList = qmsIqcInspectionKanbanMapper.queryDefaultSiteByUserId(tenantId, userId);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(siteIdList) || siteIdList.size() > 1) {
            throw new CommonException("??????????????????????????????????????????,?????????!");
        }
        String defaultSiteId = siteIdList.get(0);

        List<HmeEmployeeOutputSummary> resultList = new ArrayList<>();
        //??????????????????
        List<HmeEmployeeOutputSummary> signInList = hmeSignInOutRecordMapper.selectDataOfSignIn(tenantId , startTime , endTime);
        if(CollectionUtils.isNotEmpty(signInList)){
            resultList.addAll(signInList);
        }

        //??????????????????
        List<HmeEmployeeOutputSummary> signOutList = hmeSignInOutRecordMapper.selectDataOfSignOut(tenantId , startTime , endTime);
        if(CollectionUtils.isNotEmpty(signOutList)){
            resultList.addAll(signOutList);
        }

        if(CollectionUtils.isEmpty(resultList)){
            return result;
        }

        //??????
        resultList = resultList.stream().filter(CommonUtils.distinctByKey(item -> item.getTenantId() + "#" + item.getMaterialId()
                + "#"  + item.getUserId() + item.getProcessId() + item.getProductionVersion()))
                .collect(Collectors.toList());

        // ?????????????????????????????????
        List<String> processIdList = resultList.stream()
                .map(HmeEmployeeOutputSummary::getProcessId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<HmeOrganizationVO>> hmeOrganizationVOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(processIdList)){
            hmeOrganizationVOMap = queryOrganizationByProcessIds(tenantId , defaultSiteId , processIdList);
        }

        //??????????????????
        Map<String , List<HmeEmployeeOutputSummaryVO>> employeeOutputSummaryMap =
                querySummarys(tenantId , defaultSiteId , startTime , endTime , resultList);

        BigDecimal qty = BigDecimal.ZERO;
        for (HmeEmployeeOutputSummary dto : resultList) {
            List<HmeOrganizationVO> hmeOrganizationVOS = hmeOrganizationVOMap.get(dto.getProcessId());
            // ??????
            dto.setLineId(hmeOrganizationVOS.get(0).getLineWorkcellId());
            dto.setLineCode(hmeOrganizationVOS.get(0).getLineWorkcellCode());
            dto.setLineName(hmeOrganizationVOS.get(0).getLineWorkcellName());
            //??????
            dto.setProdLineId(hmeOrganizationVOS.get(0).getProdLineId());
            dto.setProdLineCode(hmeOrganizationVOS.get(0).getProdLineCode());
            dto.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());

            List<HmeEmployeeOutputSummaryVO> subEmployeeOutputSummaryList = employeeOutputSummaryMap.getOrDefault(dto.getUserId()
                    + "#" + dto.getProcessId() + "#" + dto.getProductionVersion() + "#" + dto.getMaterialId() + "#"
                    + CommonUtils.dateToString(dto.getJobTime() , patter) , new ArrayList<>());
            if(CollectionUtils.isEmpty(subEmployeeOutputSummaryList)){
                dto.setOutputQty(BigDecimal.ZERO);
                dto.setReworkQty(BigDecimal.ZERO);
                dto.setTotalDuration(BigDecimal.ZERO);
                dto.setActualOutputQty(BigDecimal.ZERO);
                dto.setNcQty(BigDecimal.ZERO);
            }else{
                //??????
                qty = subEmployeeOutputSummaryList.stream()
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setOutputQty(qty);

                //?????????
                qty = subEmployeeOutputSummaryList.stream()
                        .filter(item -> "Y".equals(item.getReworkFlag()))
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setReworkQty(qty);

                //???????????????
                qty = subEmployeeOutputSummaryList.stream()
                        .map(HmeEmployeeOutputSummaryVO::getTotalProductionTime)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setTotalDuration(qty);

                //????????????
                qty = subEmployeeOutputSummaryList.stream()
                        .filter(item -> "N".equals(item.getReworkFlag()))
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setActualOutputQty(qty);

                //??????
                qty = subEmployeeOutputSummaryList.get(0).getNgQty();
                dto.setNcQty(qty);
            }
            HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5 = new HmeEmployeeAttendanceExportVO5();
            hmeEmployeeAttendanceExportVO5.setUserId(dto.getUserId().toString());
            hmeEmployeeAttendanceExportVO5.setUserName(dto.getRealName());
            hmeEmployeeAttendanceExportVO5.setUserNum(dto.getLoginName());
            hmeEmployeeAttendanceExportVO5.setProcessId(dto.getProcessId());
            hmeEmployeeAttendanceExportVO5.setProcessName(dto.getProcessName());
            hmeEmployeeAttendanceExportVO5.setMaterialId(dto.getMaterialId());
            hmeEmployeeAttendanceExportVO5.setMaterialCode(dto.getMaterialCode());
            hmeEmployeeAttendanceExportVO5.setMaterialName(dto.getMaterialName());
            hmeEmployeeAttendanceExportVO5.setMaterialVersion(dto.getProductionVersion());
            hmeEmployeeAttendanceExportVO5.setProdLineId(dto.getProdLineId());
            hmeEmployeeAttendanceExportVO5.setProdLineName(dto.getProdLineName());
            hmeEmployeeAttendanceExportVO5.setLineWorkcellId(dto.getLineId());
            hmeEmployeeAttendanceExportVO5.setLineWorkcerllName(dto.getLineName());
            hmeEmployeeAttendanceExportVO5.setActualOutputNumber(dto.getActualOutputQty());
            hmeEmployeeAttendanceExportVO5.setCountNumber(dto.getOutputQty());
            hmeEmployeeAttendanceExportVO5.setDefectsNumber(dto.getNcQty());
            hmeEmployeeAttendanceExportVO5.setRepairNum(dto.getReworkQty());
            hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(dto.getTotalDuration());
            result.add(hmeEmployeeAttendanceExportVO5);
        }
        if(CollectionUtils.isNotEmpty(result)) {
            //???????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(queryDto.getProdLineIdList())) {
                result = result.stream().filter(item -> queryDto.getProdLineIdList().contains(item.getProdLineId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(queryDto.getLineWorkcellIdList())) {
                result = result.stream().filter(item -> queryDto.getLineWorkcellIdList().contains(item.getLineWorkcellId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(queryDto.getProcessIdList())) {
                result = result.stream().filter(item -> queryDto.getProcessIdList().contains(item.getProcessId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(queryDto.getUserIdList())) {
                result = result.stream().filter(item -> queryDto.getUserIdList().contains(item.getUserId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(queryDto.getMaterialIdList())) {
                result = result.stream().filter(item -> queryDto.getMaterialIdList().contains(item.getMaterialId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(queryDto.getMaterialVersionList())) {
                result = result.stream().filter(item -> queryDto.getMaterialVersionList().contains(item.getMaterialVersion())).collect(Collectors.toList());
            }
        }
        return result;
    }

    /**
     * ?????????????????????????????????
     *
     * @param tenantId  ??????id
     * @param processIdList  ??????ID??????
     * @author penglin.sui@hand-china.com 2021/7/28 17:00
     * @return java.util.Map<java.lang.String, java.util.List< com.ruike.hme.domain.vo.HmeOrganizationVO>>
     */
    private Map<String, List<HmeOrganizationVO>> queryOrganizationByProcessIds(Long tenantId, String siteId , List<String> processIdList){

        if(CollectionUtils.isEmpty(processIdList)){
            return new HashMap<>();
        }

        List<HmeOrganizationVO> allHmeOrganizationVOList = new ArrayList<>();

        List<List<String>> splitProcessIdList = CommonUtils.splitSqlList(processIdList, 1000);
        for (List<String> splitProcessIds : splitProcessIdList
        ) {
            List<HmeOrganizationVO> hmeOrganizationVOList = hmeSignInOutRecordMapper.queryOrganizationByProcessIds(tenantId , siteId , splitProcessIds);
            if(CollectionUtils.isNotEmpty(hmeOrganizationVOList)){
                allHmeOrganizationVOList.addAll(hmeOrganizationVOList);
            }
        }

        if(CollectionUtils.isEmpty(allHmeOrganizationVOList)){
            return new HashMap<>();
        }

        return allHmeOrganizationVOList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getProcessId));
    }

    /**
     * ??????????????????
     *
     * @param tenantId  ??????id
     * @param siteId    ??????id
     * @param startTime ????????????
     * @param endTime   ????????????
     * @param dtoList  ??????ID??????
     * @author penglin.sui@hand-china.com 2021/7/29 9:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     */
    private Map<String , List<HmeEmployeeOutputSummaryVO>> querySummarys(Long tenantId,
                                                                         String siteId,
                                                                         Date startTime,
                                                                         Date endTime,
                                                                         List<HmeEmployeeOutputSummary> dtoList){
        List<HmeEmployeeOutputSummaryVO> employeeOutputSummaryVOList =
                hmeSignInOutRecordMapper.querySummarys(tenantId , siteId , startTime , endTime , dtoList);
        if(CollectionUtils.isEmpty(employeeOutputSummaryVOList)){
            //????????????${1}!
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //???????????????
        List<HmeEmployeeOutputSummaryVO> ncQtyVOList =
                hmeSignInOutRecordMapper.queryNcQty(tenantId , startTime , endTime , employeeOutputSummaryVOList);
        Map<String , List<HmeEmployeeOutputSummaryVO>> ngQtyMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(ncQtyVOList)){
            ngQtyMap = ncQtyVOList.stream().collect(Collectors.groupingBy(item -> item.getSiteInBy() + "#" + item.getProcessId()
                    + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter)));
        }

        //???????????????
        for (HmeEmployeeOutputSummaryVO item : employeeOutputSummaryVOList
        ) {
            List<HmeEmployeeOutputSummaryVO> subNcQtyVOList = ngQtyMap.getOrDefault(item.getSiteInBy() + "#" + item.getProcessId()
                            + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter),
                    new ArrayList<>());

            if(CollectionUtils.isNotEmpty(subNcQtyVOList)){
                item.setNgQty(subNcQtyVOList.stream().map(e->e.getNgQty()).reduce(BigDecimal.ZERO, BigDecimal::add));
            }else{
                item.setNgQty(BigDecimal.ZERO);
            }
        }

        //????????????+??????+??????+????????????
        return employeeOutputSummaryVOList.stream().collect(Collectors.groupingBy(item -> item.getSiteInBy() + "#" + item.getProcessId()
                + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter)));
    }

    @Override
    public List<HmeEmployeeAttendanceExportVO5> sumExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto) {
        List<HmeEmployeeAttendanceExportVO5> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(dto.getProdLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getUserId()) && StringUtils.isEmpty(dto.getMaterialId())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateFrom())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateTo())){
            return resultList;
        }

        //V20210730 modify by penglin.sui for tianyang.xie ?????????????????? 00:00
        dto.setDateFrom(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateFrom() , "yyyy-MM-dd HH") + ":00:00","yyyy-MM-dd HH:mm:ss"));
        dto.setDateTo(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateTo() , "yyyy-MM-dd HH") + ":59:59","yyyy-MM-dd HH:mm:ss"));

        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProcess(tenantId, dto.getProcessId());
            dto.setProcessIdList(Arrays.asList(dto.getProcessId().split(",")));
        }
//        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isNotEmpty(dto.getLineWorkcellId())){
//            //?????????????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellId());
//        }
        if(StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            //?????????????????????????????????????????????????????????
            dto.setLineWorkcellIdList(Arrays.asList(dto.getLineWorkcellId().split(",")));
        }
//        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getLineWorkcellId()) && StringUtils.isNotEmpty(dto.getProdLineId())){
//            //?????????????????????????????????????????????????????????????????????
//            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProdLine(tenantId, dto.getProdLineId());
//        }
        if(StringUtils.isNotEmpty(dto.getProdLineId())){
            //?????????????????????????????????????????????????????????????????????
            dto.setProdLineIdList(Arrays.asList(dto.getProdLineId().split(",")));
        }
        resultList = hmeSignInOutRecordMapper.sumQuery2(tenantId , dto);
//        dto.setWorkcellIdList(workcellIdList);
//        resultList = hmeSignInOutRecordMapper.sumQuery(tenantId, dto);
        // ??????????????????
        List<Long> userIdList = resultList.stream().map(vo -> {return Long.parseLong(vo.getUserId());}).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(userIdList)) {
//            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
//        }
        //??????????????????????????????
//        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
//        Long userId = Objects.isNull(customUserDetails) ? -1L : DetailsHelper.getUserDetails().getUserId();
//        String siteId = siteMapper.selectSiteByUser(userId);
//        if(StringUtils.isEmpty(siteId)){
//            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
//        }
        // ?????????????????????????????????
        List<String> processIdList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
//        // ??????????????????
//        Map<String, List<MtExtendVO>> attrMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(processIdList)) {
//            List<HmeOrganizationVO> hmeOrganizationVOS = hmeSignInOutRecordMapper.queryOrganizationByProcessIds(tenantId, siteId, processIdList);
//            if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
//                hmeOrganizationMap = hmeOrganizationVOS.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getProcessId));
//            }
//            List<MtExtendVO> mtExtendVOList = mtExtendMapper.selectExtend(tenantId , processIdList , Collections.singletonList("OUTPUT_FLAG"));
//            if(CollectionUtils.isNotEmpty(mtExtendVOList)){
//                attrMap = mtExtendVOList.stream().collect(Collectors.groupingBy(MtExtendVO::getKeyId));
//            }
//        }
        // ??????
        List<String> materialIdList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialVersionList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getMaterialVersion).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        List<HmeEmployeeAttendanceExportVO6> countNumberList = hmeSignInOutRecordMapper.queryBatchSumCountNumber(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> countNumberMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(countNumberList)) {
//            countNumberMap = countNumberList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ?????????
        List<HmeEmployeeAttendanceExportVO6> makeList = hmeSignInOutRecordMapper.queryBatchSumInMakeNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> makeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(makeList)) {
            makeMap = makeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ?????????
//        List<HmeEmployeeAttendanceExportVO6> repairNumList = hmeSignInOutRecordMapper.queryBatchSumRepairNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> repairNumMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(repairNumList)) {
//            repairNumMap = repairNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ???????????????
        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNumList = hmeSignInOutRecordMapper.queryBatchSumEoWorkcellGroup(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoWorkcellNumList)) {
            eoWorkcellNumMap = eoWorkcellNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ??????
//        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNcList = hmeSignInOutRecordMapper.queryBatchEoWorkcellList(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNcMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(eoWorkcellNcList)) {
//            eoWorkcellNcMap = eoWorkcellNcList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }
        // ???????????????
//        List<HmeEmployeeAttendanceExportVO6> totalProductionTimeList = hmeSignInOutRecordMapper.queryBatchTotalProductionTime(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dto.getDateFrom(), dto.getDateTo());
//        Map<Object, List<HmeEmployeeAttendanceExportVO6>> totalProductionTimeMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(totalProductionTimeList)) {
//            totalProductionTimeMap = totalProductionTimeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
//        }

        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportNMap = new HashMap<>();
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportYMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)) {

            List<List<HmeEmployeeAttendanceExportVO5>> splitList = CommonUtils.splitSqlList(resultList, 3000);
            for (List<HmeEmployeeAttendanceExportVO5> results : splitList
                 ) {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportNVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagNGroupQuery(tenantId, dto,results);
                if(CollectionUtils.isNotEmpty(employeeAttendanceExportNVO10s)){
                    Map<String,List<HmeEmployeeAttendanceExportVO10>> subEmployeeAttendanceExportNMap = employeeAttendanceExportNVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
                    for (Map.Entry entry : subEmployeeAttendanceExportNMap.entrySet()) {
                        if(employeeAttendanceExportNMap.containsKey(entry.getKey())){
                            continue;
                        }
                        employeeAttendanceExportNMap.put(entry.getKey().toString() , subEmployeeAttendanceExportNMap.get(entry.getKey()));
                    }
                }
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportYVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagYGroupQuery(tenantId, dto,results);
                if(CollectionUtils.isNotEmpty(employeeAttendanceExportYVO10s)){
                    Map<String,List<HmeEmployeeAttendanceExportVO10>> subEmployeeAttendanceExportYMap = employeeAttendanceExportYVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
                    for (Map.Entry entry : subEmployeeAttendanceExportYMap.entrySet()) {
                        if(employeeAttendanceExportYMap.containsKey(entry.getKey())){
                            continue;
                        }
                        employeeAttendanceExportYMap.put(entry.getKey().toString() , subEmployeeAttendanceExportYMap.get(entry.getKey()));
                    }
                }
            }
        }

        for (HmeEmployeeAttendanceExportVO5 result:resultList) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
//            if (StringUtils.isNotBlank(result.getUserId())) {
//                MtUserInfo mtUserInfo = userInfoMap.get(Long.parseLong(result.getUserId()));
//                if (mtUserInfo != null) {
//                    // ????????????
//                    result.setUserName(mtUserInfo.getRealName());
//                    //??????
//                    result.setUserNum(mtUserInfo.getLoginName());
//                }
//            }
//            if (StringUtils.isNotBlank(result.getProcessId())) {
//                List<HmeOrganizationVO> hmeOrganizationVOS = hmeOrganizationMap.get(result.getProcessId());
//                if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
//                    // ??????
//                    result.setLineWorkcellId(hmeOrganizationVOS.get(0).getLineWorkcellId());
//                    result.setLineWorkcerllName(hmeOrganizationVOS.get(0).getLineWorkcellName());
//                    //??????
//                    result.setProdLineId(hmeOrganizationVOS.get(0).getProdLineId());
//                    result.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());
//                }
//            }

            //????????????
//            BigDecimal actualOutputNumber = BigDecimal.ZERO;
//            List<MtExtendVO> mtExtendAttrVOS = attrMap.get(result.getProcessId());
//            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
//                actualOutputNumber = hmeSignInOutRecordMapper.getSumActualOutputNumber(tenantId, result);
//            }
//            result.setActualOutputNumber(actualOutputNumber);
            //??????
//            List<HmeEmployeeAttendanceExportVO6>  exportVO6s = countNumberMap.get(spliceResultStr(result));
//            result.setCountNumber(CollectionUtils.isNotEmpty(exportVO6s) ? exportVO6s.get(0).getQty() : BigDecimal.ZERO);
            //?????????
            List<HmeEmployeeAttendanceExportVO6> marks = makeMap.get(spliceResultStr(result));
            result.setInMakeNum(CollectionUtils.isNotEmpty(marks) ? marks.get(0).getQty() : BigDecimal.ZERO);
            //?????????
//            List<HmeEmployeeAttendanceExportVO6> repairNums = repairNumMap.get(spliceResultStr(result));
//            result.setRepairNum(CollectionUtils.isNotEmpty(repairNums) ? repairNums.get(0).getQty() : BigDecimal.ZERO);
            //???????????????
            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNums = eoWorkcellNumMap.get(spliceResultStr(result));
            BigDecimal eoWorkcellNum = CollectionUtils.isNotEmpty(eoWorkcellNums) ? BigDecimal.valueOf(eoWorkcellNums.size()) : BigDecimal.ZERO;
            //?????????
//            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNcs = eoWorkcellNcMap.get(spliceResultStr(result));
//            if (CollectionUtils.isNotEmpty(eoWorkcellNcs)) {
//                List<String> eoIdList = eoWorkcellNcs.stream().map(HmeEmployeeAttendanceExportVO6::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//                List<String> workcellIds = eoWorkcellNcs.stream().map(HmeEmployeeAttendanceExportVO6::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//                BigDecimal defectsNumber = hmeSignInOutRecordMapper.getSumDefectsNumbTwo(tenantId, eoIdList, workcellIds, dto.getDateFrom(), dto.getDateTo());
//                result.setDefectsNumber(defectsNumber);
//            }
            //???????????????
            BigDecimal totalProductionTime = result.getTotalProductionTimeDecimal().divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP);
            result.setTotalProductionTime( totalProductionTime + "h");
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagNEoList = employeeAttendanceExportNMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagNEoList = employeeAttendanceExportReworkFlagNEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagYEoList = employeeAttendanceExportYMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagYEoList = employeeAttendanceExportReworkFlagYEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
                //???????????????
//                List<HmeEmployeeAttendanceExportVO6> totalProductionTimes = totalProductionTimeMap.get(spliceResultStr(result));
//                BigDecimal totalProductionTime = CollectionUtils.isNotEmpty(totalProductionTimes) ? totalProductionTimes.get(0).getTotalProductionTime() : BigDecimal.ZERO;
            }
        }
        return resultList;
    }

    @Override
    public List<HmeEmployeeAttendanceExportVO5> sumExportNew(Long tenantId, HmeEmployeeAttendanceDTO13 dto) {
        List<HmeEmployeeAttendanceExportVO5> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(dto.getProdLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getUserId()) && StringUtils.isEmpty(dto.getMaterialId())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateFrom())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateTo())){
            return resultList;
        }
        Date dateFrom = dto.getDateFrom();
        Date dateTo = dto.getDateTo();
        Date nowDate = new Date();

        //V20210730 modify by penglin.sui for tianyang.xie ?????????????????? 00:00
        dto.setDateFrom(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateFrom() , "yyyy-MM-dd HH") + ":00:00","yyyy-MM-dd HH:mm:ss"));
        dto.setDateTo(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getDateTo() , "yyyy-MM-dd HH") + ":59:59","yyyy-MM-dd HH:mm:ss"));

        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            dto.setProcessIdList(Arrays.asList(dto.getProcessId().split(",")));
        }
        if(StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            dto.setLineWorkcellIdList(Arrays.asList(dto.getLineWorkcellId().split(",")));
        }
        if(StringUtils.isNotEmpty(dto.getProdLineId())){
            dto.setProdLineIdList(Arrays.asList(dto.getProdLineId().split(",")));
        }
        //???????????????????????????????????????1??????
        Date preMaxJobTime = hmeSignInOutRecordMapper.selectMaxJobTime(tenantId);
        Date preMaxJobTimeAdd = CommonUtils.calculateDate(preMaxJobTime , 1 , Calendar.HOUR_OF_DAY , patter);

        resultList.addAll(hmeSignInOutRecordMapper.sumQuery2(tenantId , dto));

        if(dateTo.getTime() >= nowDate.getTime()){
            //????????????????????????????????????????????????,???????????????????????????????????????????????????????????????????????????
            if(dateFrom.getTime() > dto.getDateFrom().getTime()){
                if(CollectionUtils.isNotEmpty(resultList)){
                    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dto.getDateFrom(), dateFrom, dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:resultList) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
            }
            //????????????jobTime+1??????????????????????????????????????????????????????????????????????????????????????????
            if(dateTo.getTime() > preMaxJobTimeAdd.getTime()){
                List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, preMaxJobTimeAdd, dateTo, dto);
                if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                    //???????????????????????????????????????
                    for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:resultList) {
                        List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                            HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                            hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().add(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                            hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().add(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                            hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().add(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                            hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().add(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                            hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().add(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                        }
                    }
                    //???????????????????????????
                    for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:hmeEmployeeAttendanceExportVO5List) {
                        List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = resultList.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                        if(CollectionUtils.isEmpty(singleEmployeeAttendanceExportVO5List)){
                            resultList.add(hmeEmployeeAttendanceExportVO5);
                        }
                    }
                }
            }
        }else {
            //????????????????????????????????????????????????,????????????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(resultList)){
                if(dateFrom.getTime() > dto.getDateFrom().getTime()){
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dto.getDateFrom(), dateFrom, dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:resultList) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
                if(dto.getDateTo().getTime() > dateTo.getTime()){
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????
                    List<HmeEmployeeAttendanceExportVO5> hmeEmployeeAttendanceExportVO5List = employeeOutPutSummary(tenantId, dateTo, dto.getDateTo(), dto);
                    if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceExportVO5List)){
                        for (HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO5:resultList) {
                            List<HmeEmployeeAttendanceExportVO5> singleEmployeeAttendanceExportVO5List = hmeEmployeeAttendanceExportVO5List.stream().filter(item -> item.getUserId().equals(hmeEmployeeAttendanceExportVO5.getUserId())
                                    && item.getProcessId().equals(hmeEmployeeAttendanceExportVO5.getProcessId())
                                    && item.getMaterialId().equals(hmeEmployeeAttendanceExportVO5.getMaterialId())
                                    && item.getMaterialVersion().equals(hmeEmployeeAttendanceExportVO5.getMaterialVersion())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(singleEmployeeAttendanceExportVO5List)){
                                HmeEmployeeAttendanceExportVO5 hmeEmployeeAttendanceExportVO51 = singleEmployeeAttendanceExportVO5List.get(0);
                                hmeEmployeeAttendanceExportVO5.setActualOutputNumber(hmeEmployeeAttendanceExportVO5.getActualOutputNumber().subtract(hmeEmployeeAttendanceExportVO51.getActualOutputNumber()));
                                hmeEmployeeAttendanceExportVO5.setCountNumber(hmeEmployeeAttendanceExportVO5.getCountNumber().subtract(hmeEmployeeAttendanceExportVO51.getCountNumber()));
                                hmeEmployeeAttendanceExportVO5.setDefectsNumber(hmeEmployeeAttendanceExportVO5.getDefectsNumber().subtract(hmeEmployeeAttendanceExportVO51.getDefectsNumber()));
                                hmeEmployeeAttendanceExportVO5.setRepairNum(hmeEmployeeAttendanceExportVO5.getRepairNum().subtract(hmeEmployeeAttendanceExportVO51.getRepairNum()));
                                hmeEmployeeAttendanceExportVO5.setTotalProductionTimeDecimal(hmeEmployeeAttendanceExportVO5.getTotalProductionTimeDecimal().subtract(hmeEmployeeAttendanceExportVO51.getTotalProductionTimeDecimal()));
                            }
                        }
                    }
                }
            }
        }

        if(CollectionUtils.isEmpty(resultList)){
            return resultList;
        }

        // ??????????????????
        List<Long> userIdList = resultList.stream().map(vo -> {return Long.parseLong(vo.getUserId());}).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ?????????????????????????????????
        List<String> processIdList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ??????
        List<String> materialIdList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> materialVersionList = resultList.stream().map(HmeEmployeeAttendanceExportVO5::getMaterialVersion).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ?????????
        List<HmeEmployeeAttendanceExportVO6> makeList = hmeSignInOutRecordMapper.queryBatchSumInMakeNum(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dateFrom, dateTo);
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> makeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(makeList)) {
            makeMap = makeList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }
        // ???????????????
        List<HmeEmployeeAttendanceExportVO6> eoWorkcellNumList = hmeSignInOutRecordMapper.queryBatchSumEoWorkcellGroup(tenantId, userIdList, materialIdList, materialVersionList, processIdList, dateFrom, dateTo);
        Map<Object, List<HmeEmployeeAttendanceExportVO6>> eoWorkcellNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoWorkcellNumList)) {
            eoWorkcellNumMap = eoWorkcellNumList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        }

        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportNMap = new HashMap<>();
        Map<String,List<HmeEmployeeAttendanceExportVO10>> employeeAttendanceExportYMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)) {
            dto.setDateFrom(dateFrom);
            dto.setDateTo(dateTo);
            List<List<HmeEmployeeAttendanceExportVO5>> splitList = CommonUtils.splitSqlList(resultList, 3000);
            for (List<HmeEmployeeAttendanceExportVO5> results : splitList) {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportNVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagNGroupQuery(tenantId, dto,results);
                if(CollectionUtils.isNotEmpty(employeeAttendanceExportNVO10s)){
                    Map<String,List<HmeEmployeeAttendanceExportVO10>> subEmployeeAttendanceExportNMap = employeeAttendanceExportNVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
                    for (Map.Entry entry : subEmployeeAttendanceExportNMap.entrySet()) {
                        if(employeeAttendanceExportNMap.containsKey(entry.getKey())){
                            continue;
                        }
                        employeeAttendanceExportNMap.put(entry.getKey().toString() , subEmployeeAttendanceExportNMap.get(entry.getKey()));
                    }
                }
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportYVO10s = hmeSignInOutRecordMapper.batchSumEoWorkcellReworkFlagYGroupQuery(tenantId, dto,results);
                if(CollectionUtils.isNotEmpty(employeeAttendanceExportYVO10s)){
                    Map<String,List<HmeEmployeeAttendanceExportVO10>> subEmployeeAttendanceExportYMap = employeeAttendanceExportYVO10s.stream().collect(Collectors.groupingBy(e -> e.getSiteInBy() + "#" + e.getSnMaterialId() + "#" + e.getProductionVersion() + "#" + e.getParentOrganizationId()));
                    for (Map.Entry entry : subEmployeeAttendanceExportYMap.entrySet()) {
                        if(employeeAttendanceExportYMap.containsKey(entry.getKey())){
                            continue;
                        }
                        employeeAttendanceExportYMap.put(entry.getKey().toString() , subEmployeeAttendanceExportYMap.get(entry.getKey()));
                    }
                }
            }
        }

        for (HmeEmployeeAttendanceExportVO5 result:resultList) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
            //?????????
            List<HmeEmployeeAttendanceExportVO6> marks = makeMap.get(spliceResultStr(result));
            result.setInMakeNum(CollectionUtils.isNotEmpty(marks) ? marks.get(0).getQty() : BigDecimal.ZERO);
            //???????????????
            List<HmeEmployeeAttendanceExportVO6> eoWorkcellNums = eoWorkcellNumMap.get(spliceResultStr(result));
            BigDecimal eoWorkcellNum = CollectionUtils.isNotEmpty(eoWorkcellNums) ? BigDecimal.valueOf(eoWorkcellNums.size()) : BigDecimal.ZERO;
            //???????????????
            BigDecimal totalProductionTime = result.getTotalProductionTimeDecimal().divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP);
            result.setTotalProductionTime( totalProductionTime + "h");
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagNEoList = employeeAttendanceExportNMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagNEoList = employeeAttendanceExportReworkFlagNEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                List<HmeEmployeeAttendanceExportVO10> employeeAttendanceExportReworkFlagYEoList = employeeAttendanceExportYMap.getOrDefault(result.getUserId() + "#" + result.getMaterialId() + "#" + result.getMaterialVersion() + "#" + result.getProcessId() , new ArrayList<>());
                List<String> reworkFlagYEoList = employeeAttendanceExportReworkFlagYEoList.stream()
                        .map(HmeEmployeeAttendanceExportVO10::getEoId)
                        .distinct().collect(Collectors.toList());

                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
            }
        }
        return resultList;
    }

    @Override
    public Page<HmeEmployeeAttendanceDto> headDataQuery(Long tenantId, HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "??????"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "????????????"));
        }
        if (dto.getEndTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "????????????"));
        }
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
        List<String> workcellIds = new ArrayList();
        //??????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getLineWorkcellId())) {
            workcellIds .addAll(Arrays.asList(StringUtils.split(dto.getLineWorkcellId(), ",")));
        }else if(StringUtils.isNotEmpty(dto.getProductionLineId())){
            //?????????????????????????????????????????????????????????????????????
            workcellIds.addAll( hmeSignInOutRecordMapper.getLineWorkcellByProdLine(tenantId, dto.getProductionLineId(), dto.getSiteId()));
        }else if (StringUtils.isNotEmpty(dto.getWorkshopId())){
            //??????????????????????????????????????????????????????????????????
            workcellIds.addAll(hmeSignInOutRecordMapper.getLineWorkcellByWorkshop(tenantId, dto.getWorkshopId(), dto.getSiteId()));
        }
        //??????????????????????????????????????????+???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Page<HmeEmployeeAttendanceDto> result = PageHelper.doPageAndSort(pageRequest,
                () -> hmeSignInOutRecordMapper.headDataQuery2(tenantId, dto, workcellIds));
        if(CollectionUtils.isEmpty(result.getContent())){
            return result;
        }
        List<String> workIdList = result.stream().map(HmeEmployeeAttendanceDto::getWorkId).distinct().collect(Collectors.toList());
        //??????????????????????????????
        List<HmeEmployeeAttendanceExportVO9> prodLineList = hmeSignInOutRecordMapper.batchGetProdLineByLineWorkcell(tenantId, workIdList, dto.getSiteId());
        List<HmeEmployeeAttendanceExportVO9> employNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO9> groupLeaderList = new ArrayList<>();
        List<Long> unitIdList = result.stream().filter(item -> Objects.nonNull(item.getUnitId())).collect(Collectors.toList()).stream().map(HmeEmployeeAttendanceDto::getUnitId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(unitIdList)){
            //????????????????????????????????????
            employNumberList = hmeSignInOutRecordMapper.batchGetEmployNumberByUnit(tenantId, unitIdList);
            //??????????????????????????????
            groupLeaderList = hmeSignInOutRecordMapper.batchGetEmployeeNameByUnitId(tenantId, unitIdList);
        }
        //??????????????????????????????????????????????????????
        List<HmeEmployeeAttendanceDTO16> hmeEmployeeAttendanceDTO16List = new ArrayList<>();
        List<HmeEmployeeAttendanceDTO16> actualAttendanceList = new ArrayList<>();
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
            String shiftDateStr = DateUtil.format(hmeEmployeeAttendanceDto.getShiftDate(), DATE_FORMAT);
            hmeEmployeeAttendanceDto.setDate(shiftDateStr);
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                HmeEmployeeAttendanceDTO16 hmeEmployeeAttendanceDTO16 = new HmeEmployeeAttendanceDTO16();
                hmeEmployeeAttendanceDTO16.setShiftCode(hmeEmployeeAttendanceDto.getShiftCode());
                hmeEmployeeAttendanceDTO16.setShiftDate(shiftDateStr);
                hmeEmployeeAttendanceDTO16.setUnitId(hmeEmployeeAttendanceDto.getUnitId());
                hmeEmployeeAttendanceDTO16List.add(hmeEmployeeAttendanceDTO16);
            }
        }
        if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceDTO16List)){
            List<List<HmeEmployeeAttendanceDTO16>> splitList = CommonUtils.splitSqlList(hmeEmployeeAttendanceDTO16List, 500);
            for (List<HmeEmployeeAttendanceDTO16> split:splitList) {
                actualAttendanceList.addAll(hmeSignInOutRecordMapper.actualAttendanceBatchQuery(tenantId, split));
            }
        }
        List<List<HmeEmployeeAttendanceDto>> splitList = CommonUtils.splitSqlList(result, 300);
        List<HmeEmployeeAttendanceExportVO7> countNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO7> actualOutputNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO7> defectsNumberList = new ArrayList<>();
        for (List<HmeEmployeeAttendanceDto> split:splitList) {
            //??????wkcShiftId?????????ID????????????????????????????????????
            countNumberList.addAll(hmeSignInOutRecordMapper.batchGetCountNumber(tenantId, split, dto));
            //??????wkcShiftId?????????ID???????????????????????????????????????
            actualOutputNumberList.addAll(hmeSignInOutRecordMapper.batchGetActualOutputNumber(tenantId, split, dto));
            //??????wkcShiftId?????????ID????????????????????????????????????
            defectsNumberList.addAll(hmeSignInOutRecordMapper.batchGetDefectNumber(tenantId, split, dto));
        }
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
//            //??????????????????????????????
//            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
//                setTopSiteId(dto.getSiteId());
//                setParentOrganizationType("WORKCELL");
//                setParentOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
//                setOrganizationType("WORKCELL");
//                setQueryType("BOTTOM");
//            }});
//            List<String> workcellIdList2 = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
            //?????????
            List<HmeEmployeeAttendanceExportVO9> singleProdLine = prodLineList.stream().filter(item -> hmeEmployeeAttendanceDto.getWorkId().equals(item.getOrganizationId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleProdLine)) {
                hmeEmployeeAttendanceDto.setProdLineName(singleProdLine.get(0).getParentOrganizationName());
            }
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //????????????
                Integer employNumber = 0;
                List<HmeEmployeeAttendanceExportVO9> singleEmployNumber = employNumberList.stream().filter(item -> item.getOrganizationId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleEmployNumber)){
                    employNumber = singleEmployNumber.get(0).getQtyInt();
                }
                hmeEmployeeAttendanceDto.setEmployNumber(employNumber);
                //?????????
                Integer actualAttendance = 0;
                String shiftDateStr = hmeEmployeeAttendanceDto.getDate();
                List<HmeEmployeeAttendanceDTO16> singleActualAttendance = actualAttendanceList.stream().filter(item -> item.getShiftCode().equals(hmeEmployeeAttendanceDto.getShiftCode())
                        && item.getShiftDate().equals(shiftDateStr) && item.getUnitId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleActualAttendance)){
                    actualAttendance = singleActualAttendance.get(0).getActualAttendance();
                }
                hmeEmployeeAttendanceDto.setAttendanceNumber(actualAttendance);
                //?????????
                hmeEmployeeAttendanceDto.setNoWorkNumber(employNumber - actualAttendance.intValue());
                //???????????????
//                MtCalendarVO2 calendarVO = new MtCalendarVO2();
//                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
//                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
//                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
//                calendarVO.setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
//                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
//                if (StringUtils.isNotEmpty(calendarId)) {
//                    List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, hmeEmployeeAttendanceDto.getShiftCode());
//                    if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
//                        Date startTime = mtCalendarShiftList.get(0).getShiftStartTime();
//                        Date endTime = mtCalendarShiftList.get(0).getShiftEndTime();
//                        if (mtCalendarShiftList.get(0).getRestTime() != null) {
//                            Long restTime = mtCalendarShiftList.get(0).getRestTime().intValue() * HOUR;
//                            Long time = (endTime.getTime() - startTime.getTime() - restTime) * employNumber;
//                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
//                        } else {
//                            Long time = (endTime.getTime() - startTime.getTime()) * employNumber;
//                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
//                        }
//                    }
//                }
//                //???????????????
//                Long countWorkTime = Long.valueOf(0);
//                //????????????????????????????????????relId??????,?????????????????????????????????????????????
//                for (String worlcellId : workcellIdList2) {
//                    List<String> relIdList = hmeSignInOutRecordMapper.getRelId(tenantId, worlcellId, hmeEmployeeAttendanceDto.getUnitId(),
//                            hmeEmployeeAttendanceDto.getShiftCode(), hmeEmployeeAttendanceDto.getDate());
//                    if (CollectionUtils.isNotEmpty(relIdList)) {
//                        for (String relId : relIdList) {
//                            //??????relId????????????OPERATION = CLOSE?????????????????????????????????duration
//                            List<HmeSignInOutRecord> hmeSignInOutRecords = hmeSignInOutRecordRepository.select(new HmeSignInOutRecord() {{
//                                setTenantId(tenantId);
//                                setRelId(relId);
//                                setOperation("CLOSE");
//                            }});
//                            if (CollectionUtils.isNotEmpty(hmeSignInOutRecords)) {
//                                String duration = hmeSignInOutRecords.get(0).getDuration();
//                                String[] hours = duration.split(":");
//                                countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                            } else {
//                                //????????????????????????relId??????OPERATION_DATE?????????????????????
//                                HmeSignInOutRecord hmeSignInOutRecord = hmeSignInOutRecordMapper.maxOperationDateQuery(tenantId, relId);
//                                if ("OPEN".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = OPEN???????????????????????? - ????????????DATE??????
//                                    countWorkTime += (nowDate.getTime() - hmeSignInOutRecord.getDate().getTime());
//                                } else if ("OFF".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = OFF????????????duration
//                                    String duration = hmeSignInOutRecord.getDuration();
//                                    String[] hours = duration.split(":");
//                                    countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                                } else if ("ON".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = ON????????????duration + ???????????? - ????????????DATE??????
//                                    Long time1 = nowDate.getTime() - hmeSignInOutRecord.getDate().getTime();
//                                    String duration = hmeSignInOutRecord.getDuration();
//                                    String[] hours = duration.split(":");
//                                    Long time2 = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                                    countWorkTime += (time1 + time2);
//                                }
//                            }
//                        }
//                    }
//                }
//                hmeEmployeeAttendanceDto.setCountWorkTime(countWorkTime / HOUR);
                //??????
                if (hmeEmployeeAttendanceDto.getCountTime() != null && hmeEmployeeAttendanceDto.getCountWorkTime() != null) {
                    hmeEmployeeAttendanceDto.setNoWorkTime(hmeEmployeeAttendanceDto.getCountTime() - hmeEmployeeAttendanceDto.getCountWorkTime());
                }
            }
//            Date shiftStartTime = hmeEmployeeAttendanceDto.getShiftStartDate();
//            Date shiftEndTime = hmeEmployeeAttendanceDto.getShiftEndDate() == null ? nowDate : hmeEmployeeAttendanceDto.getShiftEndDate();
            //2020-09-22 edit by chaonan.hu for fang.pan ??????????????????????????????
            //2021-03-11 edit by chaonan.hu for tianyang.xie ??????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleCountNumber = countNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal countNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleCountNumber)){
                countNumber = singleCountNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setCountNumber(countNumber);
            //2021-03-11 add by chaonan.hu for tianyang.xie ?????????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleActualOutputNumber = actualOutputNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal actualOutputNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleActualOutputNumber)){
                actualOutputNumber = singleActualOutputNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setActualOutputNumber(actualOutputNumber);
            //2021-03-11 edit by chaonan.hu for tianyang.xie ??????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleDefectsNumber = defectsNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal defectsNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleDefectsNumber)){
                defectsNumber = singleDefectsNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setDefectsNumber(defectsNumber);
            //??????
            if(Objects.nonNull(hmeEmployeeAttendanceDto.getUnitId())){
                List<HmeEmployeeAttendanceExportVO9> singleGroupLeaderList = groupLeaderList.stream().filter(item -> item.getParentOrganizationId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleGroupLeaderList)){
                    List<String> employeeNameList = singleGroupLeaderList.stream().map(HmeEmployeeAttendanceExportVO9::getParentOrganizationName).collect(Collectors.toList());
                    hmeEmployeeAttendanceDto.setGroupLeaderList(employeeNameList);
                }
            }
        }
        return result;
    }

    @Override
    public Page<HmeEmployeeAttendanceRecordDto> lineDataQuery(Long tenantId, HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest) {
        Page<HmeEmployeeAttendanceRecordDto> result = new Page<>();
        List<HmeEmployeeAttendanceRecordDto> resultList = new ArrayList<>();
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String shiftDateStr = simpleDateFormat.format(dto.getShiftStartDate());
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
        //??????????????????????????????
        List<String> workcellIdList2 = hmeSignInOutRecordMapper.getWorkcellByLineWorkcellId(tenantId, dto.getWorkId());
        if(CollectionUtils.isEmpty(workcellIdList2)){
            return result;
        }
        List<String> finalWorkcellIdList = new ArrayList<>();
        //2020-11-18 add by chaonan.hu for can.wang ??????????????????????????????
        List<String> queryWorkcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getWorkcellName())){
            queryWorkcellIdList = hmeSignInOutRecordMapper.workcellNameLikeQuery(tenantId, dto.getWorkcellName());
            if(CollectionUtils.isNotEmpty(queryWorkcellIdList)){
                for (String workcellId:queryWorkcellIdList) {
                    if(workcellIdList2.contains(workcellId)){
                        finalWorkcellIdList.add(workcellId);
                    }
                }
                if(CollectionUtils.isEmpty(finalWorkcellIdList)){
                    throw new MtException("HME_EO_JOB_SN_124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_124", "HME"));
                }
            }else{
                throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_001", "HME"));
            }
        }else{
            finalWorkcellIdList = workcellIdList2;
        }
        if (CollectionUtils.isNotEmpty(finalWorkcellIdList)) {
            List<String> workcellList = new ArrayList<>();
            workcellList.addAll(finalWorkcellIdList);
//            //???????????????????????????????????????+???????????????????????????????????????????????????????????????????????????????????????????????????
//            resultList = hmeSignInOutRecordMapper.shiftDataQuery(tenantId, finalWorkcellIdList, dto.getWkcShiftId(), dto.getShiftStartDate(), shiftEndDate, dto.getSiteId(), dto.getWorkId());
            //2021-03-12 edit by chaonan.hu for tianyang.xie ????????????????????????????????????????????????+??????+??????
            result = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.shiftDataQueryNew(tenantId, workcellList, dto.getWkcShiftId(), dto));
            if (CollectionUtils.isEmpty(result)) {
                return result;
//                List<HmeEmployeeAttendanceRecordDto> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
//                result = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
            } else {
                //????????????????????????+????????????, ?????????????????????????????????
//                resultList = resultList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
//                        -> new TreeSet<>(Comparator.comparing(o -> o.getWorkcellId() + ";" + o.getEmployeeId()))), ArrayList::new));
//                resultList = resultList.stream().sorted(Comparator.comparing(HmeEmployeeAttendanceRecordDto::getOrderBy)).collect(Collectors.toList());
//                List<HmeEmployeeAttendanceRecordDto> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
//                result = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
                //??????????????????????????????
                List<String> workcellIdList = result.stream().map(HmeEmployeeAttendanceRecordDto::getWorkcellId).distinct().collect(Collectors.toList());
                List<HmeEmployeeAttendanceExportVO9> processList = hmeSignInOutRecordMapper.batchQueryProcessByWorkcell(tenantId, workcellIdList, dto.getSiteId());
                //????????????????????????????????????
                Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
                List<Long> userIdList = result.stream().map(vo -> {return Long.parseLong(vo.getEmployeeId());}).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
                }
                List<HmeEmployeeAttendanceExportVO8> makeNumList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> jobIdList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> inMakeNumList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> inMakeJobIdList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> defectsNumbList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> repairNumList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> repairNumjobIdList = new ArrayList<>();
                List<HmeEmployeeAttendanceExportVO8> eoWorkcellNumList = new ArrayList<>();
                List<List<HmeEmployeeAttendanceRecordDto>> splitList = CommonUtils.splitSqlList(result.getContent(), 300);
                for (List<HmeEmployeeAttendanceRecordDto> split:splitList) {
                    //??????????????????
                    makeNumList.addAll(hmeSignInOutRecordMapper.lineMakeNumBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //????????????????????????
                    jobIdList.addAll(hmeSignInOutRecordMapper.lineMakeNumDetailBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //??????????????????
                    inMakeNumList.addAll(hmeSignInOutRecordMapper.inMakeNumBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //????????????????????????
                    inMakeJobIdList.addAll(hmeSignInOutRecordMapper.inMakeNumDetailBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //?????????????????????
                    defectsNumbList.addAll(hmeSignInOutRecordMapper.defectsNumBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //??????????????????
                    repairNumList.addAll(hmeSignInOutRecordMapper.repairNumBtachQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //????????????????????????
                    repairNumjobIdList.addAll(hmeSignInOutRecordMapper.repairNumDetailBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                    //?????????????????????
                    eoWorkcellNumList.addAll(hmeSignInOutRecordMapper.eoWorkcellGroupBatchQuery(tenantId, dto.getWkcShiftId(), split, dto));
                }
                //???????????????????????????
                List<HmeEmployeeAttendanceRecordDto> mountGuardDateQueryList = result.getContent().stream().collect(Collectors
                        .collectingAndThen(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(o -> o.getEmployeeId() + "," + o.getWorkcellId()))), ArrayList::new));
                List<HmeEmployeeAttendanceExportVO8> mountGuardDateList = hmeSignInOutRecordMapper.mountLaidDateBatchQuery(tenantId, mountGuardDateQueryList, dto.getUnitId(),
                        dto.getShiftCode(), shiftDateStr, "OPEN");
                List<HmeEmployeeAttendanceExportVO8> laidOffDateList = hmeSignInOutRecordMapper.mountLaidDateBatchQuery(tenantId, mountGuardDateQueryList, dto.getUnitId(),
                        dto.getShiftCode(), shiftDateStr, "CLOSE");
                Date shiftStartTime = null;
                Date shiftEndTime = null;
//                MtCalendarVO2 calendarVO = new MtCalendarVO2();
//                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
//                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
//                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
//                calendarVO.setOrganizationId(dto.getWorkId());
//                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
//                if (StringUtils.isNotEmpty(calendarId)) {
//                    List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, dto.getShiftCode());
//                    if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
//                        shiftStartTime = mtCalendarShiftList.get(0).getShiftStartTime();
//                        shiftEndTime = mtCalendarShiftList.get(0).getShiftEndTime();
//                    }
//                }
                for (HmeEmployeeAttendanceRecordDto employeeAttendanceRecordDto : result) {
                    //????????????????????????
                    List<HmeEmployeeAttendanceExportVO9> process = processList.stream().filter(item -> item.getOrganizationId().equals(employeeAttendanceRecordDto.getWorkcellId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(process)){
                        employeeAttendanceRecordDto.setWorkName(process.get(0).getParentOrganizationName());
                    }
                    MtUserInfo mtUserInfo = userInfoMap.get(Long.parseLong(employeeAttendanceRecordDto.getEmployeeId()));
                    if(Objects.nonNull(mtUserInfo)){
                        //??????
                        employeeAttendanceRecordDto.setEmployName(mtUserInfo.getRealName());
                        //??????
                        employeeAttendanceRecordDto.setEmployeeNum(mtUserInfo.getLoginName());
                    }
                    //??????
                    List<HmeEmployeeAttendanceExportVO8> singleMakeNum = makeNumList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    BigDecimal makeNum = BigDecimal.ZERO;
                    if(CollectionUtils.isNotEmpty(singleMakeNum)){
                        makeNum = singleMakeNum.get(0).getQty();
                    }
                    employeeAttendanceRecordDto.setMakeNum(makeNum);
                    List<HmeEmployeeAttendanceExportVO8> singleJobId = jobIdList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    List<String> jobId = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(singleJobId)){
                        jobId = singleJobId.stream().map(HmeEmployeeAttendanceExportVO8::getJobId).collect(Collectors.toList());
                    }
                    employeeAttendanceRecordDto.setJobIdList(jobId);
                    //??????
                    List<HmeEmployeeAttendanceExportVO8> singleInMakeNum = inMakeNumList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    BigDecimal inMakeNum = BigDecimal.ZERO;
                    if(CollectionUtils.isNotEmpty(singleInMakeNum)){
                        inMakeNum = singleInMakeNum.get(0).getQty();
                    }
                    employeeAttendanceRecordDto.setInMakeNum(inMakeNum);
                    List<HmeEmployeeAttendanceExportVO8> singleInMakeJobId = inMakeJobIdList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    List<String> inMakeJobId = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(singleInMakeJobId)){
                        inMakeJobId = singleInMakeJobId.stream().map(HmeEmployeeAttendanceExportVO8::getJobId).collect(Collectors.toList());
                    }
                    employeeAttendanceRecordDto.setInMakeJobIdList(inMakeJobId);
                    //?????????
                    List<HmeEmployeeAttendanceExportVO8> singleDefectsNumb = defectsNumbList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    List<String> defectsNum = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(singleDefectsNumb)){
                        defectsNum = singleDefectsNumb.stream().map(HmeEmployeeAttendanceExportVO8::getNcRecordId).distinct().collect(Collectors.toList());
                    }
                    employeeAttendanceRecordDto.setDefectsNumb(new BigDecimal(defectsNum.size()));
                    employeeAttendanceRecordDto.setNcRecordIdList(defectsNum);
                    //??????
                    List<HmeEmployeeAttendanceExportVO8> singleRepairNum = repairNumList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    BigDecimal repairNum = BigDecimal.ZERO;
                    if(CollectionUtils.isNotEmpty(singleRepairNum)){
                        repairNum = singleRepairNum.get(0).getQty();
                    }
                    employeeAttendanceRecordDto.setRepairNum(repairNum);
                    List<HmeEmployeeAttendanceExportVO8> singleRepairNumjobId = repairNumjobIdList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    List<String> repairNumjobId = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(singleRepairNumjobId)){
                        repairNumjobId = singleRepairNumjobId.stream().map(HmeEmployeeAttendanceExportVO8::getJobId).distinct().collect(Collectors.toList());
                    }
                    employeeAttendanceRecordDto.setRepairJobIdList(repairNumjobId);
                    //?????????
                    List<HmeEmployeeAttendanceExportVO8> singleEoWorkcellNum = eoWorkcellNumList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                            && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())
                            && item.getMaterialId().equals(employeeAttendanceRecordDto.getMaterialId())).collect(Collectors.toList());
                    BigDecimal eoWorkcellNum = BigDecimal.ZERO;
                    List<String> eoIdList = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(singleEoWorkcellNum)){
                        eoIdList = singleEoWorkcellNum.stream().map(HmeEmployeeAttendanceExportVO8::getEoId).distinct().collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(eoIdList)){
                            eoWorkcellNum = BigDecimal.valueOf(eoIdList.size());
                        }
                    }
                    if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                        employeeAttendanceRecordDto.setFirstPassRate("--");
                    } else {
                        List<String> reworkFlagNEoList = new ArrayList<>();
                        List<HmeEmployeeAttendanceExportVO8> singleReworkFlagNEoList = singleEoWorkcellNum.stream().filter(item -> StringUtils.isBlank(item.getReworkFlag()) ||
                                "N".equals(item.getReworkFlag())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleReworkFlagNEoList)){
                            reworkFlagNEoList = singleReworkFlagNEoList.stream().map(HmeEmployeeAttendanceExportVO8::getEoId).distinct().collect(Collectors.toList());
                        }
                        List<String> reworkFlagYEoList = new ArrayList<>();
                        List<HmeEmployeeAttendanceExportVO8> singleReworkFlagYEoList = singleEoWorkcellNum.stream().filter(item ->
                                "Y".equals(item.getReworkFlag())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleReworkFlagYEoList)){
                            reworkFlagYEoList = singleReworkFlagYEoList.stream().map(HmeEmployeeAttendanceExportVO8::getEoId).distinct().collect(Collectors.toList());
                        }
                        BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                        if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                            eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                        }else{
                            reworkFlagNEoList = reworkFlagNEoList.stream().distinct().collect(Collectors.toList());
                        }
                        if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                            eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                        }else{
                            reworkFlagYEoList = reworkFlagYEoList.stream().distinct().collect(Collectors.toList());
                        }
                        if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                            reworkFlagNEoList.removeAll(reworkFlagYEoList);
                            eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                        }
                        BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                        employeeAttendanceRecordDto.setFirstPassRate(passPercentB.toString() + "%");
                    }
                    if (StringUtils.isNotEmpty(dto.getUnitId())) {
                        //?????????????????????
                        //????????????
                        Date mountGuardDate = null;
                        List<HmeEmployeeAttendanceExportVO8> singleMountGuardDate = mountGuardDateList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                                && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleMountGuardDate)){
                            mountGuardDate = singleMountGuardDate.get(0).getOperationDate();
                        }
                        employeeAttendanceRecordDto.setStartOperationDate(mountGuardDate);
                        //??????
                        if (mountGuardDate != null && shiftStartTime != null) {
                            employeeAttendanceRecordDto.setShiftStartTime(timeDifference(mountGuardDate, shiftStartTime));
                        }
                        //????????????
                        Date laidOffDate = null;
                        List<HmeEmployeeAttendanceExportVO8> singleLaidOffDate = laidOffDateList.stream().filter(item -> item.getEmployeeId().equals(employeeAttendanceRecordDto.getEmployeeId())
                                && item.getWorkcellId().equals(employeeAttendanceRecordDto.getWorkcellId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleLaidOffDate)){
                            laidOffDate = singleLaidOffDate.get(0).getOperationDate();
                        }
                        employeeAttendanceRecordDto.setEndOperationDate(laidOffDate);
                        //??????
                        if (laidOffDate != null && shiftEndTime != null) {
                            employeeAttendanceRecordDto.setShiftEndTime(timeDifference(laidOffDate, shiftEndTime));
                        }
                        //?????????
                        if (mountGuardDate != null && laidOffDate != null) {
                            employeeAttendanceRecordDto.setCountDate((laidOffDate.getTime() - mountGuardDate.getTime() / HOUR) + "h");
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<HmeEmployeeAttendanceDto> lineWorkcellProductExport(Long tenantId, HmeEmployeeAttendanceDto1 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "??????"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "????????????"));
        }
        if (dto.getEndTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "????????????"));
        }
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
        List<String> workcellIds = new ArrayList();
        //??????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getLineWorkcellId())) {
            workcellIds .addAll(Arrays.asList(StringUtils.split(dto.getLineWorkcellId(), ",")));
        }else if(StringUtils.isNotEmpty(dto.getProductionLineId())){
            //?????????????????????????????????????????????????????????????????????
            workcellIds.addAll( hmeSignInOutRecordMapper.getLineWorkcellByProdLine(tenantId, dto.getProductionLineId(), dto.getSiteId()));
        }else if (StringUtils.isNotEmpty(dto.getWorkshopId())){
            //??????????????????????????????????????????????????????????????????
            workcellIds.addAll(hmeSignInOutRecordMapper.getLineWorkcellByWorkshop(tenantId, dto.getWorkshopId(), dto.getSiteId()));
        }
        Date nowDate = new Date();
        //??????????????????????????????????????????+???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<HmeEmployeeAttendanceDto> result = hmeSignInOutRecordMapper.headDataQuery2(tenantId, dto, workcellIds);
        if(CollectionUtils.isEmpty(result)){
            return result;
        }
        List<String> workIdList = result.stream().map(HmeEmployeeAttendanceDto::getWorkId).distinct().collect(Collectors.toList());
        //??????????????????????????????
        List<HmeEmployeeAttendanceExportVO9> prodLineList = hmeSignInOutRecordMapper.batchGetProdLineByLineWorkcell(tenantId, workIdList, dto.getSiteId());
        List<HmeEmployeeAttendanceExportVO9> employNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO9> groupLeaderList = new ArrayList<>();
        List<Long> unitIdList = result.stream().filter(item -> Objects.nonNull(item.getUnitId())).collect(Collectors.toList()).stream().map(HmeEmployeeAttendanceDto::getUnitId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(unitIdList)){
            //????????????????????????????????????
            employNumberList = hmeSignInOutRecordMapper.batchGetEmployNumberByUnit(tenantId, unitIdList);
            //??????????????????????????????
            groupLeaderList = hmeSignInOutRecordMapper.batchGetEmployeeNameByUnitId(tenantId, unitIdList);
        }
        //??????????????????????????????????????????????????????
        List<HmeEmployeeAttendanceDTO16> hmeEmployeeAttendanceDTO16List = new ArrayList<>();
        List<HmeEmployeeAttendanceDTO16> actualAttendanceList = new ArrayList<>();
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
            String shiftDateStr = DateUtil.format(hmeEmployeeAttendanceDto.getShiftDate(), DATE_FORMAT);
            hmeEmployeeAttendanceDto.setDate(shiftDateStr);
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                HmeEmployeeAttendanceDTO16 hmeEmployeeAttendanceDTO16 = new HmeEmployeeAttendanceDTO16();
                hmeEmployeeAttendanceDTO16.setShiftCode(hmeEmployeeAttendanceDto.getShiftCode());
                hmeEmployeeAttendanceDTO16.setShiftDate(shiftDateStr);
                hmeEmployeeAttendanceDTO16.setUnitId(hmeEmployeeAttendanceDto.getUnitId());
                hmeEmployeeAttendanceDTO16List.add(hmeEmployeeAttendanceDTO16);
            }
        }
        if(CollectionUtils.isNotEmpty(hmeEmployeeAttendanceDTO16List)){
            List<List<HmeEmployeeAttendanceDTO16>> splitList = CommonUtils.splitSqlList(hmeEmployeeAttendanceDTO16List, 500);
            for (List<HmeEmployeeAttendanceDTO16> split:splitList) {
                actualAttendanceList.addAll(hmeSignInOutRecordMapper.actualAttendanceBatchQuery(tenantId, split));
            }
        }
        List<List<HmeEmployeeAttendanceDto>> splitList = CommonUtils.splitSqlList(result, 300);
        List<HmeEmployeeAttendanceExportVO7> countNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO7> actualOutputNumberList = new ArrayList<>();
        List<HmeEmployeeAttendanceExportVO7> defectsNumberList = new ArrayList<>();
        for (List<HmeEmployeeAttendanceDto> split:splitList) {
            //??????wkcShiftId?????????ID????????????????????????????????????
            countNumberList.addAll(hmeSignInOutRecordMapper.batchGetCountNumber(tenantId, split, dto));
            //??????wkcShiftId?????????ID???????????????????????????????????????
            actualOutputNumberList.addAll(hmeSignInOutRecordMapper.batchGetActualOutputNumber(tenantId, split, dto));
            //??????wkcShiftId?????????ID????????????????????????????????????
            defectsNumberList.addAll(hmeSignInOutRecordMapper.batchGetDefectNumber(tenantId, split, dto));
        }
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
//            //??????????????????????????????
//            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
//                setTopSiteId(dto.getSiteId());
//                setParentOrganizationType("WORKCELL");
//                setParentOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
//                setOrganizationType("WORKCELL");
//                setQueryType("BOTTOM");
//            }});
//            List<String> workcellIdList2 = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
            //?????????
            List<HmeEmployeeAttendanceExportVO9> singleProdLine = prodLineList.stream().filter(item -> hmeEmployeeAttendanceDto.getWorkId().equals(item.getOrganizationId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleProdLine)) {
                hmeEmployeeAttendanceDto.setProdLineName(singleProdLine.get(0).getParentOrganizationName());
            }
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //????????????
                Integer employNumber = 0;
                List<HmeEmployeeAttendanceExportVO9> singleEmployNumber = employNumberList.stream().filter(item -> item.getOrganizationId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleEmployNumber)){
                    employNumber = singleEmployNumber.get(0).getQtyInt();
                }
                hmeEmployeeAttendanceDto.setEmployNumber(employNumber);
                //?????????
                Integer actualAttendance = 0;
                String shiftDateStr = hmeEmployeeAttendanceDto.getDate();
                List<HmeEmployeeAttendanceDTO16> singleActualAttendance = actualAttendanceList.stream().filter(item -> item.getShiftCode().equals(hmeEmployeeAttendanceDto.getShiftCode())
                        && item.getShiftDate().equals(shiftDateStr) && item.getUnitId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleActualAttendance)){
                    actualAttendance = singleActualAttendance.get(0).getActualAttendance();
                }
                hmeEmployeeAttendanceDto.setAttendanceNumber(actualAttendance);
                //?????????
                hmeEmployeeAttendanceDto.setNoWorkNumber(employNumber - actualAttendance.intValue());
                //???????????????
//                MtCalendarVO2 calendarVO = new MtCalendarVO2();
//                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
//                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
//                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
//                calendarVO.setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
//                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
//                if (StringUtils.isNotEmpty(calendarId)) {
//                    List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, hmeEmployeeAttendanceDto.getShiftCode());
//                    if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
//                        Date startTime = mtCalendarShiftList.get(0).getShiftStartTime();
//                        Date endTime = mtCalendarShiftList.get(0).getShiftEndTime();
//                        if (mtCalendarShiftList.get(0).getRestTime() != null) {
//                            Long restTime = mtCalendarShiftList.get(0).getRestTime().intValue() * HOUR;
//                            Long time = (endTime.getTime() - startTime.getTime() - restTime) * employNumber;
//                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
//                        } else {
//                            Long time = (endTime.getTime() - startTime.getTime()) * employNumber;
//                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
//                        }
//                    }
//                }
//                //???????????????
//                Long countWorkTime = Long.valueOf(0);
//                //????????????????????????????????????relId??????,?????????????????????????????????????????????
//                for (String worlcellId : workcellIdList2) {
//                    List<String> relIdList = hmeSignInOutRecordMapper.getRelId(tenantId, worlcellId, hmeEmployeeAttendanceDto.getUnitId(),
//                            hmeEmployeeAttendanceDto.getShiftCode(), hmeEmployeeAttendanceDto.getDate());
//                    if (CollectionUtils.isNotEmpty(relIdList)) {
//                        for (String relId : relIdList) {
//                            //??????relId????????????OPERATION = CLOSE?????????????????????????????????duration
//                            List<HmeSignInOutRecord> hmeSignInOutRecords = hmeSignInOutRecordRepository.select(new HmeSignInOutRecord() {{
//                                setTenantId(tenantId);
//                                setRelId(relId);
//                                setOperation("CLOSE");
//                            }});
//                            if (CollectionUtils.isNotEmpty(hmeSignInOutRecords)) {
//                                String duration = hmeSignInOutRecords.get(0).getDuration();
//                                String[] hours = duration.split(":");
//                                countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                            } else {
//                                //????????????????????????relId??????OPERATION_DATE?????????????????????
//                                HmeSignInOutRecord hmeSignInOutRecord = hmeSignInOutRecordMapper.maxOperationDateQuery(tenantId, relId);
//                                if ("OPEN".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = OPEN???????????????????????? - ????????????DATE??????
//                                    countWorkTime += (nowDate.getTime() - hmeSignInOutRecord.getDate().getTime());
//                                } else if ("OFF".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = OFF????????????duration
//                                    String duration = hmeSignInOutRecord.getDuration();
//                                    String[] hours = duration.split(":");
//                                    countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                                } else if ("ON".equals(hmeSignInOutRecord.getOperation())) {
//                                    //??????OPERATION = ON????????????duration + ???????????? - ????????????DATE??????
//                                    Long time1 = nowDate.getTime() - hmeSignInOutRecord.getDate().getTime();
//                                    String duration = hmeSignInOutRecord.getDuration();
//                                    String[] hours = duration.split(":");
//                                    Long time2 = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
//                                    countWorkTime += (time1 + time2);
//                                }
//                            }
//                        }
//                    }
//                }
//                hmeEmployeeAttendanceDto.setCountWorkTime(countWorkTime / HOUR);
                //??????
                if (hmeEmployeeAttendanceDto.getCountTime() != null && hmeEmployeeAttendanceDto.getCountWorkTime() != null) {
                    hmeEmployeeAttendanceDto.setNoWorkTime(hmeEmployeeAttendanceDto.getCountTime() - hmeEmployeeAttendanceDto.getCountWorkTime());
                }
            }
//            Date shiftStartTime = hmeEmployeeAttendanceDto.getShiftStartDate();
//            Date shiftEndTime = hmeEmployeeAttendanceDto.getShiftEndDate() == null ? nowDate : hmeEmployeeAttendanceDto.getShiftEndDate();
            //2020-09-22 edit by chaonan.hu for fang.pan ??????????????????????????????
            //2021-03-11 edit by chaonan.hu for tianyang.xie ??????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleCountNumber = countNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal countNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleCountNumber)){
                countNumber = singleCountNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setCountNumber(countNumber);
            //2021-03-11 add by chaonan.hu for tianyang.xie ?????????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleActualOutputNumber = actualOutputNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal actualOutputNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleActualOutputNumber)){
                actualOutputNumber = singleActualOutputNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setActualOutputNumber(actualOutputNumber);
            //2021-03-11 edit by chaonan.hu for tianyang.xie ??????????????????????????????
            List<HmeEmployeeAttendanceExportVO7> singleDefectsNumber = defectsNumberList.stream().filter(item -> item.getWorkId().equals(hmeEmployeeAttendanceDto.getWorkId())
                    && item.getWkcShiftId().equals(hmeEmployeeAttendanceDto.getWkcShiftId())).collect(Collectors.toList());
            BigDecimal defectsNumber = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(singleDefectsNumber)){
                defectsNumber = singleDefectsNumber.get(0).getQty();
            }
            hmeEmployeeAttendanceDto.setDefectsNumber(defectsNumber);
            //??????
            if(Objects.nonNull(hmeEmployeeAttendanceDto.getUnitId())){
                List<HmeEmployeeAttendanceExportVO9> singleGroupLeaderList = groupLeaderList.stream().filter(item -> item.getParentOrganizationId().equals(hmeEmployeeAttendanceDto.getUnitId().toString())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleGroupLeaderList)){
                    List<String> employeeNameList = singleGroupLeaderList.stream().map(HmeEmployeeAttendanceExportVO9::getParentOrganizationName).collect(Collectors.toList());
                    hmeEmployeeAttendanceDto.setGroupLeaderList(employeeNameList);
                }
            }
        }
        return result;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO12> querySummarysDetail(Long tenantId, HmeEmployeeAttendanceDTO17 dto, PageRequest pageRequest) {
        List<HmeEmployeeAttendanceExportVO12> list = new ArrayList<>();
        if("ACTUALOUTPUT".equals(dto.getType())
                || "COUNTNUMBER".equals(dto.getType())
                || "REPAIRNUM".equals(dto.getType())) {
            list = hmeSignInOutRecordMapper.querySummarysDetail(tenantId, dto);
        }else if("NCNUM".equals(dto.getType())){
            list = hmeSignInOutRecordMapper.queryNcQtys(tenantId, dto);
        }else if("INMAKENUM".equals(dto.getType())){
            list = hmeSignInOutRecordMapper.queryInMake(tenantId, dto);
        }

        Page<HmeEmployeeAttendanceExportVO12> result = new Page<>();

        if(CollectionUtils.isEmpty(list)){
            return result;
        }
        boolean isProcessTimeFlag = true;
        Map<String , List<HmeEmployeeAttendanceExportVO13>> ncCodeMap = new HashMap<>();
        switch (dto.getType()){
            case "ACTUALOUTPUT":
                //????????????
                List<HmeEmployeeAttendanceExportVO12> actualOutPutList = list.stream()
                        .filter(item -> "N".equals(item.getReworkFlag()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(actualOutPutList)){
                    result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), actualOutPutList);
                }
                break;
            case "COUNTNUMBER":
                //??????
                if(CollectionUtils.isNotEmpty(list)){
                    result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
                }
                break;
            case "INMAKENUM":
                //?????????
                isProcessTimeFlag = false;
                if(CollectionUtils.isNotEmpty(list)){
                    result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
                }
                break;
            case "REPAIRNUM":
                //?????????
                List<HmeEmployeeAttendanceExportVO12> repairNumList = list.stream()
                        .filter(item -> "Y".equals(item.getReworkFlag()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(repairNumList)){
                    result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), repairNumList);
                }
                break;
            case "NCNUM":
                //?????????
                isProcessTimeFlag = false;

                if(CollectionUtils.isNotEmpty(list)){
                    result = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);

                    //??????????????????
                    List<String> ncRecordList = result.stream().map(HmeEmployeeAttendanceExportVO12::getNcRecordId)
                            .distinct().collect(Collectors.toList());
                    List<HmeEmployeeAttendanceExportVO13> ncCodeList = hmeSignInOutRecordMapper.queryNcCode(tenantId,ncRecordList);
                    if(CollectionUtils.isNotEmpty(ncCodeList)){
                        ncCodeMap = ncCodeList.stream().collect(Collectors.groupingBy(e -> e.getParentNcRecordId()));
                    }
                }

                break;
            default:
                break;
        }

        for (HmeEmployeeAttendanceExportVO12 vo : result) {
            if(isProcessTimeFlag) {
                //????????????
                if (vo.getSiteInDate() != null && vo.getSiteOutDate() != null) {
                    long time = vo.getSiteOutDate().getTime() - vo.getSiteInDate().getTime();
                    long min = 1000 * 60 * 60;
                    BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                    vo.setProcessTime(processTime);
                }
            }

            List<HmeEmployeeAttendanceExportVO13> ncCodeList = ncCodeMap.getOrDefault(vo.getNcRecordId() , new ArrayList<>());
            if(CollectionUtils.isNotEmpty(ncCodeList)) {
                List<String> ncCodeIdList = new ArrayList<>();
                List<String> ncCodeDescriptionList = new ArrayList<>();
                for (HmeEmployeeAttendanceExportVO13 item : ncCodeList
                ) {
                    ncCodeIdList.add(item.getNcCodeId());
                    ncCodeDescriptionList.add(item.getNcCodeDescription());
                }

                vo.setNcCodeIdList(ncCodeIdList);
                vo.setNcCodeDescriptionList(ncCodeDescriptionList);
            }
        }

        return result;
    }

    @Override
    @Async
    public void asyncExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<HmeEmployeeAttendanceExportVO5> resultList = this.sumExportNew(tenantId, dto);

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
                for (HmeEmployeeAttendanceExportVO5 rec : resultList) {
                    XSSFRow row = sheet.createRow(rowIndex);
                    List<String> fields = new ArrayList<>();
                    fields.add(Optional.ofNullable(rec.getUserName().toString()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getUserNum()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getLineWorkcerllName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getProcessName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialCode()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialName()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getMaterialVersion()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getActualOutputNumber() != null ? rec.getActualOutputNumber().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getCountNumber() != null ? rec.getCountNumber().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getInMakeNum() != null ? rec.getInMakeNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getDefectsNumber() != null ? rec.getDefectsNumber().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getRepairNum() != null ? rec.getRepairNum().toPlainString() : "").orElse(""));
                    fields.add(Optional.ofNullable(rec.getFirstPassRate()).orElse(""));
                    fields.add(Optional.ofNullable(rec.getTotalProductionTime()).orElse(""));
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

    public String timeDifference(Date date1, Date date2) {
        String hourStr = null;
        String minuteStr = null;
        String sStr = null;

        long diff = date1.getTime() - date2.getTime();
        //???
        long hour = diff / (1000 * 60 * 60);
        //???
        long minute = (diff - hour * (1000 * 60 * 60)) / (1000 * 60);
        //???
        long s = (diff / 1000 - hour * (60 * 60) - minute * 60);
        //???0
        if (String.valueOf(hour).length() == 1) {
            hourStr = "0" + hour;
        } else {
            hourStr = String.valueOf(hour);
        }
        if (String.valueOf(minute).length() == 1) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = String.valueOf(minute);
        }
        if (String.valueOf(s).length() == 1) {
            sStr = "0" + s;
        } else {
            sStr = String.valueOf(s);
        }
        return hourStr + minuteStr + sStr;
    }
}
