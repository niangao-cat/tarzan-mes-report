package com.ruike.common.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 * 文件删除
 * </p>
 *
 * @author penglin.sui@hand-china.com 2021/11/30 17:11
 */
public interface DeleteFileMapper {
    /**
     * <p>
     * 查询文件地址
     * </p>
     *
     * @author penglin.sui@hand-china.com 2021/11/30 17:11
     */
    List<String> fileUrlList(@Param("tenantId") Long tenantId,
                             @Param("bucketName") String bucketName,
                             @Param("directory") String directory,
                             @Param("fileTypeList") List<String> fileTypeList);
}
