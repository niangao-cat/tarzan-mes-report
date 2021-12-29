package tarzan.common.infra.repository.impl;

import tarzan.common.domain.entity.MtErrorMessage;
import tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.util.MtLanguageHelper;
import tarzan.common.domain.util.ReplaceUtil;
import tarzan.common.infra.mapper.MtErrorMessageMapper;

/**
 * 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtErrorMessageRepositoryImpl extends BaseRepositoryImpl<MtErrorMessage>
                implements MtErrorMessageRepository {
    private final static String REDIS_ERROR_MESSAGE = "tarzan:error-message:";

    @Autowired
    private MtErrorMessageMapper mtErrorMessageMapper;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public String getErrorMessageWithModule(Long tenantId, String code, String module, String... args) {
        String key = REDIS_ERROR_MESSAGE + tenantId + ":" + module + ":" + MtLanguageHelper.language();
        String message = redisHelper.hshGet(key, code);

        if (StringUtils.isEmpty(message)) {
            MtErrorMessage one = new MtErrorMessage();
            one.setTenantId(tenantId);
            one.setModule(module);
            one.setMessageCode(code);
            one = mtErrorMessageMapper.selectOne(one);
            if (null != one) {
                message = one.getMessage();
            }
        }

        if (StringUtils.isEmpty(message)) {
            return code + "";
        }

        if (ArrayUtils.isNotEmpty(args)) {
            message = ReplaceUtil.replace(message, args);
        }
        return message;
    }
}
