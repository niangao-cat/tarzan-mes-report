package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeEmployeeAttendanceExportVO7
 *
 * @author: chaonan.hu@hand-china.com 2021/07/19 13:58
 **/
@Data
public class HmeEmployeeAttendanceExportVO7 implements Serializable {
    private static final long serialVersionUID = -3827168148060072408L;

    private String wkcShiftId;

    private String workId;

    private BigDecimal qty;
}
