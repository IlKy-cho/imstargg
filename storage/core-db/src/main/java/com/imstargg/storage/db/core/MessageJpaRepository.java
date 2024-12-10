package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {
}
