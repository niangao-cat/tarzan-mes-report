package io.choerodon.mybatis.common;

import io.choerodon.mybatis.common.optional.OptionalInsertMapper;
import io.choerodon.mybatis.common.optional.OptionalUpdateMapper;
import org.hzero.mybatis.common.ConditionMapper;
import org.hzero.mybatis.common.IdsMapper;
import org.hzero.mybatis.common.OptionalSelectMapper;
import org.hzero.mybatis.common.SpecialMapper;

/**
 * @ClassName BaseMapper
 * @Description 修改查询基础类源码
 * @Author lkj
 * @Date 2020/12/21
 */
public interface BaseMapper<T> extends
        CrudMapper<T>,
        OptionalInsertMapper<T>,
        OptionalUpdateMapper<T>,
        OptionalSelectMapper<T>,
        IdsMapper<T>,
        ConditionMapper<T>,
        SpecialMapper<T>,
        Marker {
}
