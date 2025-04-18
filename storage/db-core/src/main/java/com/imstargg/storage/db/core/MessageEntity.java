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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "message",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_message__code_lang",
                        columnNames = {"code", "lang"}
                )
        }
)
public class MessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "code", length = 65, updatable = false, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", length = 25, updatable = false, nullable = false)
    private Language lang;

    @Column(name = "content", updatable = false, nullable = false)
    private String content;

    protected MessageEntity() {
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
