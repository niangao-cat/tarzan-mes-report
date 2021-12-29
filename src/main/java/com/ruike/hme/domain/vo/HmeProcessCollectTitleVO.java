package com.ruike.hme.domain.vo;

import io.choerodon.core.domain.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工序采集项报表 带标题展现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:50
 */
@Data
public class HmeProcessCollectTitleVO implements Serializable {
    private static final long serialVersionUID = -6270577347186507345L;

    private List<String> dynamicTitles;
    private Page<HmeProcessCollectVO> page;
    private Page<HmeProcessCollectVO2> gpPage;

    public HmeProcessCollectTitleVO() {
        dynamicTitles = new ArrayList<>();
        this.page = new Page<>();
        this.gpPage = new Page<>();
    }

    public HmeProcessCollectTitleVO(Page<HmeProcessCollectVO> page , Page<HmeProcessCollectVO2> gpPage) {
        dynamicTitles = new ArrayList<>();
        this.page = page;
        this.gpPage = gpPage;
    }
}
