package com.mytool.m3u8.http.download.enums;

/**
 * Created by wanghaofei on 2018/1/10.
 */

public enum Priority {

    /**
     * 手动下载的优先级
     */
    low(0),

    /**
     * 主动推送资源的手动恢复的优先级
     */
    middle(1),

    /**
     * 主动推送资源的优先级
     */
    high(2);

    Priority(int value) {
        this.value = value;
    }


    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public static Priority getInstance(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.getValue() == value) {
                return priority;
            }
        }
        return Priority.middle;
    }
}
