package com.linoer.app.games.chess.game.process;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.round.BaseEventHandler;
import com.linoer.app.games.chess.game.exception.BaseGameException;

import java.util.Queue;

public class ChessEventHandler implements BaseEventHandler{

    public volatile Queue<BaseEvent> workQueue;

    public ChessEventHandler(Queue<BaseEvent> workQueue){
        System.out.println("ChessEventHandler constructor");
        this.workQueue = workQueue;
    }

    @Override
    public void run() {

    }

    @Override
    public void process(BaseEvent event) throws BaseGameException {

    }

    @Override
    public void rollBack(BaseEvent event) {

    }

    @Override
    public void except(BaseEvent event) {

    }
}
