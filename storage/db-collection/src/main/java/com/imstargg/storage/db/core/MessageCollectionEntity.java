package com.imstargg.storage.db.core;

import com.imstargg.core.enums.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "message")
public class MessageCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "code", length = 105, updatable = false, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", length = 25, updatable = false, nullable = false)
    private Language lang;

    @Column(name = "content", updatable = false, nullable = false)
    private String content;

    protected MessageCollectionEntity() {
    }

    public MessageCollectionEntity(String code, Language lang, String content) {
        this.code = code;
        this.lang = lang;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Language getLang() {
        return lang;
    }

    public String getContent() {
        return content;
    }
}
