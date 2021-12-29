package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.api.dto.QmsIqcCalSumDTO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineBoardVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * ICQ检验看板Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
public interface QmsIqcExamineBoardMapper {


    /**
     * ICQ检验看板查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcExamineBoardDTO> selectIqcExamineBoard(@Param("tenantId") Long tenantId);

    /**
     * ICQ检验看板30天物料量查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcCalSumDTO> selectIqcDays(@Param("tenantId") Long tenantId);

    /**
     * ICQ检验看板12个月物料量查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcCalSumDTO> selectIqcMonths(@Param("tenantId") Long tenantId);


    List<QmsIqcExamineBoardVO>  ExamineTaskSecondReporQuery(@Param("tenantId") Long tenantId, @Param("locatorCodes") List<String> locatorCodes);

    List<QmsIqcExamineBoardVO>  ExamineTaskFirstReporQuery(@Param("tenantId") Long tenantId, @Param("locatorCodes") List<String> locatorCodes);

    List<QmsIqcExamineBoardVO2>  weekendDataQuery(@Param("tenantId") Long tenantId,
                                                  @Param("qcBys") List<String> qcBys,
                                                  String dayStart,
                                                  String dayEnd);
    List<QmsIqcExamineBoardVO2>  weekendNgDataQuery(@Param("tenantId") Long tenantId,
                                                    @Param("qcBys") List<String> qcBys,
                                                    String dayStart,
                                                    String dayEnd);
    List<QmsIqcExamineBoardVO2> dayNgDataQuery(@Param("tenantId") Long tenantId,
                                               @Param("qcBys") List<String> qcBys);
    List<QmsIqcExamineBoardVO2> dayDataQuery(@Param("tenantId") Long tenantId,
                                             @Param("qcBys") List<String> qcBys);

    List<QmsIqcExamineBoardVO2> mouthNgDataQuery(@Param("tenantId") Long tenantId,
                                                 @Param("qcBys") List<String> qcBys);
    List<QmsIqcExamineBoardVO2> mouthDataQuery(@Param("tenantId") Long tenantId,
                                               @Param("qcBys") List<String> qcBys);

    List<QmsIqcExamineBoardVO3> dayCheckNgQuery(@Param("tenantId") Long tenantId);

}
