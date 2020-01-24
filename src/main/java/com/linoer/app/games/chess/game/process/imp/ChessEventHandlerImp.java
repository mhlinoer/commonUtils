package com.linoer.app.games.chess.game.process.imp;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.chess.game.process.ChessEventHandler;

import java.util.Queue;

public class ChessEventHandlerImp extends ChessEventHandler {

    public ChessEventHandlerImp(Queue<BaseEvent> workQueue) {
        super(workQueue);
    }

    @Override
    public void run() {
        process();
    }
}
