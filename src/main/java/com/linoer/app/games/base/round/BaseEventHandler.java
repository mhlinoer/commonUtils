package com.linoer.app.games.base.round;

import com.linoer.app.games.base.domain.BaseEvent;

/**
 * 基础处理事件类
 */
public interface BaseEventHandler extends Runnable{

    void process(BaseEvent event);

    void rollBack(BaseEvent event);

    void except(BaseEvent event);

}
