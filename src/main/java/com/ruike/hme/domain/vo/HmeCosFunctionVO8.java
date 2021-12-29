package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosFunctionVO8
 *
 * @author: chaonan.hu@hand-china.com 2021-05-11
 **/
@Data
public class HmeCosFunctionVO8 implements Serializable {
    private int start;
    private int end;

    public HmeCosFunctionVO8(int start, int end){
        this.start = start;
        this.end = end;
    }
}
