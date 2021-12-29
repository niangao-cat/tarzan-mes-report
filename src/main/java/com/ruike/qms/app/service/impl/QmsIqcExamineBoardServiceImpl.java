package com.ruike.qms.app.service.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsIqcExamineBoardService;
import com.ruike.qms.domain.repository.QmsIqcExamineBoardRepository;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO3;
import com.ruike.qms.infra.mapper.QmsIqcExamineBoardMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * ICQ检验看板应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@Service
public class QmsIqcExamineBoardServiceImpl implements QmsIqcExamineBoardService {

    @Autowired
    private QmsIqcExamineBoardMapper qmsIqcExamineBoardMapper;

    @Autowired
    private QmsIqcExamineBoardRepository qmsIqcExamineBoardRepository;

    @Override
    @ProcessLovValue
    public Page<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> qmsIqcExamineBoardRepository.selectIqcExamineBoardForUi(tenantId));
    }

    @Override
    public List<QmsIqcCalSumDTO> selectIqcDayForUi(Long tenantId) {
        List<QmsIqcCalSumDTO> resultDto = new ArrayList<>();

        //获取当前日期(结束日期)
        Date currDate = CommonUtils.currentTimeGet();

        //开始日期
        Date preThirtyDate = CommonUtils.subDate(currDate , -29, "yyyy-MM-dd" , Calendar.DATE);

        Date beginDate = preThirtyDate;

        Calendar dd = Calendar.getInstance();

        dd.setTime(preThirtyDate);

        while (beginDate.getTime() < currDate.getTime()) {

            QmsIqcCalSumDTO qmsIqcCalSumDTO = new QmsIqcCalSumDTO();
            qmsIqcCalSumDTO.setShiftDate(CommonUtils.dateToString(beginDate , "yyyy-MM-dd"));
            qmsIqcCalSumDTO.setTotalQty(BigDecimal.ZERO);
            resultDto.add(qmsIqcCalSumDTO);
            //天数加上1
            System.out.println("beginDate:" + CommonUtils.dateToString(beginDate , "yyyy-MM-dd"));
            dd.add(Calendar.DAY_OF_MONTH, 1);
            beginDate = dd.getTime();
        }

        System.out.println("selectIqcDayForUi resultDto.size : " + resultDto.size());

        //填充日期
        List<QmsIqcCalSumDTO> qmsIqcCalSumDTOList = qmsIqcExamineBoardMapper.selectIqcDays(tenantId);
        Map<String , BigDecimal> qmsIqcCalSumDTOMap = new HashMap<>();
        qmsIqcCalSumDTOList.forEach(item -> {
            qmsIqcCalSumDTOMap.put(item.getShiftDate() , item.getTotalQty());
        });
        resultDto.forEach(item ->{
            BigDecimal totalQty = qmsIqcCalSumDTOMap.getOrDefault(item.getShiftDate() , BigDecimal.ZERO);
            if(totalQty.compareTo(BigDecimal.ZERO) > 0){
                item.setTotalQty(totalQty);
            }
        });

        return resultDto;
    }

    @Override
    public List<QmsIqcCalSumDTO> selectIqcMonthForUi(Long tenantId) {
        List<QmsIqcCalSumDTO> resultDto = new ArrayList<>();

        //获取当前日期(结束月份)
        Date currDate = CommonUtils.currentTimeGet();

        //开始月份
        Date preThirtyDate = CommonUtils.subDate(currDate , -11, "yyyy-MM-dd" , Calendar.MONTH);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(preThirtyDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(currDate);

        while (beginCalendar.compareTo(endCalendar) < 0) {

            QmsIqcCalSumDTO qmsIqcCalSumDTO = new QmsIqcCalSumDTO();
            qmsIqcCalSumDTO.setShiftDate(CommonUtils.dateToString(beginCalendar.getTime() , "yyyy-MM"));
            qmsIqcCalSumDTO.setTotalQty(BigDecimal.ZERO);
            resultDto.add(qmsIqcCalSumDTO);
            System.out.println("beginCalendar.getTime():" + CommonUtils.dateToString(beginCalendar.getTime() , "yyyy-MM-dd"));
            beginCalendar.add(Calendar.MONTH, 1);
        }

        System.out.println("selectIqcMonthForUi resultDto.size : " + resultDto.size());

        //填充日期
        List<QmsIqcCalSumDTO> qmsIqcCalSumDTOList = qmsIqcExamineBoardMapper.selectIqcMonths(tenantId);
        Map<String , BigDecimal> qmsIqcCalSumDTOMap = new HashMap<>();
        qmsIqcCalSumDTOList.forEach(item -> {
            qmsIqcCalSumDTOMap.put(item.getShiftDate() , item.getTotalQty());
        });
        resultDto.forEach(item ->{
            BigDecimal totalQty = qmsIqcCalSumDTOMap.getOrDefault(item.getShiftDate() , BigDecimal.ZERO);
            if(totalQty.compareTo(BigDecimal.ZERO) > 0){
                item.setTotalQty(totalQty);
            }
        });

        return resultDto;
    }


    @Override
    public List<QmsIqcExamineBoardVO> iqcExamineTaskReporQuery(Long tenantId){
        return qmsIqcExamineBoardRepository.iqcExamineTaskReporQuery(tenantId);
        // return null;
    }

    @Override
    public List<QmsIqcExamineBoardVO2> inspectorDataQuery(Long tenantId){

        return qmsIqcExamineBoardRepository.inspectorDataQuery(tenantId);
    }
    @Override
    public Page<QmsIqcExamineBoardVO3> dayCheckNgQuery(Long tenantId, PageRequest pageRequest){

        return qmsIqcExamineBoardRepository.dayCheckNgQuery(tenantId,pageRequest);
    }


}
