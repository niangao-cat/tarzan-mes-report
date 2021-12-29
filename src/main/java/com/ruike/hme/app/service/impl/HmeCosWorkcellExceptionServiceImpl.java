package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.query.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import com.ruike.hme.app.service.HmeCosWorkcellExceptionService;
import com.ruike.hme.domain.vo.HmeCosQuantityVO;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO4;
import com.ruike.hme.infra.mapper.HmeCosWorkcellExceptionMapper;
import com.ruike.hme.infra.util.CollectorsUtils;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * COS工位加工异常汇总表服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 10:58
 */
@Slf4j
@Service
public class HmeCosWorkcellExceptionServiceImpl implements HmeCosWorkcellExceptionService {

    private static final String SYMBOL = "#";

    private final HmeCosWorkcellExceptionMapper hmeCosWorkcellExceptionMapper;

    public HmeCosWorkcellExceptionServiceImpl(HmeCosWorkcellExceptionMapper hmeCosWorkcellExceptionMapper) {
        this.hmeCosWorkcellExceptionMapper = hmeCosWorkcellExceptionMapper;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosWorkcellExceptionVO> queryList(Long tenantId, HmeCosWorkcellExceptionDTO dto, PageRequest pageRequest) {
        long startDate = System.currentTimeMillis();
        List<HmeCosWorkcellExceptionVO> list = hmeCosWorkcellExceptionMapper.queryList(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====COS工位异常汇总报表 第一步queryList耗时：{}毫秒", endDate - startDate);
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>();
        }

        //分组
        List<HmeCosWorkcellExceptionVO> resultList = new ArrayList<>();
        Map<HmeCosWorkcellExceptionVO,List<HmeCosWorkcellExceptionVO>> listMap = list.stream().collect(Collectors.groupingBy(HmeCosWorkcellExceptionVO::group));
        for(Map.Entry<HmeCosWorkcellExceptionVO, List<HmeCosWorkcellExceptionVO>> entry : listMap.entrySet()){
            HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO = entry.getKey();
            List<String> jobIdList = new ArrayList<>();
            BigDecimal ncQuantity = BigDecimal.ZERO;
            for (HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO2 : entry.getValue()
                 ) {
                if(StringUtils.isNotBlank(hmeCosWorkcellExceptionVO2.getJobId())
                        && !jobIdList.contains(hmeCosWorkcellExceptionVO2.getJobId())){
                    jobIdList.add(hmeCosWorkcellExceptionVO2.getJobId());
                }
                if(Objects.nonNull(hmeCosWorkcellExceptionVO2.getNcQuantity())) {
                    ncQuantity = ncQuantity.add(hmeCosWorkcellExceptionVO2.getNcQuantity());
                }
            }
            hmeCosWorkcellExceptionVO.setJobIdList(jobIdList);
            hmeCosWorkcellExceptionVO.setNcQuantity(ncQuantity);
            resultList.add(hmeCosWorkcellExceptionVO);
        }

        Page<HmeCosWorkcellExceptionVO> page = Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        List<HmeCosWorkcellExceptionVO> content = page.getContent();

        displayFieldsCompletionNew(tenantId, content, list);

        return new Page<>(content,
                new PageInfo(pageRequest.getPage(), pageRequest.getSize()),
                resultList.size());
    }

    @Override
    @ProcessLovValue
    public List<HmeCosWorkcellExceptionVO> export(Long tenantId, HmeCosWorkcellExceptionDTO dto) {
        long startDate = System.currentTimeMillis();
        List<HmeCosWorkcellExceptionVO> list = hmeCosWorkcellExceptionMapper.queryList(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("<====COS工位异常汇总报表 第一步queryList耗时：{}毫秒", endDate - startDate);

        //分组
        List<HmeCosWorkcellExceptionVO> resultList = new ArrayList<>();
        Map<HmeCosWorkcellExceptionVO,List<HmeCosWorkcellExceptionVO>> listMap = list.stream().collect(Collectors.groupingBy(HmeCosWorkcellExceptionVO::group));
        for(Map.Entry<HmeCosWorkcellExceptionVO, List<HmeCosWorkcellExceptionVO>> entry : listMap.entrySet()){
            HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO = entry.getKey();
            List<String> jobIdList = new ArrayList<>();
            BigDecimal ncQuantity = BigDecimal.ZERO;
            for (HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO2 : entry.getValue()
            ) {
                if(StringUtils.isNotBlank(hmeCosWorkcellExceptionVO2.getJobId())
                        && !jobIdList.contains(hmeCosWorkcellExceptionVO2.getJobId())){
                    jobIdList.add(hmeCosWorkcellExceptionVO2.getJobId());
                }
                if(Objects.nonNull(hmeCosWorkcellExceptionVO2.getNcQuantity())) {
                    ncQuantity = ncQuantity.add(hmeCosWorkcellExceptionVO2.getNcQuantity());
                }
            }
            hmeCosWorkcellExceptionVO.setJobIdList(jobIdList);
            hmeCosWorkcellExceptionVO.setNcQuantity(ncQuantity);
            resultList.add(hmeCosWorkcellExceptionVO);
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            displayFieldsCompletionNew(tenantId, resultList, list);
        }
        log.info("<====COS工位异常汇总报表 导出查询完成");
        return resultList;
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeCosWorkcellExceptionVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<String, BigDecimal> woProcessNcQuantityMap = new HashMap<>(16);
        Map<String, BigDecimal> ncTotalQuantityMap = new HashMap<>(16);
        Map<String, List<HmeEquipmentVO4>> equipmentMap = new HashMap<>(16);
        // 计算分页数据
        List<String> jobIdList = new ArrayList<>();
        for (HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO : list
             ) {
            if(CollectionUtils.isNotEmpty(hmeCosWorkcellExceptionVO.getJobIdList())){
                jobIdList.addAll(hmeCosWorkcellExceptionVO.getJobIdList());
            }
        }
        jobIdList = jobIdList.stream().distinct().collect(Collectors.toList());

        //批量查询不良总数 2021-07-23 10:16 edit by chaonan.hu 改为分批查询
        List<HmeCosQuantityVO> ncQuery = list.stream().map(t -> new HmeCosQuantityVO(t.getWorkOrderNum(), t.getWaferNum(), t.getWorkcellProcessId(), t.getCreationDate())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(ncQuery)){
            ncQuery = ncQuery.stream().filter(item -> StringUtils.isNotBlank(item.getWorkcellProcessId())).collect(Collectors.toList());
            ncQuery = ncQuery.stream().collect(Collectors
                    .collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(o -> o.getWorkOrderNum() + "," + o.getWaferNum() + ","
                                                                + o.getWorkcellProcessId() + "," + DateUtil.formatDate(o.getCreationDate())))), ArrayList::new));
            List<HmeCosQuantityVO> hmeCosQuantityVOList = new ArrayList<>();
            List<List<HmeCosQuantityVO>> splitList = CommonUtils.splitSqlList(ncQuery, 300);
            long startDate = System.currentTimeMillis();
            int i = 1;
            for (List<HmeCosQuantityVO> split:splitList) {
                long startDate2 = System.currentTimeMillis();
                hmeCosQuantityVOList.addAll(hmeCosWorkcellExceptionMapper.batchQueryQuantity(tenantId, split));
                long endDate2 = System.currentTimeMillis();
                log.info("<====COS工位异常汇总报表 第{}次查询不良总数耗时：{}毫秒", i, endDate2 - startDate2);
                i++;
            }
            long endDate = System.currentTimeMillis();
            log.info("<====COS工位异常汇总报表 批量查询{}次不良总数总耗时：{}毫秒", i, endDate - startDate);
            if (CollectionUtils.isNotEmpty(hmeCosQuantityVOList)) {
                log.info("<====COS工位异常汇总报表 hmeCosQuantityVOList大小：{}", hmeCosQuantityVOList.size());
                ncTotalQuantityMap = hmeCosQuantityVOList.stream().collect(Collectors.toMap(rec -> rec.getWorkOrderNum() + SYMBOL + rec.getWaferNum() + SYMBOL + rec.getWorkcellProcessId() + SYMBOL + DateUtil.formatDate(rec.getCreationDate()),
                        HmeCosQuantityVO::getDefectCountQuantity));
                log.info("<====COS工位异常汇总报表 ncTotalQuantityMap：{}", ncTotalQuantityMap);
                woProcessNcQuantityMap = hmeCosQuantityVOList.stream().collect(Collectors.groupingBy(rec -> rec.getWorkOrderNum() + SYMBOL + rec.getWorkcellProcessId(), CollectorsUtils.summingBigDecimal(HmeCosQuantityVO::getDefectCountQuantity)));
                log.info("<====COS工位异常汇总报表 woProcessNcQuantityMap：{}", woProcessNcQuantityMap);
            }
        }

        //批量查询设备编码、设备描述 2021-07-23 10:16 edit by chaonan.hu 改为分批查询
        if(CollectionUtils.isNotEmpty(jobIdList)){
            List<List<String>> splitList = CommonUtils.splitSqlList(jobIdList, 1000);
            List<HmeEquipmentVO4> jobEquipmentList = new ArrayList<>();
            long startDate = System.currentTimeMillis();
            int i = 1;
            for (List<String> split:splitList) {
                long startDate2 = System.currentTimeMillis();
                jobEquipmentList.addAll(hmeCosWorkcellExceptionMapper.batchQueryEquipment(tenantId, split));
                long endDate2 = System.currentTimeMillis();
                log.info("<====COS工位异常汇总报表 第{}次查询设备信息耗时：{}毫秒", i, endDate2 - startDate2);
                i++;
            }
            long endDate = System.currentTimeMillis();
            log.info("<====COS工位异常汇总报表 批量查询{}次设备信息总耗时：{}毫秒", i, endDate - startDate);
            if (CollectionUtils.isNotEmpty(jobEquipmentList)) {
                equipmentMap = jobEquipmentList.stream().collect(Collectors.groupingBy(HmeEquipmentVO4::getJobId));
                log.info("<====COS工位异常汇总报表 equipmentMap：{}", equipmentMap);
            }
        }

        for (HmeCosWorkcellExceptionVO rec : list) {
            // 不良数量统计
            rec.setNcTotalQuantity(ncTotalQuantityMap.getOrDefault(rec.getWorkOrderNum() + SYMBOL + rec.getWaferNum() + SYMBOL + rec.getWorkcellProcessId() + SYMBOL + DateUtil.formatDate(rec.getCreationDate()), BigDecimal.ZERO));
            log.info("<====COS工位异常汇总报表 setNcTotalQuantity");
            rec.setWoProcessNcQty(woProcessNcQuantityMap.getOrDefault(rec.getWorkOrderNum() + SYMBOL + rec.getWorkcellProcessId(), BigDecimal.ZERO));
            log.info("<====COS工位异常汇总报表 setWoProcessNcQty");

            //设备编码 设备描述
            StringBuilder assetEncodings = new StringBuilder();
            StringBuilder assetNames = new StringBuilder();

            for (Map.Entry<String, List<HmeEquipmentVO4>> entry : equipmentMap.entrySet()
                 ) {
                log.info("<====COS工位异常汇总报表 循环赋值设备信息");
                if(CollectionUtils.isEmpty(rec.getJobIdList()) || !rec.getJobIdList().contains(entry.getKey())){
                    continue;
                }
                log.info("<====COS工位异常汇总报表 循环赋值设备信息通过校验");
                List<String> assetEncodingList = new ArrayList<>();
                for (HmeEquipmentVO4 hmeEquipment : entry.getValue()) {

                    if(assetEncodingList.contains(hmeEquipment.getAssetEncoding())){
                        continue;
                    }

                    if (assetEncodings.length() == 0) {
                        assetEncodings.append(hmeEquipment.getAssetEncoding());
                        assetNames.append(hmeEquipment.getAssetName());
                    } else {
                        assetEncodings.append("/").append(hmeEquipment.getAssetEncoding());
                        assetNames.append("/").append(hmeEquipment.getAssetName());
                    }
                }
            }
            
            rec.setAssetEncoding(assetEncodings.toString());
            rec.setAssetName(assetNames.toString());
        }
        log.info("<====COS工位异常汇总报表 displayFieldsCompletion完成");
    }

    private void displayFieldsCompletionNew(Long tenantId, List<HmeCosWorkcellExceptionVO> list, List<HmeCosWorkcellExceptionVO> sourceList) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<String, BigDecimal> woProcessNcQuantityMap = new HashMap<>(16);
        Map<String, BigDecimal> ncTotalQuantityMap = new HashMap<>(16);
        Map<String, List<HmeEquipmentVO4>> equipmentMap = new HashMap<>(16);
        // 计算分页数据
        List<String> jobIdList = new ArrayList<>();
        for (HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO : list
        ) {
            if(CollectionUtils.isNotEmpty(hmeCosWorkcellExceptionVO.getJobIdList())){
                jobIdList.addAll(hmeCosWorkcellExceptionVO.getJobIdList());
            }
        }
        jobIdList = jobIdList.stream().distinct().collect(Collectors.toList());

        //批量查询不良总数 2021-07-23 10:16 edit by chaonan.hu 改为分批查询
        sourceList = sourceList.stream().filter(item -> StringUtils.isNotBlank(item.getWorkcellProcessId())).collect(Collectors.toList());
        ncTotalQuantityMap = sourceList.stream().collect(Collectors.groupingBy(rec -> rec.getWorkOrderNum() + SYMBOL + rec.getWaferNum() + SYMBOL + rec.getWorkcellProcessId() + SYMBOL + DateUtil.formatDate(rec.getCreationDate()), CollectorsUtils.summingBigDecimal(HmeCosWorkcellExceptionVO::getNcQuantity)));
        log.info("<====COS工位异常汇总报表 ncTotalQuantityMap：{}", ncTotalQuantityMap);
        woProcessNcQuantityMap = sourceList.stream().collect(Collectors.groupingBy(rec -> rec.getWorkOrderNum() + SYMBOL + rec.getWorkcellProcessId(), CollectorsUtils.summingBigDecimal(HmeCosWorkcellExceptionVO::getNcQuantity)));
        log.info("<====COS工位异常汇总报表 woProcessNcQuantityMap：{}", woProcessNcQuantityMap);

        //批量查询设备编码、设备描述 2021-07-23 10:16 edit by chaonan.hu 改为分批查询
        if(CollectionUtils.isNotEmpty(jobIdList)){
            List<List<String>> splitList = CommonUtils.splitSqlList(jobIdList, 1000);
            List<HmeEquipmentVO4> jobEquipmentList = new ArrayList<>();
            long startDate = System.currentTimeMillis();
            int i = 1;
            for (List<String> split:splitList) {
                long startDate2 = System.currentTimeMillis();
                jobEquipmentList.addAll(hmeCosWorkcellExceptionMapper.batchQueryEquipment(tenantId, split));
                long endDate2 = System.currentTimeMillis();
                log.info("<====COS工位异常汇总报表 第{}次查询设备信息耗时：{}毫秒", i, endDate2 - startDate2);
                i++;
            }
            long endDate = System.currentTimeMillis();
            log.info("<====COS工位异常汇总报表 批量查询{}次设备信息总耗时：{}毫秒", i, endDate - startDate);
            if (CollectionUtils.isNotEmpty(jobEquipmentList)) {
                equipmentMap = jobEquipmentList.stream().collect(Collectors.groupingBy(HmeEquipmentVO4::getJobId));
                log.info("<====COS工位异常汇总报表 equipmentMap：{}", equipmentMap);
            }
        }

        for (HmeCosWorkcellExceptionVO rec : list) {
            // 不良数量统计
            rec.setNcTotalQuantity(ncTotalQuantityMap.getOrDefault(rec.getWorkOrderNum() + SYMBOL + rec.getWaferNum() + SYMBOL + rec.getWorkcellProcessId() + SYMBOL + DateUtil.formatDate(rec.getCreationDate()), BigDecimal.ZERO));
            log.info("<====COS工位异常汇总报表 setNcTotalQuantity");
            rec.setWoProcessNcQty(woProcessNcQuantityMap.getOrDefault(rec.getWorkOrderNum() + SYMBOL + rec.getWorkcellProcessId(), BigDecimal.ZERO));
            log.info("<====COS工位异常汇总报表 setWoProcessNcQty");

            //设备编码 设备描述
            StringBuilder assetEncodings = new StringBuilder();
            StringBuilder assetNames = new StringBuilder();

            for (Map.Entry<String, List<HmeEquipmentVO4>> entry : equipmentMap.entrySet()
            ) {
                log.info("<====COS工位异常汇总报表 循环赋值设备信息");
                if(CollectionUtils.isEmpty(rec.getJobIdList()) || !rec.getJobIdList().contains(entry.getKey())){
                    continue;
                }
                log.info("<====COS工位异常汇总报表 循环赋值设备信息通过校验");
                List<String> assetEncodingList = new ArrayList<>();
                for (HmeEquipmentVO4 hmeEquipment : entry.getValue()) {

                    if(assetEncodingList.contains(hmeEquipment.getAssetEncoding())){
                        continue;
                    }

                    if (assetEncodings.length() == 0) {
                        assetEncodings.append(hmeEquipment.getAssetEncoding());
                        assetNames.append(hmeEquipment.getAssetName());
                    } else {
                        assetEncodings.append("/").append(hmeEquipment.getAssetEncoding());
                        assetNames.append("/").append(hmeEquipment.getAssetName());
                    }
                }
            }

            rec.setAssetEncoding(assetEncodings.toString());
            rec.setAssetName(assetNames.toString());
        }
        log.info("<====COS工位异常汇总报表 displayFieldsCompletion完成");
    }
}
