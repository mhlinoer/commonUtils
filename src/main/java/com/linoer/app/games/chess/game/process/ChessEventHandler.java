package com.linoer.app.games.chess.game.process;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.round.BaseEventHandler;

import java.util.Queue;

public class ChessEventHandler implements BaseEventHandler{

    private volatile Queue<BaseEvent> workQueue;

    public ChessEventHandler(Queue<BaseEvent> workQueue){
        this.workQueue = workQueue;
    }

    @Override
    public void run() {

    }

    @Override
    public void process(BaseEvent event) {

    }

    @Override
    public void rollBack(BaseEvent event) {

    }

    @Override
    public void except(BaseEvent event) {

    }
}
