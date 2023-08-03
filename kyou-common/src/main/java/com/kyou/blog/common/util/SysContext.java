package com.kyou.blog.common.util;


/**
 * @author CC
 * time 2023-05-27
 * description
 */
public class SysContext {
    private static final ThreadLocal<Long> LOCAL=new ThreadLocal<Long>();

    public static void setUserId(Long id){
        LOCAL.set(id);
    }
    public static Long getUserId(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }


}
