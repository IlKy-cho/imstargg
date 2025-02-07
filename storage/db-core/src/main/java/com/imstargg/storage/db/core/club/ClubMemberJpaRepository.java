package com.imstargg.storage.db.core.club;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberJpaRepository extends JpaRepository<ClubMemberEntity, Long>, ClubMemberJpaRepositoryCustom {
}
