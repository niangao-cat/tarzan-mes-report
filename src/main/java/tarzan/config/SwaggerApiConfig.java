package tarzan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerApiConfig {
    //WMS
    public static final String WMS_DEMO = "wmsDemo";

    //HME
    public static final String HME_DEMO = "hmeDemo";
    public static final String HME_DISTRIBUTION_DEMAND = "HmeDistributionDemand";
    public static final String WORK_ORDER_ATTRITION_SUM = "WorkOrderAttritionSum";
    public static final String SERVICE_RETURN_CHECK = "ServiceReturnCheck";
    public static final String HME_SERVICE_SPLIT_RK05_REPORT = "hmeServiceSplitRk05Report";
    public static final String HME_EQUIPMENT_FAULT_MONITOR = "HmeEquipmentFaultMonitor";
    public static final String HME_INVENTORY_END_PRODUCT = "hmeInventoryEndProduct";
    public static final String HME_COS_WORKCELL_EXCEPTION = "hmeCosWorkcellException";
    public static final String HME_LOAD_JOB = "HmeLoadJob";
    public static final String HME_PROCESS_COLLECT = "HmeProcessCollect";
    public static final String HME_NC_DETAIL = "HmeNcDetail";
    public static final String QMS_RECEIVED_INSPECTING_BOARD = "qmsReceivedInspectingBoard";
    public static final String WMS_CHECKED_WAIT_GROUDING = "wmsCheckedWaitGrouding";
    public static final String WMS_DELIVERY_MONITORING = "wmsDeliveryMonitoring";
    public static final String WMS_DISTRIBUTION_GAP = "wmsDistributionGap";
    public static final String HME_WORK_CELL_DETAILS_REPORT = "hmeWorkCellDetailsReport";
    public static final String HME_SELF_REPAIR_REPORT = "hmeSelfRepairReport";

    //QMS
    public static final String QMS_DEMO = "qmsDemo";

    @Autowired
    public SwaggerApiConfig(Docket docket) {
        docket.tags(new Tag(WMS_DEMO, "WMS"), new Tag(HME_DEMO, "HME"), new Tag(QMS_DEMO, "QMS")
                , new Tag(HME_DISTRIBUTION_DEMAND, "配送需求滚动报表")
                , new Tag(WORK_ORDER_ATTRITION_SUM, "工单损耗汇总报表")
                , new Tag(SERVICE_RETURN_CHECK, "售后退库检测报表")
                , new Tag(HME_SERVICE_SPLIT_RK05_REPORT, "售后在制品盘点-半成品")
                , new Tag(HME_EQUIPMENT_FAULT_MONITOR, "设备故障监控")
                , new Tag(HME_INVENTORY_END_PRODUCT, "售后在制品盘点-成品报表")
                , new Tag(HME_COS_WORKCELL_EXCEPTION, "COS工位加工异常汇总表")
                , new Tag(HME_LOAD_JOB, "芯片装载作业")
                , new Tag(HME_PROCESS_COLLECT, "工序采集项报表")
                , new Tag(HME_NC_DETAIL, "工序不良记录")
                , new Tag(WMS_CHECKED_WAIT_GROUDING, "已收待上架看板")
                , new Tag(WMS_DELIVERY_MONITORING, "配送任务监控看板")
                , new Tag(QMS_RECEIVED_INSPECTING_BOARD, "已收待验看板")
                , new Tag(WMS_DISTRIBUTION_GAP, "物料配送缺口监控看板")
                , new Tag(HME_WORK_CELL_DETAILS_REPORT, "工位产量明细报表")
                , new Tag(HME_SELF_REPAIR_REPORT, "自制件返修统计报表")
        );
    }

}
