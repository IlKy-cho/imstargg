package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "club_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_club_member__clubtag_membertag", columnNames = {"club_tag", "member_tag"})
        }
)
public class ClubMemberEntity extends BaseEntity {

    @Id
    @Column(name = "club_member_id")
    private Long id;

    @Column(name = "club_tag", length = 45, updatable = false, nullable = false)
    private String clubTag;

    @Column(name = "member_tag", length = 45, updatable = false, nullable = false)
    private String memberTag;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    protected ClubMemberEntity() {
        this.id = id;
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
