package com.imstargg.core.domain.statistics;

public class ResultCounter {

    private final Counter victoryCount = new Counter();
    private final Counter defeatCount = new Counter();
    private final Counter drawCount = new Counter();

    void addVictory(long count) {
        victoryCount.add(count);
    }

    void addDefeat(long count) {
        defeatCount.add(count);
    }

    void addDraw(long count) {
        drawCount.add(count);
    }


    long getVictoryCount() {
        return victoryCount.getCount();
    }

    long getDefeatCount() {
        return defeatCount.getCount();
    }

    long getDrawCount() {
        return drawCount.getCount();
    }
}
