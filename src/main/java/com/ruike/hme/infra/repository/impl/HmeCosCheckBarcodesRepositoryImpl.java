package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.domain.repository.HmeCosCheckBarcodesRepository;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO2;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO3;
import com.ruike.hme.infra.mapper.HmeCosCheckBarcodesMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 12:27
 */
@Component
@Slf4j
public class HmeCosCheckBarcodesRepositoryImpl implements HmeCosCheckBarcodesRepository {

    @Autowired
    HmeCosCheckBarcodesMapper hmeCosCheckBarcodesMapper;

    @Override
    public Page<HmeCosCheckBarcodesVO> selectCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto, PageRequest pageRequest) {
        Page<HmeCosCheckBarcodesVO> page = PageHelper.doPage(pageRequest, () -> hmeCosCheckBarcodesMapper.selectCheckBarcodes(tenantId, dto));
        if(CollectionUtils.isNotEmpty(page.getContent())){
            List<String> jobIdList = page.getContent().stream().map(HmeCosCheckBarcodesVO::getJobId).distinct().collect(Collectors.toList());
            List<String> loadSequenceList = page.getContent().stream().map(HmeCosCheckBarcodesVO::getLoadSequence).distinct().collect(Collectors.toList());
            List<List<String>> splitJobIdList = CommonUtils.splitSqlList(jobIdList, 1000);
            List<List<String>> splitLoadSequenceList = CommonUtils.splitSqlList(loadSequenceList, 1000);
            List<HmeCosCheckBarcodesVO2> assetEncodingList = new ArrayList<>();
            for (List<String> splitJobId:splitJobIdList) {
                //批量查询设备
                assetEncodingList.addAll(hmeCosCheckBarcodesMapper.bacthGetAssetEncodingByJob(tenantId, splitJobId));
            }
            List<HmeCosCheckBarcodesVO3> labCodeList = new ArrayList<>();
            for (List<String> splitLoadSequence:splitLoadSequenceList) {
                //批量查询实验代码
                labCodeList.addAll(hmeCosCheckBarcodesMapper.bacthGetLabCodeByLoadSequence(tenantId, splitLoadSequence));
            }
            //组装job与设备对应数据
            Map<String, String> jobEquipmentMap = new HashMap<>();
            Map<String, List<String>> jobAssetEncodingMap = assetEncodingList.stream().collect(Collectors.groupingBy(HmeCosCheckBarcodesVO2::getJobId,
                    Collectors.mapping(HmeCosCheckBarcodesVO2::getAssetEncoding, Collectors.toList())));
            for (Map.Entry<String, List<String>> entry:jobAssetEncodingMap.entrySet()) {
                List<String> jobAssetEncodingList = entry.getValue().stream().distinct().collect(Collectors.toList());
                jobEquipmentMap.put(entry.getKey(), String.join("/", jobAssetEncodingList));
            }
            //组装loadSequence与实验代码对应数据
            Map<String, String> loadSequenceLabCodeMap = new HashMap<>();
            Map<String, List<String>> loadSequenceLabCodeListMap = labCodeList.stream().collect(Collectors.groupingBy(HmeCosCheckBarcodesVO3::getLoadSequence,
                    Collectors.mapping(HmeCosCheckBarcodesVO3::getLabCode, Collectors.toList())));
            for (Map.Entry<String, List<String>> entry:loadSequenceLabCodeListMap.entrySet()) {
                List<String> loadSequenceLabCodeList = entry.getValue().stream().distinct().collect(Collectors.toList());
                loadSequenceLabCodeMap.put(entry.getKey(), String.join("/", loadSequenceLabCodeList));
            }
            for (HmeCosCheckBarcodesVO dto4 : page.getContent()) {
                String equipment = jobEquipmentMap.get(dto4.getJobId());
                dto4.setEquipment(equipment);
                String labCode = loadSequenceLabCodeMap.get(dto4.getLoadSequence());
                dto4.setExperimentCode(labCode);
                //加工时长
                if(Objects.nonNull(dto4.getSiteInDate()) && Objects.nonNull(dto4.getSiteOutDate())){
                    long time = dto4.getSiteOutDate().getTime() - dto4.getSiteInDate().getTime();
                    long min = 1000 * 60;
                    BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                    dto4.setProcessTime(processTime);
                }
                //旧位置行号替换为字母
                if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                    String[] split = dto4.getRowCloumn().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }
            }
        }
        return page;
    }

    @Override
    public List<HmeCosCheckBarcodesVO> exportCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto) {
        long startDate = System.currentTimeMillis();
        List<HmeCosCheckBarcodesVO> hmeCosCheckBarcodesVOS = new ArrayList<>();
        hmeCosCheckBarcodesVOS.addAll(hmeCosCheckBarcodesMapper.selectCheckBarcodesOne(tenantId, dto));
        long endDate = System.currentTimeMillis();
        log.info("<====COS目检条码表 第一种情况查询耗时：{}毫秒",  (endDate - startDate));
        hmeCosCheckBarcodesVOS.addAll(hmeCosCheckBarcodesMapper.selectCheckBarcodesTwo(tenantId, dto));
        long endDate2 = System.currentTimeMillis();
        log.info("<====COS目检条码表 第二种情况查询耗时：{}毫秒",  (endDate2 - endDate));
        if(CollectionUtils.isNotEmpty(hmeCosCheckBarcodesVOS)){
            long startDate3 = System.currentTimeMillis();
            hmeCosCheckBarcodesVOS.sort(Comparator.comparing(HmeCosCheckBarcodesVO::getSiteInDate)
                    .thenComparing(HmeCosCheckBarcodesVO::getMaterialCode)
                    .thenComparing(HmeCosCheckBarcodesVO::getLoadRow)
                    .thenComparing(HmeCosCheckBarcodesVO::getLoadColumn));
            List<String> jobIdList = hmeCosCheckBarcodesVOS.stream().map(HmeCosCheckBarcodesVO::getJobId).distinct().collect(Collectors.toList());
            List<String> loadSequenceList = hmeCosCheckBarcodesVOS.stream().map(HmeCosCheckBarcodesVO::getLoadSequence).distinct().collect(Collectors.toList());
            List<List<String>> splitJobIdList = CommonUtils.splitSqlList(jobIdList, 1000);
            List<List<String>> splitLoadSequenceList = CommonUtils.splitSqlList(loadSequenceList, 800);
            List<HmeCosCheckBarcodesVO2> assetEncodingList = new ArrayList<>();
            for (List<String> splitJobId:splitJobIdList) {
                //批量查询设备
                long startDate4 = System.currentTimeMillis();
                assetEncodingList.addAll(hmeCosCheckBarcodesMapper.bacthGetAssetEncodingByJob(tenantId, splitJobId));
                long endDate4 = System.currentTimeMillis();
                log.info("<====COS目检条码表 单次查询设备总耗时：{}毫秒",  (endDate4 - startDate4));
            }
            long endDate3 = System.currentTimeMillis();
            log.info("<====COS目检条码表 批量查询设备总耗时：{}毫秒",  (endDate3 - startDate3));
            List<HmeCosCheckBarcodesVO3> labCodeList = new ArrayList<>();
            for (List<String> splitLoadSequence:splitLoadSequenceList) {
                //批量查询实验代码
                labCodeList.addAll(hmeCosCheckBarcodesMapper.bacthGetLabCodeByLoadSequence(tenantId, splitLoadSequence));
            }
            //组装job与实验对应数据
            long startDate4 = System.currentTimeMillis();
            Map<String, String> jobEquipmentMap = new HashMap<>();
            Map<String, List<String>> jobAssetEncodingMap = assetEncodingList.stream().collect(Collectors.groupingBy(HmeCosCheckBarcodesVO2::getJobId,
                    Collectors.mapping(HmeCosCheckBarcodesVO2::getAssetEncoding, Collectors.toList())));
            for (Map.Entry<String, List<String>> entry:jobAssetEncodingMap.entrySet()) {
                List<String> jobAssetEncodingList = entry.getValue().stream().distinct().collect(Collectors.toList());
                jobEquipmentMap.put(entry.getKey(), String.join("/", jobAssetEncodingList));
            }
            long endDate4 = System.currentTimeMillis();
            log.info("<====COS目检条码表 组装job与实验对应数据耗时：{}毫秒",  (endDate4 - startDate4));
            //组装loadSequence与实验代码对应数据
            Map<String, String> loadSequenceLabCodeMap = new HashMap<>();
            Map<String, List<String>> loadSequenceLabCodeListMap = labCodeList.stream().collect(Collectors.groupingBy(HmeCosCheckBarcodesVO3::getLoadSequence,
                    Collectors.mapping(HmeCosCheckBarcodesVO3::getLabCode, Collectors.toList())));
            for (Map.Entry<String, List<String>> entry:loadSequenceLabCodeListMap.entrySet()) {
                List<String> loadSequenceLabCodeList = entry.getValue().stream().distinct().collect(Collectors.toList());
                loadSequenceLabCodeMap.put(entry.getKey(), String.join("/", loadSequenceLabCodeList));
            }
            long startDate5 = System.currentTimeMillis();
            for (HmeCosCheckBarcodesVO dto4 : hmeCosCheckBarcodesVOS) {
                String equipment = jobEquipmentMap.get(dto4.getJobId());
                dto4.setEquipment(equipment);
                String labCode = loadSequenceLabCodeMap.get(dto4.getLoadSequence());
                dto4.setExperimentCode(labCode);
                //加工时长
                if(Objects.nonNull(dto4.getSiteInDate()) && Objects.nonNull(dto4.getSiteOutDate())){
                    long time = dto4.getSiteOutDate().getTime() - dto4.getSiteInDate().getTime();
                    long min = 1000 * 60;
                    BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                    dto4.setProcessTime(processTime);
                }
                //旧位置行号替换为字母
                if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                    String[] split = dto4.getRowCloumn().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
                }
            }
            long endDate5 = System.currentTimeMillis();
            log.info("<====COS目检条码表 循环组装数据耗时：{}毫秒",  (endDate5 - startDate5));
        }
        long endDate6 = System.currentTimeMillis();
        log.info("<====COS目检条码表 本次查询总耗时：{}毫秒",  (endDate6 - startDate));
        return hmeCosCheckBarcodesVOS;
    }
}
