package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * 返修产品直通率报表表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-05-19 11:14:12
 */
public interface HmeRepairProductPassRateService {

    /**
     * 返修产品直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/19 14:39:02
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO5
     */
    HmeProdLinePassRateVO5 repairProductPassRateQuery(Long tenantId, HmeProdLinePassRateDTO dto);

    /**
     * 返修产品直通率报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param response
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 03:18:22
     * @return void
     */
    void repairProductPassRateExport(Long tenantId, HmeProdLinePassRateDTO dto, HttpServletResponse response) throws IOException;

    /**
     * 返修产品直通率报表异步导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param request  请求
     * @param response 响应
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/10 04:49:02
     * @return void
     */
    void repairProductPassRateAsyncExport(Long tenantId, HmeProdLinePassRateDTO dto, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 返修产品日直通率报表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/20 13:59:01
     * @return com.ruike.hme.domain.vo.HmeProdLinePassRateVO8
     */
    HmeProdLinePassRateVO8 repairProductDayPassRateQuery(Long tenantId, HmeProdLinePassRateDTO2 dto) throws ParseException;

    /**
     * 返修产品日直通率报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param response
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 03:18:22
     * @return void
     */
    void repairProductDayPassRateExport(Long tenantId, HmeProdLinePassRateDTO2 dto, HttpServletResponse response) throws IOException, ParseException;

    /**
     * 返修产品日直通率报表异步导出
     * 
     * @param tenantId  租户ID
     * @param dto  查询条件
     * @param request  请求
     * @param response 响应
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/10 05:27:38 
     * @return void
     */
    void repairProductDayPassRateAsyncExport(Long tenantId, HmeProdLinePassRateDTO2 dto, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException;

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
