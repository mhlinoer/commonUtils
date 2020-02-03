package com.linoer.app.games.base.domain;

/**
 * 事件状态
 */
public enum EventType {
    ONLINE,
    MATCHING,
    READY,
    BEGIN,
    GAMING,
    ROUND_END,
    GAME_END,
    IDLE,
    OFFLINE,
    START_HANDLE,
    END_HANDLE,
    ERROR_HANDLE,
    EXIT
}
