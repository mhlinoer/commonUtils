package com.linoer.app.games.chess.game.process;

import com.linoer.app.games.base.domain.BaseEvent;
import com.linoer.app.games.base.round.BaseEventSource;
import com.linoer.app.games.chess.model.ChessEvent;

import java.util.ArrayList;
import java.util.List;

public class ChessEventSource implements BaseEventSource {
    private List<BaseEvent> events;

    public ChessEventSource(){
        this.events = new ArrayList<>();
    }

    @Override
    public void generate() {
        events.add(new ChessEvent());
    }

}
