package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePreparationSurplusChipVO2
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 13:54:12
 **/
@Data
public class HmePreparationSurplusChipVO2 implements Serializable {
    private static final long serialVersionUID = -1099047603899979397L;

    private String oldMaterialLotId;

    private String loadSequence;

    private String creationDate;
}
