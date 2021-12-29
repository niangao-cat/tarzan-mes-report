package tarzan.common.infra.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 站点相关查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 16:19
 */
public interface SiteMapper {

    /**
     * 获取当前用户对应站点id
     *
     * @param userId
     * @return
     */
    String selectSiteByUser(@Param("userId") Long userId);
}
