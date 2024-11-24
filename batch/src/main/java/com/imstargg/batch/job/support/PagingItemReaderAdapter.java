package com.imstargg.batch.job.support;

import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PagingItemReaderAdapter<T> extends AbstractPagingItemReader<T> {

    private final PagingItemReadStrategy<T> readStrategy;

    public PagingItemReaderAdapter(PagingItemReadStrategy<T> readStrategy) {
        this.readStrategy = readStrategy;
    }

    private int pageOffset = 0;

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        if (pageOffset < 0) {
            throw new IllegalArgumentException("pageOffset 는 0이상이어야 합니다.");
        }
        this.pageOffset = pageOffset;
    }

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
        else {
            results.clear();
        }

        List<T> result = readStrategy.readPage(getPageOffset() + getPage(), getPageSize());
        results.addAll(result);
    }
}
