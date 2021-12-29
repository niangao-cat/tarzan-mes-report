package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.CosCompletionDetailQuery;
import com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * COS完工芯片明细报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 15:40
 */
public interface CosCompletionDetailRepository {

    /**
     * 查询分页列表
     *
     * @param query       查询条件
     * @param pageRequest 分页
     * @return java.util.List<com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 03:28:19
     */
    Page<CosCompletionDetailRepresentation> pagedList(CosCompletionDetailQuery query, PageRequest pageRequest);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 03:28:19
     */
    List<CosCompletionDetailRepresentation> list(CosCompletionDetailQuery query);
}
