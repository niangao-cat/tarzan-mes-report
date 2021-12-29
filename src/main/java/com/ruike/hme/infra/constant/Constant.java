package com.ruike.hme.infra.constant;

/**
 * <p>
 * 常量
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 17:11
 */
public class Constant {
    private Constant() {
    }

    /**
     * 常量值
     */
    public static class ConstantValue {
        private ConstantValue() {
        }

        /**
         * 常量值 0
         */
        public static final Integer ZERO = 0;

        /**
         * 常量值 1
         */
        public static final Integer ONE = 1;

        /**
         * 常量值 2
         */
        public static final Integer TWO = 2;

        /**
         * 常量值 1
         */
        public static final Double DOUBLE_ZERO = 0.0d;

        /**
         * 字符串常量值 0
         */
        public static final String STRING_ZERO = "0";

        /**
         * 字符串常量值 1
         */
        public static final String STRING_ONE = "1";

        /**
         * 字符串常量值 2
         */
        public static final String STRING_TWO = "2";

        /**
         * 字符串常量值 2
         */
        public static final String STRING_MINUS_ONE = "-1";

        /**
         * 常量值 0
         */
        public static final Long LONG_ZERO = 0L;

        /**
         * 常量值 0
         */
        public static final Long LONG_ONE = 1L;

        /**
         * 常量值 Y
         */
        public static final String YES = "Y";

        /**
         * 常量值 N
         */
        public static final String NO = "N";

        /**
         * 常量值 HME
         */
        public static final String HME = "HME";

        /**
         * 常量值 ORDER
         */
        public static final String ORDER = "ORDER";

        public static final String E = "E";

        /**
         * 常量值 OK ,NG
         */
        public static final String OK = "OK";

        public static final String NG = "NG";

        public static final String SN = "SN";

        public static final String SCANNED = "SCANNED";

        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * SQL批量插入分页条数
         */
        public static final int SQL_ITEM_COUNT_LIMIT = 1000;

        /**
         * 材料不良-退库
         */
        public static final String TK = "TK";

        /**
         * 材料不良-放行
         */
        public static final String FX = "FX";
        public static final String B05_FAC = "B05FAC";

        public static final String ALL_PATTERN = "%";

        public static final int MAP_DEFAULT_CAPACITY = 16;

    }

    public static class BackType {
        public static final String CUSTOMER = "CUSTOMER";
        public static final String OWN = "OWN";

        private BackType() {
        }
    }

}
