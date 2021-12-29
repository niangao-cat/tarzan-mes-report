package com.ruike.qms.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.app.service.QmsPqcReportService;
import com.ruike.qms.domain.repository.QmsPqcReportRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.mapper.QmsPqcReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.common.domain.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡检报表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
@Service
public class QmsPqcReportServiceImpl implements QmsPqcReportService {

    @Autowired
    private QmsPqcReportRepository qmsPqcReportRepository;
    @Autowired
    private QmsPqcReportMapper qmsPqcReportMapper;

    @Override
    public Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
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
        return qmsPqcReportRepository.pqcReportHeadDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
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
        return qmsPqcReportRepository.pgcReportDetailDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public List<QmsPqcReportVO3> pqcReportHeadDataExportByDepartment(Long tenantId, QmsPqcReportDTO dto) {
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
        return qmsPqcReportMapper.pqcReportHeadDataExportByDepartment(tenantId, dto);
    }

    @Override
    public List<QmsPqcReportVO4> pqcReportHeadDataExportByWorkshop(Long tenantId, QmsPqcReportDTO dto) {
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
        return qmsPqcReportMapper.pqcReportHeadDataExportByWorkshop(tenantId, dto);
    }

    @Override
    public List<QmsPqcReportVO6> pqcReportHeadDataExport(Long tenantId, QmsPqcReportDTO dto) {
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
        List<QmsPqcReportVO6> qmsPqcReportVO6s = qmsPqcReportMapper.pqcReportHeadDataExport(tenantId, dto);
        if(CollectionUtil.isNotEmpty(qmsPqcReportVO6s)){
            //查询到的不同的车间ID,进而去查询各个车间的不合格数
            List<String> areaIdList = qmsPqcReportVO6s.stream().map(QmsPqcReportVO6::getAreaId).distinct().collect(Collectors.toList());
            List<QmsPqcReportVO> workshopNcNumList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(areaIdList)){
                dto.setWorkshopIdList(areaIdList);
                workshopNcNumList = qmsPqcReportMapper.workshopNcNumQuery(tenantId, dto);
            }
            //查询到的不同的工序ID,进而去查询各个工序的不合格数
            List<String> processIdList = qmsPqcReportVO6s.stream().map(QmsPqcReportVO6::getProcessId).distinct().collect(Collectors.toList());
            List<QmsPqcReportVO> processNcNumList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(processIdList)){
                dto.setProcessIdList(processIdList);
                processNcNumList = qmsPqcReportMapper.processNcNumQuery(tenantId, dto);
            }
            for (QmsPqcReportVO6 qmsPqcReportVO6:qmsPqcReportVO6s) {
                if(CollectionUtil.isNotEmpty(workshopNcNumList)){
                    List<QmsPqcReportVO> workshopNcNum = workshopNcNumList.stream().filter(item -> item.getAreaId().equals(qmsPqcReportVO6.getAreaId())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(workshopNcNum)){
                        qmsPqcReportVO6.setAreaNcNum(workshopNcNum.get(0).getAreaNcNum());
                    }
                }
                if(CollectionUtil.isNotEmpty(processNcNumList)){
                    List<QmsPqcReportVO> processNcNum = processNcNumList.stream().filter(item -> item.getProcessId().equals(qmsPqcReportVO6.getProcessId())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(processNcNum)){
                        qmsPqcReportVO6.setProcessNcNum(processNcNum.get(0).getProcessNcNum());
                    }
                }
            }
        }
        return qmsPqcReportVO6s;
    }

    @Override
    public List<QmsPqcReportVO5> pgcReportDetailDataExport(Long tenantId, QmsPqcReportDTO dto) {
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
        List<QmsPqcReportVO5> qmsPqcReportVO5s = qmsPqcReportMapper.pgcReportDetailDataExport(tenantId, dto);
        for (QmsPqcReportVO5 qmsPqcReportVO5:qmsPqcReportVO5s) {
            String inspectionFinishDateStr = DateUtil.date2String(qmsPqcReportVO5.getInspectionFinishDate(), "yyyy-MM-dd HH:mm:ss");
            qmsPqcReportVO5.setInspectionFinishDateStr(inspectionFinishDateStr);
        }
        return qmsPqcReportVO5s;
    }
}
