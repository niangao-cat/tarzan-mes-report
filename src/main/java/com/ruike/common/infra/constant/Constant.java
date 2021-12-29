package com.ruike.common.infra.constant;

/**
 * <p>
 * 常量
 * </p>
 *
 * @author penglin.sui@hand-china.com 2021/12/2 9:40
 */
public class Constant {
    private Constant() {}

    /**
     * 常量值
     */
    public static class ConstantValue {
        private ConstantValue() {
        }

        /**
         * 常量值 Y
         */
        public static final String YES = "Y";

        /**
         * 常量值 N
         */
        public static final String NO = "N";
    }

    /**
     * 操作类型
     */
    public static class OpType {
        private OpType() {}

        /**
         * ASYNC-EXPORT-异步导出
         */
        public static final String ASYNC_EXPORT = "ASYNC_EXPORT";

        /**
         * DELETE-FILE-删除文件
         */
        public static final String DELETE_FILE = "DELETE_FILE";
    }
}
