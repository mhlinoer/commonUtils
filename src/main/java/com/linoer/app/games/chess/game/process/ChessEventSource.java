package com.linoer.app.games.chess.game.process;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.domain.EventType;
import com.linoer.app.games.base.round.BaseEventSource;
import com.linoer.app.games.chess.model.ChessEvent;

import java.util.Queue;

public class ChessEventSource implements BaseEventSource {
    private Queue<BaseEvent> events;

    public ChessEventSource(Queue<BaseEvent> events){
        System.out.println("ChessEventSource constructor");
        this.events = events;
    }

    @Override
    public void generate(EventType eventType) {
        ChessEvent chessEvent = new ChessEvent();
        chessEvent.setEventType(eventType);
        chessEvent.setTRACE_ID(
                new StringBuilder(eventType.name()).append("_").append(System.currentTimeMillis()).append(":")
        );
        switch (eventType){
            case IDLE:
                break;
            case READY:
                break;
            case BEGIN:
                break;
            case GAMING:
                break;
            case ONLINE:
                break;
            case OFFLINE:
                break;
            case GAME_END:
                break;
            case MATCHING:
                break;
            case ROUND_END:
                break;
            case END_HANDLE:
                break;
            case ERROR_HANDLE:
                break;
            case START_HANDLE:
                break;
            case EXIT:
                break;
            default:
                break;
        }
        events.add(chessEvent);
    }
}
