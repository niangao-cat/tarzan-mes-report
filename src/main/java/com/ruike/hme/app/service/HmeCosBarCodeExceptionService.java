package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 *COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:28
 */
public interface HmeCosBarCodeExceptionService {
    /**
     *
     * COS条码加工异常汇总报表
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:26
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
     */
    Page<HmeCosBarCodeExceptionVO> queryList(Long tenantId, HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest);

    /**
     * COS条码加工异常汇总报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/6 15:29:49
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO>
     */
    List<HmeCosBarCodeExceptionVO> listForExport(Long tenantId, HmeCosBarCodeExceptionDTO dto);
}
