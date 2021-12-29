package com.ruike.qms.infra.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.repository.QmsPqcReportRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsPqcReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.util.DateUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检报表资源库默认实现
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
@Component
@Slf4j
public class QmsPqcReportRepositoryImpl implements QmsPqcReportRepository {

    @Autowired
    private QmsPqcReportMapper qmsPqcReportMapper;

    @Override
    public Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
//        Page<QmsPqcReportVO> resultPage = new Page<>();
//        if(StringUtils.isEmpty(dto.getWorkshopId())){
//            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pqcReportHeadDataQueryByDepartment(tenantId, dto));
//        }else{
//            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pqcReportHeadDataQueryByWorkshop(tenantId, dto));
//        }
        Page<QmsPqcReportVO> resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pqcReportHeadDataQuery(tenantId, dto));
        if(CollectionUtil.isNotEmpty(resultPage.getContent())){
            //查询到的不同的车间ID,进而去查询各个车间的不合格数
            List<String> areaIdList = resultPage.getContent().stream().map(QmsPqcReportVO::getAreaId).distinct().collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(areaIdList)){
                dto.setWorkshopIdList(areaIdList);
                List<QmsPqcReportVO> workshopNcNumList = qmsPqcReportMapper.workshopNcNumQuery(tenantId, dto);
                if(CollectionUtil.isNotEmpty(workshopNcNumList)){
                    for (QmsPqcReportVO qmsPqcReportVO:resultPage.getContent()) {
                        List<QmsPqcReportVO> qmsPqcReportVOList = workshopNcNumList.stream().filter(item -> item.getAreaId().equals(qmsPqcReportVO.getAreaId())).collect(Collectors.toList());
                        if(CollectionUtil.isNotEmpty(qmsPqcReportVOList)){
                            qmsPqcReportVO.setAreaNcNum(qmsPqcReportVOList.get(0).getAreaNcNum());
                        }
                    }
                }
            }
        }
        return resultPage;
    }

    @Override
    public Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
        Page<QmsPqcReportVO2> resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pgcReportDetailDataQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    public QmsPqcReportChartVO2 pqcReportEchartQuery(Long tenantId, QmsPqcReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }

        QmsPqcReportChartVO2 resultV0 = new QmsPqcReportChartVO2();
        List<QmsPqcReportChartVO3> qmsPqcReportChartVO3List = new ArrayList<>();
        //时间轴
        List<String> timeList = new ArrayList<>();
        //取出前台传入时间的---从时间
        Calendar cTo = Calendar.getInstance();
        cTo.setTime(dto.getInspectionFinishToDate());
        String toYearMonth = cTo.get(Calendar.YEAR) + "-" + (cTo.get(Calendar.MONTH) + 1);
        int toMonth = cTo.get(Calendar.MONTH) + 1;
        //季-判断
        int toSeason = 0;
        if(toMonth >= 1 && toMonth <= 3) {
            toSeason = 1;
        }else if (toMonth >= 4 && toMonth <= 6){
            toSeason = 2;
        }else if (toMonth >= 7 && toMonth <= 9){
            toSeason = 3;
        }else if (toMonth >= 10 && toMonth <= 12){
            toSeason = 4;
        }
        String toYearSeason = cTo.get(Calendar.YEAR) + "-" + toSeason;
        // 周
        String toYearWeek = cTo.get(Calendar.YEAR) + "-" + cTo.get(Calendar.WEEK_OF_YEAR);
        //取出前台传入时间的---至时间
        Calendar cFrom = Calendar.getInstance();
        cFrom.setTime(dto.getInspectionFinishFromDate());

        //时间轴赋值 -年
        if (QmsConstants.PqcReportChartType.YEAR.equals(dto.getTimeFlag())) {
            do {
                if(timeList.size() > 0){
                    cFrom.add(Calendar.YEAR, 1); // 开始日期加一个年直到等于结束日期为止
                }
                timeList.add(String.valueOf(cFrom.get(Calendar.YEAR)));
            } while (cFrom.get(Calendar.YEAR) < cTo.get(Calendar.YEAR));
        }



        if (QmsConstants.PqcReportChartType.MONTH.equals(dto.getTimeFlag())) {
            //时间轴赋值 -月
            String yearMonth = "";
            do {
                if(timeList.size() > 0){
                    cFrom.add(Calendar.MONTH, 1); // 开始日期加一个月直到等于结束日期为止
                }
                yearMonth = cFrom.get(Calendar.YEAR) + "-" + (cFrom.get(Calendar.MONTH) + 1);
                timeList.add(yearMonth);
            } while (!StringUtils.equals(toYearMonth, yearMonth));
        }else if (QmsConstants.PqcReportChartType.SEASON.equals(dto.getTimeFlag())) {
            //时间轴赋值 -季
            String yearSeason = "";
            do {
                if(timeList.size() > 0){
                    cFrom.add(Calendar.MONTH, 3); // 开始日期加一个季直到等于结束日期为止
                }
                int month = cFrom.get(Calendar.MONTH) + 1;
                //季-判断
                int fromSeason = 0;
                if(month >= 1 && month <= 3) {
                    fromSeason = 1;
                }else if (month >= 4 && month <= 6){
                    fromSeason = 2;
                }else if (month >= 7 && month <= 9){
                    fromSeason = 3;
                }else if (month >= 10 && month <= 12){
                    fromSeason = 4;
                }
                yearSeason = cFrom.get(Calendar.YEAR)+"-"+fromSeason;
                timeList.add(yearSeason);
            } while (!StringUtils.equals(toYearSeason, yearSeason));
        }else if (QmsConstants.PqcReportChartType.WEEK.equals(dto.getTimeFlag())) {
            //时间轴赋值 -周
            String yearWeek = "";
            do {
                if(timeList.size() > 0){
                    cFrom.add(Calendar.WEEK_OF_YEAR, 1); // 开始日期加一个周直到等于结束日期为止
                }
                yearWeek = cFrom.get(Calendar.YEAR)+"-"+cFrom.get(Calendar.WEEK_OF_YEAR);
                timeList.add(yearWeek);
            } while (!StringUtils.equals(yearWeek, toYearWeek));
        }

        List<QmsPqcReportChartVO> qmsPqcReportChartVOList = new ArrayList<>();
        if(StringUtils.isBlank(dto.getWorkshopId())) {
            if (QmsConstants.PqcReportChartType.YEAR.equals(dto.getTimeFlag())) {
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryYearByDepartment(tenantId, dto);
            }else if (QmsConstants.PqcReportChartType.MONTH.equals(dto.getTimeFlag())) {
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryMonthByDepartment(tenantId, dto);
            }else if (QmsConstants.PqcReportChartType.SEASON.equals(dto.getTimeFlag())) {
                qmsPqcReportChartVOList = qmsPqcReportMapper.querySeasonByDepartment(tenantId, dto);
            }else if (QmsConstants.PqcReportChartType.WEEK.equals(dto.getTimeFlag())) {
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryWeekByDepartment(tenantId, dto);
            }
        }else{
            if(QmsConstants.PqcReportChartType.YEAR.equals(dto.getTimeFlag())){
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryYearByWorkshop(tenantId, dto);
            }else if(QmsConstants.PqcReportChartType.MONTH.equals(dto.getTimeFlag())){
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryMonthByWorkshop(tenantId, dto);
            }else if(QmsConstants.PqcReportChartType.SEASON.equals(dto.getTimeFlag())){
                qmsPqcReportChartVOList = qmsPqcReportMapper.querySeasonByWorkshop(tenantId, dto);
            }else if(QmsConstants.PqcReportChartType.WEEK.equals(dto.getTimeFlag())){
                qmsPqcReportChartVOList = qmsPqcReportMapper.queryWeekByWorkshop(tenantId, dto);
            }
        }

        //去掉周中的-00
        if(QmsConstants.PqcReportChartType.WEEK.equals(dto.getTimeFlag())){
            for (QmsPqcReportChartVO vo : qmsPqcReportChartVOList){
                if("00".equals(vo.getTimeType().substring(5,7))){
                    int year = Integer.valueOf((vo.getTimeType().substring(0,4))) - 1;
                    String yearStr = String.valueOf(year);
                    int maxWeek = CommonUtils.getMaxWeekNumOfYear(year);
                    for (QmsPqcReportChartVO qmsPqcReportChartVO : qmsPqcReportChartVOList) {
                        if(qmsPqcReportChartVO.getTimeType().equals(year + "-" + maxWeek)){
                            qmsPqcReportChartVO.setValue(qmsPqcReportChartVO.getValue()+vo.getValue());
                        }
                    }
                    qmsPqcReportChartVOList.remove(vo);
                }
            }
        }

        //赋值
        if(CollectionUtils.isNotEmpty(timeList) && CollectionUtils.isNotEmpty(qmsPqcReportChartVOList)){

            Map<String,List<QmsPqcReportChartVO>> qmsPqcReportChartVOMap = qmsPqcReportChartVOList.stream().collect(Collectors.groupingBy(e -> e.getAreaName() + e.getProcessName()));
            for (String key : qmsPqcReportChartVOMap.keySet()) {
                QmsPqcReportChartVO3 qmsPqcReportChartVO3 = new QmsPqcReportChartVO3();

                List<QmsPqcReportChartVO> valueList = new ArrayList<QmsPqcReportChartVO>();
                for (String timeType : timeList) {
                    Iterator<Map.Entry<String, List<QmsPqcReportChartVO>>> entries = qmsPqcReportChartVOMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, List<QmsPqcReportChartVO>> entry = entries.next();
                        if(entry.getKey().equals(key)){
                            valueList = entry.getValue();
                            if (CollectionUtils.isNotEmpty(valueList)) {
                                List<QmsPqcReportChartVO> subQmsPqcReportChartVOList = valueList.stream().filter(item -> item.getTimeType().equals(timeType))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(subQmsPqcReportChartVOList)) {
                                    QmsPqcReportChartVO qmsPqcReportChartVO = new QmsPqcReportChartVO();
                                    qmsPqcReportChartVO.setAreaId(valueList.get(0).getAreaId());
                                    qmsPqcReportChartVO.setAreaName(valueList.get(0).getAreaName());
                                    qmsPqcReportChartVO.setProcessId(valueList.get(0).getProcessId());
                                    qmsPqcReportChartVO.setProcessName(valueList.get(0).getProcessName());
                                    qmsPqcReportChartVO.setTimeType(timeType);
                                    qmsPqcReportChartVO.setValue(0L);
                                    valueList.add(qmsPqcReportChartVO);
                                }
                            }
                        }
                    }
                }

                valueList.sort(Comparator.comparing(QmsPqcReportChartVO::getTimeType));

                List<Long> longs = new ArrayList<>();
                for (QmsPqcReportChartVO qmsPqcReportChartVO : valueList) {
                    longs.add(qmsPqcReportChartVO.getValue());
                }

                if(StringUtils.isEmpty(dto.getWorkshopId())) {
                    qmsPqcReportChartVO3.setAreaName(valueList.get(0).getAreaName());
                }else {
                    qmsPqcReportChartVO3.setProcessName(valueList.get(0).getProcessName());
                }
                qmsPqcReportChartVO3.setValueList(longs);

                qmsPqcReportChartVO3List.add(qmsPqcReportChartVO3);
            }
        }
        resultV0.setChartList(qmsPqcReportChartVO3List);
        resultV0.setTimeList(timeList);
        return resultV0;
    }
}
