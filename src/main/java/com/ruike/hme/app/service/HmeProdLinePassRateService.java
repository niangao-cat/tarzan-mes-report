package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 产品直通率报表表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-02-26 10:22:12
 */
public interface HmeProdLinePassRateService {

    /**
     * 产品直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:12:02
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO5
     */
    HmeProdLinePassRateVO5 prodLinePassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto);

    /**
     * 产品直通率报表导出
     *
     * @param tenantId 租户ID
     * @param params 查询条件
     * @param response
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 03:18:22
     * @return void
     */
    void prodLinePassRateExport(Long tenantId, HmeProdLinePassRateDTO params, HttpServletResponse response) throws IOException;

    /**
     * 产品直通率报表异步导出
     *
     * @param tenantId
     * @param dto
     * @param request
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/29
     */
    void asyncOnlineReportExport(Long tenantId, HmeProdLinePassRateDTO dto, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 产品日直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/2 10:13:49
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO8
     */
    HmeProdLinePassRateVO8 prodLineDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException;

    /**
     * 产品日直通率报表导出
     *
     * @param tenantId 租户ID
     * @param params 查询条件
     * @param response
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 03:18:22
     * @return void
     */
    void prodLineDayPassRateExport(Long tenantId, HmeProdLinePassRateDTO2 params, HttpServletResponse response) throws IOException, ParseException;

    /**
     * 产品日直通率报表异步导出
     *
     * @param tenantId
     * @param dto
     * @param request
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/29
     */
    void asyncProdLineDayPassRateExport(Long tenantId, HmeProdLinePassRateDTO2 dto, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 创建任务
     *
     * @param tenantId 租户ID
     * @param request  请求
     * @param response 响应
     * @param taskName 任务名
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/10 02:54:36
     * @return com.ruike.hme.domain.vo.HmeExportTaskVO
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String taskName);
}
