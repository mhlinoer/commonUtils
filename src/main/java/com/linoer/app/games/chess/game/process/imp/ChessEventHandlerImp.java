package com.linoer.app.games.chess.game.process.imp;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.chess.game.exception.BaseGameException;
import com.linoer.app.games.chess.game.process.ChessEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

public class ChessEventHandlerImp extends ChessEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChessEventHandlerImp.class);

    public ChessEventHandlerImp(Queue<BaseEvent> workQueue) {
        super(workQueue);
    }

    /**
     * 具体处理事件
     * @param event chessEvent
     * @throws BaseGameException 处理事件异常
     */
    @Override
    public void process(BaseEvent event) throws BaseGameException {
        logger.info("process event:{}", event.getTRACE_ID());
        System.out.println("ChessEventHandler process:" + event.getTRACE_ID());
    }

    /**
     * 记录异常事件
     * @param event chessEvent
     */
    @Override
    public void except(BaseEvent event) {
        logger.info("except event:{}", event.getTRACE_ID());
    }

    /**
     * 回滚
     * @param event chessEvent
     */
    @Override
    public void rollBack(BaseEvent event) {
        logger.info("rollback event:{}", event.getTRACE_ID());
    }

    @Override
    public void run() {
        BaseEvent currentEvent = this.workQueue.poll();
        assert currentEvent != null;
        try {
            process(currentEvent);
        }catch (BaseGameException e){
            e.printStackTrace();
            except(currentEvent);
        }finally {
            rollBack(currentEvent);
        }
    }
}
