package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HmeCosFunctionVO10 implements Serializable {
    private static final long serialVersionUID = -7297012750987169012L;

    List<HmeCosFunctionVO2> cosFunctionVO2List;

    Map<String , HmeCosFunctionVO4> cosFunctionVO4Map;
}
