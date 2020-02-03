package com.linoer.app.games.chess;

import com.linoer.app.concurrent.SafeThread;
import com.linoer.app.games.base.domain.BasePlayer;
import com.linoer.app.games.chess.game.ChessGaming;
import com.linoer.app.games.chess.model.ChessPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestChess{
    private static final Logger logger = LoggerFactory.getLogger(TestChess.class);
    private ChessGaming chessGaming = null;
    private ChessPlayer player1 = new ChessPlayer();
    private ChessPlayer player2 = new ChessPlayer();
    private void config() {
        System.out.println("TestChess config");
        List<BasePlayer> players = new ArrayList<>(2);
        players.add(player1);
        players.add(player2);
        chessGaming = new ChessGaming(players);
        player1.setChessGaming(chessGaming);
        player2.setChessGaming(chessGaming);
    }

    private void start(){
        System.out.println("TestChess start");
        new Thread(chessGaming).start();
        new Thread(()->{
            while (true){
                player1.getChessGaming().ready();
                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player2.getChessGaming().ready();
            }
        }).start();

    }

    public static void main(String[] args) {
        TestChess testChess = new TestChess();
        testChess.config();
        testChess.start();
    }
}
