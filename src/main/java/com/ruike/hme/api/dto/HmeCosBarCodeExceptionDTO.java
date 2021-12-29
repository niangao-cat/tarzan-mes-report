package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * COS条码加工异常汇总输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:40
 */
@Data
public class HmeCosBarCodeExceptionDTO implements Serializable {

    private static final long serialVersionUID = -620134520984423351L;


    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单号集合")
    private List<String> workOrderNumList;

    @ApiModelProperty("条码")
    private String materialLotCode;

    @ApiModelProperty("条码集合")
    private List<String> materialLotCodeList;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("WAFER集合")
    private List<String> waferNumList;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "实验代码列表", hidden = true)
    @JsonIgnore
    private List<String> labCodeList;

    @ApiModelProperty("热沉编号")
    private List<String> hotSinkCodeList;

    @ApiModelProperty("不良代码ID,以逗号分隔的多个ID集合")
    private String ncCodeId;

    @ApiModelProperty("热沉条码")
    private List<String> heatSinkMaterialLotList;

    @ApiModelProperty("热沉物料ID,以逗号分隔的多个ID集合")
    private String heatSinkMaterialId;

    @ApiModelProperty("热沉物料ID集合")
    private List<String> heatSinkMaterialIdList;

    @ApiModelProperty("热沉供应商批次")
    private List<String> heatSinkSupplierLotList;

    @ApiModelProperty("金线条码")
    private List<String> goldWireMaterialLot;

    @ApiModelProperty("金线物料ID,以逗号分隔的多个ID集合")
    private String goldWireMaterialId;

    @ApiModelProperty("金线物料ID集合")
    private List<String> goldWireMaterialIdList;

    @ApiModelProperty("金线供应商批次")
    private List<String> goldWireSupplierLot;

    @ApiModelProperty("操作者")
    private String realName;

    @ApiModelProperty("设备台机")
    private String assetEncoding;

    @ApiModelProperty("产品")
    private String materialId;

    @ApiModelProperty("操作时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String beginTime;

    @ApiModelProperty("操作时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("工位ID,以逗号分隔的多个ID集合")
    private String workcellId;

    @ApiModelProperty("工位ID集合")
    private List<String> workcellIdList;

    @ApiModelProperty("工序ID,以逗号分隔的多个ID集合")
    private String processId;

    @ApiModelProperty("工序ID集合")
    private List<String> processIdList;

    @ApiModelProperty("工段ID,以逗号分隔的多个ID集合")
    private String lineWorkcellId;

    @ApiModelProperty("工段ID集合")
    private List<String> lineWorkcellIdList;

    @ApiModelProperty("产线ID,以逗号分隔的多个ID集合")
    private String prodLineId;

    @ApiModelProperty("产线ID集合")
    private List<String> prodLineIdList;

    public void validObject() {
        if (StringUtils.isBlank(workOrderNum) && StringUtils.isBlank(materialLotCode) & (Objects.isNull(beginTime) && Objects.isNull(endTime))) {
            throw new CommonException("工单号，盒子号与操作时间范围必输其一");
        }
    }
    public void initParam(){
        this.labCodeList = StringCommonUtils.splitIntoList(this.labCode);
    }
}
