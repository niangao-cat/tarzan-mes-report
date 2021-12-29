package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.*;

import java.util.List;

import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * ICQ检验看板 应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
public interface QmsIqcExamineBoardService {

    /**
     * 获取ICQ检验看板信息
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return List<QmsIqcExamineBoardDTO>
     * @author jiangling.zheng@hand-china.com
     */
    Page<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId, PageRequest pageRequest);

    /**
     * 获取30天物料量
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcMonthDTO>
     * @author jiangling.zheng@hand-china.com
     */
    List<QmsIqcCalSumDTO> selectIqcDayForUi(Long tenantId);

    /**
     * 获取30天物料量
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcMonthDTO>
     * @author jiangling.zheng@hand-china.com
     */
    List<QmsIqcCalSumDTO> selectIqcMonthForUi(Long tenantId);

    /**
     * 待检任务统计
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcExamineBoardVO>
     * @author shicong.li@hand-china.com
     */
    List<QmsIqcExamineBoardVO> iqcExamineTaskReporQuery(Long tenantId);

    /**
     * 检验员检验数据统计
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcExamineBoardVO2>
     * @author shicong.li@hand-china.com
     */
    List<QmsIqcExamineBoardVO2> inspectorDataQuery(Long tenantId);

    /**
     * 当日检验不良信息
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcExamineBoardVO3>
     * @author shicong.li@hand-china.com
     */
    Page<QmsIqcExamineBoardVO3> dayCheckNgQuery(Long tenantId, PageRequest pageRequest);//iqcExamineReportQuery

}
