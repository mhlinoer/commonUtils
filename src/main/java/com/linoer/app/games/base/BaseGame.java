package com.linoer.app.games.base;

public interface BaseGame extends Runnable{
    // 初始化
    void init();

    // 准备
    void ready();

    // 开始
    void start();

    // 游戏中
    void gaming();

    // 结算
    void gameEnd();

    // 退出
    void exit();
}
