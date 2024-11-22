package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "club_member")
public class ClubMemberCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "club_member_id")
    private Long id;

    @Column(name = "club_id", updatable = false, nullable = false)
    private long clubId;

    @Column(name = "member_id", updatable = false, nullable = false)
    private long memberId;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    protected ClubMemberCollectionEntity() {}

    public ClubMemberCollectionEntity(long clubId, long memberId, String role) {
        this.clubId = clubId;
        this.memberId = memberId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public long getClubId() {
        return clubId;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getRole() {
        return role;
    }
}
