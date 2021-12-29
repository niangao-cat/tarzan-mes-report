package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * RowRangeDto
 *
 * @author: chaonan.hu@hand-china.com 2021-04-23
 **/
@Data
public class RowRangeDto implements Serializable {
    private int start;
    private int end;

    public RowRangeDto(int start, int end){
        this.start = start;
        this.end = end;
    }
}
