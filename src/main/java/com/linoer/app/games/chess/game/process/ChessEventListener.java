package com.linoer.app.games.chess.game.process;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.domain.EventType;
import com.linoer.app.games.base.round.BaseEventHandler;
import com.linoer.app.games.base.round.BaseEventListener;
import com.linoer.app.games.chess.game.exception.BaseGameException;
import com.linoer.app.utils.CommonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ChessEventListener implements BaseEventListener {
    private static final String TRACE = "ChessEventListener";
    private volatile Queue<BaseEvent> workQueue;
    private List<BaseEventHandler> handlerList;

    public ChessEventListener(Queue<BaseEvent> workQueue){
        this.workQueue = workQueue;
        this.handlerList = new ArrayList<>();
        this.config();
    }

    @Override
    public void registeredHandler(BaseEventHandler handler) {
        handlerList.add(handler);
    }

    @Override
    public void unRegisteredHandler(BaseEventHandler handler) {
        handlerList.remove(handler);
    }

    @Override
    public void config() {

    }

    @Override
    public void handle() throws BaseGameException {
        BaseEvent event = workQueue.poll();
        if (null == event){
            throw new BaseGameException();
        }
        event.setTRACE_ID(event.getTRACE_ID().append(TRACE).append(CommonHelper.getCurrentDateTimeStr()));
        event.setEventType(EventType.START_HANDLE);
        handlerList.forEach(handler -> handler.process(event));
    }

    @Override
    public void check() throws BaseGameException{

    }

    @Override
    public void run() {
        try {
            // 检验事件
            check();
            // 处理事件
            handle();
        } catch (BaseGameException e) {
            e.printStackTrace();
        }
    }
}
