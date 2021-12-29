package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.domain.valueobject.HmeEquipment;
import com.ruike.hme.domain.vo.HmeCosQuantityVO;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO4;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS工位加工异常汇总表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 13:01
 */
public interface HmeCosWorkcellExceptionMapper {

    /**
     * COS工位加工异常汇总表查询
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     */
    List<HmeCosWorkcellExceptionVO> queryList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosWorkcellExceptionDTO dto);

    /**
     * COS工位加工异常汇总表 设备编码、设别描述 查询
     *
     * @param tenantId
     * @param jobId
     * @return com.ruike.hme.domain.entity.HmeEquipment
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     */
    List<HmeEquipment> queryEquipment(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * COS工位加工异常汇总表 设备编码、设别描述 批量查询
     *
     * @param tenantId
     * @param jobIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO4>
     * @author penglin.sui@HAND-CHINA.COM 2021-03-12 18:21
     */
    List<HmeEquipmentVO4> batchQueryEquipment(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * COS工位加工异常汇总表 不良总数 查询
     *
     * @param hmeCosQuantityVO
     * @param tenantId
     * @return com.ruike.hme.domain.vo.HmeCosQuantityVO;
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     */
    HmeCosQuantityVO queryQuantity(@Param("tenantId") Long tenantId, @Param("hmeCosQuantityVO") HmeCosQuantityVO hmeCosQuantityVO);

    /**
     * COS工位加工异常汇总表 不良总数 查询
     *
     * @param tenantId 租户
     * @param ncQuery  查询
     * @return com.ruike.hme.domain.vo.HmeCosQuantityVO;
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     */
    List<HmeCosQuantityVO> batchQueryQuantity(@Param("tenantId") Long tenantId,
                                              @Param("list") List<HmeCosQuantityVO> ncQuery);
}
