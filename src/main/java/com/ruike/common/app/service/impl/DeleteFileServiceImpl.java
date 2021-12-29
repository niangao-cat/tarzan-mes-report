package com.ruike.common.app.service.impl;

import com.ruike.common.app.service.DeleteFileService;
import com.ruike.common.app.service.GetMesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFileInfo;
import com.ruike.common.domain.vo.MesReportFilePara;
import com.ruike.common.infra.constant.Constant;
import com.ruike.common.infra.mapper.DeleteFileMapper;
import com.ruike.hme.infra.util.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description 文件删除
 *
 * @author penglin.sui@hand-chian.com 2021/11/30 17:02
 */
@Service
public class DeleteFileServiceImpl implements DeleteFileService {

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private DeleteFileMapper deleteFileMapper;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private GetMesReportFileInfo getMesReportFileInfo;

    @Override
    public void downloadFileDelete(Long tenantId) {
        MesReportFilePara mesReportFilePara = new MesReportFilePara();
        mesReportFilePara.setOpType(Constant.OpType.DELETE_FILE);
        List<MesReportFileInfo> fileInfoList = getMesReportFileInfo.getFileInfo(tenantId , mesReportFilePara);

        for (MesReportFileInfo fileInfo : fileInfoList
             ) {
            //查询文件路径
            List<String> list = deleteFileMapper.fileUrlList(tenantId, fileInfo.getBucketName(),fileInfo.getDirectory(),fileInfo.getFileTypeList());
            if(CollectionUtils.isNotEmpty(list)){
                try {
                    fileClient.deleteFileByUrl(tenantId, fileInfo.getBucketName(), list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
