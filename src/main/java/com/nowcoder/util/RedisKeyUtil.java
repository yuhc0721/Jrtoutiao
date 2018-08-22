package com.nowcoder.util;

public class RedisKeyUtil {
    public static String SPLIT =":";
    public static String BIZ_LIKE ="LIKE";
    public static String BIZ_DISLIKE ="DISLIKE";
    public static String BIZ_EVENT = "EVENT";
    //事件
    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId,int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType)+ SPLIT + String.valueOf(entityId);
    }
}
