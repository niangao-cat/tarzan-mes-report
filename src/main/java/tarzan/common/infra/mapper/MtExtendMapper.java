package tarzan.common.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.common.domain.MtExtendVO;
import tarzan.common.domain.entity.MtErrorMessage;

import java.util.List;

public interface MtExtendMapper {
    List<MtExtendVO> selectExtend(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "keyIdList") List<String> keyIdList,
                                  @Param(value = "attrNameList") List<String> attrNameList);
}
