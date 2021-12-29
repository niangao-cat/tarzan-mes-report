package com.ruike.hme.infra.feign.fallback;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Classname HmeHzeroFileFeignClientFallback
 * @Description File调用失败回调
 * @Date 2020/7/22 10:40
 * @Author sanfeng.zhang
 */
@Component
@Slf4j
public class HmeHzeroFileFeignClientFallback implements HmeHzeroFileFeignClient {

    @Override
    public ResponseEntity<List<HmeHzeroFileDTO>> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                              @PathVariable("attachmentUUID") String attachmentUUID){
        return new ResponseEntity<List<HmeHzeroFileDTO>>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> uploadAttachByteFile(Long organizationId, String bucketName, String directory, String attachmentUuid, String fileName, String fileType, String storageCode, byte[] byteFile) {
        log.info("<=================uploadAttachMultipartFile error====================>");
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Map<String,String>> getAttachUUID(Long organizationId) {
        log.info("<=================getAttachUUID error====================>");
        return new ResponseEntity<Map<String,String>>(HttpStatus.NO_CONTENT);
    }
}