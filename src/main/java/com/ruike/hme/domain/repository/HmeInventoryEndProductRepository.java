package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeInventoryEndProductVO;
import com.ruike.hme.domain.vo.HmeInventoryEndProductVO2;
import com.ruike.hme.domain.vo.HmeInventoryEndProductVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 售后在制品盘点-成品报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:26
 */
public interface HmeInventoryEndProductRepository {

    /**
     * 售后在制品盘点-成品报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author sanfeng.zhang@hand-china.com 2021/4/1 15:33
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeInventoryEndProductVO2>
     */
    Page<HmeInventoryEndProductVO2> inventoryEndProductQuery(Long tenantId, HmeInventoryEndProductVO dto, PageRequest pageRequest);

    /**
     * 售后在制品盘点-成品报表导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/1 23:52
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInventoryEndProductVO3>
     */
    List<HmeInventoryEndProductVO3> inventoryEndProductExport(Long tenantId, HmeInventoryEndProductVO dto);
}
