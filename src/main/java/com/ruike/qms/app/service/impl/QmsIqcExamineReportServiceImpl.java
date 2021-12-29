package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.app.service.QmsIqcExamineReportService;
import com.ruike.qms.domain.repository.QmsIqcExamineReportRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.util.DateUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ICQ检验报表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
@Service
public class QmsIqcExamineReportServiceImpl implements QmsIqcExamineReportService {

    @Autowired
    private QmsIqcExamineReportRepository qmsIqcExamineReportRepository;

    @Override
    public Page<QmsIqcExamineReportVO> iqcExamineReportQuery(Long tenantId, QmsIqcExamineReportDTO dto, PageRequest pageRequest) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateFromStr())){
            String inspectionFinishDateFromStr = dto.getInspectionFinishDateFromStr() + " 00:00:00";
            Date inspectionFinishDateFrom = DateUtil.string2Date(inspectionFinishDateFromStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateToStr())){
            String inspectionFinishDateToStr = dto.getInspectionFinishDateToStr() + " 23:59:59";
            Date inspectionFinishDateTo = DateUtil.string2Date(inspectionFinishDateToStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        }
        return qmsIqcExamineReportRepository.iqcExamineReportQuery(tenantId, dto, pageRequest);
    }

    @Override
    public QmsIqcExamineReportVO2 iqcExaminePieChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateFromStr())){
            String inspectionFinishDateFromStr = dto.getInspectionFinishDateFromStr() + " 00:00:00";
            Date inspectionFinishDateFrom = DateUtil.string2Date(inspectionFinishDateFromStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateToStr())){
            String inspectionFinishDateToStr = dto.getInspectionFinishDateToStr() + " 23:59:59";
            Date inspectionFinishDateTo = DateUtil.string2Date(inspectionFinishDateToStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        }
        return qmsIqcExamineReportRepository.iqcExaminePieChartQuery(tenantId, dto);
    }

    @Override
    public QmsIqcExamineReportVO3 iqcExamineLineChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        if(StringUtils.isEmpty(dto.getInspectionFinishDateFromStr())){
            throw new CommonException("输入参数检验时间从为空,请检查!");
        }
        if(StringUtils.isEmpty(dto.getInspectionFinishDateToStr())){
            throw new CommonException("输入参数检验时间至为空,请检查!");
        }
        Date inspectionFinishDateFrom = DateUtil.string2Date(dto.getInspectionFinishDateFromStr(), "yyyy-MM-dd");
        Date inspectionFinishDateTo = DateUtil.string2Date(dto.getInspectionFinishDateToStr(), "yyyy-MM-dd");
        dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        return qmsIqcExamineReportRepository.iqcExamineLineChartQuery(tenantId, dto);
    }

    @Override
    public List<QmsIqcExamineReportVO> iqcExamineReportExport(Long tenantId, QmsIqcExamineReportDTO examineReportDTO) {
        return qmsIqcExamineReportRepository.iqcExamineReportExport(tenantId, examineReportDTO);
    }
}
