package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.ServiceReturnCheckQueryDTO;
import com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * <p>
 * 售后退库检测报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 14:10
 */
public interface ServiceReturnCheckService {

    /**
     * 查询列表
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 11:21:32
     */
    Page<ServiceReturnCheckRepresentationDTO> page(Long tenantId,
                                                   ServiceReturnCheckQueryDTO dto,
                                                   PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param exportParam 导出参数
     * @return java.util.List<com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 11:21:32
     */
    List<ServiceReturnCheckRepresentationDTO> export(Long tenantId,
                                                     ServiceReturnCheckQueryDTO dto,
                                                     ExportParam exportParam);
}
