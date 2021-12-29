package com.ruike.common.app.handler;

import com.ruike.common.app.service.DeleteFileService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@JobHandler("downloadFileDeleteJob")
public class DownloadFileDeleteJobHandler implements IJobHandler {

    @Autowired
    private DeleteFileService deleteFileService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 获取任务所属租户
        Long tenantId = tool.getBelongTenantId();
        try {
            this.deleteFileService.downloadFileDelete(tenantId);
            tool.info("Download file delete success!!!");
        } catch (Exception ex) {
            tool.error("Download file delete failed!!!" + ex.getMessage());
        }
        return ReturnT.SUCCESS;
    }
}
