package com.ruike.hme.infra.util;

import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    public static final Pattern PATTERN = Pattern.compile("-?[0-9]+.?[0-9]*");
    //邮箱判断正则表达
    public static final Pattern EMAIL = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    /***
     * @Description: 判断是否为数字
     * @author: sanfeng.zhang
     * @date 2020/7/13 20:24
     * @param numStr
     * @return : boolean
     * @version 1.0
     */
    public static boolean isNumeric(String numStr) {
        Matcher isNum = PATTERN.matcher(numStr);
        return isNum.matches();
    }

    public static String dateStrFormat(String dateStr,String pattern) {
        if(StringUtils.isBlank(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.parse(dateStr);
        }catch (Exception e){

        }
        return sdf.format(date);
    }

    /**
     * 获取某月第一天
     *
     * @param date
     * @param pattern
     * @param during
     * @author sanfeng.zhang@hand-china.com 2020/10/22 22:24
     * @return java.lang.String
     */
    public static String monthStartDayTime(Date date, String pattern, Integer during) {
        if(during == null){
            during = 0;
        }
        if(StringUtils.isBlank(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, during);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return dateFormat.format(calendar.getTime());
    }

    public static List<String> queryMonthDailyList(Date date){
        List<String> dailyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 返回指定日历字段可能捅有的最大值
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i= 1; i<=maxDay; i++){
            dailyList.add(String.valueOf(i));
        }
        return dailyList;
    }

    /***
     * @Description: 日期转字符串
     * @author: penglin.sui
     * @date 2021/6/11 11:37
     * @return : Date
     * @version 1.0
     */
    public static String dateToString(Date date , String pattern) {
        SimpleDateFormat sformat = new SimpleDateFormat(pattern);//日期格式
        String tiem = sformat.format(date);

        return tiem;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr
     * @param pattern
     * @return java.util.Date
     * @author penglin.sui@hand-china.com 2021/6/23 19:21
     */
    public static Date stringToDate(String dateStr , String pattern){
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            throw new CommonException(e);
        }
        return date;
    }

    /***
     * @Description: 获取当前时间
     * @author: penglin.sui
     * @date 2020/11/10 9:26
     * @return : Date
     * @version 1.0
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /***
     * @Description: 计算日期
     * @author: penglin.sui
     * @date 2021/6/11 11:37
     * @return : Date
     * @version 1.0
     */
    public static Date subDate(Date date , int internalDay , String pattern , int calendarType){
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, internalDay);
        Date returnDate = null;
        try {
            returnDate = dft.parse(dft.format(calendar.getTime()));
        }catch (Exception ex){

        }
        return returnDate;
    }

    /***
     * @Description: 计算月份
     * @author: penglin.sui
     * @date 2021/6/11 14:19
     * @return : Date
     * @version 1.0
     */
    private static Date subDateOfMonth(Date date , int internalDay){
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, internalDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(calendar.getTime()));
        }catch (Exception ex){

        }
        return endDate;
    }

    /**
     * 拆分数据
     *
     * @param sqlList  源数据
     * @param splitNum 拆分数量
     * @return 拆分数据
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
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
     * 返回两个日期的天数
     *
     * @param dateOne
     * @param dateTwo
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/3/9 12:52
     */
    public static Long betweenDays(Date dateOne, Date dateTwo) {
        if (dateOne == null || dateTwo == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOne);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(dateTwo);
        long timeInMillis2 = calendar.getTimeInMillis();
        return (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
    }

    //字母转化成对应数字(一位字母)
    public static String changeNum(String str) {
        char charStr = str.charAt(0);
        Integer charNum = Integer.valueOf(charStr);
        Integer result = charNum - 64;
        return result.toString();
    }

    /**
     * 判断是否是字母
     *
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */
    public static boolean isAlpha(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 获取当前时间所在年的周数
     *
     * @param date 时间
     * @return int
     * @author penglin.sui@hand-china.com 2021/1/27 21:22
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        //本年的第一个星期所需的给定最少天数
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前时间所在年的最大周数
     *
     * @param year 年份
     * @return int
     * @author penglin.sui@hand-china.com 2021/1/27 21:22
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    /**
     * 获取两个日期的月份差
     *
     * @param before 开始时间
     * @param after 结束时间
     * @return int
     * @author penglin.sui@hand-china.com 2021/1/28 22:10
     */
    public static int monthDiffGet(Date before , Date after) {
        if(after.compareTo(before) < 0){
            return 0;
        }
        Calendar cBefore  = Calendar.getInstance();
        Calendar cAfter = Calendar.getInstance();
        cBefore.setTime(before);
        cAfter.setTime(after);
        int result = cAfter.get(Calendar.MONTH) - cBefore.get(Calendar.MONTH) + 1;
        int month = (cAfter.get(Calendar.YEAR) - cBefore.get(Calendar.YEAR)) * 12;
        return (result + month);
    }

    public static String numberToUpperLetter(int num) {
        if (num <= 0) {
            return null;
        }
        StringBuilder letter = new StringBuilder();
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter.insert(0, ((char) (num % 26 + (int) 'A')));
            num = (num - num % 26) / 26;
        } while (num > 0);

        return letter.toString();

    }

    public static boolean isEmail(String email) {
        Matcher isEmail = EMAIL.matcher(email);
        return isEmail.matches();
    }

    /**
     * 手动分页
     * 默认第一页为0
     * @param nowPage 当前页
     * @param pageSize 每页数量
     * @param data 分页数据
     * @return <T> List<T>
     */
    public static <T> List<T> pagedList(int nowPage, int pageSize, List<T> data) {
        nowPage = nowPage + 1;
        int fromIndex = (nowPage - 1) * pageSize;
        if (fromIndex >= data.size()) {
            //空数组
            return Collections.emptyList();
        }
        if (fromIndex < 0) {
            //空数组
            return Collections.emptyList();
        }
        int toIndex = nowPage * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }

    /**
     * 导出任务常量值
     */
    public static class ExportTaskStateValue {
        private ExportTaskStateValue() {
        }

        /**
         * 进行中
         */
        public static final String DOING = "DOING";

        /**
         * 已完成
         */
        public static final String DONE = "DONE";

        /**
         * 已取消
         */
        public static final String CANCELED = "CANCELED";
    }

    /**
     * 表名常量值
     */
    public static class TableName {
        private TableName() {
        }

        /**
         * 数据采集项
         */
        public static final String HME_EO_JOB_DATA_RECORD = "hme_eo_job_data_record";

        /**
         * 数据采集项_1
         */
        public static final String HME_EO_JOB_DATA_RECORD_1 = "hme_eo_job_data_record_1";

        /**
         * 数据采集项_2
         */
        public static final String HME_EO_JOB_DATA_RECORD_2 = "hme_eo_job_data_record_2";
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /***
     * @Description: 计算时间
     * @author: penglin.sui
     * @date 2021/7/28 15:51
     * @return : Date
     * @version 1.0
     */
    public static Date calculateDate(Date date , int internal , int type , String pattern){
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        Date beginDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, internal);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(calendar.getTime()));
        }catch (Exception ex){

        }
        return endDate;
    }
}
