package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.domain.repository.HmeExceptionReportRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeExceptionReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtUserInfo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 10:06
 */
@Component
public class HmeExceptionReportRepositoryImpl implements HmeExceptionReportRepository {

    @Autowired
    private HmeExceptionReportMapper hmeExceptionReportMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    private static final String WORKSHOP = "WORKSHOP"; //车间
    private static final String AREA = "AREA";  // 制造部
    private static final String PROD_LINE = "PROD_LINE"; // 产线

    @Override
    public Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest) {
        Page<HmeExceptionReportVO2> reportList = PageHelper.doPage(pageRequest, () -> hmeExceptionReportMapper.queryExceptionReportList(tenantId, reportVO));
        // 批量查询发起人和关闭人信息
        List<Long> userIdList = new ArrayList<>();
        List<Long> createByIdList = reportList.stream().map(createBy -> StringUtils.isNotBlank(createBy.getCreatedBy()) ? Long.valueOf(createBy.getCreatedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(createByIdList)) {
            userIdList.addAll(createByIdList);
        }
        List<Long> closeByIdList = reportList.stream().map(closeBy -> StringUtils.isNotBlank(closeBy.getClosedBy()) ? Long.valueOf(closeBy.getClosedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(closeByIdList)) {
            userIdList.addAll(closeByIdList);
        }
        // 用户信息
        List<MtUserInfo> userInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userInfoList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, userIdList);
        }
        // 查询未绑定工位的异常信息 根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线及工位
        List<HmeExceptionReportVO2> nonBindWorkCellList = reportList.getContent().stream().filter(report -> StringUtils.isBlank(report.getWorkcellId())).collect(Collectors.toList());
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(nonBindWorkCellList)) {
            nonBindWorkCellMap = nonBindWorkCellList.stream().collect(Collectors.groupingBy(dto -> dto.getOrganizationType() + "_" + dto.getOrganizationId()));
        }
        for (Map.Entry<String, List<HmeExceptionReportVO2>> nonBindWorkCellEntry : nonBindWorkCellMap.entrySet()) {
            // 根据类型区分制造部、车间及产线
            List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellEntry.getValue();
            HmeExceptionReportVO2 hmeExceptionReportVO2 = reportVO2List.get(0);
            if (AREA.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 制造部的话  只显示制造部
                HmeModAreaVO mtModArea = hmeExceptionReportMapper.queryAreaByAreaId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                reportVO2List.forEach(vo -> {
                    vo.setAreaId(mtModArea.getAreaId());
                    vo.setAreaName(mtModArea.getAreaName());
                });
            } else if (WORKSHOP.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 车间 根据组织关系表 找到制造部
                HmeModAreaVO workshop = hmeExceptionReportMapper.queryAreaByAreaId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 查询制造部
                HmeModAreaVO mtModArea = hmeExceptionReportMapper.queryAreaByWorkShopId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                    exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                }
            } else if (PROD_LINE.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 产线 根据组织关系表 找车间、制造部
                HmeModProductionLineVO productionLine = hmeExceptionReportMapper.queryProdLineById(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 车间
                HmeModAreaVO workshop = hmeExceptionReportMapper.queryWorkShopByProdLineId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 制造部
                HmeModAreaVO mtModArea = null;
                if (workshop != null) {
                    mtModArea = hmeExceptionReportMapper.queryAreaByWorkShopId(tenantId, workshop.getAreaId());
                }
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    if (workshop != null) {
                        exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                        exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                    }
                    exceptionReportVO2.setProdLineId(productionLine.getProdLineId());
                    exceptionReportVO2.setProdLineName(productionLine.getProdLineName());
                }
            }

        }
        // 批量查询制造部
        List<String> areaAllIdList = new ArrayList<>();
        List<String> areaIdList = reportList.getContent().stream().map(HmeExceptionReportVO2::getAreaId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(areaIdList)) {
            areaAllIdList.addAll(areaIdList);
        }
        // 批量查询车间
        List<String> workShopList = reportList.getContent().stream().map(HmeExceptionReportVO2::getWorkshopId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workShopList)) {
            areaAllIdList.addAll(workShopList);
        }
        Map<String, List<HmeModAreaVO>> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaAllIdList)) {
            List<HmeModAreaVO> areaList = hmeExceptionReportMapper.batchQueryAreaByIds(tenantId, areaAllIdList);
            if (CollectionUtils.isNotEmpty(areaList)) {
                areaMap = areaList.stream().collect(Collectors.groupingBy(HmeModAreaVO::getAreaId));
            }
        }
        // 批量查询产线
        List<String> proLineIdList = reportList.getContent().stream().map(HmeExceptionReportVO2::getProdLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeModProductionLineVO>> proLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(proLineIdList)) {
            List<HmeModProductionLineVO> proLineList = hmeExceptionReportMapper.prodLineBasicPropertyBatchGet(tenantId, proLineIdList);
            if (CollectionUtils.isNotEmpty(proLineList)) {
                proLineMap = proLineList.stream().collect(Collectors.groupingBy(HmeModProductionLineVO::getProdLineId));
            }
        }

        Map<String, List<HmeModAreaVO>> areaFinalMap = areaMap;
        Map<String, List<HmeModProductionLineVO>> proLineFinalMap = proLineMap;
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellFinalMap = nonBindWorkCellMap;

        // 异常状态值集
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.EXCEPTION_STATUS", tenantId);
        // 异常类型
        List<HmeGenTypeVO> exceptionTypeList = hmeExceptionReportMapper.queryGenTypeByModuleAndTypeGroup(tenantId, "HME", "EXCEPTION_TYPE");

        List<MtUserInfo> userInfoFinalList = userInfoList;
        reportList.forEach(e -> {
            if (StringUtils.isNotBlank(e.getWorkcellId())) {
                // 绑定了工位 直接查询制造部、车间及产线
                // 制造部
                if (StringUtils.isNotBlank(e.getAreaId())) {
                    List<HmeModAreaVO> mtModAreas = areaFinalMap.get(e.getAreaId());
                    if (CollectionUtils.isNotEmpty(mtModAreas)) {
                        e.setAreaName(mtModAreas.get(0).getAreaName());
                    }
                }

                //车间
                if (StringUtils.isNotBlank(e.getWorkshopId())) {
                    List<HmeModAreaVO> workshopList = areaFinalMap.get(e.getWorkshopId());
                    if (CollectionUtils.isNotEmpty(workshopList)) {
                        e.setWorkshopName(workshopList.get(0).getAreaName());
                    }
                }
                //产线
                if (StringUtils.isNotBlank(e.getProdLineId())) {
                    List<HmeModProductionLineVO> proLines = proLineFinalMap.get(e.getProdLineId());
                    if (CollectionUtils.isNotEmpty(proLines)) {
                        e.setProdLineName(proLines.get(0).getProdLineName());
                    }
                }
            } else {
                // 未绑定工位  则根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线
                String mapKey = e.getOrganizationType() + "_" + e.getOrganizationId();
                List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellFinalMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(reportVO2List)) {
                    e.setAreaName(reportVO2List.get(0).getAreaName());
                    e.setWorkshopName(reportVO2List.get(0).getWorkshopName());
                    e.setProdLineName(reportVO2List.get(0).getProdLineName());
                }
            }

            //班组
            if (StringUtils.isNotBlank(e.getShiftId())) {
                ResponseEntity<HmeHzeroPlatformUnitDTO> unitsInfo = hmeHzeroPlatformFeignClient.getUnitsInfo(tenantId, e.getShiftId());
                if (unitsInfo.getBody() != null) {
                    e.setShiftName(unitsInfo.getBody().getUnitName());
                }
            }

            //附件名称
            if (StringUtils.isNotBlank(e.getAttachmentUuid())) {
                ResponseEntity<List<HmeHzeroFileDTO>> unitsInfo = hmeHzeroFileFeignClient.getUnitsInfo(tenantId, e.getAttachmentUuid());
                if (CollectionUtils.isNotEmpty(unitsInfo.getBody())) {
                    String attachmentName = "";
                    for (HmeHzeroFileDTO hmeHzeroFileDTO : unitsInfo.getBody()) {
                        attachmentName += StringUtils.isNotEmpty(attachmentName) ? "_" : "" + hmeHzeroFileDTO.getFileName();
                    }
                    e.setAttachmentName(attachmentName);
                    e.setFileList(unitsInfo.getBody());
                }
            }

            //异常状态描述
            if (StringUtils.isNotBlank(e.getExceptionStatus())) {
                List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getExceptionStatus())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    e.setExceptionStatusName(collect.get(0).getMeaning());
                }
            }

            //异常类型描述
            if (StringUtils.isNotBlank(e.getExceptionType())) {
                Optional<HmeGenTypeVO> exceptionTypeOpt = exceptionTypeList.stream().filter(et -> StringUtils.equals(et.getTypeCode(), e.getExceptionType())).findFirst();
                if (exceptionTypeOpt.isPresent()) {
                    e.setExceptionTypeName(exceptionTypeOpt.get().getDescription());
                }
            }

            //发起人
            if (StringUtils.isNotBlank(e.getCreatedBy())) {
                Optional<MtUserInfo> userOpt = userInfoFinalList.stream().filter(mtUserInfo -> Long.valueOf(e.getCreatedBy()).compareTo(mtUserInfo.getId()) == 0).findFirst();
                e.setCreatedByName(userOpt.isPresent() ? userOpt.get().getRealName() : "");
            }
            //响应人
            if (Strings.isNotEmpty(e.getRespondedBy())) {
                List<HmeHzeroIamUserDTO> userDTOList = hmeExceptionReportMapper.queryUserInfo(tenantId, e.getRespondedBy());
                if (CollectionUtils.isEmpty(userDTOList)) {
                    e.setRespondedByName("工号：" + e.getRespondedBy() + "，根据工号找不到用户，请与OA系统核对");
                } else {
                    List<MtUserInfo> respondedList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, Collections.singletonList(Long.valueOf(userDTOList.get(0).getId())));
                    if (CollectionUtils.isNotEmpty(respondedList)) {
                        e.setRespondedByName(respondedList.get(0).getRealName());
                    }
                }
            } else {
                e.setRespondedByName("");
            }

            //关闭人
            if (StringUtils.isNotBlank(e.getClosedBy())) {
                Optional<MtUserInfo> userOpt = userInfoFinalList.stream().filter(mtUserInfo -> Long.valueOf(e.getClosedBy()).compareTo(mtUserInfo.getId()) == 0).findFirst();
                e.setClosedByName(userOpt.isPresent() ? userOpt.get().getRealName() : "");
            }
        });
        return reportList;
    }

    @Override
    public List<HmeExceptionReportVO2> queryExceptionReportExport(Long tenantId, HmeExceptionReportVO reportVO) {
        List<HmeExceptionReportVO2> reportList = hmeExceptionReportMapper.queryExceptionReportList(tenantId, reportVO);
        // 批量查询发起人和关闭人信息
        List<Long> userIdList = new ArrayList<>();
        List<Long> createByIdList = reportList.stream().map(createBy -> StringUtils.isNotBlank(createBy.getCreatedBy()) ? Long.valueOf(createBy.getCreatedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(createByIdList)) {
            userIdList.addAll(createByIdList);
        }
        List<Long> closeByIdList = reportList.stream().map(closeBy -> StringUtils.isNotBlank(closeBy.getClosedBy()) ? Long.valueOf(closeBy.getClosedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(closeByIdList)) {
            userIdList.addAll(closeByIdList);
        }
        // 用户信息
        List<MtUserInfo> userInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userInfoList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, userIdList);
        }
        // 查询未绑定工位的异常信息 根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线及工位
        List<HmeExceptionReportVO2> nonBindWorkCellList = reportList.stream().filter(report -> StringUtils.isBlank(report.getWorkcellId())).collect(Collectors.toList());
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(nonBindWorkCellList)) {
            nonBindWorkCellMap = nonBindWorkCellList.stream().collect(Collectors.groupingBy(dto -> dto.getOrganizationType() + "_" + dto.getOrganizationId()));
        }
        for (Map.Entry<String, List<HmeExceptionReportVO2>> nonBindWorkCellEntry : nonBindWorkCellMap.entrySet()) {
            // 根据类型区分制造部、车间及产线
            List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellEntry.getValue();
            HmeExceptionReportVO2 hmeExceptionReportVO2 = reportVO2List.get(0);
            if (AREA.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 制造部的话  只显示制造部
                HmeModAreaVO mtModArea = hmeExceptionReportMapper.queryAreaByAreaId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                reportVO2List.forEach(vo -> {
                    vo.setAreaId(mtModArea.getAreaId());
                    vo.setAreaName(mtModArea.getAreaName());
                });
            } else if (WORKSHOP.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 车间 根据组织关系表 找到制造部
                HmeModAreaVO workshop = hmeExceptionReportMapper.queryAreaByAreaId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 查询制造部
                HmeModAreaVO mtModArea = hmeExceptionReportMapper.queryAreaByWorkShopId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                    exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                }
            } else if (PROD_LINE.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 产线 根据组织关系表 找车间、制造部
                HmeModProductionLineVO productionLine = hmeExceptionReportMapper.queryProdLineById(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 车间
                HmeModAreaVO workshop = hmeExceptionReportMapper.queryWorkShopByProdLineId(tenantId, hmeExceptionReportVO2.getOrganizationId());
                // 制造部
                HmeModAreaVO mtModArea = null;
                if (workshop != null) {
                    mtModArea = hmeExceptionReportMapper.queryAreaByWorkShopId(tenantId, workshop.getAreaId());
                }
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    if (workshop != null) {
                        exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                        exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                    }
                    exceptionReportVO2.setProdLineId(productionLine.getProdLineId());
                    exceptionReportVO2.setProdLineName(productionLine.getProdLineName());
                }
            }

        }
        // 批量查询制造部
        List<String> areaAllIdList = new ArrayList<>();
        List<String> areaIdList = reportList.stream().map(HmeExceptionReportVO2::getAreaId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(areaIdList)) {
            areaAllIdList.addAll(areaIdList);
        }
        // 批量查询车间
        List<String> workShopList = reportList.stream().map(HmeExceptionReportVO2::getWorkshopId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workShopList)) {
            areaAllIdList.addAll(workShopList);
        }
        Map<String, List<HmeModAreaVO>> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaAllIdList)) {
            List<HmeModAreaVO> areaList = hmeExceptionReportMapper.batchQueryAreaByIds(tenantId, areaAllIdList);
            if (CollectionUtils.isNotEmpty(areaList)) {
                areaMap = areaList.stream().collect(Collectors.groupingBy(HmeModAreaVO::getAreaId));
            }
        }
        // 批量查询产线
        List<String> proLineIdList = reportList.stream().map(HmeExceptionReportVO2::getProdLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeModProductionLineVO>> proLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(proLineIdList)) {
            List<HmeModProductionLineVO> proLineList = hmeExceptionReportMapper.prodLineBasicPropertyBatchGet(tenantId, proLineIdList);
            if (CollectionUtils.isNotEmpty(proLineList)) {
                proLineMap = proLineList.stream().collect(Collectors.groupingBy(HmeModProductionLineVO::getProdLineId));
            }
        }

        Map<String, List<HmeModAreaVO>> areaFinalMap = areaMap;
        Map<String, List<HmeModProductionLineVO>> proLineFinalMap = proLineMap;
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellFinalMap = nonBindWorkCellMap;

        // 异常状态值集
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.EXCEPTION_STATUS", tenantId);
        // 异常类型
        List<HmeGenTypeVO> exceptionTypeList = hmeExceptionReportMapper.queryGenTypeByModuleAndTypeGroup(tenantId, "HME", "EXCEPTION_TYPE");
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<MtUserInfo> userInfoFinalList = userInfoList;
        reportList.forEach(e -> {
            if (StringUtils.isNotBlank(e.getWorkcellId())) {
                // 绑定了工位 直接查询制造部、车间及产线
                // 制造部
                if (StringUtils.isNotBlank(e.getAreaId())) {
                    List<HmeModAreaVO> mtModAreas = areaFinalMap.get(e.getAreaId());
                    if (CollectionUtils.isNotEmpty(mtModAreas)) {
                        e.setAreaName(mtModAreas.get(0).getAreaName());
                    }
                }

                //车间
                if (StringUtils.isNotBlank(e.getWorkshopId())) {
                    List<HmeModAreaVO> workshopList = areaFinalMap.get(e.getWorkshopId());
                    if (CollectionUtils.isNotEmpty(workshopList)) {
                        e.setWorkshopName(workshopList.get(0).getAreaName());
                    }
                }
                //产线
                if (StringUtils.isNotBlank(e.getProdLineId())) {
                    List<HmeModProductionLineVO> proLines = proLineFinalMap.get(e.getProdLineId());
                    if (CollectionUtils.isNotEmpty(proLines)) {
                        e.setProdLineName(proLines.get(0).getProdLineName());
                    }
                }
            } else {
                // 未绑定工位  则根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线
                String mapKey = e.getOrganizationType() + "_" + e.getOrganizationId();
                List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellFinalMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(reportVO2List)) {
                    e.setAreaName(reportVO2List.get(0).getAreaName());
                    e.setWorkshopName(reportVO2List.get(0).getWorkshopName());
                    e.setProdLineName(reportVO2List.get(0).getProdLineName());
                }
            }

            //班组
            if (StringUtils.isNotBlank(e.getShiftId())) {
                ResponseEntity<HmeHzeroPlatformUnitDTO> unitsInfo = hmeHzeroPlatformFeignClient.getUnitsInfo(tenantId, e.getShiftId());
                if (unitsInfo.getBody() != null) {
                    e.setShiftName(unitsInfo.getBody().getUnitName());
                }
            }

            //附件名称
            if (StringUtils.isNotBlank(e.getAttachmentUuid())) {
                ResponseEntity<List<HmeHzeroFileDTO>> unitsInfo = hmeHzeroFileFeignClient.getUnitsInfo(tenantId, e.getAttachmentUuid());
                if (CollectionUtils.isNotEmpty(unitsInfo.getBody())) {
                    String attachmentName = "";
                    for (HmeHzeroFileDTO hmeHzeroFileDTO : unitsInfo.getBody()) {
                        attachmentName += StringUtils.isNotEmpty(attachmentName) ? "_" : "" + hmeHzeroFileDTO.getFileName();
                    }
                    e.setAttachmentName(attachmentName);
                    e.setFileList(unitsInfo.getBody());
                }
            }

            //异常状态描述
            if (StringUtils.isNotBlank(e.getExceptionStatus())) {
                List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getExceptionStatus())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    e.setExceptionStatusName(collect.get(0).getMeaning());
                }
            }
            //异常类型描述
            if (StringUtils.isNotBlank(e.getExceptionType())) {
                Optional<HmeGenTypeVO> exceptionTypeOpt = exceptionTypeList.stream().filter(et -> StringUtils.equals(et.getTypeCode(), e.getExceptionType())).findFirst();
                if (exceptionTypeOpt.isPresent()) {
                    e.setExceptionTypeName(exceptionTypeOpt.get().getDescription());
                }
            }

            //发起人
            if (StringUtils.isNotBlank(e.getCreatedBy())) {
                Optional<MtUserInfo> userOpt = userInfoFinalList.stream().filter(mtUserInfo -> Long.valueOf(e.getCreatedBy()).compareTo(mtUserInfo.getId()) == 0).findFirst();
                e.setCreatedByName(userOpt.isPresent() ? userOpt.get().getRealName() : "");
            }

            //响应人
            if (Strings.isNotEmpty(e.getRespondedBy())) {
                ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, e.getRespondedBy(), "P");
                if (Strings.isEmpty(userInfo.getBody().getLoginName())) {
                    e.setRespondedByName("工号：" + e.getRespondedBy() + "，根据工号找不到用户，请与OA系统核对");
                } else {
                    List<MtUserInfo> respondedList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, Collections.singletonList(Long.valueOf(userInfo.getBody().getId())));
                    if (CollectionUtils.isNotEmpty(respondedList)) {
                        e.setRespondedByName(respondedList.get(0).getRealName());
                    }
                }
            } else {
                e.setRespondedByName("");
            }

            //关闭人
            if (StringUtils.isNotBlank(e.getClosedBy())) {
                Optional<MtUserInfo> userOpt = userInfoFinalList.stream().filter(mtUserInfo -> Long.valueOf(e.getClosedBy()).compareTo(mtUserInfo.getId()) == 0).findFirst();
                e.setClosedByName(userOpt.isPresent() ? userOpt.get().getRealName() : "");
            }
            // 创建时间
            if (e.getCreationDate() != null) {
                e.setCreationDateStr(sdf.format(e.getCreationDate()));
            }
            // 响应时间
            if (e.getRespondTime() != null) {
                e.setRespondTimeStr(sdf.format(e.getRespondTime()));
            }
            // 关闭时间
            if (e.getCloseTime() != null) {
                e.setCloseTimeStr(sdf.format(e.getCloseTime()));
            }
        });
        return reportList;
    }
}
