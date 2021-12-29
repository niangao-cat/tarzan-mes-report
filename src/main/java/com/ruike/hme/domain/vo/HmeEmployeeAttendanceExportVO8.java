package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeEmployeeAttendanceExportVO8
 *
 * @author: chaonan.hu@hand-china.com 2021/07/19 13:58
 **/
@Data
public class HmeEmployeeAttendanceExportVO8 implements Serializable {
    private static final long serialVersionUID = -527235026795804753L;

    private String employeeId;

    private String workcellId;

    private String materialId;

    private BigDecimal qty;

    private String jobId;

    private String eoId;

    private String reworkFlag;

    private Date operationDate;

    private String ncRecordId;
}
