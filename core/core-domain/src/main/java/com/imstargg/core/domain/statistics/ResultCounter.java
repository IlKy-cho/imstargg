package com.imstargg.core.domain.statistics;

public class ResultCounter {

    private final Counter victoryCount = new Counter();
    private final Counter defeatCount = new Counter();
    private final Counter drawCount = new Counter();

    public void addVictory(long count) {
        victoryCount.add(count);
    }

    public void addDefeat(long count) {
        defeatCount.add(count);
    }

    public void addDraw(long count) {
        drawCount.add(count);
    }


    public long getVictoryCount() {
        return victoryCount.getCount();
    }

    public long getDefeatCount() {
        return defeatCount.getCount();
    }

    public long getDrawCount() {
        return drawCount.getCount();
    }
}
