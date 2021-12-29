package tarzan.common.infra.feign;

import io.choerodon.core.domain.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tarzan.common.domain.sys.MtRoleVO;
import tarzan.common.domain.sys.MtUserInfo;
import tarzan.common.infra.feign.fallback.MtRemoteIamServiceImpl;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 20:00
 */
@FeignClient(value = "${hzero.service.iam:hzero-iam}", fallback = MtRemoteIamServiceImpl.class)
public interface MtRemoteIamService {
    /**
     * 获取当前用户拥有的所有角色
     *
     * @return
     */
    @GetMapping({"/hzero/v1/{organizationId}/roles/self/roles"})
    ResponseEntity<List<MtRoleVO>> selfRoles(@PathVariable("organizationId") Long tenantId);

    /**
     * 获取所有用户，最多只能查询400（size = 400）
     *
     * @param tenantId
     * @param size
     * @author chuang.yang
     * @date 2019/10/23
     */
    @GetMapping({"/hzero/v1/{organizationId}/users/paging"})
    ResponseEntity<Page<MtUserInfo>> userAllInfoRemoteGet(@PathVariable("organizationId") Long tenantId,
                                                          @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size);

    /**
     * 获取单个用户信息
     *
     * @param tenantId
     * @param userId
     * @return
     */
    @GetMapping({"/hzero/v1/{organizationId}/users/{userId}/info"})
    ResponseEntity<MtUserInfo> userInfoRemoteGet(@PathVariable("organizationId") Long tenantId,
                                                 @PathVariable("userId") Long userId);
}
