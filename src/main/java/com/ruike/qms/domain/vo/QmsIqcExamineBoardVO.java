package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QmsIqcExamineBoardVO implements Serializable {

    private static final long serialVersionUID = -3957922826671386687L;

    //TO_LOCATOR_ID
    private String locatorId;
    private String locatorCode;
    private String locatorName;
    private Long taskCount;

}
