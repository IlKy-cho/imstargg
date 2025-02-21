package com.imstargg.batch.job.support;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonObjectMarshaller;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.core.io.WritableResource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Iterator;

public class JsonlFileItemWriter<T> extends AbstractFileItemWriter<T> {

    private static final String JSONL_OBJECT_SEPARATOR = DEFAULT_LINE_SEPARATOR;

    private JsonObjectMarshaller<T> jsonObjectMarshaller;

    public JsonlFileItemWriter() {
        this.jsonObjectMarshaller = new JacksonJsonObjectMarshaller<>();
    }

    public JsonlFileItemWriter(WritableResource resource, JsonObjectMarshaller<T> jsonObjectMarshaller) {
        Assert.notNull(resource, "resource must not be null");
        Assert.notNull(jsonObjectMarshaller, "json object marshaller must not be null");
        setResource(resource);
        setJsonObjectMarshaller(jsonObjectMarshaller);
        setExecutionContextName(ClassUtils.getShortName(getClass()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.append) {
            this.shouldDeleteIfExists = false;
        }
    }

    public void setJsonObjectMarshaller(JsonObjectMarshaller<T> jsonObjectMarshaller) {
        this.jsonObjectMarshaller = jsonObjectMarshaller;
    }

    @Override
    protected String doWrite(Chunk<? extends T> items) {
        StringBuilder lines = new StringBuilder();
        Iterator<? extends T> iterator = items.iterator();
        if (!items.isEmpty() && state.getLinesWritten() > 0) {
            lines.append(JSONL_OBJECT_SEPARATOR);
        }
        while (iterator.hasNext()) {
            T item = iterator.next();
            lines.append(this.jsonObjectMarshaller.marshal(item));
            if (iterator.hasNext()) {
                lines.append(JSONL_OBJECT_SEPARATOR);
            }
        }
        return lines.toString();
    }
}
