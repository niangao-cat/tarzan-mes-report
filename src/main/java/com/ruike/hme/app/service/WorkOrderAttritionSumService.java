package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * <p>
 * 工单损耗汇总报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 16:17
 */
public interface WorkOrderAttritionSumService {

    /**
     * 分页查询
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.api.dto.WorkOrderLossSumRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 10:43:32
     */
    Page<WorkOrderAttritionSumRepresentationDTO> page(Long tenantId,
                                                      WorkOrderAttritionSumQueryDTO dto,
                                                      PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param exportParam 导出参数
     * @return java.util.List<com.ruike.hme.api.dto.WorkOrderLossSumRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 10:43:32
     */
    List<WorkOrderAttritionSumRepresentationDTO> export(Long tenantId,
                                                        WorkOrderAttritionSumQueryDTO dto,
                                                        ExportParam exportParam);
}
