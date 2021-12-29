package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.HmeModProductionLineVO;
import com.ruike.hme.domain.vo.HmeModWorkcellVO;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2;
import com.ruike.hme.infra.constant.Constant;
import com.ruike.hme.infra.mapper.HmeWorkCellDetailsReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtUserClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工位产量明细报表
 *
 * @author xin.tian@raycuslaser 2021/06/18 14:31
 */
@Component
@Slf4j
public class HmeWorkCellDetailsReportRepositoryImpl implements HmeWorkCellDetailsReportRepository {

    @Autowired
    private HmeWorkCellDetailsReportMapper hmeWorkCellDetailsReportMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtUserClient userClient;

    private static final int DAY_30 = 30;

    @Override
    @ProcessLovValue
    public Page<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest) {

        //查询条件校验
        checkBeforeList(tenantId,reportVO);

        //查询分页列表
        Page<HmeWorkCellDetailsReportVO2> reportVO2List = PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.queryWorkCellReportList(tenantId, reportVO));
        setValues(tenantId,reportVO2List.getContent());

        return reportVO2List;
    }

    @Override
    @ProcessLovValue
    public List<HmeWorkCellDetailsReportVO2> exportWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO) {

        //查询数据并导出
        List<HmeWorkCellDetailsReportVO2> reportVO2List = hmeWorkCellDetailsReportMapper.queryWorkCellReportList(tenantId, reportVO);
        setValues(tenantId,reportVO2List);

        return reportVO2List;
    }

    @Override
    public List<HmeModWorkcellVO> workcellBasicPropertyBatchGet(Long tenantId, List<String> workcellIds) {
        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellBasicPropertyBatchGet】"));
        }
        return hmeWorkCellDetailsReportMapper.selectByIdsCustomWorkcell(tenantId, workcellIds);
    }

    @Override
    public List<HmeModProductionLineVO> prodLineBasicPropertyBatchGet(Long tenantId, List<String> prodLineIds) {
        if (CollectionUtils.isEmpty(prodLineIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineBasicPropertyBatchGet】"));
        }
        return hmeWorkCellDetailsReportMapper.selectByIdsCustomProdLine(tenantId, prodLineIds);
    }

    private void checkBeforeList(Long tenantId,HmeWorkCellDetailsReportVO reportVO){

        //选了工单号或者SN号，不校验其他条件
        if (!StringUtils.isBlank(reportVO.getMaterialLotCode()) || !StringUtils.isBlank(reportVO.getWorkOrder())){
            return;
        }

        //只传结束时间 提示选择开始时间
        if (StringUtils.isBlank(reportVO.getStartTime()) && StringUtils.isNotBlank(reportVO.getEndTime())) {
            throw new MtException("HME_EQUIPMENT_HIS_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_HIS_002", "HME"));
        }

        if (StringUtils.isNotBlank(reportVO.getEndTime()) && StringUtils.isNotBlank(reportVO.getStartTime())) {
            //开始时间不能大于结束时间
            DateTime startTime = DateUtil.parse(reportVO.getStartTime(), Constant.ConstantValue.DATE_TIME_FORMAT);
            DateTime endTime = DateUtil.parse(reportVO.getEndTime(), Constant.ConstantValue.DATE_TIME_FORMAT);
            int compare = DateUtil.compare(startTime, endTime);
            if (compare > 0) {
                throw new MtException("HME_EQUIPMENT_HIS_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_001", "HME"));
            }

            //间隔不能超过30天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(endTime);
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            if (betweenDays > DAY_30) {
                throw new MtException("HME_EQUIPMENT_HIS_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_003", "HME"));
            }
        }

        //只传开始时间 与当前时间比较 间隔不能超过30天
        if (StringUtils.isNotBlank(reportVO.getStartTime()) && StringUtils.isBlank(reportVO.getEndTime())) {
            DateTime startTime = DateUtil.parse(reportVO.getStartTime(), Constant.ConstantValue.DATE_TIME_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(new Date());
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            if (betweenDays > DAY_30) {
                throw new MtException("HME_EQUIPMENT_HIS_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_003", "HME"));
            }
        }

        //没传时间 取前一个月的数据
        if (StringUtils.isBlank(reportVO.getStartTime()) && StringUtils.isBlank(reportVO.getEndTime())) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            reportVO.setStartTime(DateUtil.format(calendar.getTime(), Constant.ConstantValue.DATE_TIME_FORMAT));
        }

        //获取当前用户的站点信息
//        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
//        MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
//        orgVO.setTopSiteId(defaultSiteId);

        //工位 必输
//        if (StringUtils.isBlank(reportVO.getWorkcellId())) {
//            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_PRO_REPORT_002", "HME"));
//        }
    }

    private void setValues(Long tenantId,List<HmeWorkCellDetailsReportVO2> reportVO2List){
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.JOB_TYPE", tenantId);
        List<String> workcellIdList = reportVO2List.stream().map(HmeWorkCellDetailsReportVO2::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> processIdList = reportVO2List.stream().map(HmeWorkCellDetailsReportVO2::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> lineWorkIdList = reportVO2List.stream().map(HmeWorkCellDetailsReportVO2::getLineWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> workIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workcellIdList)) {
            workIdList.addAll(workcellIdList);
        }
        if (CollectionUtils.isNotEmpty(processIdList)) {
            workIdList.addAll(processIdList);
        }
        if (CollectionUtils.isNotEmpty(lineWorkIdList)) {
            workIdList.addAll(lineWorkIdList);
        }
        //批量查询工位 工段 工序
        List<HmeModWorkcellVO> hmeModWorkcellList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workIdList)) {
            hmeModWorkcellList = this.workcellBasicPropertyBatchGet(tenantId, workIdList);
        }
        Map<String, List<HmeModWorkcellVO>> hmeModWorkcellMap = hmeModWorkcellList.stream().collect(Collectors.groupingBy(HmeModWorkcellVO::getWorkcellId));
        //批量查询产线
        List<String> proLineIdList = reportVO2List.stream().map(HmeWorkCellDetailsReportVO2::getProductionLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeModProductionLineVO> hmeModProductionLineList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(proLineIdList)) {
            hmeModProductionLineList = this.prodLineBasicPropertyBatchGet(tenantId, proLineIdList);
        }
        Map<String, List<HmeModProductionLineVO>> lineMap = hmeModProductionLineList.stream().collect(Collectors.groupingBy(HmeModProductionLineVO::getProdLineId));
        reportVO2List.forEach(e -> {
            //生产线处理
            if (StringUtils.isNotBlank(e.getProductionLineId())) {
                List<HmeModProductionLineVO> lineList = lineMap.get(e.getProductionLineId());
                e.setProductionLineName(CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getProdLineName() : "");
            }
            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                List<HmeModWorkcellVO> workcellList = hmeModWorkcellMap.get(e.getLineWorkcellId());
                e.setLineWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }
            //工位
            if (StringUtils.isNotBlank(e.getWorkcellId())) {
                List<HmeModWorkcellVO> workcellList = hmeModWorkcellMap.get(e.getWorkcellId());
                e.setStationWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }
            //工序
            if (StringUtils.isNotBlank(e.getProcessId())) {
                List<HmeModWorkcellVO> workcellList = hmeModWorkcellMap.get(e.getProcessId());
                e.setProcessWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }

            //作业人处理
            if (StringUtils.equals(e.getFlag(), Constant.ConstantValue.NO)) {
                e.setWorkerName(StringUtils.isNotBlank(e.getSiteInBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(e.getSiteInBy())).getRealName() : "");
                e.setJobTypeName("进站");
            } else if (StringUtils.equals(e.getFlag(), Constant.ConstantValue.YES)) {
                e.setWorkerName(StringUtils.isNotBlank(e.getSiteOutBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(e.getSiteOutBy())).getRealName() : "");
                e.setJobTypeName("出站");
            }

            //作业平台
            if (StringUtils.isNotBlank(e.getJobPlatformCode())) {
                List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getJobPlatformCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    e.setJobPlatform(collect.get(0).getMeaning());
                }
            }
        });
    }
}
