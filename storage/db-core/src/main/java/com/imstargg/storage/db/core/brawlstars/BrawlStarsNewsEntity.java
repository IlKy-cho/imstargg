package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "brawlstars_news",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brawlstars_news__linkurl", columnNames = "link_url")
        },
        indexes = {
                @Index(name = "ix_brawlstars_news__lang_publishdate", columnList = "lang, publish_date desc")
        }
)
public class BrawlStarsNewsEntity extends BaseEntity {

    @Id
    @Column(name = "brawlstars_news_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang", length = 25, nullable = false)
    private Language language;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "link_url", length = 255, nullable = false)
    private String linkUrl;

    @Column(name = "publish_date", nullable = false)
    private LocalDateTime publishDate;

    protected BrawlStarsNewsEntity() {
    }

    public Long getId() {
        return id;
    }

    public Language getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }
}
