package com.lanternfish.common.constant;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
public interface MongoConstants {

    /**
     * 排序类型 升序
     */
    String ORDER_BY_TYPE_ASC = "asc";

    /**
     * 排序类型 降序
     */
    String ORDER_BY_TYPE_DESC = "desc";

    /**
     * id字段名
     */
    String ID = "_id";

    /**
     * 修改时间字段名
     */
    String UPDATE_TIME = "updateTime";

    /**
     * 修改用户字段名
     */
    String UPDATE_USER = "updateUser";

    /**
     * 是否删除字段名
     */
    String IS_DELETED = "isDeleted";

    /**
     * 字段类型
     */
    interface FiledType {
        /**
         * 字段类型 字符串
         */
        String STRING = "string";

        /**
         * 字段类型 数字
         */
        String NUMBER = "number";

        /**
         * 字段类型 日期
         */
        String DATE = "date";
    }

    /**
     * 查询条件类型
     */
    interface OperatorType {

        String EQUAL = "equal";

        String LIKE = "like";

        String IN = "in";

        String GT = "gt";

        String GTE = "gte";

        String LT = "lt";

        String LTE = "lte";

        String BETWEEN = "between";

        String NOT_EQUAL = "notEqual";

    }
}
