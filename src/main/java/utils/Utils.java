package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName Utils
 * @Description 封装通用工具
 * @Author lkj
 * @Date 2020/12/24
 */
@Slf4j
public class Utils {


    /**
     * 生成批量插入的values，主要DataWay用
     *
     * @param values
     * @param separator
     * @param fields
     */
    public static List<String> objectListValue(List<Object> values, String[] fields, String separator) {
        List<String> result = new ArrayList<>(values.size());
        for (Object obj : values) {
            String objectValue = objectValue(obj, fields, separator);
            if (StringUtils.isEmpty(objectValue)) {
                continue;
            }
            String value = "('" + objectValue + "')";
            result.add(value);
        }
        return result;
    }


    /**
     * 获取Object值
     *
     * @param obj       要分组的类
     * @param separator 分隔的参数
     * @param fields    要取值的字段
     * @return 返回值用separator分隔
     */
    public static <T> String objectValue(T obj, String[] fields, String separator) {
        if (Objects.isNull(obj)) {
            return new String();
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        List<String> values = new ArrayList<>(fields.length);
        for (String f : fields) {
            String value = jsonObject.get(f).toString();
            values.add(value);
        }
        if (values.isEmpty()) {
            return new String();
        }
        return StringUtils.join(values, separator);
    }

    /**
     * 分割LIST
     *
     * @param sqlList  需要分割的LIST
     * @param splitNum 分割数量
     * @param <T>
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, int splitNum) {

        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    /**
     * 手动排序分页
     * 默认第一页为0
     *
     * @param nowPage    当前页
     * @param pageSize   每页数量
     * @param data       分页数据
     * @param comparator 排序实现
     * @return <T> Page<T>
     */
    public static <T> Page<T> pagedAndSorList(int nowPage, int pageSize, List<T> data, Comparator<T> comparator) {
        nowPage = nowPage + 1;
        if (Objects.nonNull(comparator)) {
            data.sort(comparator);
        }
        int fromIndex = (nowPage - 1) * pageSize;
        if (fromIndex >= data.size() || fromIndex < 0) {
            // 返回空页
            return new Page<>();
        }
        int toIndex = nowPage * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        List<T> list = data.subList(fromIndex, toIndex);
        return new Page<>(list, new PageInfo(nowPage - 1, pageSize), data.size());
    }

    /**
     * 手动分页
     * 默认第一页为0
     *
     * @param nowPage  当前页
     * @param pageSize 每页数量
     * @param data     分页数据
     * @return <T> Page<T>
     */
    public static <T> Page<T> pagedList(int nowPage, int pageSize, List<T> data) {
        return pagedAndSorList(nowPage, pageSize, data, null);
    }
}
