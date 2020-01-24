package com.linoer.app.games.base.round;

import com.linoer.app.games.chess.game.exception.BaseGameException;

/**
 * 事件监听类, 对丢入其中的source，选择对应的handler去处理
 */
public interface BaseEventListener extends Runnable{

    void registeredHandler(BaseEventHandler handler);

    void unRegisteredHandler(BaseEventHandler handler);

    // 配置
    void config();

    // 处理事件
    void handle() throws BaseGameException;

    // 拉取事件
    void check() throws BaseGameException;

}
