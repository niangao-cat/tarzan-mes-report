package tarzan.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class MtExtendVO implements Serializable {

    private static final long serialVersionUID = -6640141676420688829L;

    private String attrId;
    private String keyId;
    private String attrName;
    private String attrValue;
    private String lang;
}
