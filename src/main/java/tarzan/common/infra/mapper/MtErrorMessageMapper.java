package tarzan.common.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.common.domain.entity.MtErrorMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtErrorMessageMapper extends BaseMapper<MtErrorMessage> {

    List<MtErrorMessage> selectAllErrorMessage(@Param(value = "language") String language);

}
