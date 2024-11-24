package com.imstargg.batch.job.support;

import java.util.List;

public interface PagingItemReadStrategy<T> {
    List<T> readPage(int page, int size);
}
