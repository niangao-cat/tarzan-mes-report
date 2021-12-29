package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QmsIqcExamineBoardVO2 implements Serializable {
    private static final long serialVersionUID = 4251953970232823178L;

    private Long weekendNum;
    private Long dayNum;
    private Long mouthNum;
    private Long weekendNgNum;
    private Long dayNgNum;
    private Long mouthNgNum;
    private String qcBy;
    private String qcByName;
    private String qcByCode;
}
