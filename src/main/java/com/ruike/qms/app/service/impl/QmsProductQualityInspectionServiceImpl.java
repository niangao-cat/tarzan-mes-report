package com.ruike.qms.app.service.impl;

import com.ruike.hme.infra.mapper.HmeExceptionReportMapper;
import com.ruike.qms.app.service.QmsProductQualityInspectionService;
import com.ruike.qms.domain.repository.QmsProductQualityInspectionRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.mapper.QmsIqcInspectionKanbanMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.common.domain.sys.MtUserInfo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:30
 */
@Service
@Slf4j
public class QmsProductQualityInspectionServiceImpl implements QmsProductQualityInspectionService {

    @Autowired
    private QmsIqcInspectionKanbanMapper qmsIqcInspectionKanbanMapper;
    @Autowired
    private QmsProductQualityInspectionRepository qmsProductQualityInspectionRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeExceptionReportMapper hmeExceptionReportMapper;


    @Override
    public QmsProductQualityInspectionVO qualityInspection(Long tenantId) {
        String siteId = defaultSiteUi(tenantId);

        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.PRODUCT_INSPECTION_KANBAN_CONFIG", tenantId);

        String startTime = getLovMeaning(list, "START_TIME");
        String endTime = getLovMeaning(list, "END_TIME");
        String operationName = getLovMeaning(list, "OPERATION_NAME");

        String startTimeNew = calculTime(startTime, endTime, true);
        String endTimeNew = calculTime(startTime, endTime, false);

        //??????????????????
        String operationId = qmsProductQualityInspectionRepository.selectOperationId(tenantId, siteId, operationName);

        //????????????eo
        List<QmsProductQualityInspectionEoVO> eoVOList = qmsProductQualityInspectionRepository.selectAllEo(tenantId, operationId, startTimeNew, endTimeNew);
        log.debug(eoVOList.toString());
        //??????eo_id??????
        var eoMap = eoVOList.parallelStream().collect(Collectors.groupingBy(QmsProductQualityInspectionEoVO::getEoId));

        //????????????eo
        List<QmsProductQualityInspectionNcEoVO> ncEoVOList = qmsProductQualityInspectionRepository.selectNcEo(tenantId, siteId, operationId, startTimeNew, endTimeNew);
        log.debug(ncEoVOList.toString());
        //??????eo_id??????
        var ncEoMap = ncEoVOList.parallelStream().collect(Collectors.groupingBy(QmsProductQualityInspectionNcEoVO::getEoId));


        Map<Long, List<Long>> userVO = new LinkedHashMap<>();
        Map<String, List<Long>> typeVO = new LinkedHashMap<>();
        List<QmsProductQualityInspectionNcEoVO> eoTypeList = null;
        // ???????????????EO
        if (CollectionUtils.isNotEmpty(ncEoVOList)) {

            //??????eo_id???????????????CREATION_DATE???????????????
            var ncEoMaxDateMap = ncEoVOList.parallelStream().collect(Collectors.toMap(QmsProductQualityInspectionNcEoVO::getEoId, Function.identity(), (c1, c2) -> c1.getCreationDate().compareTo(c2.getCreationDate()) >= 0 ? c1 : c2));

            //??????eo_id???????????????
            eoTypeList = qmsProductQualityInspectionRepository.selectEoType(tenantId, siteId, new ArrayList<>(ncEoMaxDateMap.keySet()));

            //??????Map.entrySet??????key???value??????????????????????????????
            for (Map.Entry<String, QmsProductQualityInspectionNcEoVO> entry : ncEoMaxDateMap.entrySet()) {
                String eoId = entry.getKey();
                String eoType = eoTypeList.stream().filter(item -> eoId.equals(item.getEoId())).map(QmsProductQualityInspectionNcEoVO::getItemGroupCode).collect(Collectors.toList()).get(0);
                QmsProductQualityInspectionNcEoVO value = entry.getValue();

                //??????????????????created_by??????
                var noEoUserMap = ncEoMap.get(eoId).parallelStream().collect(Collectors.groupingBy(QmsProductQualityInspectionNcEoVO::getCreatedBy));

                String ncStatus = value.getNcStatus();

                for (Map.Entry<Long, List<QmsProductQualityInspectionNcEoVO>> noEoUserEntry : noEoUserMap.entrySet()) {
                    Long createdBy = noEoUserEntry.getKey();
                    //?????????=?????????=?????????????????????
                    fillUserVOMap(userVO, createdBy, (long) noEoUserEntry.getValue().size(), (long) noEoUserEntry.getValue().size());
                    log.debug(userVO.toString());
                }
                fillTypeVOMap(typeVO, eoType, (long) ncEoMap.get(eoId).size(), (long) ncEoMap.get(eoId).size());
                log.debug(typeVO.toString());
                if ("OPEN".equals(ncStatus)) {
                    //do nothing
                } else if ("CLOSED".equals(ncStatus)) {
                    //?????????????????????????????????????????????????????????
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String creationTime = value.getCreationDate().format(formatter);
                    List<String> siteOutByList = qmsProductQualityInspectionRepository.selectSiteOutEo(tenantId, eoId, operationId, creationTime);

                    if (CollectionUtils.isNotEmpty(siteOutByList)) {
                        var siteOutByMap = siteOutByList.parallelStream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                        for (Map.Entry<String, Long> siteOurEntry : siteOutByMap.entrySet()) {

                            //?????????=?????????????????????,?????????=?????????+???????????????
                            //long eoCount = noEoUserMap.get(Long.valueOf(siteOurEntry.getKey())) == null ? 0L : noEoUserMap.get(Long.valueOf(siteOurEntry.getKey())).size();
                            fillUserVOMap(userVO, Long.valueOf(siteOurEntry.getKey()), siteOurEntry.getValue(), 0L);
                            log.debug(userVO.toString());
                        }

                        fillTypeVOMap(typeVO, eoType, (long) siteOutByList.size(), 0L);
                        log.debug(typeVO.toString());
                    }
                }
            }
        }

        var diffMap = getDifferenceMap(eoMap, ncEoMap);
        log.debug(diffMap.toString());
        //??????eo_id???????????????
        List<QmsProductQualityInspectionNcEoVO> eoTypeDiffList = qmsProductQualityInspectionRepository.selectEoType(tenantId, siteId, new ArrayList<>(diffMap.keySet()));

        if (CollectionUtils.isNotEmpty(eoTypeList)) {
            eoTypeList.addAll(eoTypeDiffList);
        } else {
            eoTypeList = eoTypeDiffList;
        }

        for (Map.Entry<String, List<QmsProductQualityInspectionEoVO>> entry : diffMap.entrySet()) {
            String eoId = entry.getKey();
            String eoType = eoTypeDiffList.stream().filter(item -> eoId.equals(item.getEoId())).map(QmsProductQualityInspectionNcEoVO::getItemGroupCode).collect(Collectors.toList()).get(0);
            //??????created_by??????
            var eoUserMap = entry.getValue().parallelStream().collect(Collectors.groupingBy(QmsProductQualityInspectionEoVO::getSiteOutBy));
            for (Map.Entry<Long, List<QmsProductQualityInspectionEoVO>> eoUserEntry : eoUserMap.entrySet()) {

                //?????????=?????????????????????????????????????????????????????????hme_eo_job_sn.???SITE_OUT_BY?????????????????????
                fillUserVOMap(userVO, eoUserEntry.getKey(), (long) eoUserEntry.getValue().size(), 0L);
                log.debug(userVO.toString());
            }
            fillTypeVOMap(typeVO, eoType, (long) eoMap.get(eoId).size(), 0L);
            log.debug(typeVO.toString());
        }

        List<Long> inspectionQuantitys = new LinkedList<>();
        List<Long> inspectionBadQuantitys = new LinkedList<>();
        List<Long> standardQuantitys = new LinkedList<>();
        List<String> realNames = new LinkedList<>();


        List<MtUserInfo> userInfoList;
        List<Long> userIdList = new ArrayList<>(userVO.keySet());
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userInfoList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, userIdList);

            List<MtUserInfo> finalUserInfoList = userInfoList;
            userIdList.forEach(userId -> {
                Optional<MtUserInfo> userOpt = finalUserInfoList.stream().filter(mtUserInfo -> userId.compareTo(mtUserInfo.getId()) == 0).findFirst();
                realNames.add(userOpt.isPresent() ? userOpt.get().getRealName() : "");
                inspectionQuantitys.add(userVO.get(userId).get(0));
                inspectionBadQuantitys.add(userVO.get(userId).get(1));
                standardQuantitys.add(userVO.get(userId).get(0) - userVO.get(userId).get(1));
            });
        }
        QmsProductQualityInspectionUserVO qualityInspectionUserVO = new QmsProductQualityInspectionUserVO();
        qualityInspectionUserVO.setRealNames(realNames);
        qualityInspectionUserVO.setInspectionQuantitys(inspectionQuantitys);
        qualityInspectionUserVO.setInspectionBadQuantitys(inspectionBadQuantitys);
        qualityInspectionUserVO.setStandardQuantitys(standardQuantitys);

        List<String> typeNames = new LinkedList<>();
        List<Long> inspectionQuantitysType = new LinkedList<>();
        List<Long> inspectionBadQuantitysType = new LinkedList<>();
        List<Long> standardQuantitysType = new LinkedList<>();
        for (Map.Entry<String, List<Long>> eoTypeEntry : typeVO.entrySet()) {
            String itemGroupDescription = eoTypeList.stream().filter(item -> eoTypeEntry.getKey().equals(item.getItemGroupCode())).map(QmsProductQualityInspectionNcEoVO::getItemGroupDescription).collect(Collectors.toList()).get(0);

            typeNames.add(itemGroupDescription);
            inspectionQuantitysType.add(typeVO.get(eoTypeEntry.getKey()).get(0));
            inspectionBadQuantitysType.add(typeVO.get(eoTypeEntry.getKey()).get(1));
            standardQuantitysType.add(typeVO.get(eoTypeEntry.getKey()).get(0) - typeVO.get(eoTypeEntry.getKey()).get(1));
        }
        QmsProductQualityInspectionTypeVO productQualityInspectionTypeVO = new QmsProductQualityInspectionTypeVO();
        productQualityInspectionTypeVO.setTypeNames(typeNames);
        productQualityInspectionTypeVO.setInspectionQuantitys(inspectionQuantitysType);
        productQualityInspectionTypeVO.setInspectionBadQuantitys(inspectionBadQuantitysType);
        productQualityInspectionTypeVO.setStandardQuantitys(standardQuantitysType);

        QmsProductQualityInspectionVO qualityInspectionVO = new QmsProductQualityInspectionVO();
        qualityInspectionVO.setProductQualityInspectionUserVO(qualityInspectionUserVO);
        qualityInspectionVO.setQmsProductQualityInspectionTypeVO(productQualityInspectionTypeVO);
        return qualityInspectionVO;
    }

    @Override
    public List<QmsProductQualityInspectionNcRecordVO> ncRecord(Long tenantId) {
        String siteId = defaultSiteUi(tenantId);

        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.PRODUCT_INSPECTION_KANBAN_CONFIG", tenantId);

        String startTime = getLovMeaning(list, "START_TIME");
        String endTime = getLovMeaning(list, "END_TIME");
        String operationName = getLovMeaning(list, "OPERATION_NAME");

        String startTimeNew = calculTime(startTime, endTime, true);
        String endTimeNew = calculTime(startTime, endTime, false);

        //??????????????????
        String operationId = qmsProductQualityInspectionRepository.selectOperationId(tenantId, siteId, operationName);

        List<QmsProductQualityInspectionNcRecordVO> result = qmsProductQualityInspectionRepository.selectNcRecord(tenantId, siteId, operationId, startTimeNew, endTimeNew);

        if (CollectionUtils.isNotEmpty(result)) {

            List<Long> userIdList = result.stream().map(QmsProductQualityInspectionNcRecordVO::getCreatedBy).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            List<MtUserInfo> userInfoList = hmeExceptionReportMapper.userInfoBatchGet(tenantId, userIdList);

            List<String> eoIdList = result.stream().map(QmsProductQualityInspectionNcRecordVO::getEoId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            //??????eo_id???????????????
            List<QmsProductQualityInspectionNcEoVO> eoTypeList = qmsProductQualityInspectionRepository.selectEoType(tenantId, siteId, eoIdList);

            result.forEach(item -> {
                Optional<MtUserInfo> userOpt = userInfoList.stream().filter(mtUserInfo -> item.getCreatedBy().compareTo(mtUserInfo.getId()) == 0).findFirst();
                item.setRealName(userOpt.isPresent() ? userOpt.get().getRealName() : "");

                Optional<QmsProductQualityInspectionNcEoVO> eoTypeOption = eoTypeList.stream().filter(eoType -> item.getEoId().compareTo(eoType.getEoId()) == 0).findFirst();
                item.setItemGroupDescription(eoTypeOption.isPresent() ? eoTypeOption.get().getItemGroupDescription() : "");
            });
        }

        return result;
    }

    /**
     * ????????????????????????????????????
     *
     * @param startTime ????????????????????????HH:mm:ss??????
     * @param endTime   ????????????????????????HH:mm:ss
     * @param startFlag ??????????????????
     * @return java.lang.String
     * @author faming.yang@hand-china.com 2021/5/17 15:00
     */
    private String calculTime(String startTime, String endTime, Boolean startFlag) {
        String nowTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault()).format(Instant.now());
        String nowDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault()).format(Instant.now());


        if (startTime.compareTo(nowTime) >= 0) {
            String preDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault()).format(Instant.now().minus(1, ChronoUnit.DAYS));

            return startFlag ? preDay + " " + startTime : nowDay + " " + endTime;
        } else {
            String sufDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault()).format(Instant.now().plus(1, ChronoUnit.DAYS));

            return startFlag ? nowDay + " " + startTime : sufDay + " " + endTime;
        }
    }


    /**
     * ???????????????????????????Map
     *
     * @param userVO ?????????????????????Map
     * @param userId ?????????Id
     * @param v0     ?????????
     * @param v1     ?????????
     * @author faming.yang@hand-china.com 2021/5/14 4:42
     */
    private void fillUserVOMap(Map<Long, List<Long>> userVO, Long userId, Long v0, Long v1) {
        userVO.compute(userId, (k, v) -> {
            if (v == null) {
                v = new LinkedList<Long>() {{
                    add(v0);
                    add(v1);
                }};
            } else {
                Long oldV0 = v.get(0);
                Long oldV1 = v.get(1);
                v = new LinkedList<Long>() {{
                    add(v0 + oldV0);
                    add(v1 + oldV1);
                }};
            }

            return v;
        });
    }


    /**
     * ??????x??????????????????Map
     *
     * @param typeVO ??????????????????Map
     * @param eoType eo??????
     * @param v0     ?????????
     * @param v1     ?????????
     * @author faming.yang@hand-china.com 2021/5/14 4:42
     */
    private void fillTypeVOMap(Map<String, List<Long>> typeVO, String eoType, Long v0, Long v1) {
        typeVO.compute(eoType, (k, v) -> {
            if (v == null) {
                v = new LinkedList<Long>() {{
                    add(v0);
                    add(v1);
                }};
            } else {
                Long oldV0 = v.get(0);
                Long oldV1 = v.get(1);
                v = new LinkedList<Long>() {{
                    add(v0 + oldV0);
                    add(v1 + oldV1);
                }};
            }

            return v;
        });
    }


    /**
     * ???Map???????????????
     *
     * @param bigMap   ?????????
     * @param smallMap ?????????
     * @return java.util.Map<java.lang.String, java.util.List < com.ruike.qms.domain.vo.QmsProductQualityInspectionEoVO>>
     * @author faming.yang@hand-china.com 2021/5/14 5:15
     */
    private Map<String, List<QmsProductQualityInspectionEoVO>> getDifferenceMap(Map<String, List<QmsProductQualityInspectionEoVO>> bigMap, Map<String, List<QmsProductQualityInspectionNcEoVO>> smallMap) {
        Set<String> bigMapKey = bigMap.keySet();
        Set<String> smallMapKey = smallMap.keySet();
        //Set<String> differenceSet = Sets.difference(bigMapKey, smallMapKey);
        List<String> collect = bigMapKey.stream().filter(item -> !smallMapKey.contains(item)).collect(Collectors.toList());
        Map<String, List<QmsProductQualityInspectionEoVO>> result = new LinkedHashMap<>();
        for (String key : collect) {
            result.put(key, bigMap.get(key));
        }
        return result;
    }


    /**
     * ??????????????????
     *
     * @param tenantId ??????id
     * @return java.lang.String
     * @author faming.yang@hand-china.com 2021/5/14 10:24
     */
    private String defaultSiteUi(Long tenantId) {
        List<LovValueDTO> siteList = lovAdapter.queryLovValue("HME.KANBAN_SITE", tenantId);
        if (CollectionUtils.isEmpty(siteList)) {
            throw new CommonException("??????????????????????????????HME.KANBAN_SITE???");
        }
        return siteList.get(0).getValue();
    }


    /**
     * ??????????????????
     *
     * @param list     ??????list
     * @param lovValue ??????value
     * @return java.lang.String
     * @author faming.yang@hand-china.com 2021/5/14 10:23
     */
    private String getLovMeaning(List<LovValueDTO> list, String lovValue) {
        List<LovValueDTO> list2 = list.stream().filter(f -> StringUtils.equals(f.getValue(), lovValue)).collect(Collectors.toList());
        return list2.get(0).getMeaning();
    }

}
