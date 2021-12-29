package com.ruike.wms.infra.constant;

/**
 * @program: tarzan-mes-report
 * @name: WmsConstants
 * @description: 物流常量类
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-14 14:02
 **/
public class WmsConstants {
    private WmsConstants() {
    }

    /**
     * 配送单状态
     */
    public static class InstructionDocStatus {
        private InstructionDocStatus() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";
        /**
         * 下达
         */
        public static final String RELEASED = "RELEASED";
        /**
         * 备料中
         */
        public static final String PREPARE_EXECUTE = "PREPARE_EXECUTE";
        /**
         * 备料完成
         */
        public static final String PREPARE_COMPLETE = "PREPARE_COMPLETE";
        /**
         * 签收中
         */
        public static final String SIGN_EXECUTE = "SIGN_EXECUTE";
        /**
         * 签收完成
         */
        public static final String SIGN_COMPLETE = "SIGN_COMPLETE";
        /**
         * 关闭
         */
        public static final String CLOSED = "CLOSED";
    }

    /**
     * 月份常量
     */
    public static class Month {
        private Month() {
        }

        public static final String MONTH_01 = "01";
        public static final String MONTH_02 = "02";
        public static final String MONTH_03 = "03";
        public static final String MONTH_04 = "04";
        public static final String MONTH_05 = "05";
        public static final String MONTH_06 = "06";
        public static final String MONTH_07 = "07";
        public static final String MONTH_08 = "08";
        public static final String MONTH_09 = "09";
        public static final String MONTH_10 = "10";
        public static final String MONTH_11 = "11";
        public static final String MONTH_12 = "12";
    }

    /**
     * 颜色常量
     */
    public static class Color {
        private Color() {
        }

        public static final String COLOR_01 = "#F33148";
        public static final String COLOR_02 = "#FF4D42";
        public static final String COLOR_03 = "#FFE699";
        public static final String COLOR_04 = "#FFD966";
        public static final String COLOR_05 = "#C5E0B4";
        public static final String COLOR_06 = "#A9D18E";
        public static final String COLOR_07 = "#767171";
    }

}
