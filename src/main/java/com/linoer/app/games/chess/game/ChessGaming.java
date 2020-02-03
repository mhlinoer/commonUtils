package com.linoer.app.games.chess.game;

import com.linoer.app.games.base.BaseGaming;
import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.domain.BaseGameConfig;
import com.linoer.app.games.base.domain.BasePlayer;
import com.linoer.app.games.base.domain.EventType;
import com.linoer.app.games.chess.game.process.ChessEventListener;
import com.linoer.app.games.chess.game.process.ChessEventSource;
import com.linoer.app.games.chess.game.process.imp.ChessEventHandlerImp;
import com.linoer.app.games.chess.model.ChessGameConfig;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 象棋游戏类，和对局绑定
 *
 *
 * 1.0版本：
 * 每局游戏新建一个Gaming对象
 * source为每个动作生成事件
 * listener为全服循环器，收取事件,并发送到handler
 * handler为每个ChessGaming一个线程，随游戏对局创建和结束，对全部的事件执行process动作
 */
public class ChessGaming extends BaseGaming {

    // 玩家列表
    private List<BasePlayer> playerList;
    // 游戏参数配置
    private BaseGameConfig gameConfig;
    // 线程驱动器
    private ScheduledExecutorService executorService;
    // 事件队列
    private volatile Queue<BaseEvent> workQueue = new ConcurrentLinkedQueue<>();

    public ChessGaming(List<BasePlayer> players) {
        System.out.println("ChessGaming constructor");
        this.playerList = players;
        // 默认配置
        this.gameConfig = new ChessGameConfig();
    }

    public ChessGaming(List<BasePlayer> players, BaseGameConfig gameConfig){
        this.playerList = players;
        this.gameConfig = gameConfig;
    }

    @Override
    public void init() {
        System.out.println("ChessGaming init");
        executorService = Executors.newScheduledThreadPool(2);
        eventHandler = new ChessEventHandlerImp(workQueue);
        eventListener = new ChessEventListener(workQueue);
        eventSource = new ChessEventSource(workQueue);
        eventListener.registeredHandler(eventHandler);
    }

    @Override
    public void run() {
        System.out.println("ChessGaming run");
        init();
        executorService.scheduleWithFixedDelay(eventListener, 0, 200, TimeUnit.MILLISECONDS);
        executorService.scheduleWithFixedDelay(eventHandler, 0, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * 玩家准备
     */
    @Override
    public void ready() {
        System.out.println("ChessGaming ready");
        eventSource.generate(EventType.READY);
    }

    /**
     * 游戏中
     */
    @Override
    public void gaming() {
        eventSource.generate(EventType.GAMING);
    }

    /**
     * 游戏结束
     */
    @Override
    public void gameEnd() {
        eventSource.generate(EventType.GAME_END);
    }

    /**
     * 玩家退出
     */
    @Override
    public void exit() {
        eventSource.generate(EventType.EXIT);
    }
}
