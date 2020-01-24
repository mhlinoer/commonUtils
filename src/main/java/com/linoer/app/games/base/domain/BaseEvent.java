package com.linoer.app.games.base.domain;

/**
 * 基础事件类
 */
public class BaseEvent {
    // 事件流id
    private StringBuilder TRACE_ID;
    // 事件类型
    private EventType eventType = EventType.OFFLINE;
    // 事件最终结果
    private EventResult eventResult;

    public StringBuilder getTRACE_ID() {
        return TRACE_ID;
    }

    public void setTRACE_ID(StringBuilder TRACE_ID) {
        this.TRACE_ID = TRACE_ID;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventResult getEventResult() {
        return eventResult;
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult = eventResult;
    }

    public String getErrorPoint() {
        return errorPoint;
    }

    public void setErrorPoint(String errorPoint) {
        this.errorPoint = errorPoint;
    }

    // 事件异常的节点
    private String errorPoint;

    public static enum EventResult {
        NORMAL,
        ERROR,
        NULL
    }
}
