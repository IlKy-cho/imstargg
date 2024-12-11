package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "club_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_clubmember__clubid_memberid", columnNames = {"club_id", "member_id"})
        }
)
public class ClubMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @Column(name = "club_id", updatable = false, nullable = false)
    private long clubId;

    @Column(name = "member_id", updatable = false, nullable = false)
    private long memberId;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    protected ClubMemberEntity() {}

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
