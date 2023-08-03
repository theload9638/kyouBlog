package com.kyou.blog.common.emuration;

/**
 * @author CC
 * time 2023-07-07
 * description 数据库操作类型
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT,
    /**
     * 删除操作
     */
    DELETE;

    public static boolean contains(String type){
        OperationType[] r = values();
        for (OperationType operationType : r) {
            if (operationType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }

}
