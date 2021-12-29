package com.ruike.common.app.service.impl;

import com.ruike.common.app.service.GetMesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFilePara;
import com.ruike.common.infra.constant.Constant;
import com.ruike.hme.infra.util.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description 获取报表服务文件信息：桶名、目录名、文件类型
 *
 * @author penglin.sui@hand-chian.com 2021/11/30 17:02
 */
@Service
public class GetMesReportFileInfoImpl implements GetMesReportFileInfo {

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public List<MesReportFileInfo> getFileInfo(Long tenantId, MesReportFilePara mesReportFilePara) {
        List<MesReportFileInfo> returnList = new ArrayList<>();
        // 桶名值集
        List<LovValueDTO> bucketNameList = lovAdapter.queryLovValue("HMES.MES_REPORT_BUCKET_NAME", tenantId);
        if(StringUtils.isNotBlank(mesReportFilePara.getBucketNameValue())){
            bucketNameList = bucketNameList.stream().filter(item -> item.getValue().equals(mesReportFilePara.getBucketNameValue()))
                    .collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(bucketNameList)){
            // 目录名值集
            List<LovValueDTO> directoryList = lovAdapter.queryLovValue("HMES.MES_REPORT_DIRECTORY", tenantId);
            if(StringUtils.isNotBlank(mesReportFilePara.getDirectoryValue())){
                directoryList = directoryList.stream().filter(item -> item.getValue().equals(mesReportFilePara.getDirectoryValue()))
                        .collect(Collectors.toList());
                if(Constant.OpType.DELETE_FILE.equals(mesReportFilePara.getOpType())){
                    directoryList = directoryList.stream().filter(item -> Constant.ConstantValue.YES.equals(item.getTag()))
                            .collect(Collectors.toList());
                }
            }
            if(CollectionUtils.isNotEmpty(directoryList)){
                List<LovValueDTO> fileTypeList = lovAdapter.queryLovValue("HMES.MES_REPORT_DEL_FILE_TYPE", tenantId);
                if(StringUtils.isNotBlank(mesReportFilePara.getFileTypeValue())){
                    fileTypeList = fileTypeList.stream().filter(item -> item.getValue().equals(mesReportFilePara.getFileTypeValue()))
                            .collect(Collectors.toList());
                }
                for (LovValueDTO bucketName : bucketNameList
                     ) {
                    List<LovValueDTO> subDirectoryList = directoryList.stream().filter(item -> item.getParentValue().equals(bucketName.getValue())).collect(Collectors.toList());
                    for (LovValueDTO directory : subDirectoryList
                         ) {
                        List<LovValueDTO> subFileTypeList = fileTypeList.stream().filter(item -> item.getParentValue().equals(directory.getValue())).collect(Collectors.toList());
                        MesReportFileInfo fileInfo = new MesReportFileInfo();
                        fileInfo.setBucketName(bucketName.getValue());
                        fileInfo.setDirectory(directory.getValue());
                        if(CollectionUtils.isNotEmpty(subFileTypeList)){
                            fileInfo.setFileTypeList(subFileTypeList.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList()));
                        }else{
                            fileInfo.setFileTypeList(new ArrayList<>());
                        }
                        returnList.add(fileInfo);
                    }
                }
            }
        }
        if(Constant.OpType.ASYNC_EXPORT.equals(mesReportFilePara.getOpType())
                && CollectionUtils.isEmpty(returnList)){
            MesReportFileInfo fileInfo = new MesReportFileInfo();
            fileInfo.setBucketName(FileUtils.UploadValue.BUCKET_NAME);
            fileInfo.setDirectory(FileUtils.UploadValue.DIRECTORY);
            returnList.add(fileInfo);
        }
        return returnList;
    }
}
