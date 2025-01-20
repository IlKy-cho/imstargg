package com.imstargg.core.domain.statistics;

public class ResultCounter {

    private long victoryCount = 0;
    private long defeatCount = 0;
    private long drawCount = 0;

    void addVictory(long count) {
        victoryCount += count;
    }

    void addDefeat(long count) {
        defeatCount += count;
    }

    void addDraw(long count) {
        drawCount += count;
    }


    long getVictoryCount() {
        return victoryCount;
    }

    long getDefeatCount() {
        return defeatCount;
    }

    long getDrawCount() {
        return drawCount;
    }
}
