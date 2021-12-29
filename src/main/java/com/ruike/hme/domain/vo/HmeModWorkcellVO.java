package com.ruike.hme.domain.vo;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class HmeModWorkcellVO implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WORKCELL_CODE = "workcellCode";
    public static final String FIELD_WORKCELL_NAME = "workcellName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_WORKCELL_TYPE = "workcellType";
    public static final String FIELD_WORKCELL_LOCATION = "workcellLocation";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_WORKCELL_CATEGORY = "workcellCategory";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -2721629237151861217L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号", required = true)
    @NotBlank
    @Where
    private String workcellCode;
    @ApiModelProperty(value = "工作单元名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String workcellName;
    @ApiModelProperty(value = "工作单元描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "工作单元类型")
    @Where
    private String workcellType;
    @ApiModelProperty(value = "工作单元位置")
    @MultiLanguageField
    @Where
    private String workcellLocation;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "工作单元分类")
    @Where
    private String workcellCategory;
    @Cid
    @Where
    private Long cid;
}
