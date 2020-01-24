package com.linoer.app.games.chess.model;

import com.linoer.app.games.GameMode;
import com.linoer.app.games.base.domain.BaseGameConfig;
import com.linoer.app.games.chess.game.ChessGamingRule;

public class ChessGameConfig extends BaseGameConfig {

    public ChessGameConfig() {
        setGameMode(GameMode.CHESS);
        setGamingRule(new ChessGamingRule());
    }

}
