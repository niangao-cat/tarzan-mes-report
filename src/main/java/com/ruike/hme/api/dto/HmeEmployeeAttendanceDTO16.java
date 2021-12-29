package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEmployeeAttendanceDTO16
 *
 * @author: chaonan.hu@hand-china.com 2021/07/19 11:22
 **/
@Data
public class HmeEmployeeAttendanceDTO16 implements Serializable {
    private static final long serialVersionUID = 7306005173632913512L;

    private String shiftDate;

    private String shiftCode;

    private Long unitId;

    private Integer actualAttendance;
}
