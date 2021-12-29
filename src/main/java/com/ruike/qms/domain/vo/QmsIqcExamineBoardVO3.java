package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QmsIqcExamineBoardVO3 implements Serializable {
    private static final long serialVersionUID = 3206440985160157751L;

    private String iqcNumber;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String supplierName;
    private Long quantity;
    private String remark;

}
