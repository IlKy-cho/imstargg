package com.imstargg.batch.job.support;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class ItemListWriter<T> implements ItemWriter<List<T>>, ItemStream, InitializingBean {

    private final ItemWriter<T> itemWriter;

    public ItemListWriter(ItemWriter<T> itemWriter) {
        this.itemWriter = itemWriter;
    }

    @Override
    public void write(Chunk<? extends List<T>> chunk) throws Exception {
        List<T> totalList = new ArrayList<>();

        for (List<T> list : chunk) {
            totalList.addAll(list);
        }

        itemWriter.write(new Chunk<>(totalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (itemWriter instanceof InitializingBean initializingBean) {
            initializingBean.afterPropertiesSet();
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (itemWriter instanceof ItemStream itemStream) {
            itemStream.open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (itemWriter instanceof ItemStream itemStream) {
            itemStream.update(executionContext);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (itemWriter instanceof ItemStream itemStream) {
            itemStream.close();
        }
    }
}
