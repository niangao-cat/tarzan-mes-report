package com.ruike.hme.infra.feign;

import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.infra.feign.fallback.HmeHzeroPlatformFeignClientFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;


/**
 * platform服务调用
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 20:23
 */
@FeignClient(value = "hzero-platform", fallback = HmeHzeroPlatformFeignClientFallback.class)
public interface HmeHzeroPlatformFeignClient {


    /**
     * 获取岗位名称
     *
     * @param organizationId 租户ID
     * @param unitId         部门ID
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/units/{unitId}")
    ResponseEntity<HmeHzeroPlatformUnitDTO> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                         @PathVariable("unitId") String unitId);

    /**
     * 新建导出任务
     *
     * @return : org.springframework.http.ResponseEntity<com.ruike.hme.domain.vo.HmeExportTaskVO>
     * @author penglin.sui 2021/7/19 15:18
     */
    @PostMapping("/v1/export-task")
    ResponseEntity<HmeExportTaskVO> addExportTask(@RequestBody HmeExportTaskVO hmeExportTaskVO);

    /**
     * 更新导出任务
     *
     * @return : org.springframework.http.ResponseEntity<com.ruike.hme.domain.vo.HmeExportTaskVO>
     * @author penglin.sui 2021/7/21 10:55
     */
    @PutMapping("/v1/export-task")
    ResponseEntity<HmeExportTaskVO> updateExportTask(@RequestBody HmeExportTaskVO hmeExportTaskVO);
}