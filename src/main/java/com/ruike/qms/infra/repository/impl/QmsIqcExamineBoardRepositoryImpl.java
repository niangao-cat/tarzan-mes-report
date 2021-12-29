package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.domain.repository.QmsIqcExamineBoardRepository;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO3;
import com.ruike.qms.infra.mapper.QmsIqcExamineBoardMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtUserClient;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:18
 */
@Component
public class QmsIqcExamineBoardRepositoryImpl implements QmsIqcExamineBoardRepository {

    @Autowired
    private QmsIqcExamineBoardMapper qmsIqcExamineBoardMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public List<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId) {
        List<QmsIqcExamineBoardDTO> list = qmsIqcExamineBoardMapper.selectIqcExamineBoard(tenantId);
        list.forEach(dto -> {
            dto.setQcByName(userClient.userInfoGet(tenantId, dto.getQcBy()).getRealName());
        });
        return list;
    }


    @Override
    public List<QmsIqcExamineBoardVO> iqcExamineTaskReporQuery(Long tenantId){
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("QMS.LOCATOR",tenantId);
        List<String> locatorCodes = new ArrayList<>();
        locatorCodes = lovValueDTOS.stream().map(LovValueDTO::getValue)
                .collect(Collectors.toList());
        List<QmsIqcExamineBoardVO> firstReports = qmsIqcExamineBoardMapper.ExamineTaskFirstReporQuery(tenantId,locatorCodes);
        List<QmsIqcExamineBoardVO> secondReports = qmsIqcExamineBoardMapper.ExamineTaskSecondReporQuery(tenantId,locatorCodes);
        List<QmsIqcExamineBoardVO> returns = new ArrayList<>();
        Long total = 0L;
        for(LovValueDTO lov:lovValueDTOS){
            QmsIqcExamineBoardVO qmsIqcExamineBoardVO = new QmsIqcExamineBoardVO();
            List<QmsIqcExamineBoardVO> firsts =firstReports.stream().filter(v->v.getLocatorCode().equals(lov.getValue())).collect(Collectors.toList());
            List<QmsIqcExamineBoardVO> seconds =secondReports.stream().filter(v->v.getLocatorCode().equals(lov.getValue())).collect(Collectors.toList());
            String locatorId = "";
            Long first = 0L;
            Long second = 0L;
            if(!(CollectionUtils.isEmpty(firsts) && CollectionUtils.isEmpty(seconds))){
                //-取消：都为空，退出本层循环continue;
                locatorId = CollectionUtils.isEmpty(firsts)?seconds.get(0).getLocatorId():firsts.get(0).getLocatorId();
                first = CollectionUtils.isEmpty(firsts)?0L:firsts.get(0).getTaskCount();
                second = CollectionUtils.isEmpty(seconds)?0L:seconds.get(0).getTaskCount();
            }

            qmsIqcExamineBoardVO.setLocatorId(locatorId);
            qmsIqcExamineBoardVO.setLocatorCode(lov.getValue());
            qmsIqcExamineBoardVO.setLocatorName(lov.getMeaning());
            qmsIqcExamineBoardVO.setTaskCount(first+second);
            total= total+first+second;
            returns.add(qmsIqcExamineBoardVO);
        }
        QmsIqcExamineBoardVO totalData = new QmsIqcExamineBoardVO();
        totalData.setLocatorName("合计/批");
        totalData.setTaskCount(total);
        returns.add(totalData);
        return returns;
    }

    @Override
    public List<QmsIqcExamineBoardVO2> inspectorDataQuery(Long tenantId){
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("QMS.USER",tenantId);

        List<String> bys = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        Map<String,String> currentMouth = this.getWeekDate();
        List<QmsIqcExamineBoardVO2> weekends = qmsIqcExamineBoardMapper.weekendDataQuery(tenantId,bys,currentMouth.get("mondayDate"),currentMouth.get("weekEnd"));
        List<QmsIqcExamineBoardVO2> weekendNgs = qmsIqcExamineBoardMapper.weekendNgDataQuery(tenantId,bys,currentMouth.get("mondayDate"),currentMouth.get("weekEnd"));
        List<QmsIqcExamineBoardVO2> days = qmsIqcExamineBoardMapper.dayDataQuery(tenantId,bys);
        List<QmsIqcExamineBoardVO2> dayNgs = qmsIqcExamineBoardMapper.dayNgDataQuery(tenantId,bys);
        List<QmsIqcExamineBoardVO2> mouths = qmsIqcExamineBoardMapper.mouthDataQuery(tenantId,bys);
        List<QmsIqcExamineBoardVO2> mouthNgs = qmsIqcExamineBoardMapper.mouthNgDataQuery(tenantId,bys);
        List<QmsIqcExamineBoardVO2> qms = new ArrayList<>();
        for(LovValueDTO lov :lovValueDTOS){
            QmsIqcExamineBoardVO2 qm= new QmsIqcExamineBoardVO2();
            qm.setQcByCode(lov.getValue());
            qm.setQcByName(lov.getMeaning());

            List<QmsIqcExamineBoardVO2> weekend =weekends.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());
            qm.setWeekendNum(CollectionUtils.isEmpty(weekend)?0L:weekend.get(0).getWeekendNum());

            List<QmsIqcExamineBoardVO2> weekendNg =weekendNgs.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());
            qm.setWeekendNgNum(CollectionUtils.isEmpty(weekendNg)?0L:weekendNg.get(0).getWeekendNgNum());

            List<QmsIqcExamineBoardVO2> day =days.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());
            qm.setDayNum(CollectionUtils.isEmpty(day)?0L:day.get(0).getDayNum());

            List<QmsIqcExamineBoardVO2> dayNg =dayNgs.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());
            qm.setDayNgNum(CollectionUtils.isEmpty(dayNg)?0L:dayNg.get(0).getDayNgNum());

            List<QmsIqcExamineBoardVO2> mouth =mouths.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());
            qm.setMouthNum(CollectionUtils.isEmpty(mouth)?0L:mouth.get(0).getMouthNum());

            List<QmsIqcExamineBoardVO2> mouthNg = mouthNgs.stream().filter(v->v.getQcBy().equals(lov.getValue())).collect(Collectors.toList());

            qm.setMouthNgNum(CollectionUtils.isEmpty(mouthNg)?0L:mouthNg.get(0).getMouthNgNum());
           // qm.setQcBy(CollectionUtils.isEmpty(day)?dayNg.get(0).getQcBy():day.get(0).getQcBy());
            qms.add(qm);
        }

        return qms;
    }

    @Override
    public Map<String,String> getWeekDate() {
        Map<String,String> map = new HashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);

        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);

        //当前时间
        Date today = new Date();
        String todys1 = sdf.format(today);
        if(!todys1.substring(0,7) .equals(weekBegin.substring(0,7))){
            map.put("mondayDate", null);
        }
        if(!todys1.substring(0,7) .equals(weekEnd.substring(0,7))){
            map.put("weekEnd", null);
        }
        return map;
    }
    @Override
    public Page<QmsIqcExamineBoardVO3> dayCheckNgQuery(Long tenantId, PageRequest pageRequest){
        Page<QmsIqcExamineBoardVO3> resultPage = PageHelper.doPage(pageRequest, () -> qmsIqcExamineBoardMapper.dayCheckNgQuery(tenantId));
        return resultPage;
        //return qmsIqcExamineReportMapper.dayCheckNgQuery(tenantId);
    }
}
