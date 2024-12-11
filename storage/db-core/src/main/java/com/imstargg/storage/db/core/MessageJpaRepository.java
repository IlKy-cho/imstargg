package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByCode(String code);

    List<MessageEntity> findAllByCodeIn(List<String> codes);

    Optional<MessageEntity> findByCodeAndLang(String code, String lang);
}
