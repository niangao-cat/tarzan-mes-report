package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.api.dto.query.HmeProductionFlowQuery;
import com.ruike.hme.api.dto.representation.HmeProductionFlowRepresentation;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 生产流转查询报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 16:02
 */
public interface HmeProductionFlowRepository {
    /**
     * 查询工序流转信息
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     */
    Page<HmeProductionFlowRepresentation> pagedList(Long tenantId, PageRequest pageRequest, HmeProductionFlowQuery dto);

    /**
     * 导出
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     */
    List<HmeProductionFlowRepresentation> export(Long tenantId, HmeProductionFlowQuery dto);

    /**
     * 异步导出
     *
     * @param tenantId 租户ID
     * @param flowQuery      查询参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/11/10 10:10:13
     */
    void asyncExport(Long tenantId, HmeProductionFlowQuery flowQuery) throws IOException;
    /**
     * 创建任务
     *
     * @param tenantId 租户ID
     * @param request  请求
     * @param response 响应
     * @author penglin.sui@hand-china.com 2021/11/10 21:07
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName);

}
