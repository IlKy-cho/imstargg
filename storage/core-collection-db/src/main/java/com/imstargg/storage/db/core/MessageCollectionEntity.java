package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "message")
public class MessageCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "message_id")
    private Long id;

    @Column(name = "code", length = 105, updatable = false, nullable = false)
    private String code;

    @Column(name = "lang", length = 25, updatable = false, nullable = false)
    private String lang;

    @Column(name = "content", updatable = false, nullable = false)
    private String content;

    protected MessageCollectionEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String getContent() {
        return content;
    }
}
