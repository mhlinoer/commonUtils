package com.linoer.app.games.base.domain;

import com.linoer.app.games.GameMode;
import com.linoer.app.games.base.BaseGamingRule;

/**
 * 游戏参数配置
 */
public class BaseGameConfig {
    // 游戏模式
    private GameMode gameMode = GameMode.CHESS;
    // 场次数量
    private Integer totalRound = 3;
    // 游戏规则
    private BaseGamingRule gamingRule;

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Integer getTotalRound() {
        return totalRound;
    }

    public void setTotalRound(Integer totalRound) {
        this.totalRound = totalRound;
    }

    public BaseGamingRule getGamingRule() {
        return gamingRule;
    }

    public void setGamingRule(BaseGamingRule gamingRule) {
        this.gamingRule = gamingRule;
    }
}
