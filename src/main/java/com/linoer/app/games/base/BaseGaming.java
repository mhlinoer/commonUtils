package com.linoer.app.games.base;

import com.linoer.app.games.base.domain.BaseGameConfig;
import com.linoer.app.games.base.domain.BasePlayer;
import com.linoer.app.games.base.round.BaseEventHandler;
import com.linoer.app.games.base.round.BaseEventListener;
import com.linoer.app.games.base.round.BaseEventSource;

import java.util.List;

/**
 * 基础游戏主进程
 */
public class BaseGaming implements BaseGame, Runnable{
    // 玩家信息
    public List<BasePlayer> playerList = null;
    // 游戏参数配置
    public BaseGameConfig gameConfig = null;
    // 事件监听器
    protected BaseEventListener eventListener = null;
    // 事件处理器
    protected BaseEventHandler eventHandler = null;
    // 事件生成器
    protected BaseEventSource eventSource = null;

    @Override
    public void init() {

    }

    @Override
    public void ready() {

    }

    @Override
    public void gaming() {

    }

    @Override
    public void gameEnd() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void run() {
    }
}
