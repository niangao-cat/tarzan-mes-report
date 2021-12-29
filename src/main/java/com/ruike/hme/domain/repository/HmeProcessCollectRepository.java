package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProcessCollectTitleVO;
import com.ruike.hme.domain.vo.HmeProcessJobDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 工序采集项报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:08
 */
public interface HmeProcessCollectRepository {

    /**
     * 分页查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessCollectVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:10:13
     */
    HmeProcessCollectTitleVO pagedList(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest);

    /**
     * 查询采集项job详情列表
     *
     * @param tenantId    租户
     * @param jobId       job
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessJobDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 02:03:14
     */
    Page<HmeProcessJobDetailVO> pagedJobList(Long tenantId,
                                             String jobId,
                                             PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @param request  请求
     * @param response 响应
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:10:13
     */
    void export(Long tenantId, HmeProcessCollectQuery dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 异步导出
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 10:10:13
     */
    void asyncExport(Long tenantId, HmeProcessCollectQuery dto, List<LovValueDTO> qualityStatusLov, List<LovValueDTO> flagYnLov) throws IOException;
    /**
     * 创建任务
     *
     * @param tenantId 租户ID
     * @param request  请求
     * @param response 响应
     * @author penglin.sui@hand-china.com 2021/8/6 21:07
     */
    HmeExportTaskVO createTask(Long tenantId, HttpServletRequest request, HttpServletResponse response, String fileName);

    /**
     * 分页查询-GP
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessCollectVO>
     * @author penglin.sui@hand-china.com 2021/4/15 10:10:13
     */
    HmeProcessCollectTitleVO gpPagedList(Long tenantId, HmeProcessCollectQuery dto, PageRequest pageRequest);

    /**
     * 导出-GP
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @param request  请求
     * @param response 响应
     * @author penglin.sui@hand-china.com 2021/9/13 19:38
     */
    void gpExport(Long tenantId, HmeProcessCollectQuery dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 异步导出-GP
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @author penglin.sui@hand-china.com 2021/9/13 20:14
     */
    void gpAsyncExport(Long tenantId, HmeProcessCollectQuery dto) throws IOException;
}
