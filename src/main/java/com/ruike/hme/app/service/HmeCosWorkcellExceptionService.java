package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.query.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * COS工位加工异常汇总表 应用服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 10:57
 */
public interface HmeCosWorkcellExceptionService {
    /**
     * COS工位加工异常汇总表查询
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param pageRequest 分页
     * @return com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     */
    Page<HmeCosWorkcellExceptionVO> queryList(Long tenantId, HmeCosWorkcellExceptionDTO dto, PageRequest pageRequest);

    /**
     * COS工位加工异常汇总表导出
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 02:15:03
     */
    List<HmeCosWorkcellExceptionVO> export(Long tenantId, HmeCosWorkcellExceptionDTO dto);
}
