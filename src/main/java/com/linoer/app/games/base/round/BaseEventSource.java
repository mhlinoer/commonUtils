package com.linoer.app.games.base.round;

import com.linoer.app.games.base.domain.EventType;

/**
 * 事件源类，用以生成事件
 */
public interface BaseEventSource {

    void generate(EventType eventType);
}
