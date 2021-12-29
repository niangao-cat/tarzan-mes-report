package com.ruike.hme.infra.feign.fallback;

import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Classname HmeHzeroPlatformFeignClientFallback
 * @Description Platform调用失败回调
 * @Date 2020/7/22 10:40
 * @Author sanfeng.zhang
 */
@Component
@Slf4j
public class HmeHzeroPlatformFeignClientFallback implements HmeHzeroPlatformFeignClient {

    @Override
    public ResponseEntity<HmeHzeroPlatformUnitDTO> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                                @PathVariable("unitId") String unitId){
        return new ResponseEntity<HmeHzeroPlatformUnitDTO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HmeExportTaskVO> addExportTask(HmeExportTaskVO hmeExportTaskVO) {
        log.info("<=============addExportTask error===============>");
        return new ResponseEntity<HmeExportTaskVO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HmeExportTaskVO> updateExportTask(HmeExportTaskVO hmeExportTaskVO) {
        log.info("<=============updateExportTask error===============>");
        return new ResponseEntity<HmeExportTaskVO>(HttpStatus.NO_CONTENT);
    }
}