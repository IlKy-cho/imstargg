package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageCollectionJpaRepository extends JpaRepository<MessageCollectionEntity, Long> {

    List<MessageCollectionEntity> findAllByCode(String code);
}
