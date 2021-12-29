package tarzan.common.infra.feign.fallback;

import io.choerodon.core.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtRoleVO;
import tarzan.common.domain.sys.MtUserInfo;
import tarzan.common.infra.feign.MtRemoteIamService;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 20:01
 */
@Component
public class MtRemoteIamServiceImpl implements MtRemoteIamService {
    @Override
    public ResponseEntity<List<MtRoleVO>> selfRoles(Long tenantId) {
        return null;
    }

    @Override
    public ResponseEntity<Page<MtUserInfo>> userAllInfoRemoteGet(Long tenantId, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<MtUserInfo> userInfoRemoteGet(Long tenantId, Long userId) {
        return null;
    }
}
