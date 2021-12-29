package tarzan.common.domain.repository;

import tarzan.common.domain.entity.MtErrorMessage;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtErrorMessageRepository extends BaseRepository<MtErrorMessage>, AopProxy<MtErrorMessageRepository> {

    /**
     * 获取消息: 先查询redis，如果匹配不到，查询MySql
     * 
     * @param code
     * @param module
     * @param args
     * @return String
     */
    String getErrorMessageWithModule(Long tenantId, String code, String module, String... args);
}
