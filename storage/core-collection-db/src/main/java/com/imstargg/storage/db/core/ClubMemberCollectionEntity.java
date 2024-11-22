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

    @Column(name = "club_tag", length = 45, updatable = false, nullable = false)
    private String clubTag;

    @Column(name = "member_tag", length = 45, updatable = false, nullable = false)
    private String memberTag;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    protected ClubMemberCollectionEntity() {}

    public ClubMemberCollectionEntity(String clubTag, String memberTag, String role) {
        this.clubTag = clubTag;
        this.memberTag = memberTag;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getClubTag() {
        return clubTag;
    }

    public String getMemberTag() {
        return memberTag;
    }

    public String getRole() {
        return role;
    }
}
