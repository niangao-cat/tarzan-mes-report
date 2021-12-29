package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;

import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:17
 */
public interface QmsIqcExamineBoardRepository extends AopProxy<QmsIqcExamineBoardRepository> {

    /**
     * 获取ICQ检验看板信息
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @return List<QmsIqcExamineBoardDTO>
     */
    List<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId);



    /**
     * IQC待检任务报表
     *
     * @param tenantId
     * @author shicong.li@hand-china.com 2021/5/18 14:52
     * @return java.util.List<QmsIqcExamineBoardVO>
     */
    List<QmsIqcExamineBoardVO> iqcExamineTaskReporQuery(Long tenantId);

    /**
     * 检验员检验数据
     *
     * @param tenantId
     * @author shicong.li@hand-china.com 2021/5/18 14:52
     * @return java.util.List<QmsIqcExamineBoardVO2>
     */
    List<QmsIqcExamineBoardVO2> inspectorDataQuery(Long tenantId);

    Map<String,String> getWeekDate();

    /**
     * 本日检验不良信息
     *
     * @param tenantId
     * @author shicong.li@hand-china.com 2021/5/18 14:52
     * @return java.util.List<QmsIqcExamineBoardVO3>
     */
    Page<QmsIqcExamineBoardVO3> dayCheckNgQuery(Long tenantId, PageRequest pageRequest);
}
