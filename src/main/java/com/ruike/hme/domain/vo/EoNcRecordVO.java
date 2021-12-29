package com.ruike.hme.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * EO与不良记录关系
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/19 12:47
 */
@Data
public class EoNcRecordVO {
    private String eoId;
    private String ncRecordId;
    private Date ncDate;
    private String latestNcTag;

}
