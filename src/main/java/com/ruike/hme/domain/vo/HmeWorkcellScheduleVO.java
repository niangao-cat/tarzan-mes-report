package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeWorkcellScheduleVO
 *
 * @author: chaonan.hu@hand-china.com 2021/4/25 17:25
 **/
@Data
public class HmeWorkcellScheduleVO implements Serializable {
    private static final long serialVersionUID = -4679898877939247199L;

    private Double rate;

    private String rateType;

    private Double activity;
}
