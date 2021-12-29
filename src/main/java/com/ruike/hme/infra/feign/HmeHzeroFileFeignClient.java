package com.ruike.hme.infra.feign;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.infra.feign.fallback.HmeHzeroFileFeignClientFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * file服务调用
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 20:23
 */
@FeignClient(value = "hzero-file", fallback = HmeHzeroFileFeignClientFallback.class)
public interface HmeHzeroFileFeignClient {


    /**
     * 获取文件列表
     *
     * @param organizationId 租户ID
     * @param attachmentUUID 附件UUID
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/files/{attachmentUUID}/file")
    ResponseEntity<List<HmeHzeroFileDTO>> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                       @PathVariable("attachmentUUID") String attachmentUUID);

    /**
     * 上传文件
     *
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author penglin.sui 2021/7/19 15:18
     */
    @PostMapping("/v1/{organizationId}/files/attachment/byte")
    ResponseEntity<String> uploadAttachByteFile(@PathVariable("organizationId") Long organizationId,
                                                     @RequestParam("bucketName") String bucketName,
                                                     @RequestParam(value = "directory", required = false) String directory,
                                                     @RequestParam("attachmentUUID") String attachmentUuid,
                                                     @RequestParam(value = "fileName", required = false) String fileName,
                                                     @RequestParam(value = "fileType", required = false) String fileType,
                                                     @RequestParam(value = "storageCode", required = false) String storageCode,
                                                     @RequestBody byte[] byteFile);

    /**
     * 获取上传附件UUID
     *
     * @return : org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.String>>
     * @author penglin.sui 2021/7/20 19:35
     */
    @PostMapping("/v1/{organizationId}/files/uuid")
    ResponseEntity<Map<String,String>> getAttachUUID(@PathVariable("organizationId") Long organizationId);
}