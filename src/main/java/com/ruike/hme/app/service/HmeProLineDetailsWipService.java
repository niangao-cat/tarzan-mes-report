package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO2;
import com.ruike.hme.domain.vo.HmeProductionQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 产线日明细报表-容器应用服务
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:50
 */
public interface HmeProLineDetailsWipService {

    /**
     * Description: 在制查询报表 查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param params      参数视图
     * @return List<HmeProductionQueryDTO>
     * @author bao.xu@hand-china.com 2020/7/13 11:38
     */
    HmeProductionLineDetailsVO2 queryProductDetails(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params);

    /**
     * 在制报表-eo信息
     *
     * @param tenantId          租户id
     * @param pageRequest       分页参数
     * @param params            查询参数
     * @return
     */
    Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);

    /**
     * 在制报表-导出
     *
     * @param tenantId
     * @param params
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/13 18:29
     */
    void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException;
}
