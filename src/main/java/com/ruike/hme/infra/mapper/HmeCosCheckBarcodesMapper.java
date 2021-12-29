package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO2;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName HmeCosCheckBarcodesMapper
 * @Description cos目检条码表
 * @Author li.zhang 2021/01/19 12:30
 */
public interface HmeCosCheckBarcodesMapper {

    List<HmeCosCheckBarcodesVO> selectCheckBarcodes(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);

    List<HmeCosCheckBarcodesVO> selectCheckBarcodes2(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);

    /**
     * 批量根据jobId查询设备编码
     *
     * @param tenantId 租户ID
     * @param jobIdList jobId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/21 05:06:20
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO2>
     */
    List<HmeCosCheckBarcodesVO2> bacthGetAssetEncodingByJob(@Param("tenantId") String tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 批量根据loadSequence查询测试机台
     *
     * @param tenantId 租户ID
     * @param loadSequenceList loadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/21 05:14:54
     * @return java.util.List<HmeCosCheckBarcodesVO3>
     */
    List<HmeCosCheckBarcodesVO3> bacthGetBenchByLoadSequence(@Param("tenantId") String tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 批量根据loadSequence查询实验代码
     *
     * @param tenantId 租户ID
     * @param loadSequenceList loadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/21 05:14:54
     * @return java.util.List<HmeCosCheckBarcodesVO3>
     */
    List<HmeCosCheckBarcodesVO3> bacthGetLabCodeByLoadSequence(@Param("tenantId") String tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 导出时拆分SQL-第一种情况
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/22 09:17:58
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO>
     */
    List<HmeCosCheckBarcodesVO> selectCheckBarcodesOne(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);

    /**
     * 导出时拆分SQL-第二种情况
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/22 09:17:58
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO>
     */
    List<HmeCosCheckBarcodesVO> selectCheckBarcodesTwo(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);

}
