package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.HmeNcDetailQuery;
import com.ruike.hme.domain.valueobject.NcRecordVO;
import com.ruike.hme.domain.vo.HmeNcDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工序不良记录 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:09
 */
public interface HmeNcDetailMapper {

    /**
     * 查询列表
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 04:19:19
     */
    List<HmeNcDetailVO> selectList(@Param("tenantId") Long tenantId,
                                   @Param("dto") HmeNcDetailQuery dto);


    /**
     * 获取子记录信息
     *
     * @param tenantId     租户Id
     * @param ncRecordList 不良记录id
     * @return java.util.List<tarzan.method.domain.entity.MtNcRecord>
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/24 14:15
     */
    List<NcRecordVO> querySubCommentsByRecordIds(@Param("tenantId") Long tenantId, @Param("ncRecordList") List<String> ncRecordList);
}