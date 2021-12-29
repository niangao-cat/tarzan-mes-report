package com.ruike.hme.domain.vo;

import lombok.Data;

/**
 * <p>
 * EO工位与不良记录关系
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 15:58
 */
@Data
public class EoWorkcellStationNcInfoVO {
    private String workcellId;
    private String eoId;
    private Long ncRecordCount;
    private String ncRecordId;

    public EoWorkcellStationNcInfoVO() {
    }

    public EoWorkcellStationNcInfoVO(String workcellId, String eoId) {
        this.workcellId = workcellId;
        this.eoId = eoId;
    }
}