package com.imstargg.batch.job.support;

import org.springframework.beans.factory.annotation.Value;

public class ChunkSizeJobParameter {

    private int size;

    public ChunkSizeJobParameter(int defaultSize) {
        this.size = defaultSize;
    }

    @Value("#{jobParameters['chunk.size']}")
    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
