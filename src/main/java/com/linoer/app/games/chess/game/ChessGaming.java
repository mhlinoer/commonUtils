package com.linoer.app.games.chess.game;

import com.linoer.app.games.base.BaseGaming;
import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.domain.BaseGameConfig;
import com.linoer.app.games.base.domain.BasePlayer;
import com.linoer.app.games.chess.game.process.ChessEventHandler;
import com.linoer.app.games.chess.game.process.ChessEventListener;
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
 * eventListener循环器，遍历其中的事件，并交由指定的handler执行
 *
 * 1.0版本：
 * handler为每个ChessGaming一个线程，随游戏对局创建和结束，对全部的事件执行process动作
 * listener为全服循环器，收取事件并发送到handler
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
        executorService = Executors.newScheduledThreadPool(2);
        eventHandler = new ChessEventHandlerImp(workQueue);
        eventListener = new ChessEventListener(workQueue);
        eventListener.registeredHandler(eventHandler);
        executorService.scheduleWithFixedDelay(eventListener, 0, 60, TimeUnit.MILLISECONDS);
    }

    @Override
    public void ready() {

    }

    @Override
    public void start() {

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
