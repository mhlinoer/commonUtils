package com.linoer.app.games.chess.model;

import com.linoer.app.games.base.domain.BasePlayer;
import com.linoer.app.games.chess.game.ChessGaming;

public class ChessPlayer extends BasePlayer {
    private ChessGaming chessGaming = null;

    public ChessGaming getChessGaming() {
        return chessGaming;
    }

    public void setChessGaming(ChessGaming chessGaming) {
        this.chessGaming = chessGaming;
    }
}
